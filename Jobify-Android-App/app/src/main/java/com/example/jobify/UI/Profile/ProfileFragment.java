package com.example.jobify.UI.Profile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.jobify.AcademicsActivity;
import com.example.jobify.LoginActivity;
import com.example.jobify.Models.APIError;
import com.example.jobify.Models.Academics;
import com.example.jobify.Models.AcademicsResponse;
import com.example.jobify.Models.CredentialResponse;
import com.example.jobify.Models.User;
import com.example.jobify.R;
import com.example.jobify.UI.Home.AllJobsActivity;
import com.example.jobify.Utils.UtilService;
import com.example.jobify.WebService.RetrofitApi;
import com.example.jobify.WebService.RetrofitClient;
import com.example.jobify.databinding.FragmentProfileBinding;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProfileFragment extends Fragment {


   private FragmentProfileBinding binding;
   private UtilService utilService;

    private String name, college, rollnos, tenthmark,tenthyop,
            twelthmark,twelfthyop,collegemark,collegeyop,backlog,title;

    private Academics academicsDetail= null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        utilService = new UtilService();

        getMyAcademicsDetails(view);
        /*making personal info visible*/
        binding.personalinfo.setVisibility(View.VISIBLE);
        binding.experience.setVisibility(View.GONE);
        binding.personalinfobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                binding.personalinfo.setVisibility(View.VISIBLE);
                binding.experience.setVisibility(View.GONE);
                binding.personalinfobtn.setTextColor(getResources().getColor(R.color.primaryColorVariant));
                binding.experiencebtn.setTextColor(getResources().getColor(R.color.fadeprimaryColor));

            }
        });

        binding.experiencebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                binding.personalinfo.setVisibility(View.GONE);
                binding.experience.setVisibility(View.VISIBLE);
                binding.personalinfobtn.setTextColor(getResources().getColor(R.color.fadeprimaryColor));
                binding.experiencebtn.setTextColor(getResources().getColor(R.color.primaryColorVariant));


            }
        });

        binding.editBtn.setOnClickListener((v)->{
            startActivity(new Intent(this.getContext(), EditProfileActivity.class));
        });
        binding.editAcademicsBtn.setOnClickListener((v)->{
            startActivity(new Intent(this.getContext(), AcademicsActivity.class));
        });

        binding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Clear user data from SharedPreferences
                SharedPreferences sharedPreferences = getContext().getSharedPreferences("userSnapshot", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();

                startActivity(new Intent(getContext(), LoginActivity.class));
                getActivity().finish();
            }
        });
    }

    private void getMyAcademicsDetails(View view) {
        RetrofitApi retrofitApi = RetrofitClient.getRetrofitApiService();

        CredentialResponse userCred = utilService.getUserFromSharedPref(getContext());
        String authToken = null;
        if(userCred!=null){
            authToken = userCred.getToken();
        }


        Call<AcademicsResponse> userCall = retrofitApi.getMyAcademics("Bearer "+ authToken);
        userCall.enqueue(new Callback<AcademicsResponse>() {
            @Override
            public void onResponse(Call<AcademicsResponse> call, Response<AcademicsResponse> response) {
                if(response.isSuccessful()&& response.code()==200 && response.body()!=null){
                    academicsDetail = response.body().getAcademics();
                    Gson gson = new Gson();
                    Log.i("Profile Fragment", "onResponse: "+gson.toJson(response.body()));


                    if(academicsDetail!=null) {
                        fillAcademicsDetail(academicsDetail);
                    }


                }else{
                    // parse the response body …
                    APIError error = UtilService.parseError(response);
                    Log.d(getActivity().getLocalClassName()+"ProfileFragment", ""+error.getMsg());
                    Toast.makeText(getContext(),error.getMsg()+" ",Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<AcademicsResponse> call, Throwable t) {
               // Show error message
                Log.d("onFailure", t.getMessage());
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();
        CredentialResponse userCred = utilService.getUserFromSharedPref(this.getContext());
        if(userCred!=null){
            filluserInfo(userCred.getUser());
        }
    }

    private void filluserInfo(User user) {
        binding.nameTxt.setText(user.getName()+" "+user.getLastName());
        binding.erpIdTxt.setText("ErpId: "+user.getErpId());
        binding.emailTxt.setText(""+user.getEmail());
        binding.locationTxt.setText(""+user.getLocation());
    }

    private void fillAcademicsDetail(Academics academics){
      //  binding.nameTxt.setText(academics.getName());
        binding.titleTxt.setText(academics.getTitle());

        binding.collegeNameTxt.setText(academics.getCollege());
        binding.rollNosTxt.setText(academics.getRollnos());
        binding.tenthmarkTxt.setText(academics.getTenthmark()+ " | "+ academics.getTenthyearofpassing());
        binding.twelfthmarkTxt.setText(academics.getTwelfthmark()+ " | "+ academics.getTwelfthmarkyearofpassing());
        binding.collegeMarkTxt.setText(academics.getCollegemarks()+ " | "+ academics.getCollegeyearofpassing());

    }
}