package com.example.flavoury.ui.Setting;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;

import com.example.flavoury.databinding.FragmentSettingBinding;
import com.example.flavoury.ui.Setting.SettingViewModel;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;


public class SettingFragment extends Fragment {

    private FragmentSettingBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SettingViewModel settingViewModel =
                new ViewModelProvider(this).get(SettingViewModel.class);

        binding = FragmentSettingBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        /*
        final TextView textView = binding.viewtSetting;
        SettingViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        */
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}