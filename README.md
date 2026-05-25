# Lab 14 – Secure Storage in Android (Java)

Ce lab présente les bonnes pratiques de stockage sécurisé dans une application Android Java.  
Il couvre l’utilisation de `SharedPreferences`, `EncryptedSharedPreferences`, du stockage interne, du cache et du stockage externe app-specific.

## Fonctionnalités
- Sauvegarde des préférences utilisateur
- Stockage sécurisé d’un token chiffré
- Lecture/écriture de fichiers internes UTF-8
- Sauvegarde et chargement de données JSON
- Gestion du cache temporaire
- Export de fichiers vers le stockage externe app-specific
- Nettoyage complet des données

## Technologies utilisées
- Java
- Android SDK
- SharedPreferences
- EncryptedSharedPreferences
- MasterKey (Android Security Crypto)
- JSON (`org.json`)

## Vérifications réalisées
- Aucun secret affiché dans Logcat
- Données sensibles chiffrées
- Fichiers internes accessibles via Device File Explorer
- Cache supprimable
- Nettoyage sécurisé des données

## Auteur
Projet réalisé par Ibtissam El Bekkali dans le cadre du laboratoire Android Secure Storage.
