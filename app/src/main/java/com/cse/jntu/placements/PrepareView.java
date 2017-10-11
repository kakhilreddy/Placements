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

public class PrepareView extends AppCompatActivity implements PrepareAdapter.OnPrepareSelectedListener {
    RecyclerView recyclerView;
    Toolbar toolbar;
    PrepareAdapter adapter;
    int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prepare_view);


        recyclerView= (RecyclerView) findViewById(R.id.prepareList);
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
         position=getIntent().getExtras().getInt("prepare");
        getSupportActionBar().setTitle(Constants.subjects[position]);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Query query;
        if(position==0)
         query = FirebaseFirestore.getInstance()
                .collection("c");
        else if(position==1)
        {
            query = FirebaseFirestore.getInstance()
                    .collection("java");
        }else if(position==2)
        {  query = FirebaseFirestore.getInstance()
                .collection("ds");

        }else
        {  query = FirebaseFirestore.getInstance()
                .collection("os");

        }
        adapter=new PrepareAdapter(query,this);

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
    public void onPrepareselected(DocumentSnapshot prepare) {

        Intent intent = new Intent(this, PrepareContent.class);
        intent.putExtra("prepare", prepare.getId());
intent.putExtra("preparePos",position);
        startActivity(intent);

    }
}
