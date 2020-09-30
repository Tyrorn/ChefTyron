package Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.cheftyron.MainActivity;

import java.sql.PreparedStatement;
import java.util.ArrayList;

import Models.Ingredients;
import Models.Recipe;

public class DataBaseHandler extends SQLiteOpenHelper {
    public static String databaseName = "user_database";
    private Context context;
    private static final int DATABASE_VERSION = 1;
    //TABLES
    private static final String TABLE_INGREDIENTS = "ingredients";
    private static final String TABLE_RECIPES = "recipes";
    private static final String TABLE_RECIPE_INGREDIENTS = "recipes_ingredients";

    //INGREDIENTS COLUMNS
    private static final String INGREDIENTS_KEY_ID ="id";
    private static final String INGREDIENT_QTY = "ingredient_quantity";
    private static final String INGREDIENT_NAME = "ingredient_name";

    //RECIPE INGREDIENTS COLUMNS
    private static final String RKEY_ID = "id";
    private static final String RECIPE_INGREDIENT = "recipe_ingredient";
    private static final String RECIPE_QTY = "recipe_QTY";
    private static final String RECIPE_ID = "recipe_id";


    //RECIPES COLUMNS
    private static final String RECIPE_KEY_ID ="id";
    private static final String RECIPE_NAME = "recipe_name";
    private static final String RECIPE_SERVING = "serving";
    private static final String RECIPE_TIME = "time";
    private static final String RECIPE_INSTRUCTIONS = "instructions";


    public DataBaseHandler(@Nullable Context context) {
        super(context,databaseName,null,DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Create Ingredients/pantry table
        String Create_table_ingredients = "CREATE TABLE " + TABLE_INGREDIENTS + "("
                + INGREDIENTS_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                 + INGREDIENT_NAME + " TEXT,"
                + INGREDIENT_QTY + " TEXT);";

        db.execSQL(Create_table_ingredients);

        //Create Recipes table
        String Create_table_recipes = "CREATE TABLE " + TABLE_RECIPES
                + "(" + RECIPE_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + RECIPE_SERVING + " TEXT,"
                + RECIPE_NAME + " TEXT,"
                + RECIPE_TIME + " TEXT,"
                + RECIPE_INSTRUCTIONS + " TEXT);";


        db.execSQL(Create_table_recipes);

        //Create Recipe_Ingredients table
        String Create_table_recipe_ingredients = "CREATE TABLE " + TABLE_RECIPE_INGREDIENTS + "("
                + RKEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + RECIPE_ID + " TEXT,"
                + RECIPE_QTY + " TEXT,"
                + RECIPE_INGREDIENT + " TEXT);";

        db.execSQL(Create_table_recipe_ingredients);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INGREDIENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPE_INGREDIENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPES);

        onCreate(db);
    }
    //Ingredients table methods

