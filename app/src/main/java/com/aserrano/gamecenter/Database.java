package com.aserrano.gamecenter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;


public class Database extends SQLiteOpenHelper {

    private static final String TAG = Database.class.getSimpleName();

    private static final String DATABASE_NAME = "gamecenter";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE1 = "users";
    private static final String TABLE2 = "scores2048";
    private static final String TABLE3 = "scoresPEG";
    private static final String KEY_ID = "_id";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_HIGHSCORE = "highscore";
    private static final String KEY_GAME = "game";
    private static final String KEY_SCORE_2048 = "score2048";
    private static final String KEY_SCORE_PEG = "scorePEG";

    private static final String CREATE_TABLE1 =
            "CREATE TABLE " + TABLE1 + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    KEY_USERNAME + " TEXT, " +
                    KEY_EMAIL + " TEXT, " +
                    KEY_PASSWORD + " TEXT, " +
                    KEY_SCORE_2048 + " INTEGER, " +
                    KEY_SCORE_PEG + " INTEGER)";

    private static final String CREATE_TABLE2 =
            "CREATE TABLE " + TABLE2 + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    KEY_USERNAME + " TEXT, " +
                    KEY_GAME + " TEXT, " +
                    KEY_SCORE_2048 + " INTEGER, " +
                    "FOREIGN KEY (" + KEY_SCORE_2048 + ") REFERENCES " +
                    TABLE1 + "(" + KEY_ID + "))";

    private static final String CREATE_TABLE3 =
            "CREATE TABLE " + TABLE2 + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    KEY_USERNAME + " TEXT, " +
                    KEY_GAME + " TEXT, " +
                    KEY_SCORE_PEG + " INTEGER, " +
                    "FOREIGN KEY (" + KEY_SCORE_PEG + ") REFERENCES " +
                    TABLE1 + "(" + KEY_ID + "))";

