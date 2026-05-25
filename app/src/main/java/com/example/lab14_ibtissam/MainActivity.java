package com.example.lab14_ibtissam;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.lab14_ibtissam.cache.CacheStore;
import com.example.lab14_ibtissam.files.InternalTextStore;
import com.example.lab14_ibtissam.files.StudentsJsonStore;
import com.example.lab14_ibtissam.model.Student;
import com.example.lab14_ibtissam.prefs.AppPrefs;
import com.example.lab14_ibtissam.prefs.SecurePrefs;
import com.google.android.material.materialswitch.MaterialSwitch;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Lab14_Ibtissam";
    private final List<String> langs = Arrays.asList("fr", "en", "ar");

    private EditText etName;
    private EditText etToken;
    private Spinner spLang;
    private MaterialSwitch swDark;
    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Gestion de l'affichage bord à bord
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialisation des vues
        etName = findViewById(R.id.etName);
        etToken = findViewById(R.id.etToken);
        spLang = findViewById(R.id.spLang);
        swDark = findViewById(R.id.swDark);
        tvResult = findViewById(R.id.tvResult);

        setupLangSpinner();

        Button btnSavePrefs = findViewById(R.id.btnSavePrefs);
        Button btnLoadPrefs = findViewById(R.id.btnLoadPrefs);
        Button btnSaveJson = findViewById(R.id.btnSaveJson);
        Button btnLoadJson = findViewById(R.id.btnLoadJson);
        Button btnClear = findViewById(R.id.btnClear);

        btnSavePrefs.setOnClickListener(v -> savePrefs());
        btnLoadPrefs.setOnClickListener(v -> loadPrefsToUi());
        btnSaveJson.setOnClickListener(v -> saveJsonFile());
        btnLoadJson.setOnClickListener(v -> loadJsonFile());
        btnClear.setOnClickListener(v -> clearAll());

        // Chargement initial
        loadPrefsToUi();
    }

    private void setupLangSpinner() {
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, langs);
        spLang.setAdapter(adapter);
    }

    private void savePrefs() {
        String name = etName.getText().toString().trim();
        String lang = langs.get(Math.max(0, spLang.getSelectedItemPosition()));
        String theme = swDark.isChecked() ? "dark" : "light";

        AppPrefs.save(this, name, lang, theme, false);

        String token = etToken.getText().toString();
        if (!token.isEmpty()) {
            try {
                SecurePrefs.saveToken(this, token);
            } catch (Exception e) {
                tvResult.setText("❌ Erreur chiffrement : " + e.getMessage());
                return;
            }
        }

        try {
            CacheStore.write(this, "last_action_ibtissam.txt", "Save @ " + System.currentTimeMillis());
        } catch (Exception ignored) {}

        tvResult.setText(
                "✅ Sauvegarde réussie (Ibtissam)\n" +
                "👤 Utilisateur : " + name + "\n" +
                "🌐 Langue : " + lang + "\n" +
                "🎨 Thème : " + theme + "\n" +
                "🔐 Token : Sécurisé dans le Keystore"
        );
        Toast.makeText(this, "Préférences enregistrées", Toast.LENGTH_SHORT).show();
    }

    private void loadPrefsToUi() {
        AppPrefs.Triple triple = AppPrefs.load(this);

        etName.setText(triple.name);
        swDark.setChecked("dark".equals(triple.theme));

        int idx = langs.indexOf(triple.lang);
        spLang.setSelection(idx >= 0 ? idx : 0);

        int tokenLen = 0;
        try {
            String token = SecurePrefs.loadToken(this);
            tokenLen = (token == null) ? 0 : token.length();
        } catch (Exception ignored) {}

        tvResult.setText(
                "📂 Données chargées\n" +
                "👤 Nom : " + triple.name + "\n" +
                "🌐 Langue : " + triple.lang + "\n" +
                "🎨 Thème : " + triple.theme + "\n" +
                "🔑 Longueur Token : " + tokenLen
        );
    }

    private void saveJsonFile() {
        List<Student> students = Arrays.asList(
                new Student(1, "Ibtissam", 22),
                new Student(2, "Amine", 21),
                new Student(3, "Yasmine", 20)
        );

        try {
            StudentsJsonStore.save(this, students);
            InternalTextStore.writeUtf8(this, "logs_ibtissam.txt", "JSON exporté avec succès.");
            tvResult.setText("💾 JSON créé (" + students.size() + " élèves)\n📄 Fichier interne logs_ibtissam.txt OK.");
        } catch (Exception e) {
            tvResult.setText("❌ Erreur : " + e.getMessage());
        }
    }

    private void loadJsonFile() {
        List<Student> students = StudentsJsonStore.load(this);
        String log;
        try {
            log = InternalTextStore.readUtf8(this, "logs_ibtissam.txt");
        } catch (Exception e) {
            log = "(aucun log)";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("📋 Liste d'étudiants (JSON)\n");
        sb.append("📝 Status : ").append(log).append("\n\n");
        for (Student s : students) {
            sb.append("▫️ ").append(s.name).append(" (id:").append(s.id).append(")\n");
        }
        tvResult.setText(sb.toString());
    }

    private void clearAll() {
        AppPrefs.clear(this);
        try { SecurePrefs.clear(this); } catch (Exception ignored) {}
        StudentsJsonStore.delete(this);
        InternalTextStore.delete(this, "logs_ibtissam.txt");
        int purged = CacheStore.purge(this);

        etName.setText("");
        etToken.setText("");
        swDark.setChecked(false);
        spLang.setSelection(0);
        tvResult.setText("🧹 Tout a été effacé !\nCache vidé : " + purged + " fichiers.");
        Toast.makeText(this, "Nettoyage terminé", Toast.LENGTH_SHORT).show();
    }
}