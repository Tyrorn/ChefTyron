package Models;
import Models.Ingredients;
import Models.Recipe;
import java.util.ArrayList;

public class InitialRecipes {
   // String name, ArrayList<Ingredients> ingredients, String instructions, int size, int time
   // private ArrayList<Ingredients> list ;


    private int recipeCount = 2;

    private ArrayList<Recipe> RecipeList = new ArrayList<>();

    public InitialRecipes(){
        RecipeList.add(recipe1());
        RecipeList.add(recipe2());
    }

    public Recipe getRecipe(int i) {
        return RecipeList.get(i);
    }

    public Recipe recipe1(){
        Recipe recipe;
        ArrayList<Ingredients> list = new ArrayList<>();
        list.add(new Ingredients("Eggs",3,0,"unit"));
        list.add(new Ingredients("Salt",1,1,"Tsp"));
        list.add(new Ingredients("Pepper",1,2,"Tsp"));
        String instructions = "1. Break eggs into a bowl. \n 2. Add salt and pepper. \n 3. whisk then add to fry pan";
        recipe = new Recipe("Scrambled Eggs", list,instructions,1,20);

        return recipe;
    }

    public Recipe recipe2(){
        Recipe recipe;
        ArrayList<Ingredients> list = new ArrayList<>();
        list.add(new Ingredients("Noodles",1,3,"unit"));
        list.add(new Ingredients("Salt",1,4,"Tsp"));
        list.add(new Ingredients("Pepper",1,5,"Tsp"));
        String instructions = "1. Boil water and put into a pot. \n 2. Add salt and pepper. \n 3. Put noodles in pot";
        recipe = new Recipe("2 minute noodles", list,instructions,1,2);
        return recipe;
    }
    public int getRecipeCount(){
        return recipeCount;
    }
}
