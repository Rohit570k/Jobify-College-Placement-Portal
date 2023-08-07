package com.example.jobify.UI.Home;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.jobify.Models.APIError;
import com.example.jobify.Models.AllJobResponse;
import com.example.jobify.Models.CredentialResponse;
import com.example.jobify.Models.JobStatus.JobStatus;
import com.example.jobify.Models.JobStatus.JobTypes;
import com.example.jobify.Models.JobStatus.MonthlyApplication;
import com.example.jobify.Models.JobStatus.Status;
import com.example.jobify.Models.User;
import com.example.jobify.R;
import com.example.jobify.Utils.UtilService;
import com.example.jobify.WebService.RetrofitApi;
import com.example.jobify.WebService.RetrofitClient;
import com.example.jobify.databinding.FragmentHomeBinding;
import com.example.jobify.databinding.FragmentProfileBinding;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

     private FragmentHomeBinding binding;
     private JobStatus jobStatus;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding= FragmentHomeBinding.inflate(inflater, container, false);
        return  binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        jobStatus= new JobStatus();
        CredentialResponse userCred = new UtilService().getUserFromSharedPref(getContext());
        if(userCred!=null){
            User user =userCred.getUser();
            binding.textView2.setText(user.getName()+" "+user.getLastName());
        }

        binding.editTextText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), AllJobsActivity.class));
            }
        });

        apiCallShowStats();



    }

    private void apiCallShowStats() {
        RetrofitApi retrofitApi = RetrofitClient.getRetrofitApiService();
        String authToken="";
        CredentialResponse userCred = new UtilService().getUserFromSharedPref(getContext());
        if(userCred!=null){
            authToken=userCred.getToken();
        }
        Call<JobStatus> call = retrofitApi.showStats("Bearer "+authToken);
        call.enqueue(new Callback<JobStatus>() {
            @Override
            public void onResponse(Call<JobStatus> call, Response<JobStatus> response) {
                if(response.isSuccessful() && response.code() == 200){
                    Log.d("TAG", "onViewCreated: "+ new Gson().toJson(response.body()));

                    jobStatus = response.body();
                    showdrivestatusPieChart();
                    showdrivetypePieChart();
                    showMonthlyDrive();

                }else{
                    // parse the response body …
                    APIError error = UtilService.parseError(response);
                    Log.d("error message", error.getMsg());
                    Toast.makeText(getContext(),error.getMsg()+" ",Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<JobStatus> call, Throwable t) {
                // Show error message
                Log.d("onFailure", t.getMessage());
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void showMonthlyDrive() {


        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();

        List<MonthlyApplication> list = new ArrayList<>();
         list = jobStatus.getMonthlyApplications(); // Replace this with your actual method to retrieve the monthly application data

        for (int i = 0; i < list.size(); i++) {
            MonthlyApplication monthlyApplication = list.get(i);
            entries.add(new BarEntry(i, monthlyApplication.getCount()));
            labels.add(monthlyApplication.getDate());
        }

        BarDataSet dataSet = new BarDataSet(entries, "                            Numbers of Drive in 5 Months");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS); // Set colors for the bars
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setValueTextSize(16f);

        BarData barData = new BarData(dataSet);

        binding.barChart.getDescription().setEnabled(false); // Disable chart description
        binding.barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels)); // Set custom labels on the x-axis
        binding.barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM); // Position the x-axis at the bottom
        binding.barChart.getXAxis().setGranularity(1f); // Set the interval for displaying x-axis labels
        binding.barChart.getAxisLeft().setGranularity(1f); // Set the interval for displaying y-axis labels
        binding.barChart.getAxisRight().setEnabled(false); // Disable the right-side y-axis
        binding.barChart.setFitBars(true); // Adjust the bar width to fit the chart width
        binding.barChart.setDrawValueAboveBar(true); // Display the value above each bar


        binding.barChart.setData(barData);
//        binding.barChart.getDescription().setText("DBGFHBDV");
        binding.barChart.animateY(1000);
        binding.barChart.invalidate(); // Refresh the chart
    }
    private void showdrivestatusPieChart() {
        Log.d("TAG", "onViewCreated: "+ new Gson().toJson(jobStatus.getStatus()));

        ArrayList<PieEntry> statuses = new ArrayList<>();
        Status status = jobStatus.getStatus();
        statuses.add(new PieEntry(status.getOngoing(),"ongoing"));
        statuses.add(new PieEntry(status.getCompleted(),"completed"));
        statuses.add(new PieEntry(status.getUpcoming(),"upcoming"));


        PieDataSet pieDataSet = new PieDataSet(statuses,"Drive stats");
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieDataSet.setValueTextColor(Color.BLACK);
        pieDataSet.setValueTextSize(16f);

        PieData pieData = new PieData(pieDataSet);

        binding.pieChartStats.setData(pieData);
        binding.pieChartStats.getDescription().setEnabled(true);
        binding.pieChartStats.setCenterText("Drive Status");
        binding.pieChartStats.animate();
        binding.pieChartStats.invalidate();
    }
    private void showdrivetypePieChart() {
        Log.d("TAG", "onViewCreated: "+ new Gson().toJson(jobStatus.getMonthlyApplications()));

        ArrayList<PieEntry> statuses = new ArrayList<>();
        JobTypes status = jobStatus.getJobTypes();
        statuses.add(new PieEntry(status.getFulltime(),"Full-time"));
        statuses.add(new PieEntry(status.getParttime(),"Part-time"));
        statuses.add(new PieEntry(status.getInternship(),"Internship"));
        statuses.add(new PieEntry(status.getRemote(),"remote"));


        PieDataSet pieDataSet = new PieDataSet(statuses,"Type ");
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieDataSet.setValueTextColor(Color.BLACK);
        pieDataSet.setValueTextSize(16f);

        PieData pieData = new PieData(pieDataSet);

        binding.pieChartTypes.setData(pieData);
        binding.pieChartTypes.getDescription().setEnabled(true);
        binding.pieChartTypes.setCenterText("Job Types");
        binding.pieChartTypes.animate();
        binding.pieChartTypes.invalidate(); // Refresh the chart
    }
}