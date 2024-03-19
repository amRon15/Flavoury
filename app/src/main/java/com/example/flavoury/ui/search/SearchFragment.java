package com.example.flavoury.ui.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flavoury.R;
import com.example.flavoury.databinding.FragmentSearchBinding;

import java.lang.reflect.Array;

public class SearchFragment extends Fragment {

    private FragmentSearchBinding binding;
    RecyclerView historyRecyclerView;

    String[] myArray = {"Egg","Fish","Apple"};


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SearchViewModel searchViewModel =
                new ViewModelProvider(this).get(SearchViewModel.class);

        binding = FragmentSearchBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        historyRecyclerView = root.findViewById(R.id.search_historyList);
        SearchHistoryAdapter searchHistoryAdapter = new SearchHistoryAdapter(myArray);
        historyRecyclerView.setAdapter(searchHistoryAdapter);
        historyRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));

        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}