    public void addIngredient(Ingredients newIngredient){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
       // values.put(INGREDIENTS_KEY_ID,newIngredient.getId());
        values.put(INGREDIENT_NAME, newIngredient.getmIngredient());
        values.put(INGREDIENT_QTY, newIngredient.getmQuantity());
        db.insert(TABLE_INGREDIENTS,null,values);

        Log.d("saved","Saved to DB");

    }
    public Ingredients getIngredient(int id){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(TABLE_INGREDIENTS,new String[] {INGREDIENTS_KEY_ID,INGREDIENT_NAME,INGREDIENT_QTY}
        , INGREDIENTS_KEY_ID +"=?",
                new String[]{String.valueOf(id)},null,null,null);

        if(cursor != null)
            cursor.moveToFirst();

            Ingredients ingredient = new Ingredients();
            ingredient.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(INGREDIENTS_KEY_ID))));
            ingredient.setmIngredient(cursor.getString(cursor.getColumnIndex(INGREDIENT_NAME)));
            ingredient.setmQuantity(Integer.parseInt(cursor.getString(cursor.getColumnIndex(INGREDIENT_QTY))));


        return ingredient;
    }

    public ArrayList<Ingredients> getAllIngredients(){
        SQLiteDatabase db = this.getReadableDatabase();

        ArrayList<Ingredients> list = new ArrayList<>();
        Cursor cursor = db.query(TABLE_INGREDIENTS,new String[] {INGREDIENTS_KEY_ID,INGREDIENT_NAME,INGREDIENT_QTY}
                ,null,
                null,null,null,null);

        if(cursor.moveToFirst()){
            do {
                Ingredients ingredient = new Ingredients();
                ingredient.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(INGREDIENTS_KEY_ID))));
                ingredient.setmIngredient(cursor.getString(cursor.getColumnIndex(INGREDIENT_NAME)));
                ingredient.setmQuantity(Integer.parseInt(cursor.getString(cursor.getColumnIndex(INGREDIENT_QTY))));
                list.add(ingredient);
            }while(cursor.moveToNext());
        }
        return list;
    }

    public int updateIngredient(Ingredients ingredient){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(INGREDIENTS_KEY_ID,ingredient.getId());
        values.put(INGREDIENT_NAME, ingredient.getmIngredient());
        values.put(INGREDIENT_QTY, ingredient.getmQuantity());

        return db.update(TABLE_INGREDIENTS,values, INGREDIENTS_KEY_ID + "=?",new String[]{String.valueOf(ingredient.getId())});

    }

    public void deleteIngredient(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_INGREDIENTS, INGREDIENTS_KEY_ID + "=?",
                new String[]{String.valueOf(id)});
       // db.close();
    }

    public int IngredientsCount(){
        String countQuery = "SELECT * FROM " + TABLE_INGREDIENTS;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(countQuery,null);
        return cursor.getCount();
    }


    //Recipe methods

    public void addRecipe(Recipe recipe){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(RECIPE_NAME, recipe.getRecipeName());
        values.put(RECIPE_INSTRUCTIONS, recipe.getInstructions());
        values.put(RECIPE_SERVING, recipe.getServingSize());
        values.put(RECIPE_TIME, recipe.getTime());
        db.insert(TABLE_RECIPES,null,values);

//        addRecipeIngredients(recipe);

    }
    public Recipe getRecipe(int id){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(TABLE_RECIPES,new String[] {RECIPE_KEY_ID,RECIPE_NAME,RECIPE_SERVING,RECIPE_TIME,RECIPE_INSTRUCTIONS}
                , RECIPE_KEY_ID +"=?",
                new String[]{String.valueOf(id)},null,null,null);

        if (cursor!=null)
            cursor.moveToFirst();
            Recipe recipe = new Recipe();
            recipe.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(RECIPE_KEY_ID))));
            recipe.setRecipeName(cursor.getString(cursor.getColumnIndex(RECIPE_NAME)));
            recipe.setServingSize(Integer.parseInt(cursor.getString(cursor.getColumnIndex(RECIPE_SERVING))));
            recipe.setInstructions(cursor.getString(cursor.getColumnIndex(RECIPE_INSTRUCTIONS)));
            recipe.setTime(Integer.parseInt(cursor.getString(cursor.getColumnIndex(RECIPE_TIME))));

        return recipe;
    }
    public ArrayList<Recipe> getAllRecipes(){
        SQLiteDatabase db = this.getReadableDatabase();

        ArrayList<Recipe> list = new ArrayList<>();
        Cursor cursor = db.query(TABLE_RECIPES,new String[] {RECIPE_KEY_ID,RECIPE_NAME,RECIPE_SERVING,RECIPE_TIME,RECIPE_INSTRUCTIONS}
                , null,
               null,null,null,null);

        if(cursor.moveToFirst()){
            do {
                Recipe recipe = new Recipe();
                recipe.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(RECIPE_KEY_ID))));
                recipe.setRecipeName(cursor.getString(cursor.getColumnIndex(RECIPE_NAME)));
                recipe.setServingSize(Integer.parseInt(cursor.getString(cursor.getColumnIndex(RECIPE_SERVING))));
                recipe.setInstructions(cursor.getString(cursor.getColumnIndex(RECIPE_INSTRUCTIONS)));
                recipe.setTime(Integer.parseInt(cursor.getString(cursor.getColumnIndex(RECIPE_TIME))));

                list.add(recipe);
            }while(cursor.moveToNext());
        }
        return list;

    }

//    private static final String RKEY_ID = "id";
//    private static final String RECIPE_INGREDIENT = "recipe_ingredient";
//    private static final String RECIPE_QTY = "recipe_QTY";
//    private static final String RECIPE_ID = "recipe_id";

    //Recipe Ingredient Section
    public void addRecipeIngredients(int id, ArrayList<Ingredients> list){
        SQLiteDatabase db = this.getWritableDatabase();

        for (int i =0; i<list.size(); i++){
            ContentValues values = new ContentValues();
            values.put(RECIPE_ID, id);
            values.put(RECIPE_INGREDIENT, list.get(i).getmIngredient());
            values.put(RECIPE_QTY,list.get(i).getmQuantity());
            db.insert(TABLE_RECIPE_INGREDIENTS,null,values);
        }
    }

    public ArrayList<Ingredients> getRecipeIngredients(int id){
        SQLiteDatabase db = this.getReadableDatabase();

        ArrayList<Ingredients> list = new ArrayList<>();

        Cursor cursor = db.query (TABLE_RECIPE_INGREDIENTS, new String[]{RECIPE_KEY_ID, RECIPE_INGREDIENT,RECIPE_QTY}, RECIPE_ID + "=?",
                new String[]{String.valueOf(id)},null,null,null);


        if(cursor.moveToFirst())
            do {
                Ingredients ingredient = new Ingredients();
              //  ingredient.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(RE))));
                ingredient.setmIngredient(cursor.getString(cursor.getColumnIndex(RECIPE_INGREDIENT)));
                ingredient.setmQuantity(Integer.parseInt(cursor.getString(cursor.getColumnIndex(RECIPE_QTY))));

                list.add(ingredient);
            }while(cursor.moveToNext());

        return list;
    }
}

