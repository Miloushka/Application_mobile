# Guide de Configuration de l'Application Mobile

## Préparations de Base

### Ajouter des Ressources Visuelles

- **Icône**  
  Pour ajouter une icône à votre application, placez l'image dans le dossier suivant :  
  `app/res/drawable/new/image_asset`

- **Texte**  
  Pour définir un texte, modifiez le fichier :  
  `app/res/values/strings.xml`

- **Couleur**  
  Pour ajouter une couleur, éditez le fichier :  
  `app/res/values/colors.xml`

- **Dimension (ex. marge)**  
  Pour ajouter une dimension (comme une marge), modifiez le fichier :  
  `app/res/values/dimens.xml`

## Utilisation des Éléments Prédéfinis

Pour référencer un élément prédéfini (tel qu'une couleur, une chaîne de texte ou une dimension) dans votre fichier `activity_main.xml`, utilisez la syntaxe suivante :

- **Syntaxe générale**  
  `"@fichier/nom"`

  - **Fichiers possibles** : `@color`, `@dimens`, `@string`
  - **Nom** : Correspond au nom de l'élément tel que défini dans le fichier (par ex., `app_name`, `primaryColor`).

- **Exemple d’utilisation**  
  Pour appeler le nom de l'application défini dans `strings.xml`, utilisez `@string/app_name`.

---

## Architecture de l'Application

### Structure des Activités (Dossier `kotlin`)

- **AccountActivity** : Gère le compte utilisateur (ajout des infos personnelles et modification du mot de passe).
- **AddCategorieActivity** : Permet d'ajouter une dépense.
- **AnnualActivity** : Affiche les dépenses annuelles.
- **BaseActivity** : Gère la barre de navigation et permet de fermer le clavier lorsque l'utilisateur clique à l'extérieur d'un `EditText`.
- **CategorieListItems** : Classe de liste déroulante des catégories.
- **ConnectionActivity** : Permet de s'identifier.
- **CustomAdapter** : Personnalise l'affichage de la liste déroulante des catégories.
- **MainActivity** : Gère la page principale (home).
- **MonthActivity** : Affiche les dépenses mensuelles.
- **ResetPasswordActivity** : Permet de réinitialiser le mot de passe.
- **SelectDate** : Classe pour la sélection de la date (formats : JJ/MM/AAAA, MM/AAAA, AAAA).

### Structure des Ressources

#### Dossier `app/res/anim`
- Définit les animations pour la barre de navigation.

#### Dossier `app/res/layout`
- **activity_account** : Gère l'affichage de la page du compte utilisateur.
- **activity_add_categorie** : Gère l'affichage de la page d'ajout de dépense.
- **activity_categorie_spinner_layout** : Gère l'affichage de la liste déroulante des catégories.
- **activity_connection** : Gère l'affichage de la page de connexion.
- **activity_annual** : Gère l'affichage de la page du budget annuel.
- **activity_main** : Gère l'affichage de la page principale (home).
- **activity_month** : Gère l'affichage de la page du budget mensuel.
- **activity_reset_password** : Gère l'affichage de la page de réinitialisation du mot de passe.

#### Dossier `app/res/menu`
- **navigation_bar** : Définit l'affichage de la barre de navigation.
