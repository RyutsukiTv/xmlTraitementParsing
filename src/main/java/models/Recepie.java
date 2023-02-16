package models;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// modelise une recette s'inspirant du fichier des donnees
public class Recepie {
    private String title;
    private String id;

    private ArrayList<Ingredient> ingredients;


    private List<String> instructions;

    private List<String> nutrition;

    private String relatedRef;
    private String relatedDesc;

    private String comment;

    private String date;

    public Recepie(String title, String id, ArrayList<Ingredient> ingredients, List<String> instructions, List<String> nutrition, String relatedRef, String relatedDesc, String comment, String date) {
        this.title = title;
        this.id = id;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.nutrition = nutrition;
        this.relatedRef = relatedRef;
        this.relatedDesc = relatedDesc;
        this.comment = comment;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<Ingredient> getIngredients() {
        // retourne les ingrédiants de la recette, mais aussi ceux des sous-recettes
        ArrayList<Ingredient> allIngredients = new ArrayList<>();
        for (Ingredient ingredient : ingredients) {
            allIngredients.add(ingredient);
            if (ingredient.getIngredient() != null) {
                allIngredients.addAll(ingredient.getIngredient());
            }
        }
        return allIngredients;
    }

    public void setIngredients(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<String> getInstructions() {
        return instructions;
    }

    public void setInstructions(List<String> instructions) {
        this.instructions = instructions;
    }

    public List<String> getNutrition() {
        return nutrition;
    }

    public void setNutrition(List<String> nutrition) {
        this.nutrition = nutrition;
    }

    public String getRelatedRef() {
        return relatedRef;
    }

    public void setRelatedRef(String relatedRef) {
        this.relatedRef = relatedRef;
    }

    public String getRelatedDesc() {
        return relatedDesc;
    }

    public void setRelatedDesc(String relatedDesc) {
        this.relatedDesc = relatedDesc;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void ingredientsToString() {
        for (Ingredient ingredient : ingredients) {
            System.out.println(ingredient.toString());
        }
    }

    public int nombreSteps () {
        return instructions.size() + this.ingredients.stream().mapToInt(Ingredient::nbSteps).sum();
    }

    public int nbIngredients() {
        return this.ingredients.stream().mapToInt(Ingredient::nbIngredients).sum();
    }

    public void ToIngrediant(){
        for (Ingredient ingredient : ingredients) {
            if (ingredient != null) {
                System.out.println(ingredient.toString());
            }
        }
    }
}