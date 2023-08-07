package com.example.jobify.UI.Profile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.jobify.LoginActivity;
import com.example.jobify.Models.APIError;
import com.example.jobify.Models.CredentialResponse;
import com.example.jobify.Models.User;
import com.example.jobify.R;
import com.example.jobify.SignUpActivity;
import com.example.jobify.UI.MainActivity;
import com.example.jobify.Utils.UtilService;
import com.example.jobify.WebService.RetrofitApi;
import com.example.jobify.WebService.RetrofitClient;
import com.example.jobify.databinding.ActivityEditProfileBinding;
import com.google.gson.Gson;

import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity {
private ActivityEditProfileBinding binding;
private UtilService utilService;
private String authToken;

private String erpId, name, lastName, email, location;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        utilService = new UtilService();

        CredentialResponse userCred = utilService.getUserFromSharedPref(EditProfileActivity.this);
        if(userCred!=null){
            authToken=userCred.getToken();
            fillEditText(userCred.getUser());
        }


        binding.submitBtn.setOnClickListener((view) -> {
            utilService.hideKeyboard(view, EditProfileActivity.this);
            erpId = binding.erpIdEdt.getText().toString();
            name = binding.nameEdt.getText().toString();
            lastName = binding.lastNameEdt.getText().toString();
            email = binding.emailEdt.getText().toString();
            location = binding.locationEdt.getText().toString();

            if (validate(view)) {
                updateUser(view);
            }
        });
    }

    private void updateUser(View view) {
        binding.submitBtn.setText("Please Wait...!");
        RetrofitApi retrofitApi = RetrofitClient.getRetrofitApiService();

        User userReq = new User(name,lastName,email,location,Integer.parseInt(erpId));
        Call<CredentialResponse> userCall = retrofitApi.updateUser("Bearer "+authToken,userReq);
        userCall.enqueue(new Callback<CredentialResponse>() {
            @Override
            public void onResponse(Call<CredentialResponse> call, Response<CredentialResponse> response) {
                if(response.isSuccessful()&& response.code()==200&& response!=null){
                    CredentialResponse userCred = response.body();
                    //Log.i(TAG, "onResponse: after logon" + new Gson().toJson(response.body()));
                    // Save user data in SharedPreferences
                    utilService.setUserToSharedPref(userCred,EditProfileActivity.this);

                    binding.submitBtn.setText("Save Changes");
                    // Show success message and navigate to next screen
                    Toast.makeText(EditProfileActivity.this, "Profile Updated!", Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    //Log.e(TAG,response.code()+" "+response.errorBody().toString());
                    binding.submitBtn.setText("Save Changes");

                    // parse the response body …
                    APIError error = UtilService.parseError(response);
                    Log.d("error message", error.getMsg());
                    Toast.makeText(EditProfileActivity.this,error.getMsg()+" ",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CredentialResponse> call, Throwable t) {
                // Show error message
                binding.submitBtn.setText("Save Changes");
                Toast.makeText(EditProfileActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private boolean validate(View view) {
        boolean isValid=false;

        if (TextUtils.isEmpty(erpId)){
            utilService.showSnackbar(view, "Please enter erpid");
            return isValid;
        }
        if (TextUtils.isEmpty(name)){
            utilService.showSnackbar(view, "Please enter name");
            return isValid;
        }
        if (TextUtils.isEmpty(lastName)){
            utilService.showSnackbar(view, "Please enter lastName");
            return isValid;
        }
        if (TextUtils.isEmpty(email)){
            utilService.showSnackbar(view, "Please enter email");
            return isValid;
        }
        if (TextUtils.isEmpty(location)){
            utilService.showSnackbar(view, "Please enter location");
            return isValid;
        }
        return true;
    }

    private void fillEditText(User user) {
        binding.erpIdEdt.setText(""+user.getErpId());
        binding.nameEdt.setText(""+user.getName());
        binding.lastNameEdt.setText(""+user.getLastName());
        binding.emailEdt.setText(""+user.getEmail());
        binding.locationEdt.setText(""+user.getLocation());
    }
}