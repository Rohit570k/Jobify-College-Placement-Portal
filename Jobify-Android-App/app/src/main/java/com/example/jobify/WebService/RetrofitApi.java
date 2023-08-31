package com.example.jobify.WebService;

import com.example.jobify.Models.Academics;
import com.example.jobify.Models.AcademicsResponse;
import com.example.jobify.Models.AllJobResponse;
import com.example.jobify.Models.Application;
import com.example.jobify.Models.AppliedJobResponse;
import com.example.jobify.Models.AppliedStatus.AppliedStatus;
import com.example.jobify.Models.AppliedStatus.MyStatus;
import com.example.jobify.Models.ApplyJobRequest;
import com.example.jobify.Models.CredentialResponse;
import com.example.jobify.Models.Job;
import com.example.jobify.Models.JobStatus.JobStatus;
import com.example.jobify.Models.User;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface RetrofitApi {

    @POST("/api/v1/auth/register")
    Call<CredentialResponse> register(@Body User user);

    @POST("/api/v1/auth/login")
    Call<CredentialResponse> login(@Body User user);

    @PATCH("/api/v1/auth/updateUser")
    Call<CredentialResponse> updateUser(@Header("Authorization") String authHeader, @Body User user);

    @GET("/api/v1/jobs")
    Call<AllJobResponse> getAllJobs(@Header("Authorization") String authHeader, @QueryMap Map<String, String> queryParams);

    @GET("/api/v1/jobs/stats")
    Call<JobStatus> showStats(@Header("Authorization") String authHeader);



    //Application model
    @POST("/api/v1/application/")
    Call<Application> applyJob(@Header("Authorization") String authHeader, @Body ApplyJobRequest applyJobRequest);

    @GET("/api/v1/application/")
    Call<AppliedJobResponse> getMyApplication(@Header("Authorization") String authHeader,@QueryMap Map<String, String> queryParams);

    @GET("/api/v1/application/stats")
    Call<AppliedStatus> showMyAppliedStats(@Header("Authorization") String authHeader);


    // GET /api/v1/application/selected/646f68abd3f549ea260ba1d9
    // if found selected  list of selected user
    // else error msg  No one with status code 400
    @GET("/api/v1/application/selected/{jobId}")
    Call<List<User>> getSelectedStudent(@Header("Authorization") String authHeader, @Path("jobId") String jobId);





    //Academics detal model
    @POST("/api/v1/academics/")
    Call<AcademicsResponse> postAcademicsDetail(@Header("Authorization") String authHeader, @Body Academics academics);

    @GET("/api/v1/academics/")
    Call<AcademicsResponse> getMyAcademics(@Header("Authorization") String authHeader);



}
