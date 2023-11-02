package com.example.chatz;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.chatz.Models.Users;

import java.util.ArrayList;

public class DBhelper extends SQLiteOpenHelper {
    private static final String DB_NAME  = "CHATZDATABASE";
    private static final int DB_VERSION  = 2;
    private static final String TABLE_NAME = "USERS";
    private static final String USERID = "USERID";
    private static final String EMAIL = "EMAIL";
    private static final String USERNAME = "username";
    private static final String PROFILEPIC = "profilepic";

    // for table 2

    private static final String tablename2 = "USERSMESSAGES";
    private  static final String sender = "SENDER";
    private  static final String receiver = "RECEIVER";
    private  static final String message = "MESSAGE";

    public DBhelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table " + TABLE_NAME +
                "(" + USERID + " TEXT PRIMARY KEY," + EMAIL + " TEXT," + USERNAME + " TEXT," + PROFILEPIC + " TEXT " +")");

        db.execSQL("create table " + tablename2 + " ( " +
                sender  + " text," +
                message + " text," +
                receiver + " text " + ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL(" DROP TABLE IF EXISTS "+ TABLE_NAME);

        onCreate(db);

    }
    public void addusers(String userid , String email , String username , String profilepic)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USERID,userid);
        contentValues.put(EMAIL,email);
        contentValues.put(USERNAME,username);
        contentValues.put(PROFILEPIC,profilepic);
        db.insert(TABLE_NAME,null,contentValues);

    }
    public ArrayList<Users> fetchdata()
    {
        ArrayList<Users> arrayList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME,null);
        while(cursor.moveToNext())
        {
            Users users = new Users();
            users.setUserid(cursor.getString(0));
            users.setEmail(cursor.getString(1));
            users.setUsername(cursor.getString(2));
            users.setProfilepic(cursor.getString(3));
            arrayList.add(users);
        }
        return arrayList;
    }


    public void addMessage(String sender_id,String mess ,String receiver_id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(sender,sender_id);
        contentValues.put(message,mess);
        contentValues.put(receiver,receiver_id);
        db.insert(tablename2,null,contentValues);
    }
}

