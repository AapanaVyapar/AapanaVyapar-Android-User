package com.aapanavyapar.aapanavyapar;

import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.aapanavyapar.aapanavyapar.services.Address;
import com.aapanavyapar.dataModel.DataModel;
import com.aapanavyapar.dataModel.ViewDataModel;
import com.aapanavyapar.serviceWrappers.GetProfileWrapper;
import com.aapanavyapar.serviceWrappers.UpdateAddressWrapper;

import static java.lang.Long.getLong;


public class ProfileFragment extends Fragment {

    EditText userName,fullName,houseDetails,streetDetails,landMark,pinCode,city,state,country,mobileNo;
    Button Add;

    ViewDataModel viewDataModel;
    DataModel dataModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        viewDataModel = new ViewModelProvider(requireActivity()).get(ViewDataModel.class);
        dataModel = new ViewModelProvider(requireActivity()).get(DataModel.class);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){

        userName = view.findViewById(R.id.profile_user_name_input);
        fullName = view.findViewById(R.id.profile_user_full_name_input);
        houseDetails = view.findViewById(R.id.profile_user_street_house_details_input);
        streetDetails = view.findViewById(R.id.profile_user_street_details_input);
        landMark = view.findViewById(R.id.profile_user_landmark_input);
        pinCode = view.findViewById(R.id.profile_user_pin_code_input);
        city = view.findViewById(R.id.profile_user_city_input);
        state = view.findViewById(R.id.profile_user_state_input);
        country = view.findViewById(R.id.profile_user_country_input);
        mobileNo = view.findViewById(R.id.profile_user_phoneNo_input);
        Add = view.findViewById(R.id.profile_user_updateData);


        GetProfileWrapper profileWrapper = new GetProfileWrapper(requireActivity());
        profileWrapper.updateProfile(getContext(), dataModel.getAuthToken(), dataModel.getRefreshToken());

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
                    Address address = Address.newBuilder()
                            .setFullName(FullName)
                            .setHouseDetails(HouseDetails)
                            .setStreetDetails(StreetDetails)
                            .setLandMark(LandMark)
                            .setPinCode(PinCode)
                            .setCity(City)
                            .setState(State)
                            .setCountry(Country)
                            .setPhoneNo(MobileNo)
                            .build();
                    UpdateAddressWrapper updateAddressWrapper = new UpdateAddressWrapper(requireActivity());
                    updateAddressWrapper.updateAddress(getContext(), dataModel.getAuthToken(), dataModel.getRefreshToken(), address);

                    userName.setText(viewDataModel.getUserName());
                    fullName.setText(viewDataModel.getAddress().getFullName());
                    houseDetails.setText(viewDataModel.getAddress().getHouseDetails());
                    streetDetails.setText(viewDataModel.getAddress().getStreetDetails());
                    landMark.setText(viewDataModel.getAddress().getLandMark());
                    pinCode.setText(viewDataModel.getAddress().getPinCode());
                    city.setText(viewDataModel.getAddress().getCity());
                    state.setText(viewDataModel.getAddress().getState());
                    country.setText(viewDataModel.getAddress().getCountry());
                    mobileNo.setText(viewDataModel.getAddress().getPhoneNo());
                }
            }
        });

        userName.setText(viewDataModel.getUserName());
        fullName.setText(viewDataModel.getAddress().getFullName());
        houseDetails.setText(viewDataModel.getAddress().getHouseDetails());
        streetDetails.setText(viewDataModel.getAddress().getStreetDetails());
        landMark.setText(viewDataModel.getAddress().getLandMark());
        pinCode.setText(viewDataModel.getAddress().getPinCode());
        city.setText(viewDataModel.getAddress().getCity());
        state.setText(viewDataModel.getAddress().getState());
        country.setText(viewDataModel.getAddress().getCountry());
        mobileNo.setText(viewDataModel.getAddress().getPhoneNo());

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
}