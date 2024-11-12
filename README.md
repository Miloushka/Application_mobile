# Application Mobile

## Guide de configuration

### Ajouter une icône
Pour ajouter une icône à votre application, accédez à :
app/res/drawable/new/image_asset

### Ajouter un texte
Pour définir un texte, modifiez le fichier suivant :
app/res/values/strings.xml

### Ajouter une couleur
Pour ajouter une couleur, éditez le fichier :
app/res/values/colors.xml

### Ajouter une dimension (par exemple, une marge)
Pour ajouter une dimension (comme une marge), modifiez le fichier :
app/res/values/dimens.xml


## Utiliser les éléments prédéfinis

Pour référencer un élément prédéfini (comme une couleur, une chaîne de texte, ou une dimension) dans votre fichier `activity_main.xml`, utilisez la syntaxe suivante :

```
"@fichier/nom"
Fichier : @color, @dimens, @string
Nom : Utilisez le nom de l'élément tel qu'il est défini dans le fichier (par exemple, app_name, primaryColor)

Exemple : Pour appeler le nom de l'application défini dans strings.xml, utilisez @string/app_name.
```



## Architecture 

### Dossier kotlin

AccountActivity : Permet de gérer le compte utilisateur (rentrer les infos pero + modifier mdp)
AddCategorieActivity : Permet d'ajouter une dépense 
AnnualActivity : Permet de voir les dépenses annuel
BaseActivity : Permet de gérer la barre de navigation, fermer le clavier lorsque l'utilisateur clique a côté d'un EditText
CategorieListItems : Class de liste déroulante des catégories
ConnectionActivity : Permet de s'identifier 
CustomAdapter : Permet de personnaliser l'affichage de la liste déroulante des catégories
MainActivité : Permet de gérer la page principal (home)
MonthActivity : Permet de voir les dépenses d'un mois 
ResetPasswordActivity : Permet de réinitialiser le mot de passe 
SelectDate : class qui permet de selectionner la date, JJ/MM/AAAA ou MM/AAAA ou AAAA



### app/res/anim
permet de faire des animations pour la barre de navigation


### app/res/layout
activity_account : permet de gérer le visuel de la page du compte utilisateur 
activity_add_catégorie : permet de gérer le visuel de la page d'ajout d'une dépense 
activity_categorie_spinner_layout : permet de gérer le visuel de la liste déroulante des catégories 
activity_connection : permet de gérer le visuel de la page de connection 
activity_annual : permet de gérer le visuel de la page du budget sur une année
activity_main : permet de gérer le visuel de la page principale (home)
activity_month : permet de gérer le visuel de la page du budget sur un mois
activity_reset_password : permet de gérer le visuel de la page de réinitialisation du mot de passe 

### app/res/menu
navigation_bar : permet de gérer le visuel de la barre de navigation

