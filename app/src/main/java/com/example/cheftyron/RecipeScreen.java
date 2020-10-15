package com.example.cheftyron;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import Adapters.recipeIngredientAdapter;
import Database.DataBaseHandler;
import Models.Ingredients;
import Models.Recipe;

public class RecipeScreen extends AppCompatActivity {
    private int recipeLoc;
    private TextView rName, rQty, rCookTime,rInstructions;
    private ArrayList<Ingredients> list = new ArrayList<>();
    private RecyclerView rv;
    private recipeIngredientAdapter ingredientAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_screen);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        recipeLoc = intent.getIntExtra("pos",0);
        rv = findViewById(R.id.recyclerView);
        rName = findViewById(R.id.recipeName);
        rQty = findViewById(R.id.servingQty);
        rCookTime = findViewById(R.id.cookTime);
        rInstructions = findViewById(R.id.Instructions);

        init();
    }
    public void init(){
        Button button = findViewById(R.id.back_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecipeScreen.this, MainActivity.class);
                intent.putExtra("loc",2);
                startActivity(intent);
            }
        });
        DataBaseHandler db = new DataBaseHandler(RecipeScreen.this);
        Recipe recipe = db.getRecipe(recipeLoc);
        rName.setText(recipe.getRecipeName());
        rQty.setText(Integer.toString(recipe.getServingSize()));
        rCookTime.setText(Integer.toString(recipe.getTime()));
//
        list = db.getRecipeIngredients(recipeLoc);
        ingredientAdapter = new recipeIngredientAdapter(this,list);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(ingredientAdapter);

//
        rInstructions.setText(recipe.getInstructions());

    }
}