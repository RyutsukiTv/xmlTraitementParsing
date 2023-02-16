package com.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import repositories.RecepieRepo;
import models.*;

import java.util.stream.Collectors;


public class Controller {
    @FXML
    private TextArea questionReponse;
    @FXML
    private Text questionContent;

    @FXML
    public void onQuestion(ActionEvent event) {
        RecepieRepo recepieRepo = new RecepieRepo();
        recepieRepo.init("recipes.xml");
        Button button = (Button) event.getSource();
        button.setStyle("-fx-background-color: #59ff1b;");
        String buttonText = button.getText();
        try {
            int number = Integer.parseInt(buttonText);
            String result;
            switch (number) {
                case 4 -> {
                        questionContent.setText("Ecrire une méthode qui permet de lister les titres des recettes\n");
                        result = String.join("\n", recepieRepo.getAllTitles());
                        questionReponse.setText(result);
                }
                case 5 -> {
                        questionContent.setText("Ecrire une méthode qui permet de calculer le nombre total d’œufs utilisés. ");
                        result = "" + recepieRepo.countEggs();
                        questionReponse.setText(result);
                }
                case 6 -> {
                        questionContent.setText(" Ecrire une méthode qui permet de retourner les recettes utilisant l’huile d’olive");
                        result = recepieRepo.getOilRecipe().stream().map(Recepie::getTitle).collect(Collectors.joining("\n"));
                        questionReponse.setText(result);
                }
                case 7 -> {
                        questionContent.setText("Ecrire une méthode qui permet de calculer le nombre d’œufs par recette.");
                        result = recepieRepo.countEggsPerRecipe().entrySet().stream().map(e -> e.getKey().getTitle() + " : " + e.getValue()).collect(Collectors.joining("\n"));
                        questionReponse.setText(result);
                }
                case 8 -> {
                        questionContent.setText("Ecrire une méthode qui permet de retourner les recettes fournissant moins de 500\n" +
                            "calories.");
                        result = recepieRepo.getLess500Calories().stream().map(Recepie::getTitle).collect(Collectors.joining("\n"));
                        questionReponse.setText(result);
                }
                case 9 -> {
                    questionContent.setText("Ecrire une méthode qui retourne la quantité de sucre utilisée par la recette Zuppa Inglese");
                    result = "" + recepieRepo.getSugarOnZuppaInglese();
                    questionReponse.setText(result);
                }
                case 10 -> {
                        questionContent.setText("Ecrire une méthode qui affiche les 2 premières étapes de la recette Zuppa Inglese");
                        result = String.join("\n", recepieRepo.getTwoFirstStepOfZuppa());
                        questionReponse.setText(result);
                }
                case 11 -> {
                        questionContent.setText(" Ecrire une méthode qui retourne les recettes qui nécessitent plus de 5 étapes ");
                        result = recepieRepo.getMoreThan5Step().stream().map(Recepie::getTitle).collect(Collectors.joining("\n"));
                        questionReponse.setText(result);
                }
                case 12 -> {
                        questionContent.setText("Ecrire une méthode qui retourne les recettes qui ne contiennent pas de beure");
                        result = recepieRepo.getRecipeWithoutButter().stream().map(Recepie::getTitle).collect(Collectors.joining("\n"));
                        questionReponse.setText(result);
                }
                case 13 -> {
                        questionContent.setText("Ecrire une méthode qui retourne les recettes ayant des ingrédients en communs avec la\n" +
                                "recette Zuppa Inglese.");
                        result = recepieRepo.getRecipesWithCommonIngredients().stream().map(Recepie::getTitle).collect(Collectors.joining("\n"));
                        questionReponse.setText(result);
                }
                case 14 -> {
                    questionContent.setText("Ecrire une méthode qui affiche la recette la plus calorique.");
                    result = "" + recepieRepo.RecipieMostCaloric().getTitle();
                    questionReponse.setText(result);
                }
                case 15 -> {
                    questionContent.setText(" Ecrire une méthode qui retourne l’unité la plus fréquente");
                    result = "" + recepieRepo.mostFrequentUnit();
                    questionReponse.setText(result);
                }
                case 16 -> {
                        questionContent.setText(" Ecrire une méthode qui calcule le nombre d’ingrédients par recette ");
                        result = recepieRepo.nbIngredientPerRecipe().entrySet().stream()
                                .map(e -> e.getKey().getTitle() + " : " + e.getValue()).collect(Collectors.joining("\n"));
                        questionReponse.setText(result);
                }
                case 17 -> {
                    questionContent.setText(" Ecrire une méthode qui retourne la recette comportant le plus de fat\n");
                    result = "" + recepieRepo.getRecipeMostFat().getTitle();
                    questionReponse.setText(result);
                }
                case 18 -> {
                    questionContent.setText("Ecrire une méthode qui calcule l’ingrédient le plus utilisé ");
                    result = "" + recepieRepo.mostUsedIngredient();
                    questionReponse.setText(result);
                }
                case 19 -> {
                        questionContent.setText("Ecrire une méthode qui affiche les recettes triées par nombre d’ingrédients.\n");

                        result = recepieRepo.recipeperNbingrediant().entrySet().stream()
                                .map(e -> e.getKey() + " : " + e.getValue().stream().map(Recepie::getTitle)
                                        .collect(Collectors.joining(", "))).collect(Collectors.joining("\n"));
                        questionReponse.setText(result);
                }
                case 20 -> {
                        questionContent.setText("Ecrire une méthode qui affiche pour chaque ingrédient, les recettes qui l’utilisent.");

                        result = recepieRepo.getRecipesByIngredient().entrySet().stream()
                                .map(e -> e.getKey().getName() + " : " + e.getValue().stream()
                                        .map(Recepie::getTitle).collect(Collectors.joining(", "))).
                                collect(Collectors.joining("\n"));
                        questionReponse.setText(result);
                }
                case 21 -> {
                        questionContent.setText("Ecrire une méthode qui calcule la répartition des recettes par étape de réalisation");
                        result = recepieRepo.RecettesParNbInstructions().entrySet().stream()
                                .map(e -> e.getKey() + " : " + e.getValue().stream()
                                        .map(Recepie::getTitle).collect(Collectors.joining(", "))).
                                collect(Collectors.joining("\n"));
                        questionReponse.setText(result);
                }
                case 22 -> {
                    questionContent.setText("Ecrire une méthode qui calcule la recette la plus facile (avec le moins d’étape)\n");
                    result = "" + recepieRepo.getEasiestRecipe().getTitle();
                    questionReponse.setText(result);
                }
            }
        } catch (NumberFormatException e) {
            questionContent.setText("Erreur Syntax");
            questionReponse.setText("Please verify the textcontent of button for convertion str to int");
        }

    }
}