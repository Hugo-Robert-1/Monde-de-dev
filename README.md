# Monde-de-dev (MVP)

## Description

Monde de dev a pour projet de devenir un réseau social qui permettra de mettre en relation des dévelopeurs qui cherchent du travail et des employeurs, via des thèmes communs. Avant de proposer de telles fonctionnalitées, l'application est actuellement disponible avec une version minimale (Minimum Viable Product). 
Elle propose un système d'abonnements à des thèmes, qui permettera par la suite d'afficher dans le feed de l'utilisateur des articles en relation avec ce thème. Un utilisateur aura la possibilité d'écrire des commentaires sur des pûblications, ainsi que d'écrire des articles.

## Stacks techniques

Ce projet est full stack, il inclut la partie front-end ainsi que la partie back-end.

Front-end :
- Angular CLI : version 14.1.3
- Node.js : version 16+

Back-end : 
- Java : version 21
- Base de données MySQL : driver 8.0
- Framework Spring Boot : version 3.5.3

## Back

## Installation du projet 
 ### 1. Cloner le dépôt :
  ```git clone https://github.com/Hugo-Robert-1/Monde-de-dev.git ```
 ### 2. Se placer dans le dossier du projet, partie back :
  ```cd Monde-de-dev/back```
 ### 3. Installer les dépendances du projet :
  - Via Windows, powershell : `` .\mvnw clean install ``
  - Via Linux/MacOs, bash ;  `` ./mvnw clean install ``


## Installer une base de données MySQL en local
 1. **Installer MySQL** : Assurez-vous que MySQL est installé sur votre machine. Si ce n'est pas le cas, vous pouvez le télécharger et l'installer via [MySQL Downloads](https://dev.mysql.com/downloads/) en suivant le programme d'installation.

 2. **Créer la base de données** : Connectez-vous à MySQL et créez la base de données `monde_de_dev`.

    ```sql
    CREATE DATABASE monde_de_dev;
    ```
 
 3. **Mise en place des tables de la base de données et seeding avec des données** : Pour ce projet, un script SQL peut être trouver dans le dossier back/src/main/resources/script.sql.  

## Mise en place des variables d'environnement

Plusieurs variables d'environnement sont à définir :
 - Connexion à une base de donnée MySQL : DB_USER, DB_PASSWORD, DB_URL
 - Temps de validité en seconde d'un token JWT pour l'authentification :  JWT_EXPIRATION_TIME
 - Temps de validité en seconde d'un refresh token JWT pour l'authentification : JWT_REFRESH_EXPIRATION_TIME
 
La mise en place de ces variables peuvent être faites de différentes manières : 
 - Pour un usage **strictement local** (pour des raisons évidentes de sécurité), il est possible de directement remplacer dans le fichier à l'adresse suivante : src\main\resources\application.properties les propriétés ${...} par les valeurs souhaitées.
 - Via votre éditeur de code (Eclipse ou Spring tool suite par exemple), il est possible de définir directement les variables d'environnement qui seront utilisées lorsqu'un projet est lancé. Veuillez vous rapprocher de la documentation de votre éditeur pour savoir comment les mettre en place.
 - Pour un usage en production, utiliser les variables d'environnement système. Pour cela, créer un fichier .env à la racine du projet, contenant les variables d'environnement à définir. En fonction de votre système d'exploitation, utiliser le script suivant pour charger les variables :
    - Sous Windows PowerShell :
      ```Get-Content .env | Where-Object { $_ -match '=' -and $_ -notmatch '^\s*#' -and $_.Trim() -ne '' } | ForEach-Object { $name, $value = $_ -split '=', 2; [System.Environment]::SetEnvironmentVariable($name.Trim(), $value.Trim(), "Process") }```

    - Sous Linux/macOS :
     ``export $(cat .env | xargs)``


## Générer le couple de clés RSA 
 L'authentification est gérée via l'utilisation d'une clé publique et une clé privée respectant le chiffrement RSA2048. 
 - La clée publique doit avoir comme nom de fichier public.pem et la clée privée doit avoir comme nom de fichier private.pem.
 - Elles doivent être stockées dans le dossier situé à l'addresse : src/main/resources/keys

 Voici un script permettant de générer ces deux clées au bon format via openssl : \
 ``openssl genpkey -algorithm RSA -out private.pem -pkeyopt rsa_keygen_bits:2048``\
 ``openssl pkey -in private.pem -pubout -out public.pem``

## Démarrer le projet
 Dans le dossier du projet :
 - Via Windows, powershell : `` .\mvnw spring-boot:run ``
 - Via Linux/MacOs, bash ;  `` ./mvnw spring-boot:run ``




 ## Front

### Installation du projet 
 ### 1. Cloner le dépôt (passer cette étpae si déjà réaliser précédemment):
  ```git clone https://github.com/Hugo-Robert-1/Monde-de-dev.git ```
 ### 2. Se placer dans le dossier du projet :
   ```cd Monde-de-dev/front```
 ### 3. Installer les dépendances du projet :
  ``npm install``

### Utilisation 
 ### 1. Locale :
 Utiliser la commande `npm start` pour lancer un serveur de développement. Il sera effectif à l'adresse suivante : `http://localhost:4200/`
 ### 2. Build :
 Utiliser la commande `npm build` pour construire le projet. Il sera disponible dans le dossier `dist/`

