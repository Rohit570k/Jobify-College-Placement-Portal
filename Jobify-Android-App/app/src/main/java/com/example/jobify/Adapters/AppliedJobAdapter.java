package com.example.jobify.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.jobify.Models.Application;
import com.example.jobify.Models.AppliedApplication;
import com.example.jobify.Models.Job;
import com.example.jobify.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AppliedJobAdapter extends RecyclerView.Adapter<AppliedJobAdapter.ViewHolder> {

    private List<AppliedApplication> applicationList ;

    public AppliedJobAdapter(List<AppliedApplication> applicationList) {
        this.applicationList = applicationList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.job_applied_layout,parent,false);
        return new AppliedJobAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AppliedApplication application = applicationList.get(position);
        Job jobItem = application.getApplied();

        // Bind the data to the views in the ViewHolder
        holder.companyName.setText(jobItem.getCompany());
        holder.jobPosition.setText(jobItem.getPosition());
        holder.jobType.setText(jobItem.getJobType());
        Glide.with(holder.image).load(jobItem.getCompanyImg()).into(holder.image);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
        try {
            Date date = sdf.parse(jobItem.getCreatedAt());
            holder.dateTime.setText(new SimpleDateFormat("dd MMMM yyyy").format(date));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        holder.status.setText("Status of my application : "+application.getStatus());

    }

    @Override
    public int getItemCount() {
        return applicationList.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder{
        TextView companyName, jobPosition, jobType, dateTime , status;
        ImageView image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            companyName = itemView.findViewById(R.id.textCompanyName);
            jobPosition = itemView.findViewById(R.id.textJobTitle);
            jobType = itemView.findViewById(R.id.textJobType);
            dateTime = itemView.findViewById(R.id.textJobTime);
            image = itemView.findViewById(R.id.imageCompany);
            status = itemView.findViewById(R.id.status);
        }
    }
}
