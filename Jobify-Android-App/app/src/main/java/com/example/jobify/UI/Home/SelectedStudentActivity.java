package com.example.jobify.UI.Home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.net.wifi.hotspot2.pps.Credential;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.jobify.Adapters.SelectedStudentAdapter;
import com.example.jobify.Adapters.UpcomingJobAdapter;
import com.example.jobify.Models.APIError;
import com.example.jobify.Models.AllJobResponse;
import com.example.jobify.Models.CredentialResponse;
import com.example.jobify.Models.Job;
import com.example.jobify.Models.User;
import com.example.jobify.R;
import com.example.jobify.Utils.UtilService;
import com.example.jobify.WebService.RetrofitApi;
import com.example.jobify.WebService.RetrofitClient;
import com.example.jobify.databinding.ActivitySelectedStudentBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectedStudentActivity extends AppCompatActivity implements UpcomingJobAdapter.ClickListerner {


    private ActivitySelectedStudentBinding binding;

    private UpcomingJobAdapter completeJobAdapter;
    private SelectedStudentAdapter selectedAdapter;
    private   String authToken="";
    private List<Job> completedJobList = new ArrayList<>();
    

    private boolean isScrolling= true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySelectedStudentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        CredentialResponse userCred = new UtilService().getUserFromSharedPref(this);
        if(userCred!=null){
            authToken=userCred.getToken();
        }
        getCompletedDrive(authToken);

        binding.upcomingDrivesRecylerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == RecyclerView.SCROLL_STATE_IDLE && isScrolling) {
                    isScrolling = false;
                    LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    Log.i("TAG1", "onScrolled: " + recyclerView);


                    int position = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
                    if (position > -1) {

                        Job jobItem = completedJobList.get(position);
                        getItemClick(jobItem);
                    }
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                isScrolling= true;
            }

        });
    }

    private void getCompletedDrive(String authToken) {
        RetrofitApi retrofitApi = RetrofitClient.getRetrofitApiService();

        // Create a query parameter map
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("status", "completed");
        queryParams.put("sort", "latest");

        Call<AllJobResponse> call = retrofitApi.getAllJobs("Bearer "+authToken, queryParams);
        call.enqueue(new Callback<AllJobResponse>() {
            @Override
            public void onResponse(Call<AllJobResponse> call, Response<AllJobResponse> response) {

                if(response.isSuccessful()&& response.code()==200&& response.body()!=null){
                    completedJobList.clear();
                    completedJobList = response.body().getJobs();

                    if(!completedJobList.isEmpty() ) {
                        setUpHorizonatalRecylerView(authToken);
                    }
                }else{
                    // parse the response body …
                    APIError error = UtilService.parseError(response);
                    Log.d("error message",""+ error.getMsg());
                    Toast.makeText(SelectedStudentActivity.this,error.getMsg()+" ",Toast.LENGTH_SHORT).show();

                }


            }

            @Override
            public void onFailure(Call<AllJobResponse> call, Throwable t) {
                // Show error message
                Log.d("onFailure", ""+t.getMessage());
                Toast.makeText(SelectedStudentActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void setUpHorizonatalRecylerView( String authToken) {

        completeJobAdapter = new UpcomingJobAdapter(completedJobList, this, this::getItemClick);
        binding.upcomingDrivesRecylerView.setAdapter(completeJobAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        binding.upcomingDrivesRecylerView.setLayoutManager(linearLayoutManager);
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(binding.upcomingDrivesRecylerView);
        binding.upcomingDrivesRecylerView.setPreserveFocusAfterLayout(true);
    }

    private void getSelectedStudent(String authToken,Job jobitem) {
        binding.notSelectedView.setVisibility(View.GONE);
        binding.selectedStdRecyclerView.setAdapter(null);
        binding.nosSelectedTxt.setText("Selected Student : ");

        RetrofitApi retrofitApi = RetrofitClient.getRetrofitApiService();

        String jobid = jobitem.get_id();

        Call<List<User>> call= retrofitApi.getSelectedStudent("Bearer "+authToken,jobid);


        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if(response.isSuccessful()&& response.code()==200&& response.body()!=null){
                    List<User> userList = response.body();
                    binding.nosSelectedTxt.append(String.valueOf(userList.size()));

                  setUpSelectedStudentRecyclerView(userList);
                }else{
                    // parse the response body …
                    APIError error = UtilService.parseError(response);
                    Log.d(getLocalClassName(),""+""+ error.getMsg());
                    if(error.getMsg().equals("No one")){
                        binding.notSelectedView.setVisibility(View.VISIBLE);
                    }
                    Toast.makeText(SelectedStudentActivity.this,error.getMsg()+" ",Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                // Show error message
                Log.d("onFailure", ""+t.getMessage());
                Toast.makeText(SelectedStudentActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void setUpSelectedStudentRecyclerView(List<User> userList) {
//       User user = new  UtilService().getUserFromSharedPref(this).getUser();
//        userList.add(user);
//        userList.add(user);userList.add(user);
//        userList.add(user);userList.add(user);
//        userList.add(user);userList.add(user);
//        userList.add(user);
//        userList.add(user);
//        userList.add(user);


        selectedAdapter = new SelectedStudentAdapter(userList);

        binding.selectedStdRecyclerView.setAdapter(selectedAdapter);
        binding.selectedStdRecyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    public void getItemClick(Job jobitem) {
        //TODO do an api call to find selected student amd set on recycelerview
        binding.companyNameTxt.setText(jobitem.getCompany());

        Glide.with(binding.backgroundImg).load(jobitem.getCompanyImg())
                .into(binding.backgroundImg);

        getSelectedStudent(authToken,jobitem);
    }
}