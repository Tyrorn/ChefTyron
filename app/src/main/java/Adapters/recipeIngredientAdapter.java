package Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cheftyron.R;

import java.util.ArrayList;

import Models.Ingredients;

public class recipeIngredientAdapter extends RecyclerView.Adapter<recipeIngredientAdapter.MyViewHolder> {
    private ArrayList<Ingredients> mList;
    private Context context;

    public recipeIngredientAdapter(@NonNull Context context, ArrayList<Ingredients> list) {
        this.context = context;
        this.mList = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_ingredient, parent,false);

        return new recipeIngredientAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.name.setText(mList.get(position).getmIngredient());

        holder.quantity.setText(Integer.toString(mList.get(position).getmQuantity()));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView quantity;
        public MyViewHolder(@NonNull View v) {
            super(v);
            name = v.findViewById(R.id.ingredientName);
            quantity = v.findViewById(R.id.ingredientQuantity);

        }
    }
}
