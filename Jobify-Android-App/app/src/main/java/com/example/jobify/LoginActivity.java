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

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private TextView signupBtn;

    private EditText email_ET, password_ET;
    private Button loginBtn;
    ProgressBar progressBar;
    private String email, password;
    UtilService utilService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signupBtn = findViewById(R.id.signupText);
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,SignUpActivity.class));
            }
        });

        loginBtn = findViewById(R.id.signupButton);
        progressBar = findViewById(R.id.progress_bar);
        email_ET = findViewById(R.id.emailEdt);
        password_ET = findViewById(R.id.passwordEdt);
        utilService = new UtilService();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                utilService.hideKeyboard(view, LoginActivity.this);
                email = email_ET.getText().toString();
                password = password_ET.getText().toString();

                if (validate(view)) {
                    loginUser(view);
                }
            }
        });
    }
    private void loginUser(View view) {

        progressBar.setVisibility(View.VISIBLE);

        RetrofitApi retrofitApi = RetrofitClient.getRetrofitApiService();
        User userreq = new User(email,password);

        Call<CredentialResponse> userCall = retrofitApi.login(userreq);
        userCall.enqueue(new Callback<CredentialResponse>() {
            @Override
            public void onResponse(Call<CredentialResponse> call, Response<CredentialResponse> response) {
                if(response.isSuccessful() && response.code()==200 && response.body()!=null){
                    CredentialResponse userCred = response.body();
                    Log.i(TAG, "onResponse:after api call "+response.body()+response.message()+response);


                    // Save user data in SharedPreferences
                    SharedPreferences sharedPreferences = getSharedPreferences("userSnapshot", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    // Convert user object to JSON string using Gson library

                    Gson gson = new Gson();
                    String userJson = gson.toJson(userCred);
                    Log.i(TAG, "onResponse:checking userJson "+userJson);
                    // Save user JSON string in SharedPreferences
                    editor.putString("userCred", userJson);
                    editor.apply();

                    progressBar.setVisibility(View.GONE);
                    // Navigate user to main screen
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Log.e(TAG,response.code()+" "+response.errorBody().toString());
                    progressBar.setVisibility(View.GONE);

                    // parse the response body …
                    APIError error = UtilService.parseError(response);
                    Log.d("error message", error.getMsg());
                    Toast.makeText(LoginActivity.this,error.getMsg()+" ",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<CredentialResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(LoginActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
    public boolean validate(View view) {
        boolean isValid=false;

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

    @Override
    protected void onStart() {
        super.onStart();
        UtilService utilService1 = new UtilService();
        CredentialResponse userCred= utilService1.getUserFromSharedPref(LoginActivity.this);
        if(userCred!=null){
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
    }
}