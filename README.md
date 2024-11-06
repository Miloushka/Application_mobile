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
