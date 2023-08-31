package com.example.jobify.UI.Home;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.jobify.Models.APIError;
import com.example.jobify.Models.Application;
import com.example.jobify.Models.ApplyJobRequest;
import com.example.jobify.Models.CredentialResponse;
import com.example.jobify.Models.Job;
import com.example.jobify.Models.JobStatus.JobStatus;
import com.example.jobify.R;
import com.example.jobify.UI.MainActivity;
import com.example.jobify.Utils.UtilService;
import com.example.jobify.WebService.RetrofitApi;
import com.example.jobify.WebService.RetrofitClient;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JobPopupDialog extends Dialog {

    private Job jobItem;
    private ImageView image;
    private TextView name , position, jobType;
    private TextView ctc, location, driveDate, jd;
    private Button clear, apply;

    public JobPopupDialog(@NonNull Context context, Job jobItem) {
        super(context);
        this.jobItem = jobItem;
    }

    public void showPopup() {
        setContentView(R.layout.job_dialog_layout);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setCancelable(false);
        // Customize the dialog layout and handle additional views as needed
         image = findViewById(R.id.jobImage);
         name = findViewById(R.id.companyName);
        position = findViewById(R.id.position);
        jobType = findViewById(R.id.jobType);

        ctc = findViewById(R.id.jobCTC);
        location = findViewById(R.id.location);
        driveDate = findViewById(R.id.jobPostTime);
        jd = findViewById(R.id.description);

        clear = findViewById(R.id.clearButton);
        apply = findViewById(R.id.applyButton);

        // Access the jobItem and update the dialog views with the relevant data
        Glide.with(image).load(jobItem.getCompanyImg()).into(image);

        name.setText(jobItem.getCompany());
        position.setText(jobItem.getPosition());
        jobType.setText(jobItem.getJobType());

        ctc.setText("CTC : "+jobItem.getJobCTC());
        location.setText("Location: "+jobItem.getJobLocation());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
        try {
            Date date = sdf.parse(jobItem.getCreatedAt());
            driveDate.setText("Job Posted: "+ new SimpleDateFormat("dd MMMM yyyy").format(date));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        jd.setText(jobItem.getJobDescription());

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
               // Toast.makeText(getContext(), "Cancel clicked", Toast.LENGTH_SHORT).show();

            }
        });

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apicallApplyJob();
                dismiss();
            }
        });

        // Show the dialog
        show();


    }

    private void apicallApplyJob() {
        RetrofitApi retrofitApi = RetrofitClient.getRetrofitApiService();
        String authToken="";
        CredentialResponse userCred = new UtilService().getUserFromSharedPref(getContext());
        if(userCred!=null){
            authToken=userCred.getToken();
        }
        ApplyJobRequest applyJobRequest = new ApplyJobRequest(jobItem.get_id());
        Call<Application> call = retrofitApi.applyJob("Bearer "+authToken,applyJobRequest);

        call.enqueue(new Callback<Application>() {
            @Override
            public void onResponse(Call<Application> call, Response<Application> response) {
                if(response.isSuccessful() && response.code() == 201 && response.body()!=null){
                    Application application = response.body();
                    Log.d("TAG", "onViewCreated: "+ new Gson().toJson(application));
                    Toast.makeText(getContext(),"Applied in: "+application.getCompany(),Toast.LENGTH_LONG).show();
                }else{
                    // parse the response body …
                    APIError error = UtilService.parseError(response);
                    Log.d("error message", error.getMsg());
                    Toast.makeText(getContext(),error.getMsg()+" ",Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<Application> call, Throwable t) {
                // Show error message
                Log.d("onFailure", t.getMessage());
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }
}
