package com.example.jobify.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.jobify.Models.Job;
import com.example.jobify.R;
import com.example.jobify.UI.Home.JobPopupDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class JobAdapter extends RecyclerView.Adapter<JobAdapter.ViewHolder>{

    private List<Job> jobItemList ;

    public JobAdapter(List<Job> jobItemList) {
        this.jobItemList = jobItemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.jobs_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Job jobItem = jobItemList.get(position);

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




        // Set OnClickListener for the card
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create and show the popup dialog with the clicked job data
                JobPopupDialog popupDialog = new JobPopupDialog(v.getContext(), jobItem);
                popupDialog.showPopup();
            }
        });
    }

    @Override
    public int getItemCount() {
       return jobItemList.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder{
        TextView  companyName, jobPosition, jobType, dateTime;
        ImageView image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            companyName = itemView.findViewById(R.id.textCompanyName);
            jobPosition = itemView.findViewById(R.id.textJobTitle);
            jobType = itemView.findViewById(R.id.textJobType);
            dateTime = itemView.findViewById(R.id.textJobTime);
            image = itemView.findViewById(R.id.imageCompany);

        }
    }
}
