package Models;

import java.util.ArrayList;

public class Recipe {
    private String recipeName;
    private ArrayList<Ingredients> ingredientsList;
    private  String instructions;
    private int servingSize;

    public Recipe (String name, ArrayList<Ingredients> ingredients, String instructions, int size){
        this.recipeName = name;
        this.ingredientsList = ingredients;
        this.instructions = instructions;
        this.servingSize = size;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public ArrayList<Ingredients> getIngredientsList() {
        return ingredientsList;
    }

    public void setIngredientsList(ArrayList<Ingredients> ingredientsList) {
        this.ingredientsList = ingredientsList;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public int getServingSize() {
        return servingSize;
    }

    public void setServingSize(int servingSize) {
        this.servingSize = servingSize;
    }
}
