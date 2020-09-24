package com.example.cheftyron;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import Adapters.IngredientsAdapter;
import Models.Ingredients;

public class IngredientsPage extends AppCompatActivity {
    private static final String TAG = "IngredientsPage";
    final ArrayList<Ingredients> ingredientsList = new ArrayList<>();
    private RecyclerView rv;
    private IngredientsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients_page);
        Log.d(TAG,"onCreate:Started");
//        final ArrayList<Ingredients> stock = new ArrayList<>();
//        final ListView mListView = findViewById(R.id.ingredientListView);

        init();
        //Some Test Data for the meantime



    }
    public void init(){
        rv = findViewById(R.id.ingredientsrView);
//        for (int i = 0; i < 7; i++){
//            ingredientsList.add(new Ingredients("Tyron is awesome: "+i,i));
//        }
        mAdapter = new IngredientsAdapter(this, ingredientsList);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(mAdapter);



        Button add = findViewById(R.id.ingredientsAddButton);
        add.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                EditText ingredient =  findViewById(R.id.ingredientItemEdit);
                EditText quantity =  findViewById(R.id.ingredientQuantityEdit);
                boolean unique =true;
                try {
                    if(ingredient.getText().toString().equals("")){
                        Toast.makeText(IngredientsPage.this, "Please enter in an ingredient", Toast.LENGTH_LONG).show();
                    }
                    else {
                        for (int i = 0; i < ingredientsList.size(); i++) {
                            if (ingredient.getText().toString().equals(ingredientsList.get(i).getmIngredient())) {
                                unique = false;
                                break;
                            }
                        }
                        if (unique) {
                            ingredientsList.add(new Ingredients(ingredient.getText().toString(), Integer.parseInt(quantity.getText().toString())));
                            mAdapter.notifyDataSetChanged();
                            Toast.makeText(IngredientsPage.this, ingredient.getText().toString() + " added", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(IngredientsPage.this, "Your item " + ingredient.getText().toString() + " has already been stored", Toast.LENGTH_LONG).show();
                        }
                    }
                }
                catch(Exception e){
                    Toast.makeText(IngredientsPage.this, "Please ensure the quantity is a number", Toast.LENGTH_LONG).show();
                }



            }
        });
    }
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
                mAdapter.notifyDataSetChanged();
            }

        }

    }


    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onPause(){
        super.onPause();
    }
}