package com.example.jobify;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jobify.Models.APIError;
import com.example.jobify.Models.CredentialResponse;
import com.example.jobify.Models.User;
import com.example.jobify.UI.MainActivity;
import com.example.jobify.Utils.UtilService;
import com.example.jobify.WebService.RetrofitApi;
import com.example.jobify.WebService.RetrofitClient;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {
  private TextView loginBtn;
    private static final String TAG = "Signup Activivty";
    private EditText name_ET, email_ET, password_ET;
    private Button registerBtn;
    ProgressBar progressBar;
    private String name, email, password;
    UtilService utilService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        registerBtn= (Button) findViewById(R.id.signupButton);
        progressBar = findViewById(R.id.progress_bar);
        name_ET = findViewById(R.id.nameEdt);
        email_ET = findViewById(R.id.emailEdt);
        password_ET = findViewById(R.id.passwordEdt);
        utilService = new UtilService();

        loginBtn = findViewById(R.id.signinText);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this,LoginActivity.class));
            }
        });

        //Register user ,validate our user then using the rest API completes the registration.
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                utilService.hideKeyboard(view, SignUpActivity.this);
                name = name_ET.getText().toString();
                email = email_ET.getText().toString();
                password = password_ET.getText().toString();

                if (validate(view)){
                    registerUser(view);
                }
            }
        });

    }
    //Api Call for register user
    private void registerUser(View view){
        progressBar.setVisibility(View.VISIBLE);
        RetrofitApi retrofitApi = RetrofitClient.getRetrofitApiService();

        User userReq = new User(name,email,password);
        Call<CredentialResponse> userCall = retrofitApi.register(userReq);
        userCall.enqueue(new Callback<CredentialResponse>() {
            @Override
            public void onResponse(Call<CredentialResponse> call, Response<CredentialResponse> response) {
                if(response.isSuccessful()&& response.code()==201&& response!=null){
                    CredentialResponse userCred = response.body();
                    Log.i(TAG, "onResponse: after logon" + new Gson().toJson(response.body()));
                    // Save user data in SharedPreferences
                    SharedPreferences sharedPreferences = getSharedPreferences("userSnapshot", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    // Convert user object to JSON string using Gson library
                    Gson gson = new Gson();
                    String userJson = gson.toJson(userCred);
                    // Save user JSON string in SharedPreferences
                    editor.putString("userCred", userJson);
                    editor.apply();


                    progressBar.setVisibility(View.GONE);
                    // Show success message and navigate to next screen
                    Toast.makeText(SignUpActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Log.e(TAG,response.code()+" "+response.errorBody().toString());
                    progressBar.setVisibility(View.GONE);

                    // parse the response body …
                    APIError error = UtilService.parseError(response);
                    Log.d("error message", error.getMsg());
                    Toast.makeText(SignUpActivity.this,error.getMsg()+" ",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CredentialResponse> call, Throwable t) {
                // Show error message
                progressBar.setVisibility(View.GONE);
                Toast.makeText(SignUpActivity.this, "Registration failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


    public boolean validate(View view){
        boolean isValid =false;
        if (TextUtils.isEmpty(name)) {
            utilService.showSnackbar(view, "Please enter name");
            return isValid;
        }
        if (TextUtils.isEmpty(email)){
            utilService.showSnackbar(view, "Please enter email");
            return isValid;
        }
        if (TextUtils.isEmpty(password)){
            utilService.showSnackbar(view, "Please enter password");
            return isValid;
        }
        return true;
    }

}