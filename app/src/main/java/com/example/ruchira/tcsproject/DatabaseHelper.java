package com.example.ruchira.tcsproject;

/**
 * Created by Ruchira on 11-07-2017.
 */



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.alicebot.ab.Contact;

/**
 * Created by USER1 on 07-07-2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION =1;
    private static final String DATABASE_NAME ="contacts.db";
    private static final String TABLE_NAME ="contacts";
    private static final String COLUMN_ID ="id";
    private static final String COLUMN_NAME ="name";
    private static final String COLUMN_EMAIL ="email";
    private static final String COLUMN_MOB ="mob";
    private static final String COLUMN_UNAME ="uname";
    private static final String COLUMN_PASS ="pass";
    // private static final String COLUMN_BALANCE = "balance";

    SQLiteDatabase db;
    private static final String TABLE_CREATE = "create table contacts ( id integer primary key not null ," + "name text not  null ,email text not null , mob text not null, uname text not null ,pass text not null,balance integer);";

    public DatabaseHelper(Context context)
    {
        super(context , DATABASE_NAME ,null ,DATABASE_VERSION );

    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
        this.db=db;

    }
    public void insertContact(com.example.ruchira.tcsproject.Contact c)
    {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        String query = "select * from contacts";
        Cursor cursor = db.rawQuery(query ,null);
        int count= cursor.getCount();
        values.put(COLUMN_ID ,count);
        values.put(COLUMN_NAME ,c.getName());
        values.put(COLUMN_EMAIL ,c.getEmail());
        values.put(COLUMN_MOB ,c.getMob());
        values.put(COLUMN_UNAME ,c.getUname());
        values.put(COLUMN_PASS ,c.getPass());
        // values.put(COLUMN_BALANCE,2000);

        db.insert(TABLE_NAME , null ,values);
        db.close();


    }
    public String getDataForChat(String username){
        db=this.getReadableDatabase();
        String query="select * from contacts";
        Cursor cursor = db.rawQuery(query,null);
        //com.example.ruchira.tcsproject.Contact c1 =new com.example.ruchira.tcsproject.Contact();
        String data1=" ";
        if(cursor.moveToFirst()){
            do {

                if(username.equals(cursor.getString(4))) {


                    data1 = cursor.getString(2);
                    break;

                }
            }while(cursor.moveToNext());

        }
        cursor.close();
        return data1;
    }

    public String getAcc(String username){
        db=this.getReadableDatabase();
        String query="select * from contacts";
        Cursor cursor = db.rawQuery(query,null);
        //com.example.ruchira.tcsproject.Contact c1 =new com.example.ruchira.tcsproject.Contact();
        String data1=" ";
        if(cursor.moveToFirst()){
            do {

                if(username.equals(cursor.getString(4))) {


                    data1 = cursor.getString(0)+100;
                    break;

                }
            }while(cursor.moveToNext());

        }
        cursor.close();
        return data1;
    }
    public void getnewbalance(String balance,String username){
        db=this.getReadableDatabase();
        String query="select * from contacts";
        Cursor cursor = db.rawQuery(query,null);
        //com.example.ruchira.tcsproject.Contact c1 =new com.example.ruchira.tcsproject.Contact();
        // String data1=" ";
        if(cursor.moveToFirst()){
            do {

                if(username.equals(cursor.getString(4))) {

                    com.example.ruchira.tcsproject.Contact c1= new com.example.ruchira.tcsproject.Contact();
                    ContentValues values = new ContentValues();
                    c1.setEmail(balance);
                    values.put(COLUMN_EMAIL,c1.getEmail());

                    break;

                }
            }while(cursor.moveToNext());

        }
        cursor.close();


    }
    public String searchPass(String uname)
    {
        String user= uname;
        db= this.getReadableDatabase();
        String query ="select uname, pass from "+TABLE_NAME;
        Cursor cursor =db.rawQuery(query ,null);
        String a,b;
        b= "not found";
        if(cursor.moveToFirst())

        {
            do {
                a= cursor.getString(0);

                if( a.equals(uname))
                {
                    b=cursor.getString(1);
                    break;
                }

            } while(cursor.moveToNext());
        }
        return b;
    }

    public String searchUser(String uname)
    {

        db= this.getReadableDatabase();
        String query ="select uname from "+TABLE_NAME;
        Cursor cursor =db.rawQuery(query ,null);
        String a,b;
        b= "";
        if(cursor.moveToFirst())

        {
            do {
                a= cursor.getString(0);
                if( a.equals(uname))
                {
                    b=cursor.getString(0);
                    break;
                }

            } while(cursor.moveToNext());
        }
        return b;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query="DROP TABLE IF EXISTS"+TABLE_NAME;
        db.execSQL(query);
        this.onCreate(db);
        /**  if (newVersion > oldVersion) {
         db.execSQL("ALTER TABLE contacts ADD COLUMN balance INTEGER ");
         }**/

    }
}

