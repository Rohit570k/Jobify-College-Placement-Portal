package com.example.jobify.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.example.jobify.Models.APIError;
import com.example.jobify.Models.CredentialResponse;
import com.example.jobify.Models.User;
import com.example.jobify.WebService.RetrofitClient;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;

public class UtilService {

    public static final String BASE_URL = "https://1882-2409-408a-2c07-cbc9-1001-ddb2-de68-3ce4.ngrok-free.app";
    public void hideKeyboard(View view , Context context)
    {
        // if nothing is currently
        // focus then this will protect
        // the app from crash
        if (view != null) {
            // now assign the system
            // service to InputMethodManager
            InputMethodManager manager = (InputMethodManager)
                    context.getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    public CredentialResponse getUserFromSharedPref(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("userSnapshot", Context.MODE_PRIVATE);
        String userJson = sharedPreferences.getString("userCred", null);
        if (userJson != null) {
            Gson gson = new Gson();
            CredentialResponse userCred = gson.fromJson(userJson, CredentialResponse.class);
            return userCred;
        }
        return null;
    }

    public void setUserToSharedPref(CredentialResponse userCred ,Context context){
        // Save user data in SharedPreferences
        SharedPreferences sharedPreferences = context.getSharedPreferences("userSnapshot", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Convert user object to JSON string using Gson library
        Gson gson = new Gson();
        String userJson = gson.toJson(userCred);
        // Save user JSON string in SharedPreferences
        editor.putString("userCred", userJson);
        editor.apply();
    }

    public void showSnackbar(View view, String text) {
        Snackbar snackbar = Snackbar.make(view,text,Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    public static APIError parseError(Response<?> response) {
        Converter<ResponseBody, APIError> converter =
                RetrofitClient.retrofit()
                        .responseBodyConverter(APIError.class, new Annotation[0]);

        APIError error;

        try {
            error = converter.convert(response.errorBody());
        } catch (IOException e) {
            return new APIError();
        }

        return error;
    }
}
