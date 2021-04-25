package com.aapanavyapar.validators;

import android.widget.EditText;

import java.util.regex.Pattern;

import static android.util.Patterns.EMAIL_ADDRESS;
import static android.util.Patterns.PHONE;

public class validators {
    private static final Pattern PIN_CODE_PATTERN = Pattern.compile(("/^(\\d{4}|\\d{6})$/"));
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

    public static boolean validateUserName(EditText inputUserName){

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
    public static boolean validateMail(EditText inputMail){

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
    public static boolean validatePhone(EditText inputPhone){

        String phone_input = inputPhone.getText().toString().trim();
        if(phone_input.isEmpty())
        {
            inputPhone.setError("Field Can't Be Empty");
            return false;

        }
        else if(!PHONE.matcher(phone_input).matches()){
            inputPhone.setError("Please Enter The Valid Phone Number");
            return false;
        }
        else {

            inputPhone.setError(null);
            return true;
        }
    }
    public static boolean validatePinCode(EditText inputPinCode){
        return true;
       /* String pinCodeInput = inputPinCode.getText().toString().trim();
        if(pinCodeInput.isEmpty())
        {
            inputPinCode.setError("Field Can't Be Empty");
            return false;

        }
        else if(!PIN_CODE_PATTERN.matcher(pinCodeInput).matches()){
            inputPinCode.setError("Please Enter Valid Pincode");
            return false;
        }
        else {

            inputPinCode.setError(null);
            return true;
        }*/
    }

    public static boolean validatePasswordSignIn(EditText inputPassword){//To check weather the field of password is empty or not

        String passwordInput = inputPassword.getText().toString().trim();
        if(passwordInput.isEmpty())
        {
            inputPassword.setError("Field Can't Be Empty");
            return false;

        }
        else if(!PASSWORD_PATTERN.matcher(passwordInput).matches()){
            inputPassword.setError("Invalid Password");
            return false;
        }
        else {

            inputPassword.setError(null);
            return true;
        }
    }

    public static boolean validatePasswordSignUp(EditText inputPassword){//To check weather the field of password is empty or not

        String passwordInput = inputPassword.getText().toString().trim();
        if(passwordInput.isEmpty())
        {
            inputPassword.setError("Field Can't Be Empty");
            return false;

        }
        else if(!PASSWORD_PATTERN.matcher(passwordInput).matches()){
            inputPassword.setError("Password Is Not Strong Enough");
            return false;
        }
        else {

            inputPassword.setError(null);
            return true;
        }
    }
}
