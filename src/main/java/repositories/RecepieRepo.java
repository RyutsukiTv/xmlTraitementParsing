package repositories;
import models.Ingredient;
import models.Recepie;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;



public class RecepieRepo{
    private List<Recepie> recipeCollection = new ArrayList<>();

    //get all recipes
    public List<Recepie> getrecipeCollection(){
        return recipeCollection;
    }

    private List<String> parseSteps(NodeList steps, Boolean isDirect){
        List<String> stepsList = new ArrayList<>();
        for (int k = 0; k < steps.getLength() ; k++) {
            Element stepElement = (Element) steps.item(k);
            if (stepElement != null) {
                String parent = stepElement.getParentNode().getParentNode().getNodeName();
                if (isDirect && parent.equals("rcp:recipe")) stepsList.add(stepElement.getTextContent());
                else if(!isDirect) stepsList.add(stepElement.getTextContent());
            }
        }
        return stepsList;
    }

    private List<String> parseStepIngredients(NodeList steps, String parent){
        List<String> stepsList = new ArrayList<>();
        for (int k = 0; k < steps.getLength() ; k++) {
            Element stepElement = (Element) steps.item(k);
            if (stepElement != null) {
                Element currparent = (Element) stepElement.getParentNode().getParentNode();
                if (currparent.getAttribute("name").equals(parent)) stepsList.add(stepElement.getTextContent());
            }
        }
        return stepsList;
    }

    private Ingredient parseIngredient(Element nameingredientElement){
        String nameingredients = nameingredientElement.getAttribute("name");

        if (!nameingredientElement.getAttribute("amount").equals("null") && !nameingredientElement.getAttribute("amount").equals("")) {
            return new Ingredient(nameingredients,
                    nameingredientElement.getAttribute("amount"),
                    nameingredientElement.getAttribute("unit"));
        }else {
            NodeList childNodes = nameingredientElement.getChildNodes();
            ArrayList<Ingredient> sousIngredientList = new ArrayList<>();
            for (int k = 0; k < childNodes.getLength(); k++) {
                if (childNodes.item(k).getNodeType() == Node.ELEMENT_NODE) {
                    if (childNodes.item(k).getAttributes().getNamedItem("name") != null) {
                        sousIngredientList.add(parseIngredient((Element) childNodes.item(k)));
                    }
                }
            }
            //get steps
            NodeList steps = nameingredientElement.getElementsByTagName("rcp:step");
            List<String> stepsList = parseStepIngredients(steps, nameingredients);

            return new Ingredient(nameingredients, stepsList, sousIngredientList);
        }
    }

    private Recepie parseRecepie(Element recipeElement) {
        //get title
        String title = recipeElement.getElementsByTagName("rcp:title").item(0).getTextContent();
        //get date of the recipe
        String date = recipeElement.getElementsByTagName("rcp:date").item(0).getTextContent();
        //get id
        String id = recipeElement.getAttribute("id");

        //get ingredients
        NodeList ingredientNodes = recipeElement.getElementsByTagName("rcp:ingredient");
        ArrayList<Ingredient> ingrediantList = new ArrayList<>();

        for (int j = 0; j < ingredientNodes.getLength(); j++) {
            Element nameingredientElement = (Element) ingredientNodes.item(j);
            Node parentNode = nameingredientElement.getParentNode();
            if (parentNode.getNodeName().equals("rcp:recipe")) {
                ingrediantList.add(parseIngredient(nameingredientElement));
            }
        }

        //get step preparation
        NodeList stepNodes = recipeElement.getElementsByTagName("rcp:step");
        List<String> steps = parseSteps(stepNodes, true);

        //get nutrition
        List<String> nutrition = new ArrayList<>();
        NodeList nutritionNodes = recipeElement.getElementsByTagName("rcp:nutrition");
        Element nutritionElement = (Element) nutritionNodes.item(0);
        nutrition.add(nutritionElement.getAttribute("calories"));
        nutrition.add(nutritionElement.getAttribute("fat"));
        nutrition.add(nutritionElement.getAttribute("carbohydrates"));
        nutrition.add(nutritionElement.getAttribute("protein"));


        //get relate of recipe and desc
        String relatedRef = "";
        String relatedDesc = "";
        Element relatedElement = (Element) recipeElement.getElementsByTagName("rcp:related").item(0);
        if (relatedElement != null) {
            relatedRef = relatedElement.getAttribute("ref");
            relatedDesc = relatedElement.getTextContent() ;
        }
        //get comment
        String comment="";
        if (recipeElement.getElementsByTagName("rcp:comment").getLength()>0){
            comment = recipeElement.getElementsByTagName("rcp:comment").item(0).getTextContent();
        }

        return new Recepie(title,  id, ingrediantList, steps, nutrition,  relatedRef,  relatedDesc,  comment,  date);
    }

