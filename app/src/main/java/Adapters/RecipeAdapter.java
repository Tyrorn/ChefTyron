package Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cheftyron.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

import Models.Ingredients;
import Models.Recipe;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Recipe> recipes;

    public RecipeAdapter(Context context, ArrayList<Recipe> recipe ){
        this.context = context;
        this.recipes = recipe;


    }

    @NonNull
    @Override
    public RecipeAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipes, parent,false);

        return new RecipeAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeAdapter.MyViewHolder holder, int position) {
        holder.rname.setText(recipes.get(position).getRecipeName());
        holder.rSize.setText(Integer.toString(recipes.get(position).getServingSize()));

    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public CheckBox checkbox;
        public TextView rname;
        public TextView rSize;
        public ImageView image;

        public MyViewHolder(@NonNull View v) {
            super(v);
            checkbox = v.findViewById(R.id.recipeCheckBox);
            rname = v.findViewById(R.id.recipeName);
            rSize = v.findViewById(R.id.recipeSize);
            image = v.findViewById(R.id.recipePicture);

        }
    }
}
