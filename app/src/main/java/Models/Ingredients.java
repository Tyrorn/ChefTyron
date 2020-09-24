package Models;

import android.widget.Button;

public class Ingredients{
    private String mIngredient;
    private int mQuantity;
    private boolean mRemove;

    public Ingredients(String item, int quantity){
        this.mIngredient = item;
        this.mQuantity = quantity;
        this.mRemove = false;
    }


    public String getmIngredient() {
        return mIngredient;
    }

    public void setmIngredient(String mIngredient) {
        this.mIngredient = mIngredient;
    }

    public int getmQuantity() {
        return mQuantity;
    }

    public void setmQuantity(int mQuantity) {
        this.mQuantity = mQuantity;
    }
    public void Remove(){

    }

    public boolean ismRemove() {
        return mRemove;
    }

    public void setmRemove() {
        this.mRemove = true;
    }
}
