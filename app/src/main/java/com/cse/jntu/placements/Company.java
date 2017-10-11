package com.cse.jntu.placements;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.cse.jntu.placements.models.Question;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.List;

public class Company extends AppCompatActivity implements CompanyAdapter.OnCompanySelectedListener {
    RecyclerView recyclerView;
    Toolbar toolbar;

    CompanyAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company);
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        recyclerView= (RecyclerView) findViewById(R.id.companiesList);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Companies");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Query query = FirebaseFirestore.getInstance()
                .collection("companies");
        adapter = new CompanyAdapter(query, this) {
            @Override
            protected void onDataChanged() {
                // Show/hide content if the query returns empty.

            }};
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
    @Override
    protected void onStart() {
        super.onStart();
        if(adapter!=null)
            adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (adapter != null) {
            adapter.stopListening();}
    }
    @Override
    public void onCompanySelected(DocumentSnapshot company) {
        Intent intent = new Intent(this, CompanyView.class);
        intent.putExtra("company", company.getId());

        startActivity(intent);
    }
}