    public void init(String fileName) {
        try {
            File file = new File(fileName);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();
            NodeList recipeNodes = doc.getElementsByTagName("rcp:recipe");
            for (int i = 0; i < recipeNodes.getLength(); i++) {
                Element recipeElement = (Element) recipeNodes.item(i);
                Recepie recepie = parseRecepie(recipeElement);
                this.recipeCollection.add(recepie);
            }

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }

    //Ecrire une m??thode qui permet de lister les titres des recettes
    public List<String> getAllTitles(){
        return this.recipeCollection.stream().map(Recepie::getTitle).collect(Collectors.toList());
    }

    //Ecrire une m??thode qui permet de calculer le nombre total d?????ufs utilis??s.
    public int countEggs(){
        //Get the total amount of eggs in all recipes and return it
        return this.recipeCollection.stream().mapToInt(recepie -> recepie.getIngredients().stream()
                .filter(ingredient -> ingredient.getName().contains("egg")).
                mapToInt(ingredient -> Integer.parseInt(ingredient.getAmount())).sum()).sum();
    }

    //Ecrire une m??thode qui permet de retourner les recettes utilisant l???huile d???olive
    public List<Recepie> getOilRecipe(){
        return this.recipeCollection.stream().filter(recepie -> recepie.getIngredients().stream().anyMatch(ingredient -> ingredient.getName().contains("olive oil"))).collect(Collectors.toList());
    }

    //Ecrire une m??thode qui permet de calculer le nombre d?????ufs par recette
    public Map<Recepie, Integer> countEggsPerRecipe() {
        return this.recipeCollection.stream().collect(Collectors.toMap(recepie -> recepie, recepie -> recepie.getIngredients().stream()
                .filter(ingredient -> ingredient.getName().contains("egg")).
                mapToInt(ingredient -> Integer.parseInt(ingredient.getAmount())).sum()));
    }

    //Ecrire une m??thode qui permet de retourner les recettes fournissant moins de 500 calories
    public List<Recepie> getLess500Calories(){
        return this.recipeCollection.stream().filter(recepie -> Integer.parseInt(recepie.getNutrition().get(0)) < 500).collect(Collectors.toList());
    }

    //Ecrire une m??thode qui retourne la quantit?? de sucre utilis??e par la recette Zuppa Inglese
    public double getSugarOnZuppaInglese() {
        Recepie zuppaInglese = this.recipeCollection.stream().filter(recepie -> recepie.getTitle().equals("Zuppa Inglese")).findFirst().get();
        return zuppaInglese.getIngredients().stream().filter(ingredient -> ingredient.getName().contains("sugar")).mapToDouble(ingredient -> Double.parseDouble(ingredient.getAmount())).sum();
    }

    //Ecrire une m??thode qui affiche les 2 premi??res ??tapes de la recette Zuppa Inglese
    public List<String> getTwoFirstStepOfZuppa(){
        return this.recipeCollection.stream()
                .filter(recepie -> recepie.getTitle().equals("Zuppa Inglese"))
                .flatMap(recepie -> recepie.getInstructions().stream())
                .limit(2)
                .collect(Collectors.toList());
    }

    //Ecrire une m??thode qui retourne les recettes qui n??cessitent plus de 5 ??tapes
    public List<Recepie> getMoreThan5Step(){
        return this.recipeCollection.stream()
                .filter(recepie -> recepie.nombreSteps()>5)
                .collect(Collectors.toList());
    }

    //Ecrire une m??thode qui retourne les recettes ayant des ingr??dients en communs avec la recette Zuppa Inglese.
    public List<Recepie> getRecipesWithCommonIngredients() {
        // R??cup??rer le nom des ingr??dients de la recette Zuppa Inglese
        List<String> zuppaIngredients = this.recipeCollection.stream()
                .filter(recepie -> recepie.getTitle().equals("Zuppa Inglese"))
                .flatMap(recepie -> recepie.getIngredients().stream())
                .map(Ingredient::getName).toList();

        // R??cup??rer les recettes qui ont des ingr??dients en commun avec la recette Zuppa Inglese
        return this.recipeCollection.stream()
                .filter(recepie -> recepie.getIngredients().stream().anyMatch(ingredient -> zuppaIngredients.contains(ingredient.getName())))
                .collect(Collectors.toList());
    }

    //Ecrire une m??thode qui retourne les recettes qui ne contiennent pas de beure
    public List<Recepie> getRecipeWithoutButter(){
        return this.recipeCollection.stream()
                .filter(recepie -> recepie.getIngredients().stream().noneMatch(ingredient -> ingredient.getName().contains("butter")))
                .collect(Collectors.toList());
    }

    //Ecrire une m??thode qui affiche la recette la plus calorique
    public Recepie RecipieMostCaloric(){
        return recipeCollection.stream().max((r1, r2)->r1.getNutrition().get(0).compareTo(r2.getNutrition().get(0))).get();
    }

    //Ecrire une m??thode qui retourne l???unit?? la plus fr??quente
    public String mostFrequentUnit(){
        // R??cup??rer la liste des unit??s
        List<String> units = this.recipeCollection.stream()
                .flatMap(recipe -> recipe.getIngredients().stream())
                .map(Ingredient::getUnit).toList();

        // R??cup??rer l'unit?? la plus fr??quente, en faisant attention aux unit??s vides
        return units.stream()
                .filter(unit -> unit != null && !unit.isEmpty())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    //Ecrire une m??thode qui calcule le nombre d???ingr??dients par recette
    public Map<Recepie, Integer> nbIngredientPerRecipe() {
        return this.recipeCollection.stream().collect(Collectors.toMap(recepie -> recepie, Recepie::nbIngredients));
    }

    //Ecrire une m??thode qui retourne la recette comportant le plus de fat
    public Recepie getRecipeMostFat(){
        return this.recipeCollection.stream().max((r1,r2)->r1.getNutrition().get(1).compareTo(r2.getNutrition().get(1))).get();
    }

    // Ecrire une m??thode qui calcule l???ingr??dient le plus utilis??
    public String mostUsedIngredient(){
        return recipeCollection.stream().flatMap(r->r.getIngredients().stream()).collect(Collectors.groupingBy(Ingredient::getName,Collectors.counting())).entrySet().stream().max((e1,e2)->e1.getValue().compareTo(e2.getValue())).get().getKey();
    }

    // Ecrire une m??thode qui affiche les recettes tri??es par nombre d???ingr??dients.
    public Map<Integer, List<Recepie>> recipeperNbingrediant(){
        Map<Recepie, Integer> nbIngredientsParRecette = nbIngredientPerRecipe();
        return nbIngredientsParRecette.entrySet().stream().collect(Collectors.groupingBy(Map.Entry::getValue,Collectors.mapping(Map.Entry::getKey,Collectors.toList())));
    }

    //Ecrire une m??thode qui affiche pour chaque ingr??dient, les recettes qui l???utilisent.
    public Map<Ingredient, List<Recepie>> getRecipesByIngredient() {
        //Cr??er une liste de tous les noms des ingr??dients de toutes les recettes
        List<String> ingredients = this.recipeCollection.stream()
                .flatMap(recipe -> recipe.getIngredients().stream())
                .map(Ingredient::getName).toList();

        //Avec l'api stream, parcourir la liste des ingr??dients et r??cup??rer pour chaque ingr??dient, les recettes qui l'utilisent
        return ingredients.stream()
                .distinct()
                .collect(Collectors.toMap(
                        ingredient -> new Ingredient(ingredient, (List<String>) null, null),
                        ingredient -> this.recipeCollection.stream()
                                .filter(recipe -> recipe.getIngredients().stream().anyMatch(ingredient1 -> ingredient1.getName().equals(ingredient)))
                                .collect(Collectors.toList())
                ));
    }

    //Ecrire une m??thode qui calcule la r??partition des recettes par ??tape de r??alisation
    public Map<Integer, List<Recepie>> RecettesParNbInstructions(){
        return recipeCollection.stream().collect(Collectors.groupingBy(Recepie::nombreSteps,Collectors.toList()));
    }

    //Ecrire une m??thode qui calcule la recette la plus facile (avec le moins d?????tape)
    public Recepie getEasiestRecipe() {
        return RecettesParNbInstructions().entrySet().stream().min((e1,e2)->e1.getKey().compareTo(e2.getKey())).get().getValue().get(0);
    }

    public static void main(String[] args) {
        RecepieRepo recepieRepo = new RecepieRepo();
        recepieRepo.init("recipes.xml");

        //Tester les m??thodes de la classe
        //m??thode titre des recettes
        System.out.println("Titre des recettes :");
        recepieRepo.getAllTitles().forEach(System.out::println);

        System.out.println("\n\n");

        //m??thode qui calcule le nombre d'oeufs utilis??s
        System.out.println("Nombre d'oeufs utilis??s : "+recepieRepo.countEggs());

        System.out.println("\n\n");

        //m??thode qui retourne les recettes avec de l'huile d'olive
        System.out.println("Recettes avec de l'huile d'olive :");
        recepieRepo.getOilRecipe().forEach(r->System.out.println(r.getTitle()));

        System.out.println("\n\n");

        //m??thode qui calcul le nombre d'oeuf par recette
        System.out.println("Nombre d'oeufs par recette :");
        recepieRepo.countEggsPerRecipe().forEach((k,v)->System.out.println(k.getTitle()+" : "+v));

        System.out.println("\n\n");


        //m??thode qui retourne les recettes ?? moins de 500 calories
        System.out.println("Recettes ?? moins de 500 calories :");
        recepieRepo.getLess500Calories().forEach(r->System.out.println(r.getTitle()));

        System.out.println("\n\n");


        //m??thode qui retourne la quantit?? de sucre dans la recette Zuppa Inglese
        System.out.println("Quantit?? de sucre dans la recette Zuppa Inglese : "+recepieRepo.getSugarOnZuppaInglese());

        System.out.println("\n\n");


        //m??thode qui affiche les deux premi??res ??tapes de la recette Zuppa Inglese
        System.out.println("Deux premi??res ??tapes de la recette Zuppa Inglese :");
        recepieRepo.getTwoFirstStepOfZuppa().forEach(System.out::println);

        System.out.println("\n\n");


        //m??thode qui retourne les recettes avec plus de 5 ??tapes
        System.out.println("Recettes avec plus de 5 ??tapes :");
        recepieRepo.getMoreThan5Step().forEach(r->System.out.println(r.getTitle()));

        System.out.println("\n\n");


        //m??thode qui retourne les recettes qui ne contiennent pas de beure
        System.out.println("Recettes sans beurre :");
        recepieRepo.getRecipeWithoutButter().forEach(r->System.out.println(r.getTitle()));

        System.out.println("\n\n");


        //m??thode qui retourne les recettes ayant des ingr??dients en commun avec la recette Zuppa Inglese
        System.out.println("Recettes ayant des ingr??dients en commun avec la recette Zuppa Inglese :");
        recepieRepo.getRecipesWithCommonIngredients().forEach(r->System.out.println(r.getTitle()));

        System.out.println("\n\n");


        //afficher la recette la plus calorique
        recepieRepo.RecipieMostCaloric();

        System.out.println("\n\n");


        //retourne l'unit?? la plus fr??quente
        System.out.println("L'unit?? la plus fr??quente est : "+recepieRepo.mostFrequentUnit());

        System.out.println("\n\n");


        //nombre d'ingr??dient par recette
        System.out.println("Nombre d'ingr??dients par recette :");
        recepieRepo.nbIngredientPerRecipe().forEach((k,v)->System.out.println(k.getTitle()+" : "+v));

        System.out.println("\n\n");


        //recette comportant le plus de fat
        System.out.println("Recette comportant le plus de fat : " + recepieRepo.getRecipeMostFat().getTitle());

        System.out.println("\n\n");


        //ingr??dient le plus utilis??
        System.out.println("Ingr??dient le plus utilis?? : "+recepieRepo.mostUsedIngredient());

        System.out.println("\n\n");


        //Affiche les recettes tri??es par nombre d'ingr??dients
        System.out.println("Recettes tri??es par nombre d'ingr??dients :");
        recepieRepo.nbIngredientPerRecipe().forEach((k,v)->System.out.println(k.getTitle()+" : "+v));

        System.out.println("\n\n");


        //Afficher pour chaque ingr??dient les recettes qui l'utilisent
        System.out.println("Afficher pour chaque ingr??dient les recettes qui l'utilisent :");
        recepieRepo.getRecipesByIngredient();

        System.out.println("\n\n");


        //calcule la r??partition des recettes par ??tape de r??alisation
        System.out.println("R??partition des recettes par ??tape de r??alisation :");
        recepieRepo.RecettesParNbInstructions().forEach((k,v)->System.out.println(k+" ??tapes : "+v.stream().map(r->r.getTitle()).collect(Collectors.toList())));

        System.out.println("\n\n");


        //calcule la recette la plus facile (avec le moins d?????tape)
        System.out.println("Recette la plus facile : "+recepieRepo.getEasiestRecipe().getTitle());
    }
}