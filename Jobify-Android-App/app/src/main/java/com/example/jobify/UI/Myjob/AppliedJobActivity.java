package com.example.jobify.UI.Myjob;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jobify.Adapters.AppliedJobAdapter;
import com.example.jobify.Adapters.JobAdapter;
import com.example.jobify.Models.APIError;
import com.example.jobify.Models.AllJobResponse;
import com.example.jobify.Models.AppliedApplication;
import com.example.jobify.Models.AppliedJobResponse;
import com.example.jobify.Models.CredentialResponse;
import com.example.jobify.R;
import com.example.jobify.UI.Home.AllJobsActivity;
import com.example.jobify.Utils.UtilService;
import com.example.jobify.WebService.RetrofitApi;
import com.example.jobify.WebService.RetrofitClient;
import com.example.jobify.databinding.ActivityAllJobsBinding;
import com.example.jobify.databinding.ActivityAppliedJobBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppliedJobActivity extends AppCompatActivity {
    private ActivityAppliedJobBinding binding;

    private List<AppliedApplication> applicationList;
    private AppliedJobAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAppliedJobBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);



        applicationList = new ArrayList<>();

        binding.editTextSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // Application  search using search button of keyboard
                    apicallgetMyApplication();
                    new UtilService().hideKeyboard(v,AppliedJobActivity.this);
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
            apicallgetMyApplication();
            new UtilService().hideKeyboard(view,AppliedJobActivity.this);
        });
        apicallgetMyApplication();
    }

    private void apicallgetMyApplication() {
        RetrofitApi retrofitApi = RetrofitClient.getRetrofitApiService();
        String authToken="";
        CredentialResponse userCred = new UtilService().getUserFromSharedPref(AppliedJobActivity.this);
        if(userCred!=null){
            authToken=userCred.getToken();
        }
        // Create a query parameter map
        String status = binding.spinnerTypes.getSelectedItem().toString();
        String sort = binding.spinnerSorting.getSelectedItem().toString();
        String search = binding.editTextSearch.getText().toString();
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("status", status);
        queryParams.put("sort", sort);
        if(!search.isEmpty()) {
            queryParams.put("search", search);
        }
        Call<AppliedJobResponse> call = retrofitApi.getMyApplication("Bearer "+authToken, queryParams);
        call.enqueue(new Callback<AppliedJobResponse>() {
            @Override
            public void onResponse(Call<AppliedJobResponse> call, Response<AppliedJobResponse> response) {
                if(response.isSuccessful()&& response.code()==200&& response.body()!=null){
                    applicationList.clear();
                    applicationList.addAll(response.body().getMyApplications());
                    binding.textJobNos.setText(response.body().getTotalApplication()+" Jobs Applications");
                    setUpEventRecyclerView();
                }else{
                    // parse the response body …
                    APIError error = UtilService.parseError(response);
                    Log.d("error message", ""+error.getMsg());
                    Toast.makeText(AppliedJobActivity.this,error.getMsg()+" ",Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<AppliedJobResponse> call, Throwable t) {
// Show error message
                Log.d("onFailure", t.getMessage());
                Toast.makeText(AppliedJobActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void setUpEventRecyclerView() {
        adapter=new AppliedJobAdapter(applicationList);
        binding.recyclerViewCards.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        binding.recyclerViewCards.setLayoutManager(linearLayoutManager);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}