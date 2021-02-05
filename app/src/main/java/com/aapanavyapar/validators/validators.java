package com.aapanavyapar.validators;

import android.widget.EditText;
import android.widget.TextView;

import java.util.regex.Pattern;

import static android.util.Patterns.EMAIL_ADDRESS;
import static android.util.Patterns.PHONE;

public class validators {
    private static final Pattern PINCODE_PATTERN = Pattern.compile(("/^(\\d{4}|\\d{6})$/"));
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(("^" +
            "(?=.*[0-9])" +         //at least 1 digit
            "(?=.*[a-z])" +         //at least 1 lower case letter
            "(?=.*[A-Z])" +         //at least 1 upper case letter
            "(?=.*[a-zA-Z])" +      //any letter
            "(?=.*[@#$%^&+=])" +    //at least 1 special character
            "(?=\\S+$)" +           //no white spaces
            ".{4,}" +               //at least 4 characters
            "$"));
            ;

    public boolean validateUserName(EditText inputUserName){

        String user_input = inputUserName.getText().toString().trim();
        if(user_input.isEmpty())
        {
            inputUserName.setError("Field Can't Be Empty");
            return false;

        }
        else if(user_input.length()>=20){
            inputUserName.setError("Username is to long");
            return false;
        }
        else {

            inputUserName.setError(null);
            return true;
        }
    }
    public boolean validateMail(EditText inputMail){

        String email_input = inputMail.getText().toString().trim();
        if(email_input.isEmpty())
        {
            inputMail.setError("Field Can't Be Empty");
            return false;

        }
        else if(!EMAIL_ADDRESS.matcher(email_input).matches()){
            inputMail.setError("Please Enter Valid Email");
            return false;
        }
        else {

            inputMail.setError(null);
            return true;
        }
    }
    private boolean validatePhone(EditText inputPhno){

        String phone_input = inputPhno.getText().toString().trim();
        if(phone_input.isEmpty())
        {
            inputPhno.setError("Field Can't Be Empty");
            return false;

        }
        else if(!PHONE.matcher(phone_input).matches()){
            inputPhno.setError("Please Enter The Valid Phone Number");
            return false;
        }
        else {

            inputPhno.setError(null);
            return true;
        }
    }
    public boolean validatePincode(EditText inputPincode){

        String pincode_input = inputPincode.getText().toString().trim();
        if(pincode_input.isEmpty())
        {
            inputPincode.setError("Field Can't Be Empty");
            return false;

        }
        else if(!PINCODE_PATTERN.matcher(pincode_input).matches()){
            inputPincode.setError("Please Enter Valid Pincode");
            return false;
        }
        else {

            inputPincode.setError(null);
            return true;
        }
    }
    public boolean validatePpassword(EditText inputPassword){//To check wheather the field of password is empty or not

        String password_input = inputPassword.getText().toString().trim();
        if(password_input.isEmpty())
        {
            inputPassword.setError("Field Can't Be Empty");
            return false;

        }
        else if(!PASSWORD_PATTERN.matcher(password_input).matches()){
            inputPassword.setError("Password Is Not Strong Enough");
            return false;
        }
        else {

            inputPassword.setError(null);
            return true;
        }
    }
}
