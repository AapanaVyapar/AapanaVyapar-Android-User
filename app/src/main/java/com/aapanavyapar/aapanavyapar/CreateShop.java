package com.aapanavyapar.aapanavyapar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.aapanavyapar.aapanavyapar.services.Address;
import com.aapanavyapar.aapanavyapar.services.AuthenticationGrpc;
import com.aapanavyapar.aapanavyapar.services.Category;
import com.aapanavyapar.aapanavyapar.services.CreateShopRequest;
import com.aapanavyapar.aapanavyapar.services.CreateShopResponse;
import com.aapanavyapar.aapanavyapar.services.Location;
import com.aapanavyapar.aapanavyapar.services.OperationalHours;
import com.aapanavyapar.aapanavyapar.services.ViewProviderServiceGrpc;
import com.aapanavyapar.constants.constants;
import com.aapanavyapar.dataModel.DataModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusException;
import io.grpc.StatusRuntimeException;

public class CreateShop extends Fragment {

    private DataModel dataModel;

    ManagedChannel mChannel;
    ViewProviderServiceGrpc.ViewProviderServiceBlockingStub blockingStub;

    EditText shopNameEditText, primaryImageEditText, imageUrlsEditText, businessInfoEditText, locationEditText;
    EditText fullNameEditText, houseDetailsEditText, streetEditText, landmarkEditText, pinCodeEditText, cityEditText, stateEditText, countryEditText, businessPhoneEditText;
    Button createShop;

    ArrayList<Category> categories;

    public CreateShop() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        dataModel = new ViewModelProvider(requireActivity()).get(DataModel.class);
        mChannel = ManagedChannelBuilder.forTarget(MainActivity.VIEW_SERVICE_ADDRESS).usePlaintext().build();
        blockingStub = ViewProviderServiceGrpc.newBlockingStub(mChannel);

        categories = new ArrayList<>();

