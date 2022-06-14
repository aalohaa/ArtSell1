package com.example.artsell.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.artsell.R;
import com.example.artsell.adapters.HomeAdapter;
import com.example.artsell.adapters.PopularAdapter;
import com.example.artsell.adapters.RecommendedAdapter;
import com.example.artsell.adapters.ViewAllAdapter;
import com.example.artsell.models.HomeCategory;
import com.example.artsell.models.PopularModel;
import com.example.artsell.models.RecommendedModel;
import com.example.artsell.models.ViewAllModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MarketFragment extends Fragment{

    public MarketFragment() {

    }

    ScrollView scrollView;

    RecyclerView recyclerView;
    FirebaseFirestore db;

    // Whole Market Items
    List<ViewAllModel> viewAllModelList;
    ViewAllAdapter viewAllAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_market, container, false);
        db = FirebaseFirestore.getInstance();

        recyclerView = root.findViewById(R.id.marketRecyclerView);
        scrollView = root.findViewById(R.id.scroll_view);

        scrollView.setVisibility(View.GONE);

        //All items
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        viewAllModelList = new ArrayList<>();
        viewAllAdapter = new ViewAllAdapter(this.getActivity(), viewAllModelList);
        recyclerView.setAdapter(viewAllAdapter);

        // Getting art
        db.collection("AllProducts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ViewAllModel viewAllModel = document.toObject(ViewAllModel.class);
                                viewAllModelList.add(viewAllModel);
                                viewAllAdapter.notifyDataSetChanged();

                                scrollView.setVisibility(View.VISIBLE);

                            }
                        } else {
                            Toast.makeText(getActivity(), "Error"+task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        return root;
    }
}