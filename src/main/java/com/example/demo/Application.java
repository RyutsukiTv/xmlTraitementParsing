package com.example.demo;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import repositories.RecepieRepo;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("main.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 640, 430);
        stage.setTitle("YouCook Application");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void sleepSecond(int time){
        try {
            TimeUnit.SECONDS.sleep(time);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void generateText(String text , String color,int time) throws InterruptedException {
        for (int i = 0; i < text.length(); i++) {
            System.out.print(color+text.charAt(i)+ANSI_RESET);
            Thread.sleep(time); // pause de 100 millisecondes
        }
        System.out.print("\n");
    }
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";

    public static void main(String[] args) throws InterruptedException {
        RecepieRepo recepieRepo = new RecepieRepo();
        recepieRepo.init("recipes.xml");
        boolean isActif1 = true;
        boolean isActif2 = true;
        isActif1 = true;
        isActif2 = true;
        System.out.println(ANSI_YELLOW+" __  __          _____          __  \n" +
                " \\ \\/ /__  __ __/ ___/__  ___  / /__\n" +
                "  \\  / _ \\/ // / /__/ _ \\/ _ \\/  '_/\n" +
                "  /_/\\___/\\_,_/\\___/\\___/\\___/_/\\_\\ "+ANSI_RESET);

        while (isActif1){
            isActif1 = true;
            isActif2 = true;
            generateText("S??lectionner le mode de pr??sentation du r??sultat:",ANSI_CYAN,30);
            Scanner sc = new Scanner(System.in);
            System.out.println(ANSI_YELLOW+"1: Interface Terminal       2: Interface Graphique      0: Quitter"+ANSI_RESET);
            if (sc.hasNextInt()){
                int str = sc.nextInt();
                sleepSecond(1);
                switch (str){
                    case 0:
                        generateText("Au revoir et merci d'avoir essay?? YouCook",ANSI_PURPLE,10);
                        sleepSecond(1);
                        isActif1 = false;
                        isActif2 = false;
                        break;
                    case 1:
                        System.out.println(ANSI_BLUE+"Interface Terminal"+ANSI_RESET);
                        sleepSecond(2);
                        while(isActif2){
                            Scanner question = new Scanner(System.in);
                            generateText("Veuillez renseigner le numero de la question: (0: Quitter, 1: Retours)",ANSI_CYAN,20);
                            System.out.println(ANSI_CYAN+"Les questions vont de 4 ?? 22."+ANSI_RESET);
                            if (question.hasNextInt()){
                                int questionstr = question.nextInt();
                                if(questionstr!=0||questionstr!=1){
                                    System.out.println(ANSI_PURPLE+"Vous avez selectionn?? : "+questionstr+ANSI_RESET);
                                    sleepSecond(1);
                                }
                                if(questionstr==0){
                                    System.out.println(ANSI_RED+"Fermeture du Programme "+ANSI_RESET);
                                }
                                if(questionstr==1){
                                    System.out.println(ANSI_RED+"Retours"+ANSI_RESET);
                                }
                                sleepSecond(1);
                                switch (questionstr){
                                    case 0:
                                        generateText("Au revoir et merci d'avoir essay?? YouCook",ANSI_PURPLE,10);
                                        sleepSecond(1);
                                        isActif1 = false;
                                        isActif2 = false;
                                        break;
                                    case 1:
                                        isActif2 = false;
                                        break;
                                    case 4:
                                        //m??thode titre des recettes
                                        System.out.println("Titre des recettes :");
                                        recepieRepo.getAllTitles().forEach(System.out::println);
                                        break;
                                    case 5:
                                        //m??thode qui calcule le nombre d'oeufs utilis??s
                                        System.out.println("Nombre d'oeufs utilis??s : "+recepieRepo.countEggs());
                                        break;
                                    case 6:
                                        //m??thode qui retourne les recettes avec de l'huile d'olive
                                        System.out.println("Recettes avec de l'huile d'olive :");
                                        recepieRepo.getOilRecipe().forEach(r->System.out.println(r.getTitle()));
                                        break;
                                    case 7:
                                        //m??thode qui calcul le nombre d'oeuf par recette
                                        System.out.println("Nombre d'oeufs par recette :");
                                        recepieRepo.countEggsPerRecipe().forEach((k,v)->System.out.println(k.getTitle()+" : "+v));
                                        break;
                                    case 8:
                                        //m??thode qui retourne les recettes ?? moins de 500 calories
                                        System.out.println("Recettes ?? moins de 500 calories :");
                                        recepieRepo.getLess500Calories().forEach(r->System.out.println(r.getTitle()));
                                        break;
                                    case 9:
                                        //m??thode qui retourne la quantit?? de sucre dans la recette Zuppa Inglese
                                        System.out.println("Quantit?? de sucre dans la recette Zuppa Inglese : "+recepieRepo.getSugarOnZuppaInglese());
                                        break;
                                    case 10:
                                        //m??thode qui affiche les deux premi??res ??tapes de la recette Zuppa Inglese
                                        System.out.println("Deux premi??res ??tapes de la recette Zuppa Inglese :");
                                        recepieRepo.getTwoFirstStepOfZuppa().forEach(System.out::println);
                                        break;
                                    case 11:
                                        //m??thode qui retourne les recettes avec plus de 5 ??tapes
                                        System.out.println("Recettes avec plus de 5 ??tapes :");
                                        recepieRepo.getMoreThan5Step().forEach(r->System.out.println(r.getTitle()));
                                        break;
                                    case 12:
                                        //m??thode qui retourne les recettes qui ne contiennent pas de beure
                                        System.out.println("Recettes sans beurre :");
                                        recepieRepo.getRecipeWithoutButter().forEach(r->System.out.println(r.getTitle()));
                                        break;
                                    case 13:
                                        //m??thode qui retourne les recettes ayant des ingr??dients en commun avec la recette Zuppa Inglese
                                        System.out.println("Recettes ayant des ingr??dients en commun avec la recette Zuppa Inglese :");
                                        recepieRepo.getRecipesWithCommonIngredients().forEach(r->System.out.println(r.getTitle()));
                                        break;
                                    case 14:
                                        //afficher la recette la plus calorique
                                        System.out.println("Recettes la plus calorique:");
                                        System.out.println(recepieRepo.RecipieMostCaloric().getTitle());
                                        break;
                                    case 15:
                                        //retourne l'unit?? la plus fr??quente
                                        System.out.println("L'unit?? la plus fr??quente est : "+recepieRepo.mostFrequentUnit());

                                        break;
                                    case 16:
                                        //nombre d'ingr??dient par recette
                                        System.out.println("Nombre d'ingr??dients par recette :");
                                        recepieRepo.nbIngredientPerRecipe().forEach((k,v)->System.out.println(k.getTitle()+" : "+v));

                                        break;
                                    case 17:
                                        //recette comportant le plus de fat
                                        System.out.println("Recette comportant le plus de fat : " + recepieRepo.getRecipeMostFat().getTitle());

                                        break;
                                    case 18:

                                        //ingr??dient le plus utilis??
                                        System.out.println("Ingr??dient le plus utilis?? : "+recepieRepo.mostUsedIngredient());

                                        break;
                                    case 19:
                                        //Affiche les recettes tri??es par nombre d'ingr??dients
                                        System.out.println("Recettes tri??es par nombre d'ingr??dients :");
                                        recepieRepo.nbIngredientPerRecipe().forEach((k,v)->System.out.println(k.getTitle()+" : "+v));
                                        break;
                                    case 20:
                                        //Afficher pour chaque ingr??dient les recettes qui l'utilisent
                                        System.out.println("Afficher pour chaque ingr??dient les recettes qui l'utilisent :");
                                        recepieRepo.getRecipesByIngredient();
                                        break;
                                    case 21:
                                        //calcule la r??partition des recettes par ??tape de r??alisation
                                        System.out.println("R??partition des recettes par ??tape de r??alisation :");
                                        recepieRepo.RecettesParNbInstructions().forEach((k,v)->System.out.println(k+" ??tapes : "+v.stream().map(r->r.getTitle()).collect(Collectors.toList())));
                                        break;
                                    case 22:
                                        //calcule la recette la plus facile (avec le moins d?????tape)
                                        System.out.println("Recette la plus facile : "+recepieRepo.getEasiestRecipe().getTitle());
                                        break;
                                    default:
                                        System.out.println(ANSI_RED+"Valeur invalide: Les questions vont de 4 ?? 22."+ANSI_RESET);
                                        sleepSecond(1);
                                        break;
                                }
                                sleepSecond(1);
                            }else {
                                System.out.println(ANSI_RED+"Valeur Invalide"+ANSI_RESET);
                            }
                        }
                        break;
                    case 2:
                        System.out.println(ANSI_BLUE+"Interface Graphique"+ANSI_RESET);
                        launch();
                        generateText("Au revoir et merci d'avoir essay?? YouCook",ANSI_PURPLE,10);
                        sleepSecond(1);
                        isActif1 = false;
                        break;
                }
            }else {
                System.out.println(ANSI_RED+"Valeur Invalide"+ANSI_RESET);
            }

        }

    }
}