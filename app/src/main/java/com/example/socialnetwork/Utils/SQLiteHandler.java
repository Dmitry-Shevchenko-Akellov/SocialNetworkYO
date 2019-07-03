package com.example.socialnetwork.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SQLiteHandler extends SQLiteOpenHelper {

    private static final String TAG = SQLiteHandler.class.getSimpleName();

    private static final int DATABASE_VERSION = 14;

    private static final String DATABASE_NAME = "android_api";

    private static final String TABLE_USER = "user";
    private static final String TABLE_WALL = "wall";
    private static final String TABLE_FOLLOWING = "following";
    private static final String TABLE_SHOPS = "shops";
    private static final String TABLE_DIALOGS = "dialogs";
    private static final String TABLE_FOLLOWERS = "followers";
    private static final String TABLE_PRODUCT = "product";

    private static final String KEY_ID = "id";
    private static final String KEY_UID = "unique_id";
    private static final String KEY_WALL_ID = "wall_id";
    private static final String KEY_SHOPS_ID = "shops_id";
    private static final String KEY_FOLLOWING_ID = "following_id";
    private static final String KEY_FOLLOWERS_ID = "followers_id";
    private static final String KEY_DIALOGS_ID = "dialogs_id";
    private static final String KEY_PRODUCT_ID = "product_id";
    private static final String KEY_POSITION = "position";

    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+ KEY_UID + " TEXT NOT NULL UNIQUE"+ ")";

        String CREATE_WALL_TABLE = "CREATE TABLE " + TABLE_WALL + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+ KEY_WALL_ID + " TEXT NOT NULL UNIQUE," + KEY_POSITION + " TEXT" + ")";

        String CREATE_DIALOGS_TABLE = "CREATE TABLE " + TABLE_DIALOGS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+ KEY_DIALOGS_ID + " TEXT NOT NULL UNIQUE," + KEY_POSITION + " TEXT" + ")";

        String CREATE_SHOPS_TABLE = "CREATE TABLE " + TABLE_SHOPS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+ KEY_SHOPS_ID + " TEXT NOT NULL UNIQUE," + KEY_POSITION + " TEXT" + ")";

        String CREATE_FOLLOWING_TABLE = "CREATE TABLE " + TABLE_FOLLOWING + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+ KEY_FOLLOWING_ID + " TEXT NOT NULL UNIQUE," + KEY_POSITION + " TEXT" + ")";

        String CREATE_FOLLOWERS_TABLE = "CREATE TABLE " + TABLE_FOLLOWERS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+ KEY_FOLLOWERS_ID + " TEXT NOT NULL UNIQUE," + KEY_POSITION + " TEXT" + ")";

        String CREATE_PRODUCT_TABLE = "CREATE TABLE " + TABLE_PRODUCT + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+ KEY_PRODUCT_ID + " TEXT NOT NULL UNIQUE," + KEY_POSITION + " TEXT" + ")";

        db.execSQL(CREATE_LOGIN_TABLE);
        db.execSQL(CREATE_WALL_TABLE);
        db.execSQL(CREATE_FOLLOWING_TABLE);
        db.execSQL(CREATE_FOLLOWERS_TABLE);
        db.execSQL(CREATE_SHOPS_TABLE);
        db.execSQL(CREATE_DIALOGS_TABLE);
        db.execSQL(CREATE_PRODUCT_TABLE);

        Log.d(TAG, "Database tables created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SHOPS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FOLLOWERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FOLLOWING);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WALL);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DIALOGS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT);

        onCreate(db);
    }

    public void addUser(String uid) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_UID, uid);

        long id = db.insert(TABLE_USER, null, values);
        db.close();
        Log.d(TAG, "New user inserted into sqlite: " + id + " " + uid);
    }
    public void addWall(String wall_id, String position) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_WALL, null, null);

        ContentValues values = new ContentValues();
        values.put(KEY_WALL_ID, wall_id);
        values.put(KEY_POSITION, position);

        long id = db.insert(TABLE_WALL, null, values);
        db.close(); // Closing database connection
        Log.d(TAG, "New wall inserted into sqlite: "+ id + " "  + wall_id + " " + position);
    }

    public void addShop(String shops_id, String position) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SHOPS, null, null);

        ContentValues values = new ContentValues();
        values.put(KEY_SHOPS_ID, shops_id); // uid
        values.put(KEY_POSITION, position);

        long id = db.insert(TABLE_SHOPS, null, values);
        db.close();
        Log.d(TAG, "New shop inserted into sqlite: "+ id + " " + shops_id + " " + position);
    }
    public void addDialog(String dialogs_id, String position) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_DIALOGS, null, null);

        ContentValues values = new ContentValues();
        values.put(KEY_DIALOGS_ID, dialogs_id); // uid
        values.put(KEY_POSITION, position);

        long id = db.insert(TABLE_DIALOGS, null, values);
        db.close();
        Log.d(TAG, "New dialog inserted into sqlite: "+ id + " " + dialogs_id + " " + position);
    }
    public void addFollower(String follower_id, String position) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FOLLOWERS, null, null);

        ContentValues values = new ContentValues();
        values.put(KEY_FOLLOWERS_ID, follower_id); // uid
        values.put(KEY_POSITION, position);

        long id = db.insert(TABLE_FOLLOWERS, null, values);
        db.close(); // Closing database connection
        Log.d(TAG, "New follower inserted into sqlite: " + id + " "  + follower_id + " " + position);
    }
    public void addFollowing(String following_id, String position) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_FOLLOWING_ID, following_id); // uid
        values.put(KEY_POSITION, position);

        long id = db.insert(TABLE_FOLLOWING, null, values);
        db.close(); // Closing database connection
        Log.d(TAG, "New following inserted into sqlite: " + id + " "  + following_id + " " + position);
    }

    public void addProduct(String product_id, String position) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PRODUCT_ID, product_id); // uid
        values.put(KEY_POSITION, position);

        long id = db.insert(TABLE_PRODUCT, null, values);
        db.close(); // Closing database connection
        Log.d(TAG, "New product inserted into sqlite: " + id + " "  + product_id + " " + position);
    }

    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_USER;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put("main_id", cursor.getString(1));
        }
        cursor.close();
        db.close();
        Log.d(TAG, "Fetching user from Sqlite: " + user.toString());

        return user;
    }

    public HashMap<String, String> getUserFollowing(String userid) {
        String newId = ("'" + userid + "'");
        String selectQuery = "SELECT EXISTS(SELECT * FROM following WHERE following_id = " + newId + ");";
        Log.d(TAG, "sqlite: " + selectQuery);
        HashMap<String, String> user = new HashMap<String, String>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        Log.d(TAG, "Fetching id from Sqlite0: " + cursor.getString(0));
        if (cursor.getCount() > 0) {
            user.put("main_id", cursor.getString(0));
        }
        cursor.close();
        db.close();
        Log.d(TAG, "Fetching id from Sqlite: " + user.toString());

        return user;
    }
    public void deleteFollowing(String userid) {
        String newId = ("\"" + userid + "\"");
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("following", "following_id = " + newId, null);
    }

    public HashMap<String, String> getDialog(String userid) {
        String newId = ("\"" + userid + "\"");
        String selectQuery = "SELECT dialogs_id FROM dialogs WHERE " + KEY_POSITION + " = " + newId + ";";
        Log.d(TAG, "sqlite: " + selectQuery);
        HashMap<String, String> user = new HashMap<String, String>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put("main_id", cursor.getString(0));
        }
        cursor.close();
        db.close();
        Log.d(TAG, "Fetching id from Sqlite: " + user.toString());

        return user;
    }

    public void insertFollowing(String userid) {
        String newId = ("\"" + userid + "\"");
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("following", "following_id = " + newId, null);
    }

    public void deleteFollowingErs() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FOLLOWERS, null, null);
        db.delete(TABLE_FOLLOWING, null, null);

        Log.d(TAG, "Deleted all user info from FOllowersIng");
    }
    public void deleteProduct() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PRODUCT, null, null);

        Log.d(TAG, "Deleted all user info from FOllowersIng");
    }
    public void deleteUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USER, null, null);
        db.delete(TABLE_SHOPS, null, null);
        db.delete(TABLE_FOLLOWERS, null, null);
        db.delete(TABLE_FOLLOWING, null, null);
        db.delete(TABLE_WALL, null, null);
        db.delete(TABLE_DIALOGS, null, null);
        db.delete(TABLE_PRODUCT, null, null);
        db.close();

        Log.d(TAG, "Deleted all user info from sqlite");
    }

}
