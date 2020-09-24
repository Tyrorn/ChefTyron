package Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cheftyron.R;

import java.util.ArrayList;
import java.util.List;

import Models.Ingredients;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.MyViewHolder> {
    private ArrayList<Ingredients>mList;
    private Context mContext;

    public IngredientsAdapter(Context context, ArrayList<Ingredients> list){
        mList = list;
        mContext = context;
    }
    @NonNull
    @Override
    public IngredientsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ingredients, parent,false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final IngredientsAdapter.MyViewHolder holder, final int position) {
        holder.add.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                int quantity = mList.get(position).getmQuantity();
                quantity +=1;
                mList.get(position).setmQuantity(quantity);
                holder.Qty.setText(Integer.toString(mList.get(position).getmQuantity()));

            }
        });

        holder.Ingredient.setText(mList.get(position).getmIngredient());

        holder.Qty.setText(Integer.toString(mList.get(position).getmQuantity()));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView Ingredient;
        public TextView Qty;
        public Button add;
        public Button remove;
        public MyViewHolder(View v){
            super(v);
            add = v.findViewById(R.id.ingredientsAdd);
            remove = v.findViewById(R.id.ingredientsRemove);

            Ingredient = v.findViewById(R.id.ingredientsItemText);
            Qty = v.findViewById(R.id.ingredientsQuantityText);

        }
    }
}
