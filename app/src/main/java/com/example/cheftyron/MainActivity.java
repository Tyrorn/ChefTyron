package com.example.cheftyron;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.PersistableBundle;
import android.view.LayoutInflater;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;

import android.widget.EditText;

import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import Adapters.IngredientsAdapter;
import Adapters.RecipeAdapter;
import Adapters.recipeIngredientAdapter;
import Database.DataBaseHandler;
import Models.Ingredients;
import Models.InitialRecipes;
import Models.Recipe;
import Models.onCheckClick;


public class MainActivity extends AppCompatActivity implements onCheckClick, RecipeAdapter.onRecipeListener {

    private ArrayList<Ingredients> ingredientsList = new ArrayList<>();
    private RecyclerView rv;
    private IngredientsAdapter ingredientsAdapter;
    private AlertDialog.Builder popupBuilder;
    private AlertDialog popup;
    private TextView welcome;

    private ArrayList<Recipe> recipesList = new ArrayList<>();
    private RecipeAdapter recipeAdapter;

    private ArrayList<Ingredients> tempGroceriesList = new ArrayList<>();
    private ArrayList<Ingredients> groceriesList = new ArrayList<>();
    private recipeIngredientAdapter groceriesAdapter;

    private int adapterUsed;

    private Spinner spinner;
    private String[] measurements =new String[]{"Tsp", "Tbsp", "ml", "mg","unit"};
    private String qtyType ;


    private FloatingActionButton fab ;
   // private ArrayList<Ingredients> testList = new ArrayList<>();

    private DataBaseHandler db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        db = new DataBaseHandler(this);
        rv = findViewById(R.id.rViewMain);
        welcome = findViewById(R.id.Welcome);
        welcome.setVisibility(View.VISIBLE);

        // Pull data for ingredients----------------------------------------------------
        ArrayList<Ingredients> temp = new ArrayList<>();
        temp = db.getAllIngredients();
        for (Ingredients i : temp){
            Ingredients ingredients = new Ingredients();
            ingredients.setId(i.getId());
            ingredients.setmIngredient(i.getmIngredient());
            ingredients.setmQuantity(i.getmQuantity());
            ingredients.setQtyType(i.getQtyType());
            ingredientsList.add(ingredients);
        }




        //Pull Recipes database------------------------------------
        ArrayList<Recipe> temp1;
        temp1 = db.getAllRecipes();
        if (db.getRecipesCount() <=0){
            InitialRecipes recipes = new InitialRecipes();
            for (int i=0;i<recipes.getRecipeCount();i++){
                Recipe recipe;
                recipe = recipes.getRecipe(i);
                db.addRecipe(recipe);
                db.addRecipeIngredients(db.getRecipe(i+1).getId(),recipe.getIngredientsList());
            }

        }
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

        //------------------------------------------------------------

        //set Adapters
       // groceriesAdapter = new IngredientsAdapter(this,groceriesList);
        groceriesAdapter = new recipeIngredientAdapter(this,groceriesList);
        ingredientsAdapter = new IngredientsAdapter(this, ingredientsList);
        recipeAdapter = new RecipeAdapter(this, recipesList, this,this);
        //------------------------------------------------------------

        rv.setLayoutManager(new LinearLayoutManager(this));

