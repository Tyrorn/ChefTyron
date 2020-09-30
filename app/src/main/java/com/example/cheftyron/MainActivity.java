package com.example.cheftyron;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import android.widget.EditText;

import android.widget.Toast;

import java.util.ArrayList;

import Adapters.IngredientsAdapter;
import Adapters.RecipeAdapter;
import Database.DataBaseHandler;
import Models.Ingredients;
import Models.Recipe;
import Models.onCheckClick;


public class MainActivity extends AppCompatActivity implements onCheckClick{

    private ArrayList<Ingredients> ingredientsList = new ArrayList<>();
    private RecyclerView rv;
    private IngredientsAdapter ingredientsAdapter;
    private AlertDialog.Builder popupBuilder;
    private AlertDialog popup;

    private ArrayList<Recipe> recipesList = new ArrayList<>();
    private RecipeAdapter recipeAdapter;

    private ArrayList<Ingredients> tempGroceriesList = new ArrayList<>();
    private ArrayList<Ingredients> groceriesList = new ArrayList<>();
    private IngredientsAdapter groceriesAdapter;


    private FloatingActionButton fab ;
    private ArrayList<Ingredients> testList = new ArrayList<>();

    private DataBaseHandler db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        db = new DataBaseHandler(this);
        rv = findViewById(R.id.rViewMain);

        // Pull data for ingredients and Recipes----------------------------------------------------
        ArrayList<Ingredients> temp = new ArrayList<>();
        temp = db.getAllIngredients();
        for (Ingredients i : temp){
            Ingredients ingredients = new Ingredients();
            ingredients.setId(i.getId());
            ingredients.setmIngredient(i.getmIngredient());
            ingredients.setmQuantity(i.getmQuantity());
            ingredientsList.add(ingredients);
        }
       // ingredientCount = db.IngredientsCount();


        testList.add(new Ingredients("eggs", 4,1));
        testList.add(new Ingredients("milk", 2,2));
        testList.add(new Ingredients("sausages", 2,3));
        testList.add(new Ingredients("crater", 2,4));
        testList.add(new Ingredients("why tho", 3,5));

        //------------------------
      //  create data for recipes IMPORTANT!!! ----------------------- name, ingredients, instructions, serving size
//        for (int t = 1;t<6;t++){
//            db.addRecipe(new Recipe("sample "+t,testList,"test for now", t,30,t));
//            db.addRecipeIngredients(db.getRecipe(t).getId(),testList);
//           // recipesList.add(new Recipe("sample "+t,testList,"test for now", t,30,t));
//        }
        ArrayList<Recipe> temp1;
        temp1 = db.getAllRecipes();
        for (int i=0; i<temp1.size();i++){
            Recipe recipe = new Recipe();
            recipe.setId(temp1.get(i).getId());
            recipe.setTime(temp1.get(i).getTime());
            recipe.setServingSize(temp1.get(i).getServingSize());
            recipe.setInstructions(temp1.get(i).getInstructions());
            recipe.setRecipeName(temp1.get(i).getRecipeName());
            recipe.setIngredientsList(db.getRecipeIngredients(recipe.getId()));
            recipesList.add(recipe);
        }
//        for (Recipe i : temp1){
//
//        }
        recipeAdapter = new RecipeAdapter(this, recipesList, this);



