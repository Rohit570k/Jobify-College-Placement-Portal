package com.example.jobify.UI.Myjob;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.jobify.Models.APIError;
import com.example.jobify.Models.AppliedStatus.AppliedStatus;
import com.example.jobify.Models.AppliedStatus.CompanyName;
import com.example.jobify.Models.AppliedStatus.MyStatus;
import com.example.jobify.Models.CredentialResponse;
import com.example.jobify.Models.JobStatus.JobStatus;
import com.example.jobify.Models.JobStatus.MonthlyApplication;
import com.example.jobify.Models.User;
import com.example.jobify.R;
import com.example.jobify.UI.Home.AllJobsActivity;
import com.example.jobify.Utils.UtilService;
import com.example.jobify.WebService.RetrofitApi;
import com.example.jobify.WebService.RetrofitClient;
import com.example.jobify.databinding.FragmentAppliedBinding;
import com.example.jobify.databinding.FragmentHomeBinding;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AppliedFragment extends Fragment {

    private FragmentAppliedBinding binding;
    private AppliedStatus appliedStatus;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAppliedBinding.inflate(inflater,container,false);
        return  binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        appliedStatus = new AppliedStatus();
        CredentialResponse userCred = new UtilService().getUserFromSharedPref(getContext());
        if(userCred!=null){
            User user =userCred.getUser();
            binding.textView.setText("Hello "+ user.getName()+" "+user.getLastName());
        }
        binding.editTextText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), AppliedJobActivity.class));
            }
        });

        apicallMyDrivestatus();
    }

    private void apicallMyDrivestatus() {
        RetrofitApi retrofitApi = RetrofitClient.getRetrofitApiService();
        String authToken="";
        CredentialResponse userCred = new UtilService().getUserFromSharedPref(getContext());
        if(userCred!=null){
            authToken=userCred.getToken();
        }
        Call<AppliedStatus> call = retrofitApi.showMyAppliedStats("Bearer "+authToken);
        call.enqueue(new Callback<AppliedStatus>() {
            @Override
            public void onResponse(Call<AppliedStatus> call, Response<AppliedStatus> response) {
                if(response.isSuccessful() && response.code() == 200){
                    Log.d("TAG", "onViewCreated: "+ new Gson().toJson(response.body()));

                    appliedStatus = response.body();
                    showAppliedDriveGraph();
                    showCompanyStatus();

                }else{
                    // parse the response body …
                    APIError error = UtilService.parseError(response);
                    Log.d("error message",""+ error.getMsg());
                    Toast.makeText(getContext(),error.getMsg()+" ",Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<AppliedStatus> call, Throwable t) {
                // Show error message
                Log.d("onFailure", t.getMessage());
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void showCompanyStatus() {
        CompanyName companyName = appliedStatus.getCompanyName();

        List<String> selected = companyName.getSelected();

        if(!selected.isEmpty()){
            StringBuilder selectedString = new StringBuilder();
            for(String company : selected){
                selectedString.append(company+"\n");
            }
            binding.selectedTxt.setText(selectedString.toString());
        }



        List<String> interview = companyName.getInterview();

        if(!interview.isEmpty()){
            StringBuilder selectedString = new StringBuilder();
            for(String company : interview){
                selectedString.append(company+"\n");
            }
            binding.interviewTxt.setText(selectedString.toString());
        }




        List<String> secondRound = companyName.getSecondround();

        if(!secondRound.isEmpty()){
            StringBuilder selectedString = new StringBuilder();
            for(String company : secondRound){
                selectedString.append(company+"\n");
            }
            binding.secondRoundTxt.setText(selectedString.toString());
        }



        List<String> firstRound = companyName.getFirstround();

        if(!firstRound.isEmpty()){
            StringBuilder selectedString = new StringBuilder();
            for(String company : firstRound){
                selectedString.append(company+"\n");
            }
            binding.firstRoundTxt.setText(selectedString.toString());
        }
    }

    private void showAppliedDriveGraph() {


        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();

        MyStatus myStatus = appliedStatus.getMyStatus();
        int[] values = {myStatus.getFirstround(), myStatus.getSecondround(), myStatus.getInterview(), myStatus.getSelected()};

        for (int i = 0; i < values.length; i++) {
            entries.add(new BarEntry(i, values[i]));
            labels.add(getLabelByIndex(i)); // Replace this with your method to get the label by index
        }

        BarDataSet dataSet = new BarDataSet(entries, "                      My Applied Job Status ");
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

    private String getLabelByIndex(int i) {
        if(i==0) return "1st Round";
        else if(i==1) return "2nd Round";
        else if(i==2) return "Interview";
        else if(i==3) return "Selected";
        else return "Wrong Choice";
    }


}