        //restore adapter state if screen rotates----
        if(savedInstanceState!=null){

            adapterUsed = savedInstanceState.getInt("ADAPTER_USED");
            switch(adapterUsed){
                case 1:
                    ingredientsInit();
                    break;
                case 2:
                    recipesInit();
                    break;
                case 3:
                    groceriesInit();
                    break;
            }
        }
        Intent intent = getIntent();
        if(intent.getIntExtra("loc",0) == 2){
            recipesInit();

        };
        //---------------------------------------------
        init();
    }


    void init(){

        //Traversing Adapters ----------------------------------------------------------------------



        //Ingredients--------------------------------------
        Button ingredients = findViewById(R.id.ingredientsButton);
        ingredients.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                ingredientsInit();
            }
        });
        // Recipes------------------------------------------
        Button recipe = findViewById(R.id.RecipesButton);
        recipe.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                recipesInit();
            }
        });
        //Grocery list---------------------------------------
        final Button groceries = findViewById(R.id.GroceryListButton);
        groceries.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){


                groceriesInit();

            }
        });
        //------------------------------------------------------------------------------------------

    }

    public void groceriesInit(){
        //Toast.makeText(MainActivity.this,"whyyy!!!",Toast.LENGTH_SHORT).show();
        rv.setAdapter(groceriesAdapter);
        adapterUsed =3;
        welcome.setVisibility(View.INVISIBLE);

        Button button = findViewById(R.id.centerButton);
        button.setVisibility(View.INVISIBLE);
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
        rv.setAdapter(recipeAdapter);
        adapterUsed =2;
        recipeAdapter.notifyDataSetChanged();
        welcome.setVisibility(View.INVISIBLE);

        fab = findViewById(R.id.fab);
        fab.setVisibility(View.VISIBLE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, createRecipe.class);
                startActivity(intent);
            }
        });

        Button button = findViewById(R.id.centerButton);
        button.setVisibility(View.VISIBLE);
        button.setText(R.string.createGroceries);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createGroceries();
            }
        });

    }

    //Use Ingredients adapter for the Main activity RecyclerView------------------------------------
    void ingredientsInit(){
        welcome.setVisibility(View.INVISIBLE);
        adapterUsed =1;

        rv.setAdapter(ingredientsAdapter);
        Button button = findViewById(R.id.centerButton);
        button.setVisibility(View.INVISIBLE);
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
   public void createIngredientsPopupDialog(){
        final EditText groceryItem,qty;



        popupBuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.ingredientspopup,null);
//        final LayoutInflater layout = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        groceryItem = view.findViewById(R.id.ingredientsAddText);
        qty = view.findViewById(R.id.ingredientsQuantityText);
        Button add = view.findViewById(R.id.popupAdd);

        spinner = (Spinner)view.findViewById(R.id.popUpSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(view.getContext(),R.array.measurements_array,R.layout.support_simple_spinner_dropdown_item);
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext(),R.layout.ingredientspopup,R.layout.,measurements);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                qtyType = measurements[i];
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });


        popupBuilder.setView(view);
        popup = popupBuilder.create();
        popup.show();

        add.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                boolean unique =true;
                try {
                    //if grocery name is empty
                    if(groceryItem.getText().toString().equals("")){
                        Toast.makeText(MainActivity.this, "Please enter in an ingredient", Toast.LENGTH_LONG).show();
                    }
                    else {
                        //Check if grocery is unique to avoid double ups
                        for (int i = 0; i < ingredientsList.size(); i++) {
                            if (groceryItem.getText().toString().equals(ingredientsList.get(i).getmIngredient())) {
                                unique = false;
                                break;
                            }
                        }
                        //if unique
                        if (unique) {
                           // ingredientCount +=1;
                            Ingredients ingredient = new Ingredients(groceryItem.getText().toString(), Integer.parseInt(qty.getText().toString()));
                            ingredient.setQtyType(qtyType);
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
            //add ingredients to groceries list
            boolean found = false;
            for (int i =0; i<ingredients.size();i++){
                for (int j =0; j<tempGroceriesList.size();j++){
                    if (ingredients.get(i).getmIngredient().equals(tempGroceriesList.get(j).getmIngredient())){
                        tempGroceriesList.get(j).addQuantity(ingredients.get(i).getmQuantity());
                        found = true;
                    }
                }
                if (!found) {
                    tempGroceriesList.add(new Ingredients(ingredients.get(i).getmIngredient(),ingredients.get(i).getmQuantity(),0, ingredients.get(i).getQtyType()));

                }
            }


        }
        else{
            //remove ingredients from groceries list
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
                                groceriesList.add(new Ingredients(tempGroceriesList.get(i).getmIngredient(),difference,0, tempGroceriesList.get(i).getQtyType()));
                            }
                            else{
                                groceriesList.get(index).addQuantity(difference);
                            }
                        }
                        else{
                            groceriesList.add(new Ingredients(tempGroceriesList.get(i).getmIngredient(),difference,0, tempGroceriesList.get(i).getQtyType()));
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
                    groceriesList.add(new Ingredients(tempGroceriesList.get(i).getmIngredient(),tempGroceriesList.get(i).getmQuantity(),0, tempGroceriesList.get(i).getQtyType()));
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

    @Override
    protected void onResume() {
        super.onResume();

    }


    @Override
    protected void onPause(){
        super.onPause();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        savedInstanceState.putInt("ADAPTER_USED",adapterUsed);
        super.onSaveInstanceState(savedInstanceState);
    }


    @Override
    public void onRecipeClick(int position) {
        Intent intent = new Intent(this, RecipeScreen.class);
        intent.putExtra("pos",position+1);
        startActivity(intent);
    }
}