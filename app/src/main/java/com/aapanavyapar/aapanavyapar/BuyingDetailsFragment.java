package com.aapanavyapar.aapanavyapar;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.aapanavyapar.viewData.BuyingData;
import com.aapanavyapar.viewData.ProductData;

import org.jetbrains.annotations.NotNull;

public class BuyingDetailsFragment extends Fragment {

    ProductData productData;

    EditText fullNameEditText, buildingDetailsEditText, streetDetailsEditText, landmarkEditText, pinCodeEditText;
    EditText phoneNoEditText, cityDetailsEditText, stateDetailsEditText, countryDetailsEditText, quantityDetailsEditText;
    Button buyNow;

    DataModel dataModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_buying_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dataModel = new ViewModelProvider(requireActivity()).get(DataModel.class);

        assert getArguments() != null;
        productData = (ProductData) getArguments().getSerializable("dataFill");

        fullNameEditText = view.findViewById(R.id.buying_form_full_name_input);
        phoneNoEditText = view.findViewById(R.id.buying_form_phone_no_input);
        buildingDetailsEditText = view.findViewById(R.id.buying_form_house_details_input);
        streetDetailsEditText = view.findViewById(R.id.buying_form_street_details_input);
        landmarkEditText = view.findViewById(R.id.buying_form_landmark_input);
        pinCodeEditText = view.findViewById(R.id.buying_form_pin_code_input);
        cityDetailsEditText = view.findViewById(R.id.buying_form_city_input);
        stateDetailsEditText = view.findViewById(R.id.buying_form_state_input);
        countryDetailsEditText = view.findViewById(R.id.buying_form_country_input);
        quantityDetailsEditText = view.findViewById(R.id.buying_form_quantity_input);

        buyNow = view.findViewById(R.id.buying_form_submit);

        buyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int quantity = Integer.parseInt(quantityDetailsEditText.getText().toString());
                    if (quantity <= 0) {
                        Toast.makeText(getContext(), "Invalid Quantity", Toast.LENGTH_LONG).show();
                        return;
                    }

                    BuyingData buyingData = new BuyingData(
                            dataModel.getAuthToken(),
                            dataModel.getRefreshToken(),
                            productData.getProductId(),
                            productData.getShopId(),
                            Address.newBuilder()
                                    .setFullName(fullNameEditText.getText().toString())
                                    .setHouseDetails(buildingDetailsEditText.getText().toString())
                                    .setStreetDetails(stateDetailsEditText.getText().toString())
                                    .setLandMark(landmarkEditText.getText().toString())
                                    .setPinCode(pinCodeEditText.getText().toString())
                                    .setCity(cityDetailsEditText.getText().toString())
                                    .setState(stateDetailsEditText.getText().toString())
                                    .setCountry(countryDetailsEditText.getText().toString())
                                    .setPhoneNo(phoneNoEditText.getText().toString())
                                    .build(),
                            Integer.parseInt(quantityDetailsEditText.getText().toString())
                    );

                    Intent intent  = new Intent(getContext(), BuyingActivity.class);
                    intent.putExtra("BuyingData", buyingData);
                    startActivity(intent);

                }catch (NumberFormatException e) {
                    Toast.makeText(getContext(), "Invalid Quantity", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}