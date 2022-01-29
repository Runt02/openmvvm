package com.runt.open.mvvm.ui.main.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.runt.open.mvvm.base.fragments.BaseFragment;
import com.runt.open.mvvm.databinding.FragmentHomeBinding;

public class HomeFragment extends BaseFragment<FragmentHomeBinding,HomeViewModel> {



    @Override
    public void initViews() {
        final TextView textView = binding.textHome;
        viewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
    }

}