        //------------------------------------------------------------
        groceriesAdapter = new IngredientsAdapter(this,groceriesList);
        ingredientsAdapter = new IngredientsAdapter(this, ingredientsList);
        rv.setLayoutManager(new LinearLayoutManager(this));
        init();
    }


    void init(){

        //Traversing Adapters ----------------------------------------------------------------------

        //Ingredients--------------------------------------
        Button ingredients = findViewById(R.id.ingredientsButton);
        ingredients.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                rv.setAdapter(ingredientsAdapter);
                ingredientsInit();
            }
        });
        // Recipes------------------------------------------
        Button recipe = findViewById(R.id.RecipesButton);
        recipe.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                rv.setAdapter(recipeAdapter);
                recipesInit();
            }
        });
        //Grocery list---------------------------------------
        final Button groceries = findViewById(R.id.GroceryListButton);
        groceries.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                rv.setAdapter(groceriesAdapter);
                groceriesInit();

            }
        });
        //------------------------------------------------------------------------------------------

    }

    public void groceriesInit(){
        fab = findViewById(R.id.fab);
        fab.setVisibility(View.VISIBLE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                groceriesList.clear();
                groceriesAdapter.notifyDataSetChanged();
            }
        });

    }
    public void recipesInit() {
        fab = findViewById(R.id.fab);
        fab.setVisibility(View.VISIBLE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createGroceries();
            }
        });

    }

    //Use Ingredients adapter for the Main activity RecyclerView------------------------------------
    void ingredientsInit(){
        fab = findViewById(R.id.fab);
        fab.setVisibility(View.VISIBLE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createIngredientsPopupDialog();
            }
        });



        ingredientsAdapter.notifyDataSetChanged();
    }
    //----------------------------------------------------------------------------------------------


    // Pop up to add more ingredients to current inventory -----------------------------------------
    private void createIngredientsPopupDialog(){
        final EditText groceryItem,qty;

        popupBuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.ingredientspopup,null);
        groceryItem = view.findViewById(R.id.ingredientsAddText);
        qty = view.findViewById(R.id.ingredientsQuantityText);
        Button add = view.findViewById(R.id.popupAdd);

        popupBuilder.setView(view);
        popup = popupBuilder.create();
        popup.show();

        add.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                boolean unique =true;
                try {
                    if(groceryItem.getText().toString().equals("")){
                        Toast.makeText(MainActivity.this, "Please enter in an ingredient", Toast.LENGTH_LONG).show();
                    }
                    else {
                        for (int i = 0; i < ingredientsList.size(); i++) {
                            if (groceryItem.getText().toString().equals(ingredientsList.get(i).getmIngredient())) {
                                unique = false;
                                break;
                            }
                        }
                        if (unique) {
                           // ingredientCount +=1;
                            Ingredients ingredient = new Ingredients(groceryItem.getText().toString(), Integer.parseInt(qty.getText().toString()));
                            ingredientsList.add(ingredient);
                            db.addIngredient(ingredient);
                            ingredientsAdapter.notifyDataSetChanged();
                            Toast.makeText(MainActivity.this, groceryItem.getText().toString() + " added", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Your item " + groceryItem.getText().toString() + " has already been stored", Toast.LENGTH_LONG).show();
                        }
                    }
                }
                catch(Exception e){
                    Toast.makeText(MainActivity.this, "Please ensure the quantity is a number", Toast.LENGTH_LONG).show();
                }

                popup.cancel();
            }
        });


    }


    // --------------------------------------------------------------------------------------------

    //Grocery stuff--------------------------------

    //See if item is in list, return index if found-----------------------
    public int findItem(ArrayList<Ingredients> list, String ingredient){
        for (int i=0;i<list.size();i++){
            if(list.get(i).getmIngredient().equals(ingredient)){
                return i;
            }
        }
        return -1;
    }
    //-------------------------------------------------------------------

    //Create temporary list of all desired ingredients from selected recipes------------------------
    @Override
    public void onClick(ArrayList<Ingredients> ingredients, boolean b) {
        if (b){
            boolean found = false;
            for (int i =0; i<ingredients.size();i++){
                for (int j =0; j<tempGroceriesList.size();j++){
                    if (ingredients.get(i).getmIngredient().equals(tempGroceriesList.get(j).getmIngredient())){
                        tempGroceriesList.get(j).addQuantity(ingredients.get(i).getmQuantity());
                        found = true;
                    }
                }
                if (!found) {
                    tempGroceriesList.add(new Ingredients(ingredients.get(i).getmIngredient(),ingredients.get(i).getmQuantity(),0));
                }
            }

//            Toast.makeText(this, Integer.toString(tempGroceriesList.get(0).getmQuantity()), Toast.LENGTH_LONG).show();
        }
        else{
            for (int i =0; i<ingredients.size();i++){
                for (int j =0; j<tempGroceriesList.size();j++){
                    if (ingredients.get(i).getmIngredient().equals(tempGroceriesList.get(j).getmIngredient())){
                        tempGroceriesList.get(j).removeQuantity(ingredients.get(i).getmQuantity());
                    }
                }
            }
        }


    }
    //----------------------------------------------------------------------------------------------

    //Take temporary list and create groceries list -----------------------------------------------
    public void createGroceries(){
        groceriesList.clear();
        for (int i =0;i<tempGroceriesList.size();i++){
            boolean found = false;
            for (int t=0; t<ingredientsList.size();t++ ){
                if (tempGroceriesList.get(i).getmIngredient().equals(ingredientsList.get(t).getmIngredient())){
                    if (tempGroceriesList.get(i).getmQuantity() > ingredientsList.get(t).getmQuantity()){
                        int difference = tempGroceriesList.get(i).getmQuantity() - ingredientsList.get(t).getmQuantity();
                        int index =findItem(groceriesList,tempGroceriesList.get(i).getmIngredient());
                        if ( index != -1){
                            if(groceriesList.size()== 0){
                                groceriesList.add(new Ingredients(tempGroceriesList.get(i).getmIngredient(),difference,0));
                            }
                            else{
                                groceriesList.get(index).addQuantity(difference);
                            }
                        }
                        else{
                            groceriesList.add(new Ingredients(tempGroceriesList.get(i).getmIngredient(),difference,0));
                        }
                    }

                    found = true;
                    break;
                }

            }
            if(!found){

                int index = findItem(groceriesList,tempGroceriesList.get(i).getmIngredient());
                if(index != -1){
                    groceriesList.get(index).addQuantity(2);


                }
                else{
                    groceriesList.add(new Ingredients(tempGroceriesList.get(i).getmIngredient(),tempGroceriesList.get(i).getmQuantity(),0));
                }
            }
        }
        tempGroceriesList.clear();
    groceriesAdapter.notifyDataSetChanged();
    }
    //-----------------------------------------------------------------------------------------------


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



}