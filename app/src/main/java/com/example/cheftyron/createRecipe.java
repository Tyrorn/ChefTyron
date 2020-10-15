package com.example.cheftyron;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import Adapters.recipeIngredientAdapter;
import Database.DataBaseHandler;
import Models.Ingredients;
import Models.Recipe;

public class createRecipe extends AppCompatActivity {
    private ArrayList<Ingredients> ingredients_list;
    private AlertDialog.Builder popupBuilder;
    private AlertDialog popup;
    private recipeIngredientAdapter ingredientsAdapter;
    private RecyclerView rv;

    //Views
    private Button addRecipe;
    private EditText rname,rsize,rtime,rinstructions;
    private String[] measurements =new String[]{"Tsp", "Tbsp", "ml", "mg","Unit"};
    private String qtyType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_recipe);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ingredients_list = new ArrayList<>();
        rv = findViewById(R.id.recipeIngredientRV);
        rv.setLayoutManager(new LinearLayoutManager(this));
        ingredientsAdapter = new recipeIngredientAdapter(this, ingredients_list);
        rv.setAdapter(ingredientsAdapter);
        addRecipe = findViewById(R.id.finishButton);
        rname = findViewById(R.id.recipeName);
        rsize = findViewById(R.id.recipeServingSize);
        rtime = findViewById(R.id.recipeCookTime);
        rinstructions = findViewById(R.id.recipeTextBox);


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createIngredientsPopupDialog();

            }
        });
        addRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean save = true;
                String name = rname.getText().toString();
                int time =-1;
                int Servingsize =-1;
                if (!rtime.getText().toString().equals("")){
                    time = Integer.parseInt(rtime.getText().toString());
                }

                if(!rsize.getText().toString().equals("")) {
                    Servingsize = Integer.parseInt(rsize.getText().toString());
                }


                String instructions = rinstructions.getText().toString();


                if(name.equals("")){
                    Toast.makeText(createRecipe.this,"Please give the recipe a name",Toast.LENGTH_LONG).show();
                    save = false;
                }
                if(Servingsize <=0){
                    Toast.makeText(createRecipe.this,"Please enter an appropriate serving size",Toast.LENGTH_LONG).show();
                    save = false;
                }
                if(time<=0){
                    Toast.makeText(createRecipe.this,"Please enter an appropriate cooking time",Toast.LENGTH_LONG).show();
                    save = false;
                }
                if(instructions.equals("")){
                    Toast.makeText(createRecipe.this,"Please write the instructions",Toast.LENGTH_LONG).show();
                    save = false;
                }
                if(ingredients_list.size()<=0){
                    Toast.makeText(createRecipe.this,"Please add ingredients",Toast.LENGTH_LONG).show();
                    save = false;
                }
                if(save){
                    DataBaseHandler db = new DataBaseHandler(createRecipe.this);
                    db.addRecipe(new Recipe(name,ingredients_list,instructions,Servingsize,time));
                    db.addRecipeIngredients(db.getRecipesCount(),ingredients_list);
                    Intent intent = new Intent(createRecipe.this,MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void createIngredientsPopupDialog(){
        final EditText groceryItem,qty;

        Spinner spinner;

        popupBuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.ingredientspopup,null);
        groceryItem = view.findViewById(R.id.ingredientsAddText);
        qty = view.findViewById(R.id.ingredientsQuantityText);
        Button add = view.findViewById(R.id.popupAdd);

        spinner = (Spinner)view.findViewById(R.id.popUpSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(view.getContext(),R.array.measurements_array,R.layout.support_simple_spinner_dropdown_item);
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
                    if(groceryItem.getText().toString().equals("")){
                        Toast.makeText(createRecipe.this, "Please enter in an ingredient", Toast.LENGTH_LONG).show();
                    }
                    else {
                        for (int i = 0; i < ingredients_list.size(); i++) {
                            if (groceryItem.getText().toString().equals(ingredients_list.get(i).getmIngredient())) {
                                unique = false;
                                break;
                            }
                        }
                        if (unique) {
                            // ingredientCount +=1;
                            Ingredients ingredient = new Ingredients(groceryItem.getText().toString(), Integer.parseInt(qty.getText().toString()));
                            ingredient.setQtyType(qtyType);
                            ingredients_list.add(ingredient);
                            ingredientsAdapter.notifyDataSetChanged();
                            Toast.makeText(createRecipe.this, groceryItem.getText().toString() + " added", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(createRecipe.this, "Your item " + groceryItem.getText().toString() + " has already been stored", Toast.LENGTH_LONG).show();
                        }
                    }
                }
                catch(Exception e){
                    Toast.makeText(createRecipe.this, "Please ensure the quantity is a number", Toast.LENGTH_LONG).show();
                }

                popup.dismiss();
            }
        });
    }
}