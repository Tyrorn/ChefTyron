package Models;

import android.widget.Button;

public class Ingredients{
    private String mIngredient;
    private int mQuantity;
    private String qtyType;
    private boolean mRemove;
    private int id;

    public Ingredients(){
    }
    public Ingredients(String item, int quantity){
        this.mIngredient = item;
        this.mQuantity = quantity;
        this.mRemove = false;
    }


    public Ingredients(String item, int quantity, int id, String type){
        this.mIngredient = item;
        this.mQuantity = quantity;
        this.id = id;
        this.mRemove = false;
        this.qtyType = type;
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
    public void addQuantity(int qty){
        this.mQuantity +=qty;
    }
    public void removeQuantity(int qty){
        this.mQuantity-=qty;
    }

    public boolean ismRemove() {
        return mRemove;
    }

    public void setmRemove() {
        this.mRemove = true;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQtyType() {
        return qtyType;
    }

    public void setQtyType(String qtyType) {
        this.qtyType = qtyType;
    }
}
