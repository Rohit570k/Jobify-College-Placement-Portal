package com.example.jobify.UI.Home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jobify.Adapters.JobAdapter;
import com.example.jobify.Models.APIError;
import com.example.jobify.Models.AllJobResponse;
import com.example.jobify.Models.CredentialResponse;
import com.example.jobify.Models.Job;
import com.example.jobify.Models.User;
import com.example.jobify.R;
import com.example.jobify.UI.Profile.EditProfileActivity;
import com.example.jobify.Utils.UtilService;
import com.example.jobify.WebService.RetrofitApi;
import com.example.jobify.WebService.RetrofitClient;
import com.example.jobify.databinding.ActivityAllJobsBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllJobsActivity extends AppCompatActivity {
    private ActivityAllJobsBinding binding;
     private JobAdapter jobAdapter;
     private  List<Job> jobList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAllJobsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        jobList = new ArrayList<>();

        binding.editTextSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // Job  search using search button
                    getAllJobs();
                    new UtilService().hideKeyboard(v,AllJobsActivity.this);
                    return true;
                }
                return false;
            }
        });
        // Set an OnClickListener for the clear button
        binding.buttonClearFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Reset the spinner selection to its default value
                binding.spinnerTypes.setSelection(0); // Assuming the default value is at index 0
                binding.spinnerSorting.setSelection(0);
            }
        });
        binding.searchButton.setOnClickListener((view)->{
            getAllJobs();
            new UtilService().hideKeyboard(view,AllJobsActivity.this);
        });

        getAllJobs();
    }

    private void getAllJobs() {
        binding.progressbar.setVisibility(View.VISIBLE);
        RetrofitApi retrofitApi = RetrofitClient.getRetrofitApiService();
        String authToken="";
        CredentialResponse userCred = new UtilService().getUserFromSharedPref(AllJobsActivity.this);
        if(userCred!=null){
            authToken=userCred.getToken();
        }

        // Create a query parameter map
        String jobType = binding.spinnerTypes.getSelectedItem().toString();
        String sort = binding.spinnerSorting.getSelectedItem().toString();
        String search = binding.editTextSearch.getText().toString();
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("jobType", jobType);
        queryParams.put("sort", sort);
        if(!search.isEmpty()) {
            queryParams.put("search", search);
        }

        Call<AllJobResponse> call = retrofitApi.getAllJobs("Bearer "+authToken, queryParams);
        call.enqueue(new Callback<AllJobResponse>() {
            @Override
            public void onResponse(Call<AllJobResponse> call, Response<AllJobResponse> response) {
                binding.progressbar.setVisibility(View.GONE);
                if(response.isSuccessful()&& response.code()==200&& response!=null){
                    jobList.clear();
                    jobList.addAll(response.body().getJobs());
                    binding.textJobNos.setText(response.body().getTotalJobs()+" Jobs Found");
                    setUpEventRecyclerView();
                }else{
                    // parse the response body …
                    APIError error = UtilService.parseError(response);
                    Log.d("error message", error.getMsg());
                    Toast.makeText(AllJobsActivity.this,error.getMsg()+" ",Toast.LENGTH_SHORT).show();

                }


            }

            @Override
            public void onFailure(Call<AllJobResponse> call, Throwable t) {
                binding.progressbar.setVisibility(View.GONE);

                // Show error message
                Log.d("onFailure", t.getMessage());
                Toast.makeText(AllJobsActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });



    }

    private void setUpEventRecyclerView() {
        jobAdapter=new JobAdapter(jobList);
        binding.recyclerViewCards.setAdapter(jobAdapter);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        binding.recyclerViewCards.setLayoutManager(linearLayoutManager);
        jobAdapter.notifyDataSetChanged();
    }

}