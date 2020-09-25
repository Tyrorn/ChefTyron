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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import Adapters.IngredientsAdapter;
import Adapters.RecipeAdapter;
import Models.Ingredients;
import Models.Recipe;

public class MainActivity extends AppCompatActivity {

    final ArrayList<Ingredients> ingredientsList = new ArrayList<>();
    private RecyclerView rv;
    private IngredientsAdapter ingredientsAdapter;
    private AlertDialog.Builder popupBuilder;
    private AlertDialog popup;
    final ArrayList<Recipe> recipesList = new ArrayList<>();
    private RecipeAdapter recipeAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        rv = findViewById(R.id.rViewMain);
        // create data for ingredients list-----
        for (int i = 0; i < 6; i++){
            ingredientsList.add(new Ingredients("Tyron is awesome: "+i,i));

        }
        //------------------------
        //create data for recipes IMPORTANT!!! ----------------------- name, ingredients, instructions, serving size

        for (int t = 0;t<6;t++){
            recipesList.add(new Recipe("sample"+t,ingredientsList,"test for now", t));

        }
        recipeAdapter = new RecipeAdapter(this, recipesList);


        //------------------------------------------------------------

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
        Button groceries = findViewById(R.id.GroceryListButton);
        groceries.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
            }
        });
        //------------------------------------------------------------------------------------------

    }

    public void recipesInit() {
        recipeAdapter.notifyDataSetChanged();
        CheckBox check;

    }

    //Use Ingredients adapter for the Main activity RecyclerView------------------------------------
    void ingredientsInit(){
        FloatingActionButton fab = findViewById(R.id.fab);
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
                            ingredientsList.add(new Ingredients(groceryItem.getText().toString(), Integer.parseInt(qty.getText().toString())));
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

    //
    public void ingredientsRemoveHandler(View view) {
        LinearLayout vwparentrow = (LinearLayout) view.getParent();
        TextView child = (TextView)vwparentrow.getChildAt(0);
        String item = child.getText().toString();
        int position;

        Toast.makeText(this, item +" deleted", Toast.LENGTH_LONG).show();
        for (int i = 0; i < ingredientsList.size(); i++){
            if (ingredientsList.get(i).getmIngredient().equals(item)){
                position = i;
                ingredientsList.remove(position);
                ingredientsAdapter.notifyDataSetChanged();
            }

        }
//
    }




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