        return inflater.inflate(R.layout.fragment_create_shop, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(!dataModel.CanWeUseTokenForThis(constants.External)){
            Toast.makeText(getContext(), "Please Try Again ..!!", Toast.LENGTH_LONG).show();

            if (Objects.requireNonNull(Navigation.findNavController(view).getCurrentDestination()).getId() == R.id.CreateShopFragment) {
                NavDirections createShopNav = CreateShopDirections.actionCreateShopFragmentToSigninFragment();
                Navigation.findNavController(view).navigate(createShopNav);
            }
        }

        shopNameEditText = view.findViewById(R.id.create_shop_shop_name);
        primaryImageEditText = view.findViewById(R.id.create_shop_primary_image_input);
        primaryImageEditText = view.findViewById(R.id.create_shop_primary_image_input);
        imageUrlsEditText = view.findViewById(R.id.create_shop_images_input);
        businessInfoEditText = view.findViewById(R.id.create_shop_business_info_input);
        locationEditText = view.findViewById(R.id.create_shop_location_input);
        fullNameEditText = view.findViewById(R.id.create_shop_full_name_input);
        houseDetailsEditText = view.findViewById(R.id.create_shop_house_details_input);
        streetEditText = view.findViewById(R.id.create_shop_street_details_input);
        landmarkEditText = view.findViewById(R.id.create_shop_landmark_input);
        pinCodeEditText = view.findViewById(R.id.create_shop_pin_code_input);
        cityEditText = view.findViewById(R.id.create_shop_city_input);
        stateEditText = view.findViewById(R.id.create_shop_state_input);
        countryEditText = view.findViewById(R.id.create_shop_country_input);
        businessPhoneEditText = view.findViewById(R.id.create_shop_country_phoneNo_input);

        createShop = view.findViewById(R.id.create_shop);
      

        view.findViewById(R.id.create_shop_SPORTS_AND_FITNESS).setOnClickListener(this::onCheckboxClicked);
        view.findViewById(R.id.create_shop_ELECTRIC).setOnClickListener(this::onCheckboxClicked);
        view.findViewById(R.id.create_shop_DEVOTIONAL).setOnClickListener(this::onCheckboxClicked);
        view.findViewById(R.id.create_shop_AGRICULTURAL).setOnClickListener(this::onCheckboxClicked);
        view.findViewById(R.id.create_shop_WOMENS_CLOTHING).setOnClickListener(this::onCheckboxClicked);
        view.findViewById(R.id.create_shop_WOMENS_ACCESSORIES).setOnClickListener(this::onCheckboxClicked);
        view.findViewById(R.id.create_shop_MENS_CLOTHING).setOnClickListener(this::onCheckboxClicked);
        view.findViewById(R.id.create_shop_MENS_ACCESSORIES).setOnClickListener(this::onCheckboxClicked);
        view.findViewById(R.id.create_shop_HOME_GADGETS).setOnClickListener(this::onCheckboxClicked);
        view.findViewById(R.id.create_shop_TOYS).setOnClickListener(this::onCheckboxClicked);
        view.findViewById(R.id.create_shop_ELECTRONIC).setOnClickListener(this::onCheckboxClicked);
        view.findViewById(R.id.create_shop_DECORATION).setOnClickListener(this::onCheckboxClicked);
        view.findViewById(R.id.create_shop_FOOD).setOnClickListener(this::onCheckboxClicked);
        view.findViewById(R.id.create_shop_STATIONERY).setOnClickListener(this::onCheckboxClicked);
        view.findViewById(R.id.create_shop_BAGS).setOnClickListener(this::onCheckboxClicked);
        view.findViewById(R.id.create_shop_HARDWARE).setOnClickListener(this::onCheckboxClicked);
        view.findViewById(R.id.create_shop_FURNITURE).setOnClickListener(this::onCheckboxClicked);
        view.findViewById(R.id.create_shop_PACKAGING_AND_PRINTING).setOnClickListener(this::onCheckboxClicked);
        view.findViewById(R.id.create_shop_BEAUTY_AND_PERSONAL_CARE).setOnClickListener(this::onCheckboxClicked);
        view.findViewById(R.id.create_shop_CHEMICALS).setOnClickListener(this::onCheckboxClicked);
        view.findViewById(R.id.create_shop_GARDEN).setOnClickListener(this::onCheckboxClicked);
        view.findViewById(R.id.create_shop_KITCHEN).setOnClickListener(this::onCheckboxClicked);
        view.findViewById(R.id.create_shop_MACHINERY).setOnClickListener(this::onCheckboxClicked);


        createShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String[] images = imageUrlsEditText.getText().toString().trim().split(",");
                String[] location = locationEditText.getText().toString().trim().split(",");

                CreateShopRequest request = CreateShopRequest.newBuilder()
                        .setApiKey(MainActivity.API_KEY)
                        .setToken(dataModel.getAuthToken())
                        .setShopName(shopNameEditText.getText().toString())
                        .addAllImages(Arrays.asList(images))
                        .setPrimaryImage(primaryImageEditText.getText().toString())
                        .setAddress(Address.newBuilder()
                                .setFullName(fullNameEditText.getText().toString())
                                .setHouseDetails(houseDetailsEditText.getText().toString())
                                .setStreetDetails(streetEditText.getText().toString())
                                .setLandMark(landmarkEditText.getText().toString())
                                .setPinCode(pinCodeEditText.getText().toString())
                                .setCity(cityEditText.getText().toString())
                                .setState(stateEditText.getText().toString())
                                .setCountry(countryEditText.getText().toString())
                                .setPhoneNo(businessPhoneEditText.getText().toString())
                                .build())
                        .setLocation(Location.newBuilder()
                                .setLatitude(location[0])
                                .setLongitude(location[1])
                                .build())
                        .addAllCategory(categories)
                        .setBusinessInformation(businessInfoEditText.getText().toString())
                        .setOperationalHours(OperationalHours.newBuilder()
                                .addAllSunday(Arrays.asList("8:00AM", "10:PM"))
                                .addAllMonday(Arrays.asList("8:00AM", "10:PM"))
                                .addAllTuesday(Arrays.asList("8:00AM", "10:PM"))
                                .addAllWednesday(Arrays.asList("8:00AM", "10:PM"))
                                .addAllThursday(Arrays.asList("8:00AM", "10:PM"))
                                .addAllFriday(Arrays.asList("8:00AM", "10:PM"))
                                .addAllSaturday(Arrays.asList("8:00AM", "10:PM"))
                                .build())
                        .build();
                try {
                    CreateShopResponse shopResponse = blockingStub.withDeadlineAfter(1, TimeUnit.MINUTES).createShop(request);
                    if(!shopResponse.getOk()){
                        Toast.makeText(getContext(), "Fail To Create Shop ..!!", Toast.LENGTH_LONG).show();

                        if (Objects.requireNonNull(Navigation.findNavController(view).getCurrentDestination()).getId() == R.id.CreateShopFragment) {
                            NavDirections createShopNav = CreateShopDirections.actionCreateShopFragmentToSigninFragment();
                            Navigation.findNavController(view).navigate(createShopNav);
                        }

                    }else {

                        Toast.makeText(getContext(), "Success Shop ..!!", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(getContext(), ViewProvider.class);
                        intent.putExtra("Token", dataModel.getRefreshToken());
                        intent.putExtra("AuthToken", dataModel.getAuthToken());
//                        intent.putExtra("ShopData", shopInfo);
                        intent.putExtra("Access", dataModel.getAccess());
                        startActivity(intent);
                    }

                }catch (StatusRuntimeException e){
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Fail To Create Shop ..!!", Toast.LENGTH_LONG).show();

                    if (Objects.requireNonNull(Navigation.findNavController(view).getCurrentDestination()).getId() == R.id.CreateShopFragment) {
                        NavDirections createShopNav = CreateShopDirections.actionCreateShopFragmentToSigninFragment();
                        Navigation.findNavController(view).navigate(createShopNav);
                    }

                }
            }
        });

    }

    public void onCheckboxClicked(@NotNull View view) {
        boolean checked = ((CheckBox) view).isChecked();
                
        switch(view.getId()) {
            case R.id.create_shop_SPORTS_AND_FITNESS:
                if (checked)
                    categories.add(Category.SPORTS_AND_FITNESS);
                break;
            case R.id.create_shop_ELECTRIC:
                if (checked)
                    categories.add(Category.ELECTRIC);
                break;
            case R.id.create_shop_DEVOTIONAL:
                if (checked)
                    categories.add(Category.DEVOTIONAL);
                break;            
            case R.id.create_shop_AGRICULTURAL:
                if (checked)
                    categories.add(Category.AGRICULTURAL);
                break;
            case R.id.create_shop_WOMENS_CLOTHING:
                if (checked)
                    categories.add(Category.WOMENS_CLOTHING);
                break;
            case R.id.create_shop_WOMENS_ACCESSORIES:
                if (checked)
                    categories.add(Category.WOMENS_ACCESSORIES);
                break;
            case R.id.create_shop_MENS_CLOTHING:
                if (checked)
                    categories.add(Category.MENS_CLOTHING);
                break;
            case R.id.create_shop_MENS_ACCESSORIES:
                if (checked)
                    categories.add(Category.MENS_ACCESSORIES);
                break;                                
            case R.id.create_shop_HOME_GADGETS:
                if (checked)
                    categories.add(Category.HOME_GADGETS);
                break;
            case R.id.create_shop_TOYS:
                if (checked)
                    categories.add(Category.TOYS);
                break;
            case R.id.create_shop_ELECTRONIC:
                if (checked)
                    categories.add(Category.ELECTRONIC);
                break;
            case R.id.create_shop_DECORATION:
                if (checked)
                    categories.add(Category.DECORATION);
                break;
            case R.id.create_shop_FOOD:
                if (checked)
                    categories.add(Category.FOOD);
                break;
            case R.id.create_shop_STATIONERY:
                if (checked)
                    categories.add(Category.STATIONERY);
                break;
            case R.id.create_shop_BAGS:
                if (checked)
                    categories.add(Category.BAGS);
                break;
            case R.id.create_shop_HARDWARE:
                if (checked)
                    categories.add(Category.HARDWARE);
                break;
            case R.id.create_shop_FURNITURE:
                if (checked)
                    categories.add(Category.FURNITURE);
                break;
            case R.id.create_shop_PACKAGING_AND_PRINTING:
                if (checked)
                    categories.add(Category.PACKAGING_AND_PRINTING);
                break;
            case R.id.create_shop_BEAUTY_AND_PERSONAL_CARE:
                if (checked)
                    categories.add(Category.BEAUTY_AND_PERSONAL_CARE);
                break;
            case R.id.create_shop_CHEMICALS:
                if (checked)
                    categories.add(Category.CHEMICALS);
                break;
            case R.id.create_shop_GARDEN:
                if (checked)
                    categories.add(Category.GARDEN);
                break;
            case R.id.create_shop_KITCHEN:
                if (checked)
                    categories.add(Category.KITCHEN);
                break;
            case R.id.create_shop_MACHINERY:
                if (checked)
                    categories.add(Category.MACHINERY);
                break;
        }
    }
}