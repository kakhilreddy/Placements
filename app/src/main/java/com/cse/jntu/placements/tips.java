package com.cse.jntu.placements;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class tips extends AppCompatActivity implements TipsAdapter.OnTipSelectedListener {
    RecyclerView recyclerView;
    Toolbar toolbar;
    TipsAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tips);
        recyclerView= (RecyclerView) findViewById(R.id.tipslist);
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Tips");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Query query = FirebaseFirestore.getInstance()
                .collection("tips");
        adapter=new TipsAdapter(query,this);

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
    public void onTipelected(DocumentSnapshot question) {
        Intent intent = new Intent(this, TipView.class);
        intent.putExtra("tip", question.getId());

        startActivity(intent);
    }
}
