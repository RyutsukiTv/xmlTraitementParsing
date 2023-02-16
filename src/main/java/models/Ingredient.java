package models;


import java.util.ArrayList;
import java.util.List;

public class Ingredient {
    public String name;
    public String amount;
    public String unit;
    public List<String> step;
    public ArrayList<Ingredient> ingredient;

    public Ingredient(String name, String amount, String unit, List<String> step, ArrayList<Ingredient> ingredient) {
        this.name = name;
        this.step = step;
        this.ingredient = ingredient;
    }
    public Ingredient(String name, List<String> step, ArrayList<Ingredient> ingredient) {
        this.name = name;
        this.step = step;
        this.ingredient = ingredient;
    }

    public Ingredient(String name, String amount, String unit) {
        this.name = name;
        this.amount = amount;
        this.unit = unit;

    }

    public ArrayList<Ingredient> getIngredient() {
        return ingredient;
    }

    public void setIngredient(ArrayList<Ingredient> ingredient) {
        this.ingredient = ingredient;
    }


    public List<String> getStep() {
        return step;
    }

    public void setStep(List<String> step) {
        this.step = step;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int nbIngredients(){
        if (this.ingredient == null) {
            return 1;
        }
        return this.ingredient.stream().mapToInt(Ingredient::nbIngredients).sum() + 1;
    }

    public int nbSteps(){
        if (this.step == null) {
            return 0;
        }
        return this.step.size() + this.ingredient.stream().mapToInt(Ingredient::nbSteps).sum();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Nom : ").append(name);
        if (amount != null) {
            sb.append(" Quantité : ").append(amount);
        }
        if (unit != null) {
            sb.append(" Unité : ").append(unit);
        }
        if (ingredient != null) {
            sb.append(" Ingredient : ").append(ingredient);
        }
        return sb.toString();
    }
}