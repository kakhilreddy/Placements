package com.cse.jntu.placements;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cse.jntu.placements.models.InterviewExperience;
import com.cse.jntu.placements.models.Question;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class QA extends AppCompatActivity  implements QuestionAdapter.OnQuestionSelectedListener{

    RecyclerView recyclerView;
    Toolbar toolbar;
    List<Question> questions;
    QuestionAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_q);
        recyclerView= (RecyclerView) findViewById(R.id.questionlist);
        questions=new ArrayList<>();
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Q/A");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      //  adapter=new MyAdapter();
        Query query = FirebaseFirestore.getInstance()
                .collection("questions");
        adapter = new QuestionAdapter(query, this) {
            @Override
            protected void onDataChanged() {
                // Show/hide content if the query returns empty.

            }};
        /*query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                //List<DocumentSnapshot> snap=documentSnapshots.getDocuments();
                final List<DocumentChange> change=documentSnapshots.getDocumentChanges();
                for(DocumentChange s : change)
                {

                    final DocumentSnapshot documentSnapshot=s.getDocument();
                    Question question;
                    String q=documentSnapshot.getString("question");

                    String username=documentSnapshot.getString("username");
                    long views=new Double(Double.valueOf(documentSnapshot.getDouble("views"))).longValue();

                    String id=s.getDocument().getId();


                    question=new Question(id,q,username,views);
                    questions.add(question);
               /*     DocumentReference ref=FirebaseFirestore.getInstance().collection("ie").document(id);
                    Query query1=ref.collection("comments").orderBy("time");
                    query1.addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                            List<DocumentChange> changes=documentSnapshots.getDocumentChanges();
                            for(DocumentChange s: changes)
                            {
                                DocumentSnapshot snapshot=s.getDocument();
                                Log.d("comment",snapshot.getString("comment"));
                            }
                        }
                    });


                Log.d("list",questions.toString());
        /*for(DocumentSnapshot s: snap)
        {
            list.add(new menu(s.getString("name")));
        }
                adapter.notifyDataSetChanged();
            }
        });*/
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
    public void onQuestionSelected(DocumentSnapshot question) {

        Intent intent = new Intent(this, QuestionView.class);
        intent.putExtra("question", question.getId());
intent.putExtra("questionTitle",question.getString("question"));
        startActivity(intent);
        //overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
    }


}
