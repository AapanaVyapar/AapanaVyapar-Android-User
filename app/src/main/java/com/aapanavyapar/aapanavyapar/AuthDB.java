package com.aapanavyapar.aapanavyapar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class AuthDB extends SQLiteOpenHelper {

    public static  final String DbName = "authDB";
    public static String strSeparator = "_, _";
    public AuthDB(@Nullable Context context) {
        super(context, DbName,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String q = " create table RefTokenTable (id integer primary key, Token text ,Access text )";
        db.execSQL(q);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists RefTokenTable");
        onCreate(db);

    }

    public void clearDatabase(){
        SQLiteDatabase db =this.getWritableDatabase();

        db.execSQL("drop table if exists RefTokenTable");
        onCreate(db);
        db.close();
    }

    public Boolean insertData(String token , int []arr){

        clearDatabase();
        SQLiteDatabase db =this.getWritableDatabase();

        ContentValues c=new ContentValues();

        c.put("Token",token);
        c.put("Access", convertArrayToString(arr));

        long r=db.insert("RefTokenTable",null,c);
        db.close();
        if (r==-1) return false;
        else
            return true;

    }

    public Boolean dbIsEmpty(){
        SQLiteDatabase db =this.getWritableDatabase();
        long numRows = DatabaseUtils.queryNumEntries(db, "RefTokenTable");
        db.close();
        return numRows != 1;

    }

    public Cursor getRefToken(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cur = db.rawQuery("select * from RefTokenTable",null);
        return cur;
    }

    public static String convertArrayToString(int[] arr){
        String str="";
        for(int i=0; i<arr.length; i++)
        {
            str = str+arr[i];
            if(i<arr.length-1)
            {
                str = str+strSeparator;
            }

        }
        return str;
    }
    public static int[] convertStringToArray(String str){
        String[] arr = str.split(strSeparator);
        int[] arr1 = new int[arr.length];
        for(int i=0 ;i<arr.length;i++)
        {
            arr1[i] = Integer.parseInt(arr[i]);
        }
        return arr1;
    }


}

