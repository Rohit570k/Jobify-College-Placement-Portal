package com.example.jobify.Adapters;

import android.content.Context;
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

import java.util.List;

public class UpcomingJobAdapter extends RecyclerView.Adapter<UpcomingJobAdapter.ViewHolder> {

    private List<Job> jobItemList ;
    private Context context;
    private ClickListerner clickListerner;

    public UpcomingJobAdapter(List<Job> jobItemList , Context context, ClickListerner clickListerner){
        this.jobItemList = jobItemList;
        this.context = context;
        this.clickListerner = clickListerner;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.job_item_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Job jobItem = jobItemList.get(position);

        holder.bindView(jobItem);
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                clickListerner.getItemClick();
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return jobItemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView companyName, jobPosition, jobCTC;
        ImageView image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.imageview);
            companyName = itemView.findViewById(R.id.companyNameTxt);

            jobPosition = itemView.findViewById(R.id.jobPositionTxt);
            jobCTC = itemView.findViewById(R.id.jobCTCTxt);



        }

        public void bindView(Job jobItem) {
            companyName.setText(jobItem.getCompany());
            jobPosition.setText(jobItem.getPosition());
            jobCTC.setText(jobItem.getJobCTC());

            Glide.with(image).load(jobItem.getCompanyImg()).into(image);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListerner.getItemClick(jobItem);
                }
            });
        }
    }

    public static interface ClickListerner {
         void getItemClick(Job jobitem);
    }




}
