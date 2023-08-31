package com.example.jobify.Adapters;

import android.icu.text.UnicodeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jobify.Models.User;
import com.example.jobify.R;

import org.w3c.dom.Text;

import java.util.List;

public class SelectedStudentAdapter extends RecyclerView.Adapter<SelectedStudentAdapter.ViewHolder>{

    private List<User> userList ;
    public SelectedStudentAdapter(List<User> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.job_selected_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = userList.get(position);

        holder.name.setText(user.getName()+" "+ user.getLastName());
        if(holder.erpId.getText().equals("Erp Id:- ")) {
            holder.erpId.append(String.valueOf(user.getErpId()));
        }

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView name , erpId;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.studentNameTxt);
            erpId = itemView.findViewById(R.id.erpIdTxt);
        }
    }
}
