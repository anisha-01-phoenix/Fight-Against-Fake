package com.example.fightagainstfake.complaints;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fightagainstfake.R;
import com.example.fightagainstfake.databinding.FragmentAddComplaintBinding;

public class addComplaint extends Fragment {


    FragmentAddComplaintBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddComplaintBinding.inflate(inflater, container, false);


        binding.addComplaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), complaintAdd.class);
                startActivity(intent);
                binding.videoView.pause();

            }
        });

        Handler handler = new Handler();
        Runnable run = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 1000);
            }
        };

        handler.post(run);

        new CountDownTimer(1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {

            }
        };

        binding.videoView.setVideoPath("android.resource://" + getActivity().getPackageName() + "/" +R.raw.complain_screen);
        binding.videoView.start();


        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.videoView.setVideoPath("android.resource://" + getActivity().getPackageName() + "/" +R.raw.complain_screen);
        binding.videoView.start();

    }

    @Override
    public void onStart() {
        super.onStart();
        binding.videoView.setVideoPath("android.resource://" + getActivity().getPackageName() + "/" +R.raw.complain_screen);
        binding.videoView.start();
    }
}