    private SQLiteDatabase mWritableDB;
    private SQLiteDatabase mReadableDB;

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE1);
        //db.execSQL(CREATE_TABLE2);
        //db.execSQL(CREATE_TABLE3);
        fillDatabaseWithData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE1);
        //db.execSQL("DROP TABLE IF EXISTS " + TABLE2);
        //db.execSQL("DROP TABLE IF EXISTS " + TABLE3);
        onCreate(db);
    }

    private void fillDatabaseWithData(SQLiteDatabase db) {

        String username = "admin";
        String email = "admin@gmail.com";
        String password = "admin";
        int score2048 = 0;
        int scorePEG = 0;


        ContentValues values = new ContentValues();

        values.put(KEY_USERNAME, username);
        values.put(KEY_EMAIL, email);
        values.put(KEY_PASSWORD, password);
        values.put(KEY_SCORE_2048, score2048);
        values.put(KEY_SCORE_PEG, scorePEG);

        db.insert(TABLE1, null, values);
    }

    public boolean checkLogin(String username, String password) {

        String query = "SELECT " + KEY_USERNAME + ", " + KEY_PASSWORD +
                " FROM " + TABLE1 + " WHERE " + KEY_USERNAME +
                " = ? AND " + KEY_PASSWORD + " = ?";

        Cursor cursor = null;

        try {
            if (mReadableDB == null) {
                mReadableDB = getReadableDatabase();
            }

            cursor = mReadableDB.rawQuery(query, new String[]{username, password});

            if (cursor.getCount() > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }
        return false;
    }

    public boolean checkUsername(String username) {

        String query = "SELECT " + KEY_USERNAME + " FROM " +
                TABLE1 + " WHERE " + KEY_USERNAME + " = ?";

        Cursor cursor = null;

        try {
            if (mReadableDB == null) {
                mReadableDB = getReadableDatabase();
            }

            cursor = mReadableDB.rawQuery(query, new String[]{username});
            if (cursor.getCount() > 0) {
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }
        System.out.println(cursor.getCount());
        return false;
    }

    public String[] selectAll(String username) {

        String query = "SELECT " + KEY_USERNAME + ", " + KEY_EMAIL + " ," +
                KEY_SCORE_2048 + ", " + KEY_SCORE_PEG + " FROM " + TABLE1 +
                " WHERE " + KEY_USERNAME + " = ?";

        Cursor cursor = null;

        String[] args = new String[4];

        try {
            if (mReadableDB == null) {
                mReadableDB = getReadableDatabase();
            }
            cursor = mReadableDB.rawQuery(query, new String[]{username});
            if (cursor.moveToFirst()) {

                do {

                    args[0] = cursor.getString(0);
                    args[1] = cursor.getString(1);
                    args[2] = cursor.getString(2);
                    args[3] = cursor.getString(3);

                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return args;
    }

    public void updateScorePEG(String username, int scorePEG){

        ContentValues values = new ContentValues();
        values.put(KEY_SCORE_PEG, scorePEG);
        String[] args = new String[]{username};
        try{
            if (mWritableDB == null){
                mWritableDB = getWritableDatabase();
            }
            if (checkScorePEG(username, scorePEG)){
                System.out.println("CAMBIANDO EL SCORE DEL PEG...");
                mWritableDB.update(TABLE1, values, KEY_USERNAME + "=?", args);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private boolean checkScorePEG(String username, int scorePEG) {

        String query = "SELECT " + KEY_SCORE_PEG +
                " FROM " + TABLE1 + " WHERE " +
                KEY_USERNAME + " =?";

        Cursor cursor = null;

        try{
            if (mReadableDB == null){
                mReadableDB = getReadableDatabase();
            }
            cursor = mReadableDB.rawQuery(query, new String[]{username});

            if (cursor.moveToFirst()){
                int aux = cursor.getInt(0);
                if(aux < scorePEG){
                    return true;
                }else {
                    return false;
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return false;
    }

    public void updateScore2048(String username, int score2048) {

        ContentValues values = new ContentValues();
        values.put(KEY_SCORE_2048, score2048);
        String[] args = new String[]{username};
        try {
            if (mWritableDB == null) {
                mWritableDB = getWritableDatabase();
            }
            if (checkScore2048(username, score2048)){
                System.out.println("CAMBIANDO EL SCORE DEL 2048...");
                mWritableDB.update(TABLE1, values, KEY_USERNAME
                        + "=?", args);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean checkScore2048(String username, int score2048) {

        String query = "SELECT " + KEY_SCORE_2048 +
                " FROM " + TABLE1 +
                " WHERE " + KEY_USERNAME + " =?";

        Cursor cursor = null;
        try {
            if (mReadableDB == null) {
                mReadableDB = getReadableDatabase();
            }

            cursor = mReadableDB.rawQuery(query, new String[]{username});

            if (cursor.moveToFirst()) {
                int aux = cursor.getInt(0);
                if(aux < score2048) {
                    return true;
                }
            } else {
                return false;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("HEY 3");
        return false;
    }

    public boolean insert(String username, String email, String password, int score2048, int scorePEG) {

        long newId = 0;

        ContentValues values = new ContentValues();
        values.put(KEY_USERNAME, username);
        values.put(KEY_EMAIL, email);
        values.put(KEY_PASSWORD, password);
        values.put(KEY_SCORE_2048, score2048);
        values.put(KEY_SCORE_PEG, scorePEG);

        try {
            if (mWritableDB == null) {
                mWritableDB = getWritableDatabase();
            }

            newId = mWritableDB.insert(TABLE1, null, values);
            if (newId == -1) {
                return false;
            } else {
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updatePassword(String username, String password){

        ContentValues values = new ContentValues();
        values.put(KEY_PASSWORD, password);
        String[] args = new String[]{username};

        try{
            if (mWritableDB == null){
                mWritableDB = getWritableDatabase();
            }

            if (checkPasswordRepeat(username, password)){
                System.out.println("CAMBIANDO LA CONTRASEÑA");
                mWritableDB.update(TABLE1, values, KEY_USERNAME + "=?", args);
                return true;

            }else{
                System.out.println("LA CONTRASEÑA ES IGUAL");
                return false;
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    private boolean checkPasswordRepeat(String username, String password) {

        String query = "SELECT " + KEY_PASSWORD +
                " FROM " + TABLE1 + " WHERE " +
                KEY_USERNAME + " = ?";

        Cursor cursor = null;
        try{
            if (mReadableDB == null){
                mReadableDB = getReadableDatabase();
            }
            cursor = mReadableDB.rawQuery(query, new String[]{username});

            if (cursor.moveToFirst()){
                String aux = cursor.getString(0);
                if (aux.equals(password)){
                    return false;
                }
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return true;
    }

    public Cursor getData(){

        String query = "SELECT " + KEY_USERNAME + ", " +
                KEY_SCORE_2048 + ", " + KEY_SCORE_PEG +
                " FROM " + TABLE1;

        Cursor cursor = null;

        try{
            if (mReadableDB == null){
                mReadableDB = getReadableDatabase();
            }

            cursor = mReadableDB.rawQuery(query, null);
            return cursor;

        }catch (Exception e){
            e.printStackTrace();
        }
        return cursor;
    }

    public boolean deleteScore(String username){

        ContentValues values = new ContentValues();
        values.put(KEY_SCORE_2048, 0);
        values.put(KEY_SCORE_PEG, 0);
        String[] args = new String[]{username};

        try{
            if (mWritableDB == null){
                mWritableDB = getWritableDatabase();
            }

            mWritableDB.update(TABLE1, values,KEY_USERNAME + "=?", args);
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public void deleteUser(String username){

        String[] args = new String[]{username};

        try{
            if (mWritableDB == null){
                mWritableDB = getWritableDatabase();
            }
            mWritableDB.delete(TABLE1, KEY_USERNAME + "=?", args);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
