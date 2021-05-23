package com.aapanavyapar.aapanavyapar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.aapanavyapar.aapanavyapar.services.Address;
import com.aapanavyapar.aapanavyapar.services.Buying;
import com.aapanavyapar.aapanavyapar.services.BuyingServiceGrpc;
import com.aapanavyapar.aapanavyapar.services.CapturePaymentRequest;
import com.aapanavyapar.aapanavyapar.services.CapturePaymentResponse;
import com.aapanavyapar.aapanavyapar.services.CreateOrderRequest;
import com.aapanavyapar.aapanavyapar.services.CreateOrderResponse;
import com.aapanavyapar.constants.constants;
import com.aapanavyapar.serviceWrappers.UpdateToken;
import com.aapanavyapar.viewData.BuyingData;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

public class BuyingActivity extends AppCompatActivity implements PaymentResultListener {

    static {
        System.loadLibrary("keys");
    }

    private static final String TAG = BuyingActivity.class.getSimpleName();

    private final String BUYING_SERVICE_ADDRESS = "192.168.43.200:9359";

    public native String getNativeKeyRazorPay();
    public native String getNativeAPIKey();

    ManagedChannel mChannel;
    BuyingServiceGrpc.BuyingServiceBlockingStub blockingStub;

    BuyingData buyingData;
    String orderId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buying);

        mChannel = ManagedChannelBuilder.forTarget(BUYING_SERVICE_ADDRESS).usePlaintext().build();
        blockingStub = BuyingServiceGrpc.newBlockingStub(mChannel);

        Intent intent = getIntent();
        BuyingData buyingData = (BuyingData) intent.getSerializableExtra("BuyingData");

        CreateOrderResponse response = null;
        try {
            CreateOrderRequest request = CreateOrderRequest.newBuilder()
                    .setApiKey(new String(Base64.decode(getNativeAPIKey(), Base64.DEFAULT)))
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
                            .setApiKey(new String(Base64.decode(getNativeAPIKey(), Base64.DEFAULT)))
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
                        Toast.makeText(getApplicationContext(), "Error While Creating Your Order ..!!", Toast.LENGTH_LONG).show();
                        return;
                    }
                }

            }else {
                Toast.makeText(getApplicationContext(), "Error While Creating Your Order ..!!", Toast.LENGTH_LONG).show();
                return;
            }
        }

        Checkout checkout = new Checkout();

        checkout.setKeyID(new String(Base64.decode(getNativeKeyRazorPay(), Base64.DEFAULT)));
        Toast.makeText(getApplicationContext(), new String(Base64.decode(getNativeKeyRazorPay(), Base64.DEFAULT)), Toast.LENGTH_LONG).show();

        checkout.setImage(R.drawable.logoav);

        orderId = response.getOrderId();

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

            checkout.open(BuyingActivity.this, options);

//            Checkout.clearUserData(this);

        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), "Error Occurred", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentSuccess(String paymentId) {
        Toast.makeText(this,"Payment successful", Toast.LENGTH_SHORT).show();

        CapturePaymentRequest request = CapturePaymentRequest.newBuilder()
                .setApiKey(new String(Base64.decode(getNativeAPIKey(), Base64.DEFAULT)))
                .setRazorpayPaymentId(paymentId)
                .setToken(buyingData.getAuthToken())
                .setRazorpayOrderId(orderId)
                .build();
        CapturePaymentResponse response = null;
        try{
            response = blockingStub.withDeadlineAfter(2, TimeUnit.MINUTES).capturePayment(request);

        }catch (StatusRuntimeException e) {
            if (e.getMessage().equals("UNAUTHENTICATED: Request With Invalid Token")) {
                UpdateToken updateToken = new UpdateToken();
                if (updateToken.GetUpdatedToken(buyingData.getRefreshToken())) {
                    buyingData.setAuthToken(updateToken.getAuthToken());
                    CapturePaymentRequest reRequest = CapturePaymentRequest.newBuilder()
                            .setApiKey(new String(Base64.decode(getNativeAPIKey(), Base64.DEFAULT)))
                            .setRazorpayPaymentId(paymentId)
                            .setToken(buyingData.getAuthToken())
                            .setRazorpayOrderId(orderId)
                            .build();
                    try {
                        response = blockingStub.withDeadlineAfter(2, TimeUnit.MINUTES).capturePayment(reRequest);

                    }catch (StatusRuntimeException e1) {
                        Toast.makeText(getApplicationContext(), "Fail To Capture Your Payment If Payment Debited From Your Account it Will Be Back in 6 - 7 working days", Toast.LENGTH_LONG).show();
                        return;
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "Unable To Update Refresh Token ..!!", Toast.LENGTH_LONG).show();
                    return;
                }
            }else {
                Toast.makeText(getApplicationContext(), "Fail To Capture Your Payment If Payment Debited From Your Account it Will Be Back in 6 - 7 working days", Toast.LENGTH_LONG).show();
                return;
            }
        }
        Log.d(TAG, "Response : " + response);
        Log.d(TAG, "Success : " + paymentId);
    }

    @Override
    public void onPaymentError(int i, String s) {
        switch (i) {
            case Checkout.NETWORK_ERROR:
                Toast.makeText(this,"Payment is not successful NETWORK_ERROR", Toast.LENGTH_SHORT).show();
                break;
            case Checkout.INVALID_OPTIONS:
                Toast.makeText(this,"Payment is not successful INVALID_OPTIONS", Toast.LENGTH_SHORT).show();
                break;
            case Checkout.PAYMENT_CANCELED:
                Toast.makeText(this,"Payment is not successful PAYMENT_CANCELED", Toast.LENGTH_SHORT).show();
                break;
            case Checkout.TLS_ERROR:
                Toast.makeText(this,"Payment is not successful TLS_ERROR", Toast.LENGTH_SHORT).show();
                break;
        }

        Toast.makeText(this,"Payment is not successful", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "Error : " + s);
        orderId = "";
    }

}