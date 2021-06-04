package com.aapanavyapar.aapanavyapar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class ProfileDB extends SQLiteOpenHelper {
    public static final String DBName= "ProfileDB";

    public ProfileDB(@Nullable Context context) {
        super(context, DBName, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String query =" create table ProfileDataTable (id integer primary key ,UserName text,FullName text,HouseDetails text,StreetDetails text,LandMark text,PinCode text,City text,State text,Countrytext text,MobileNo text)";
        db.execSQL(query);
        db.execSQL("create table FavTable (id integer primary key,ObjectId text)");
        db.execSQL("create table CartTable (id integer primary key,ObjectId text )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("delete from ProfileDataTable");
        onCreate(db);
    }

    public Boolean insert_Data(String userName,String fullName,String houseDetails,String streetDetails,String landMark,String pinCode,String city,String state,String country,String mobileNo){
        SQLiteDatabase db =this.getWritableDatabase();
        ContentValues c=new ContentValues();
        c.put("UserName",userName);
        c.put("FullName",fullName);
        c.put("HouseDetails",houseDetails);
        c.put("StreetDetails",streetDetails);
        c.put("LandMark",landMark);
        c.put("PinCode",pinCode);
        c.put("City",city);
        c.put("State",state);
        c.put("Countrytext",country);
        c.put("MobileNo",mobileNo);
       // int numRows = (int) DatabaseUtils.longForQuery(db, "SELECT COUNT(*) FROM ProfileDataTable", null);
        long numRows = DatabaseUtils.queryNumEntries(db, "ProfileDataTable");
        if (numRows>0){

            Cursor cursor = db.rawQuery("select * from ProfileDataTable where UserName=?",new String[]{userName});
            if(cursor.getCount()>0){
                long r=db.update("ProfileDataTable",c,"UserName=?",new String[]{userName});
                db.close();
                if(r==-1) return false;
                else
                    return true;

            }
            else{
                return false;
            }

        }
        else {
            long r=db.insert("ProfileDataTable",null,c);
            db.close();
            if (r==-1) return false;
            else
                return true;

        }

    }

    public Boolean profileInfo(){
        SQLiteDatabase db =this.getWritableDatabase();
        long numRows = DatabaseUtils.queryNumEntries(db, "ProfileDataTable");
        db.close();
        if(numRows>0)return true;
        else
            return false;

    }

    public Cursor getInfo(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cur = db.rawQuery("select * from ProfileDataTable",null);

        return cur;
    }

    public boolean insertToFavTable(String objectId){
        SQLiteDatabase db =this.getWritableDatabase();
        ContentValues c=new ContentValues();
        c.put("ObjectId",objectId);
        long r=db.insert("FavTable",null,c);
        if(r==-1){
            db.close();
            return false;
        }
        else {
            db.close();
            return true;
        }

    }

    public boolean deleteFromFavTable(String objId){
        SQLiteDatabase db =this.getWritableDatabase();
        long numRows = DatabaseUtils.queryNumEntries(db, "FavTable");
        if(numRows>0){
           long r = db.delete("FavTable","ObjectId=?",new String[]{objId});
           if(r==-1){
               db.close();
               return false;
           }
           else{
               db.close();
               return true;
           }
        }
        else{
            db.close();
            return false;
        }
    }


    public boolean deleteFromCartTable(String objId){
        SQLiteDatabase db =this.getWritableDatabase();
        long numRows = DatabaseUtils.queryNumEntries(db, "CartTable");
        if(numRows>0){
            long r = db.delete("CartTable","ObjectId=?",new String[]{objId});
            if(r==-1){
                db.close();
                return false;
            }
            else{
                db.close();
                return true;
            }
        }
        else{
            db.close();
            return false;
        }
    }
    public void clearFavTable(){
        SQLiteDatabase db =this.getWritableDatabase();
        db.execSQL("delete from FavTable");
        db.close();
    }


    public void clearCartTable(){
        SQLiteDatabase db =this.getWritableDatabase();
        db.execSQL("delete from CartTable");
        db.close();
    }

    public boolean insertToCartTable(String objectId){
        SQLiteDatabase db =this.getWritableDatabase();
        ContentValues c=new ContentValues();
        c.put("ObjectId",objectId);
        long r=db.insert("CartTable",null,c);
        db.close();
        if(r==-1){
            return false;
        }
        else {
            return true;
        }
    }
}
