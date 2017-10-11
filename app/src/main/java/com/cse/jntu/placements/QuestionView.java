package com.cse.jntu.placements;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.cse.jntu.placements.models.InterviewExperience;
import com.cse.jntu.placements.models.Question;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuestionView extends AppCompatActivity implements EventListener<DocumentSnapshot> {

    Question question;
    DocumentReference ref;
    private FirebaseFirestore mFirestore;
    private DocumentReference questionRef;
    private ListenerRegistration questionRegistration;
    EditText commentInput;
    ImageView commentSend;
    TextView questionTitle;
    private CommentAdapter commentAdapter;
    private RecyclerView commentRecycler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_view);
        String questionId = getIntent().getExtras().getString("question");
        if (questionId == null) {
            throw new IllegalArgumentException("Must pass extra " + "question");
        }

        this.questionTitle= (TextView) findViewById(R.id.questionTitle);

        commentRecycler= (RecyclerView) findViewById(R.id.comments);
        commentInput= (EditText) findViewById(R.id.commentInput);
        commentSend= (ImageView) findViewById(R.id.commentSend);

        commentSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sendComment();
                commentInput.getText().clear();
            }
        });
        // Initialize Firestore
        mFirestore = FirebaseFirestore.getInstance();

        // Get reference to the restaurant
        questionRef = mFirestore.collection("questions").document(questionId);
        Query commentQuery = questionRef
                .collection("comments").orderBy("time", Query.Direction.DESCENDING);
        commentAdapter = new CommentAdapter(commentQuery) {
            @Override
            protected void onDataChanged() {
                /*if (getItemCount() == 0) {
                    commentRecycler.setVisibility(View.GONE);
                   // mEmptyView.setVisibility(View.VISIBLE);
                } else {
                    commentRecycler.setVisibility(View.VISIBLE);
                   // mEmptyView.setVisibility(View.GONE);
                }*/
            }
        };
        commentRecycler.setLayoutManager(new LinearLayoutManager(this));
        commentRecycler.setAdapter(commentAdapter);

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }
    private void sendComment() {

        String comment=commentInput.getText().toString();
        String username=getSharedPreferences("profile",MODE_PRIVATE).getString("name",null);
        Map<String,Object> map=new HashMap<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss");
        String format = simpleDateFormat.format(new Date());
        commentInput.clearComposingText();
        map.put("comment",comment);
        map.put("username",username);
        map.put("time", new Date());

        questionRef.collection("comments").add(map);
    }

    @Override
    protected void onStart() {
        super.onStart();
        commentAdapter.startListening();
        questionRegistration = questionRef.addSnapshotListener(this);
    }

    private void updateViews(DocumentReference ref) {

    }

    @Override
    protected void onStop() {
        super.onStop();
        commentAdapter.stopListening();
        if (questionRegistration != null) {
            questionRegistration.remove();
            questionRegistration = null;
        }
    }

    @Override
    public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {

        onQuestionLoaded(documentSnapshot);
    }
boolean isFirstTime=false;
    private void onQuestionLoaded(DocumentSnapshot documentSnapshot) {


        Log.d("test",documentSnapshot.getString("question"));
        long views=new Double(Double.valueOf(documentSnapshot.getDouble("views"))).longValue();
        questionTitle.setText(documentSnapshot.getString("question"));
        if(!isFirstTime)
        {views++;
        Map<String,Object> map=new HashMap<>();
        map.put("views",views);
        questionRef.update(map);
        isFirstTime=true;}
        updateViews(questionRef);
    }
}
