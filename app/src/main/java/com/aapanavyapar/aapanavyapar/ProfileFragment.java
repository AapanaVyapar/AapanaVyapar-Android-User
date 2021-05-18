package com.aapanavyapar.aapanavyapar;

import android.content.Context;
import android.database.Cursor;
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
import android.widget.LinearLayout;
import android.widget.Toast;

import static java.lang.Long.getLong;


public class ProfileFragment extends Fragment {

    EditText userName,fullName,houseDetails,streetDetails,landMark,pinCode,city,state,country,mobileNo;
    Button Add;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){
        ProfileDB g = new ProfileDB(getContext());
        SQLiteDatabase db = g.getReadableDatabase();

    //        LinearLayout layout = (LinearLayout)view.findViewById(R.id.profile_fragment_layout);
    //        Add = new Button(getContext());
    //        Add.setText("SET");

        userName = view.findViewById(R.id.profile_edittext_user_name);
        fullName = view.findViewById(R.id.profile_edit_text_fullname);
        houseDetails = view.findViewById(R.id.profile_edit_text_housedetails);
        streetDetails = view.findViewById(R.id.profile_edit_text_streetdetails);
        landMark = view.findViewById(R.id.profile_edit_text_landmark);
        pinCode = view.findViewById(R.id.profile_edit_text_pincode);
        city = view.findViewById(R.id.profile_edit_text_city);
        state = view.findViewById(R.id.profile_edit_text_state);
        country = view.findViewById(R.id.profile_edit_text_country);
        mobileNo = view.findViewById(R.id.profile_edit_text_mobileno);
        Add = view.findViewById(R.id.profile_button);



        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String UserName = userName.getText().toString();
                String FullName = fullName.getText().toString();
                String HouseDetails = houseDetails.getText().toString();
                String StreetDetails = streetDetails.getText().toString();
                String LandMark = landMark.getText().toString();
                String PinCode = pinCode.getText().toString();
                String City = city.getText().toString();
                String State = state.getText().toString();
                String Country = country.getText().toString();
                String MobileNo = mobileNo.getText().toString();

                if(UserName.isEmpty()||FullName.isEmpty()||
                        HouseDetails.isEmpty()||StreetDetails.isEmpty()||
                        LandMark.isEmpty()||PinCode.isEmpty()||City.isEmpty()||
                        State.isEmpty()||Country.isEmpty()||MobileNo.isEmpty()
                ){
                    Toast.makeText(getContext(),"fill all required fields",Toast.LENGTH_SHORT).show();
                }
                else{


                    boolean i= g.insert_Data(UserName,FullName,
                            HouseDetails,StreetDetails,LandMark,
                           PinCode,City,State,Country,MobileNo);
                    if(i){
                        Toast.makeText(getContext(),"Successful",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(getContext(),"Not Successful",Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        if(g.profileInfo()){
            Cursor t = g.getInfo();
            while(t.moveToNext()) {
                userName.setText(t.getString(1));
                fullName.setText(t.getString(2));
                houseDetails.setText(t.getString(3));
                streetDetails.setText(t.getString(4));
                landMark.setText(t.getString(5));
                pinCode.setText(t.getString(6));
                city.setText(t.getString(7));
                state.setText(t.getString(8));
                country.setText(t.getString(9));
                mobileNo.setText(t.getString(10));
            }
        }
        else {
            userName.setText("");
            fullName.setText("");
            houseDetails.setText("");
            streetDetails.setText("");
            landMark.setText("");
            pinCode.setText("");
            city.setText("");
            state.setText("");
            country.setText("");
            mobileNo.setText("");
        }
    }

//    public Boolean checkFieldEmpty(){
//        String UserName = userName.getText().toString();
//           String FullName = fullName.getText().toString();
//            String HouseDetails = houseDetails.getText().toString();
//            String StreetDetails = streetDetails.getText().toString();
//            String LandMark = landMark.getText().toString();
//            String PinCode = pinCode.getText().toString();
//            String City = city.getText().toString();
//            String State = state.getText().toString();
//            String Country = country.getText().toString();
//            String MobileNo = mobileNo.getText().toString();
//        if(UserName.isEmpty()||FullName.isEmpty()||
//                   HouseDetails.isEmpty()||StreetDetails.isEmpty()||
//                    LandMark.isEmpty()||PinCode.isEmpty()||City.isEmpty()||
//                    State.isEmpty()||Country.isEmpty()||MobileNo.isEmpty()
//            ) return false;
//
//        else return true;
//    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

}