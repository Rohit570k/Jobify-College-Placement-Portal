package com.example.jobify;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.jobify.Models.APIError;
import com.example.jobify.Models.Academics;
import com.example.jobify.Models.AcademicsResponse;
import com.example.jobify.Models.CredentialResponse;
import com.example.jobify.UI.MainActivity;
import com.example.jobify.UI.Profile.EditProfileActivity;
import com.example.jobify.Utils.UtilService;
import com.example.jobify.WebService.RetrofitApi;
import com.example.jobify.WebService.RetrofitClient;
import com.example.jobify.databinding.ActivityAcademicsBinding;
import com.example.jobify.databinding.ActivityEditProfileBinding;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AcademicsActivity extends AppCompatActivity {
    private ActivityAcademicsBinding binding;
    private UtilService utilService;
    private String authToken;

    private String name, college, rollnos, tenthmark,tenthyop,
    twelthmark,twelfthyop,collegemark,collegeyop,backlog,title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAcademicsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        utilService = new UtilService();

        CredentialResponse userCred = utilService.getUserFromSharedPref(AcademicsActivity.this);
        if(userCred!=null){
            authToken=userCred.getToken();
        }

        binding.submitBtn.setOnClickListener((v)->{
            utilService.hideKeyboard(v, AcademicsActivity.this);
            name = binding.nameEdt.getText().toString().trim();
            college= binding.collegeEdt.getText().toString().trim();
            rollnos= binding.rollNoEdt.getText().toString().trim();
            tenthmark= binding.tenthmarkEdt.getText().toString().trim();
            tenthyop= binding.tenthyopEdt.getText().toString().trim();
            twelthmark= binding.twelfthmarkEdt.getText().toString().trim();
            twelfthyop= binding.twelfthyopEdt.getText().toString().trim();
            collegemark= binding.collegemarkEdt.getText().toString().trim();
            collegeyop= binding.collegeyopEdt.getText().toString().trim();
            backlog= binding.backlogEdt.getText().toString().trim();
            title= binding.tiltleEdt.getText().toString().trim();

            if(validate(v)){
                doApicallPostAcademics(v);
            }
        });
    }

    private void doApicallPostAcademics(View v) {
        binding.submitBtn.setText("Uploading...");
        RetrofitApi retrofitApi = RetrofitClient.getRetrofitApiService();

        Academics academics = new Academics(name,college,rollnos,tenthmark,tenthyop,
                twelthmark,twelfthyop,collegemark,collegeyop,backlog,title);

        Call<AcademicsResponse> userCall = retrofitApi.postAcademicsDetail("Bearer "+ authToken,academics);

        userCall.enqueue(new Callback<AcademicsResponse>() {
            @Override
            public void onResponse(Call<AcademicsResponse> call, Response<AcademicsResponse> response) {
                if(response.isSuccessful()&& response.code()==201&& response.body()!=null){
                    binding.submitBtn.setText("Upload Details");
                    Gson gson = new Gson();
                    Log.i("Academics Activity", "onResponse: "+gson.toJson(response.body()));

                    Toast.makeText(AcademicsActivity.this,response.body().getAcademics().getName()+" Academics uploaded ",Toast.LENGTH_LONG).show();

                    startActivity(new Intent(AcademicsActivity.this, MainActivity.class));

                }else{
                   binding.submitBtn.setText("Upload Details");

                    // parse the response body …
                    APIError error = UtilService.parseError(response);
                    Log.d("error message", error.getMsg());
                    Toast.makeText(AcademicsActivity.this,error.getMsg()+" ",Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<AcademicsResponse> call, Throwable t) {
                // Show error message
                binding.submitBtn.setText("Upload Details");
                Toast.makeText(AcademicsActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private boolean validate(View v) {
        boolean isValid = false;

        if(TextUtils.isEmpty(name)){
            utilService.showSnackbar(v,"Enter your name");
            return isValid;
        }
        if(TextUtils.isEmpty(college)){
            utilService.showSnackbar(v,"Enter college name");
            return isValid;
        }
        if(TextUtils.isEmpty(rollnos)){
            utilService.showSnackbar(v,"Enter college roll no");
            return isValid;
        }
        if(TextUtils.isEmpty(tenthmark)){
            utilService.showSnackbar(v,"Enter 10th percentage or cgpa");
            return isValid;
        }
        if(TextUtils.isEmpty(tenthyop)){
            utilService.showSnackbar(v,"Enter 10th yop");
            return isValid;
        }
        if(TextUtils.isEmpty(twelthmark)){
            utilService.showSnackbar(v,"Enter 12th mark");
            return isValid;
        }
        if(TextUtils.isEmpty(twelfthyop)){
            utilService.showSnackbar(v,"Enter yop of 12th");
            return isValid;
        }
        if(TextUtils.isEmpty(collegemark)){
            utilService.showSnackbar(v,"Enter college percentage");
            return isValid;
        }
        if(TextUtils.isEmpty(collegeyop)){
            utilService.showSnackbar(v,"Enter yop of college");
            return isValid;
        }
        if(TextUtils.isEmpty(backlog)){
            utilService.showSnackbar(v,"Enter backlog ");
            return isValid;
        }
        if(TextUtils.isEmpty(title)){
            utilService.showSnackbar(v,"Enter title first");
            return isValid;
        }
        return true;
    }
}