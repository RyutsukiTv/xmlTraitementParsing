# Récupération de données dans un document XML

## <span style="color:#f1a8a9">🗂️ Répartition des tâches</span>

### 🧑‍💻 Ryutsuki

- Parsing les informations générales des recettes (sans les ingrédients/étapes)
- Réalisation des méthodes demandées aux questions 4 à 16
- Design de l'interface graphique
- Intégration du choix d'interface (GUI ou textuel)
- Réalisation du compte-rendu/readme

### 👩‍💻 Chips

- Parsing des ingrédients et les étapes
- Réalisation des méthodes 17 à 22
- Réalisation de l'interface textuelle
- Test de toutes les méthodes
- Création des exécutables

## 💻 <span style="color:#f1a8a9">Déroulé du projet</span>

### Parsing du fichier :

Pour parser le fichier, nous avons utilisé la manipulation du DOM avec nodeList. Nous avons choisi de créer une classe Recepie, liée à une classe Ingredient (pouvant elle-même contenir des Ingredient)

### Création des méthodes de manipulation des données :

Pour manipuler les données comme indiqué dans l'énoncé, nous avons utilisé autant que possible l'API stream, que ce soit pour l'affichage ou pour la récupération des données demandées. Pour les méthodes où il était demandé d'afficher les données, nous avons fait le choix de renvoyer les données afin de pouvoir les utiliser dans notre interface visuelle.

### Réalisation d'une interface :

Nous avons réalisé 2 interfaces, une visuelle et une textuelle. Il est possible de choisir quelle interface on veut en lançant le .exe. L'interface textuelle laisse l'utilisateur choisir le numéro de la question qu'il veut en tapant son numéro, tandis que sur l'interface visuelle, il suffit de cliquer sur le numéro de la question.

### Création d'un exécutable :

Pour rendre ce projet plus facile d'utilisation, nous avons fait le choix de créer un fichier .exe en plus du fichier jar, à l'aide de l'outil launch4j. Cependant le fichier jar est tout de même accessible dans le code source à l'emplacement ./out/artifacts/ProjectFinal_jar/ProjectFinal.jar

## ⚠️<span style="color:#f1a8a9"> Informations à savoir</span>

- Le fichier recipes.xml a besoin d'être au même niveau que le fichier .exe pour être lu
- L'interface a été réalisée avec JavaFX
