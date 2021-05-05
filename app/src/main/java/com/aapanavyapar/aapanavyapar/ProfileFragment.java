package com.aapanavyapar.aapanavyapar;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class ProfileFragment extends Fragment {

    EditText userName,fullName,houseDetails,streetDetails,landMark,pinCode,city,state,country,mobileNo;
    String  SQLiteDataBaseQueryHolder,UserName,FullName,HouseDetails,StreetDetails,LandMark,PinCode,City,State,Country,MobileNum;
    SQLiteDatabase sqLiteDatabaseObj;
    Boolean EditTextEmptyHold;
    Button Add;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){
    userName = (EditText)view.findViewById(R.id.profile_edittext_user_name);
    fullName = (EditText)view.findViewById(R.id.profile_edit_text_fullname);
    houseDetails = (EditText)view.findViewById(R.id.profile_edit_text_housedetails);
    streetDetails = (EditText)view.findViewById(R.id.profile_edit_text_streetdetails);
    landMark = (EditText)view.findViewById(R.id.profile_edit_text_landmark);
    pinCode = (EditText)view.findViewById(R.id.profile_edit_text_pincode);
    city = (EditText)view.findViewById(R.id.profile_edit_text_city);
    state = (EditText)view.findViewById(R.id.profile_edit_text_state);
    country = (EditText)view.findViewById(R.id.profile_edit_text_country);
    mobileNo = (EditText)view.findViewById(R.id.profile_edit_text_mobileno);
    Add = (Button)view.findViewById(R.id.profile_button);

    Add.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            SQLiteDataBaseBuild();

            SQLiteTableBuild();

            CheckEditTextStatus();

            InsertDataIntoSQLiteDatabase();

            EmptyEditTextAfterDataInsert();

        }
    });

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }
    public void SQLiteDataBaseBuild(){

        sqLiteDatabaseObj = sqLiteDatabaseObj.openOrCreateDatabase("ProfileDB", (SQLiteDatabase.CursorFactory) getContext(),null);

    }
    public void SQLiteTableBuild(){

        sqLiteDatabaseObj.execSQL("CREATE TABLE ProfileTable(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, User_Name VARCHAR,Full_Name VARCHAR,House_Details VARCHAR,Street_Details,VHARCHAR,Land_Mark VARCHAR,Pincode VARCHAR,City VARCHAR,State VARCHAR,Country VARCHAR, Phone_Number VARCHAR);");

    }
    public void CheckEditTextStatus(){
        UserName = userName.getText().toString();
        FullName = fullName.getText().toString();
        HouseDetails = houseDetails.getText().toString();
        StreetDetails = streetDetails.getText().toString();
        LandMark = landMark.getText().toString();
        PinCode = pinCode.getText().toString();
        City = city.getText().toString();
        State = state.getText().toString();
        Country = country.getText().toString();
        MobileNum = mobileNo.getText().toString();


        if(TextUtils.isEmpty(FullName) ||
                TextUtils.isEmpty(UserName)||TextUtils.isEmpty(HouseDetails)||
                TextUtils.isEmpty(StreetDetails)||TextUtils.isEmpty(LandMark)||
                TextUtils.isEmpty(PinCode)||TextUtils.isEmpty(City)||
                TextUtils.isEmpty(State)||TextUtils.isEmpty(Country)||
                TextUtils.isEmpty(MobileNum)){

            EditTextEmptyHold = false ;

        }
        else {

            EditTextEmptyHold = true ;
        }
    }
    public void InsertDataIntoSQLiteDatabase(){

        if(EditTextEmptyHold == true)
        {

            SQLiteDataBaseQueryHolder = "INSERT INTO ProfileTable (User_Name,Full_Name,House_Detais,Street_Details,Land_Mark,Pincode,City,State,Country,Phone_Number) VALUES" +
                    "('"+UserName+"', '"+FullName+"', '"+HouseDetails+"', '"+StreetDetails+"', '"+LandMark+"','"+PinCode+"', '"+City+"','"+State+"','"+Country+"','"+MobileNum+"');";

            sqLiteDatabaseObj.execSQL(SQLiteDataBaseQueryHolder);

            Toast.makeText(getContext(),"Data Inserted Successfully", Toast.LENGTH_LONG).show();

        }
        else {

            Toast.makeText(getContext(),"Please Fill All The Required Fields.", Toast.LENGTH_LONG).show();

        }

    }
    public void EmptyEditTextAfterDataInsert(){

        userName.getText().clear();

        fullName.getText().clear();
        houseDetails.getText().clear();
        streetDetails.getText().clear();
        landMark.getText().clear();
        pinCode.getText().clear();
        city.getText().clear();
        state.getText().clear();
        country.getText().clear();
        mobileNo.getText().clear();


    }
}