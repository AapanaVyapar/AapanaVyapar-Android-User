package com.aapanavyapar.aapanavyapar;

import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.aapanavyapar.aapanavyapar.services.Address;
import com.aapanavyapar.aapanavyapar.services.BuyingServiceGrpc;
import com.aapanavyapar.aapanavyapar.services.CreateOrderRequest;
import com.aapanavyapar.aapanavyapar.services.CreateOrderResponse;
import com.aapanavyapar.dataModel.DataModel;
import com.aapanavyapar.serviceWrappers.UpdateToken;
import com.aapanavyapar.viewData.BuyingData;
import com.aapanavyapar.viewData.ProductData;
import com.razorpay.Checkout;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

public class BuyingDetailsFragment extends Fragment {

    ProductData productData;

    EditText fullNameEditText, buildingDetailsEditText, streetDetailsEditText, landmarkEditText, pinCodeEditText;
    EditText phoneNoEditText, cityDetailsEditText, stateDetailsEditText, countryDetailsEditText, quantityDetailsEditText;
    Button buyNow;

    DataModel dataModel;


    ManagedChannel mChannel;
    BuyingServiceGrpc.BuyingServiceBlockingStub blockingStub;

    BuyingData buyingData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mChannel = ManagedChannelBuilder.forTarget(ViewProvider.BUYING_SERVICE_ADDRESS).usePlaintext().build();
        blockingStub = BuyingServiceGrpc.newBlockingStub(mChannel);

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
                            productData.getProductId(),
                            dataModel.getAuthToken(),
                            dataModel.getRefreshToken(),
                            productData.getShopId(),
                            fullNameEditText.getText().toString(),
                            buildingDetailsEditText.getText().toString(),
                            stateDetailsEditText.getText().toString(),
                            landmarkEditText.getText().toString(),
                            pinCodeEditText.getText().toString(),
                            cityDetailsEditText.getText().toString(),
                            stateDetailsEditText.getText().toString(),
                            countryDetailsEditText.getText().toString(),
                            phoneNoEditText.getText().toString(),
                            Integer.parseInt(quantityDetailsEditText.getText().toString())
                    );
                    startPayment(buyingData);

                }catch (NumberFormatException e) {
                    Toast.makeText(getContext(), "Invalid Quantity", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    public void startPayment(BuyingData buyingData){

        CreateOrderResponse response = null;
        try {
            CreateOrderRequest request = CreateOrderRequest.newBuilder()
                    .setApiKey(MainActivity.API_KEY)
                    .setToken(buyingData.getAuthToken())
                    .setProductId(buyingData.getProductId())
                    .setShopId(buyingData.getShopId())
                    .setAddress(Address.newBuilder()
                            .setFullName(buyingData.getFullName())
                            .setHouseDetails(buyingData.getHouseDetails())
                            .setStreetDetails(buyingData.getStreetDetails())
                            .setLandMark(buyingData.getLandmark())
                            .setPinCode(buyingData.getPinCode())
                            .setCity(buyingData.getCity())
                            .setState(buyingData.getState())
                            .setCountry(buyingData.getCountry())
                            .setPhoneNo(buyingData.getPhoneNo())
                            .build())
                    .setQuantity(buyingData.getQuantity())
                    .build();

            Log.d("BuyingActivity", buyingData.getProductId());
            Log.d("BuyingActivity", buyingData.getShopId());

            response = blockingStub.withDeadlineAfter(2, TimeUnit.MINUTES).placeOrder(request);

        }catch (StatusRuntimeException e) {
            e.printStackTrace();
            if (e.getMessage().equals("UNAUTHENTICATED: Request With Invalid Token")) {
                UpdateToken updateToken = new UpdateToken();
                if (updateToken.GetUpdatedToken(buyingData.getRefreshToken())) {
                    buyingData.setAuthToken(updateToken.getAuthToken());

                    CreateOrderRequest reRequest = CreateOrderRequest.newBuilder()
                            .setApiKey(MainActivity.API_KEY)
                            .setToken(buyingData.getAuthToken())
                            .setProductId(buyingData.getProductId())
                            .setShopId(buyingData.getShopId())
                            .setAddress(Address.newBuilder()
                                    .setFullName(buyingData.getFullName())
                                    .setHouseDetails(buyingData.getHouseDetails())
                                    .setStreetDetails(buyingData.getStreetDetails())
                                    .setLandMark(buyingData.getLandmark())
                                    .setPinCode(buyingData.getPinCode())
                                    .setCity(buyingData.getCity())
                                    .setState(buyingData.getState())
                                    .setCountry(buyingData.getCountry())
                                    .setPhoneNo(buyingData.getPhoneNo())
                                    .build())
                            .setQuantity(buyingData.getQuantity())
                            .build();

                    Log.d("BuyingActivity", buyingData.getFullName());
                    Log.d("BuyingActivity", buyingData.getHouseDetails());
                    Log.d("BuyingActivity", buyingData.getStreetDetails());
                    Log.d("BuyingActivity", buyingData.getLandmark());
                    Log.d("BuyingActivity", buyingData.getPinCode());
                    Log.d("BuyingActivity", buyingData.getCity());
                    Log.d("BuyingActivity", buyingData.getState());
                    Log.d("BuyingActivity", buyingData.getCountry());
                    Log.d("BuyingActivity", buyingData.getPhoneNo());

                    try {
                        response = blockingStub.withDeadlineAfter(2, TimeUnit.MINUTES).placeOrder(reRequest);

                    }catch (StatusRuntimeException e1) {
                        e1.printStackTrace();
                        Toast.makeText(getContext(), "Error While Creating Your Order ..!!", Toast.LENGTH_LONG).show();
                        return;
                    }
                } else {
                    Toast.makeText(getContext(), "Unable To Update Refresh Token ..!!", Toast.LENGTH_LONG).show();
                    return;
                }

            }else {
                Toast.makeText(getContext(), "Error While Creating Your Order ..!!", Toast.LENGTH_LONG).show();
                return;
            }
        }
        Checkout checkout = new Checkout();

        checkout.setKeyID(new String(Base64.decode(ViewProvider.getNativeKeyRazorPay(), Base64.DEFAULT)));
        Toast.makeText(getContext(), new String(Base64.decode(ViewProvider.getNativeKeyRazorPay(), Base64.DEFAULT)), Toast.LENGTH_LONG).show();

        checkout.setImage(R.drawable.logoav);

        try{
            JSONObject options = new JSONObject();

            options.put("name", response.getProductName());
            options.put("amount", response.getAmount());
            options.put("currency", response.getCurrency());
            options.put("order_id", response.getOrderId());

            options.put("image", response.getProductImage());
            options.put("theme.color", "#3399cc");

            JSONObject retryObj = new JSONObject();
            retryObj.put("enabled", false);
            options.put("retry", retryObj);

            checkout.open(getActivity(), options);

            //            Checkout.clearUserData(this);

        } catch (JSONException e) {
            Toast.makeText(getContext(), "Error Occurred", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        mChannel.shutdownNow();
    }
}