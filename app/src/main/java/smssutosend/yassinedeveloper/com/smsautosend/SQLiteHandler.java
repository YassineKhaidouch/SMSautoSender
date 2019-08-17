package smssutosend.yassinedeveloper.com.smsautosend;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;

//import android.util.Log;

public class SQLiteHandler extends SQLiteOpenHelper {

//    private static final String TAG = SQLiteHandler.class.getSimpleName();

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "android_api";
    // Login table name
    private static final String TABLE_USER = "USER";
    // Login Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NUMBER = "number";
    private static final String KEY_CALLTYPE = "calltype";
    private static final String KEY_CALLDATE = "calldate";
    private static final String KEY_STATUS = "status";

    private static final String TABLE_TETX_MESSAGE = "TEXT";
    // Login Table Columns names
    private static final String KEY_TEXT = "text";
    private static final String KEY_TIMEOUT = "timeout";
    private static final String KEY_STARTD = "startdate";
    private static final String KEY_STARTT = "starttime";
    private static final String KEY_ENDD = "enddate";
    private static final String KEY_ENDT = "endtime";


    // TEST TABLE   UNIQUE
    // Server table
    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                KEY_NUMBER + " TEXT ,"+
                KEY_CALLTYPE + " TEXT ,"+
                KEY_CALLDATE + " TEXT," +
                KEY_STATUS + " TEXT )";
        db.execSQL(CREATE_LOGIN_TABLE);


        String CREATE_TEXT_TABLE = "CREATE TABLE " + TABLE_TETX_MESSAGE + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                KEY_NUMBER + " TEXT,"+
                KEY_TEXT + " TEXT ,"+
                KEY_STARTD + " TEXT ,"+
                KEY_STARTT + " TEXT ,"+
                KEY_ENDD + " TEXT ,"+
                KEY_ENDT + " TEXT ,"+
                KEY_TIMEOUT + " TEXT NOT NULL)";
        db.execSQL(CREATE_TEXT_TABLE);

        //Log.d(TAG, "Database tables created");
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TETX_MESSAGE);
        // Create tables again
        onCreate(db);
    }
    /**
     * Storing user details in database
     * */
    public void addData( String number, String calltype, String calldate, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_NUMBER, number);
        values.put(KEY_CALLTYPE, calltype);
        values.put(KEY_CALLDATE, calldate);
        values.put(KEY_STATUS, status);
        // Inserting Row
        db.insert(TABLE_USER, null, values);
        db.close(); // Closing database connection
        //Log.d(TAG, "New user inserted into sqlite: " + id);
    }

    public void addTextMessage(String number, String text,String startdate,String starttime,String enddate,String endtime ,String timeout) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_NUMBER, number);
        values.put(KEY_TEXT, text);
        values.put(KEY_STARTD, startdate);
        values.put(KEY_STARTT, starttime);
        values.put(KEY_ENDD,enddate);
        values.put(KEY_ENDT,endtime);
        values.put(KEY_TIMEOUT, timeout);
        // Inserting Row
        db.insert(TABLE_TETX_MESSAGE, null, values);
        db.close(); // Closing database connection
        //Log.d(TAG, "New user inserted into sqlite: " + id);
    }

    public HashMap<String, String> getTextMessage() {
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_TETX_MESSAGE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put("id", cursor.getString(0));
            user.put("number", cursor.getString(1));
            user.put("text", cursor.getString(2));
            user.put("startdate", cursor.getString(3));
            user.put("starttime", cursor.getString(4));
            user.put("enddate", cursor.getString(5));
            user.put("endtime", cursor.getString(6));
            user.put("timeout", cursor.getString(7));
        }
        cursor.close();
        db.close();
        // return user
        //Log.d(TAG, "Fetching user from Sqlite: " + user.toString());
        return user;
    }
    public void update(String id, String number, String calltype, String calldate, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
       // values.put(KEY_ID, id); // Name
        values.put(KEY_NUMBER, number);
        values.put(KEY_CALLTYPE , calltype);
        values.put(KEY_CALLDATE , calldate);
        values.put(KEY_STATUS, status);
        // Inserting Row
        db.update(TABLE_USER, values, KEY_ID + " = " + id, null);
        db.close(); // Closing database connection
    }

    public Cursor fetch() {
        String[] columns = new String[] { KEY_ID, KEY_NUMBER, KEY_CALLTYPE, KEY_CALLDATE, KEY_STATUS };
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USER, columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public void deleteUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_USER, null, null);
        db.close();
        //Log.d(TAG, "Deleted all user info from sqlite");
    }
    public void deleteTextMessage() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_TETX_MESSAGE, null, null);
        db.close();
        //Log.d(TAG, "Deleted all user info from sqlite");
    }
}