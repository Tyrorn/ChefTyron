package Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cheftyron.MainActivity;
import com.example.cheftyron.R;
import com.example.cheftyron.RecipeScreen;

import org.w3c.dom.Text;

import java.util.ArrayList;

import Models.Ingredients;
import Models.Recipe;
import Models.onCheckClick;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Recipe> recipes;
    private ArrayList<Ingredients> ingredientsList;
    private onCheckClick listener;
    private onRecipeListener recipeListener;

    public RecipeAdapter(Context context, ArrayList<Recipe> recipe, onCheckClick listener, onRecipeListener listen){
        this.context = context;
        this.recipes = recipe;
        this.listener = listener;
        this.recipeListener = listen;



    }

    @NonNull
    @Override
    public RecipeAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipes, parent,false);



        return new RecipeAdapter.MyViewHolder(v, recipeListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeAdapter.MyViewHolder holder, final int position) {
        holder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()){
                    listener.onClick(recipes.get(position).getIngredientsList(), true);

                }
                else{
                    listener.onClick(recipes.get(position).getIngredientsList(), false);
                }
            }
        });



        holder.rname.setText(recipes.get(position).getRecipeName());
        holder.rSize.setText(Integer.toString(recipes.get(position).getServingSize()));
        holder.time.setText(Integer.toString(recipes.get(position).getTime()));


    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public CheckBox checkbox;
        public TextView rname;
        public TextView rSize;
        public ImageView image;
        public TextView time;
        onRecipeListener listens;

        public MyViewHolder(@NonNull View v, onRecipeListener listener) {
            super(v);
            checkbox = v.findViewById(R.id.recipeCheckBox);
            rname = v.findViewById(R.id.recipeName);
            rSize = v.findViewById(R.id.recipeSize);
            time = v.findViewById(R.id.recipeTime);
            this.listens = listener;
           // image = v.findViewById(R.id.recipePicture);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listens.onRecipeClick(getAdapterPosition());
        }
    }
    public interface onRecipeListener{
        void onRecipeClick(int position);
    }
}
