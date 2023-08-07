package com.example.jobify.UI.Profile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jobify.LoginActivity;
import com.example.jobify.Models.CredentialResponse;
import com.example.jobify.Models.User;
import com.example.jobify.R;
import com.example.jobify.Utils.UtilService;
import com.example.jobify.databinding.FragmentProfileBinding;

import okhttp3.internal.Util;


public class ProfileFragment extends Fragment {


   private FragmentProfileBinding binding;
   private UtilService utilService;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        utilService = new UtilService();
        /*making personal info visible*/
        binding.personalinfo.setVisibility(View.VISIBLE);
        binding.experience.setVisibility(View.GONE);
        binding.personalinfobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                binding.personalinfo.setVisibility(View.VISIBLE);
                binding.experience.setVisibility(View.GONE);
                binding.personalinfobtn.setTextColor(getResources().getColor(R.color.primaryColorVariant));
                binding.experiencebtn.setTextColor(getResources().getColor(R.color.fadeprimaryColor));

            }
        });

        binding.experiencebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                binding.personalinfo.setVisibility(View.GONE);
                binding.experience.setVisibility(View.VISIBLE);
                binding.personalinfobtn.setTextColor(getResources().getColor(R.color.fadeprimaryColor));
                binding.experiencebtn.setTextColor(getResources().getColor(R.color.primaryColorVariant));


            }
        });

        binding.editBtn.setOnClickListener((v)->{
            startActivity(new Intent(this.getContext(), EditProfileActivity.class));
        });

        binding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Clear user data from SharedPreferences
                SharedPreferences sharedPreferences = getContext().getSharedPreferences("userSnapshot", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();

                startActivity(new Intent(getContext(), LoginActivity.class));
                getActivity().finish();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        CredentialResponse userCred = utilService.getUserFromSharedPref(this.getContext());
        if(userCred!=null){
            filluserInfo(userCred.getUser());
        }
    }

    private void filluserInfo(User user) {
        binding.nameTxt.setText(user.getName()+" "+user.getLastName());
        binding.erpIdTxt.setText("ErpId: "+user.getErpId());
        binding.emailTxt.setText(""+user.getEmail());
        binding.locationTxt.setText(""+user.getLocation());
    }
}