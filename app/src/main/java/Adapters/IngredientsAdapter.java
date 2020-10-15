package Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cheftyron.R;

import java.util.ArrayList;
import java.util.List;

import Database.DataBaseHandler;
import Models.Ingredients;


public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.MyViewHolder> {
    private ArrayList<Ingredients>mList;
    private Context mContext;
//    private onUpdateingredients listener;
//, onUpdateingredients listener
    public IngredientsAdapter(Context context, ArrayList<Ingredients> list){
        this.mList = list;
        this.mContext = context;
//        this.listener = listener;
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
                int index = mList.get(position).getId();
                int quantity = mList.get(position).getmQuantity();
                quantity +=1;
                mList.get(position).setmQuantity(quantity);
                updateItem(position);
                holder.Qty.setText(Integer.toString(mList.get(position).getmQuantity()));


            }
        });
        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int index = mList.get(position).getId();
                if(mList.get(position).getmQuantity() == 0){
                    deleteItem(index);
                    mList.remove(position);
                    notifyDataSetChanged();
                }
                else {
                    mList.get(position).removeQuantity(1);
                    holder.Qty.setText(Integer.toString(mList.get(position).getmQuantity()));
                    updateItem(position);
                }



            }
        });
        holder.Ingredient.setText(mList.get(position).getmIngredient());
        holder.qtyType.setText(mList.get(position).getQtyType());

        holder.Qty.setText(Integer.toString(mList.get(position).getmQuantity()));
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView Ingredient;
        public TextView Qty;
        public TextView qtyType;
        public Button add;
        public Button remove;

        public MyViewHolder(View v){
            super(v);
            add = v.findViewById(R.id.ingredientsAdd);
            remove = v.findViewById(R.id.ingredientsRemove);
            qtyType = v.findViewById(R.id.qtyType);
            Ingredient = v.findViewById(R.id.ingredientsItemText);
            Qty = v.findViewById(R.id.ingredientsQuantityText);

        }
    }

    public void updateItem(int id){
        DataBaseHandler db = new DataBaseHandler(mContext);
        Ingredients ingredients = mList.get(id);
        db.updateIngredient(ingredients);

    }

    public void deleteItem(int id){
        DataBaseHandler db = new DataBaseHandler(mContext);
        db.deleteIngredient(id);
    }
}
