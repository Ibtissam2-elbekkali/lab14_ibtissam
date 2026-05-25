package com.example.lab14_ibtissam.files;

import android.content.Context;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

public final class InternalTextStore {
    private InternalTextStore() {}

    public static void writeUtf8(Context context, String fileName, String content) throws Exception {
        try (FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE)) {
            fos.write(content.getBytes(StandardCharsets.UTF_8));
        }
    }

    public static String readUtf8(Context context, String fileName) throws Exception {
        try (FileInputStream fis = context.openFileInput(fileName)) {
            ByteArrayOutputStream result = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }
            return result.toString(StandardCharsets.UTF_8.name());
        }
    }

    public static boolean delete(Context context, String fileName) {
        return context.deleteFile(fileName);
    }
}