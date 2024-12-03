# Documentation du Projet

## Présentation du Projet

Ce projet Java Software Logging est une application web composée de deux parties principales :

1. **Backend** : Développé en Java avec Spring Boot (version 23), il gère la logique métier et les interactions avec la base de données MongoDB. Le backend est configuré pour fonctionner avec un conteneur MongoDB, facilitant ainsi la gestion des données.

2. **Frontend** : Développé avec Angular, il fournit une interface utilisateur interactive. Le frontend est également configuré pour fonctionner avec un conteneur Zipkin, permettant de suivre les traces des requêtes pour une meilleure observabilité.

## Installation

### Prérequis

- Docker et Docker Compose doivent être installés sur votre machine.
- Java 23 doit être installé pour le backend.

### Étapes d'Installation

1. **Cloner le dépôt** :

   ```bash
   git clone <URL_DU_DEPOT>
   cd <NOM_DU_REPO>
   ```

2. **Configurer le Backend** :

   - Accédez au répertoire du backend :
     ```bash
     cd backend
     ```
   - Démarrez le conteneur MongoDB :
     ```bash
     docker-compose up -d
     ```

3. **Construire et exécuter le Backend** :

   - Assurez-vous d'être dans le répertoire `backend` et exécutez :
     ```bash
     ./mvnw spring-boot:run
     ```

4. **Configurer le Frontend** :

   - Ouvrez un nouveau terminal et accédez au répertoire du frontend :

     ```bash
     cd frontend
     ```

   - Installer les dépendances nécessaires :

     ```bash
     npm install
     ```

   - Démarrez le conteneur Zipkin :
     ```bash
     docker-compose up -d
     ```

5. **Construire et exécuter le Frontend** :
   - Dans le répertoire `frontend`, exécutez :
     ```bash
     ng serve
     ```

### Accéder à l'Application

- Le frontend sera accessible à l'adresse suivante : [http://localhost:4200](http://localhost:4200)
- L'interface de Zipkin sera accessible à l'adresse suivante : [http://localhost:9411](http://localhost:9411)

## Commandes Utiles

- Pour arrêter les conteneurs Docker :

  ```bash
  docker-compose down
  ```

- Pour reconstruire le projet backend :

  ```bash
  ./mvnw clean install
  ```

- Pour exécuter les tests unitaires du backend :
  ```bash
  ./mvnw test
  ```

## Conclusion

Ce projet offre une architecture moderne avec un backend robuste en Java et un frontend dynamique en Angular, le tout orchestré avec Docker pour une gestion simplifiée des conteneurs. N'hésitez pas à explorer le code et à contribuer !
