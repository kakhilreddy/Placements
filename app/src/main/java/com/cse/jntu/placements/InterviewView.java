package com.cse.jntu.placements;

import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cse.jntu.placements.models.Comment;
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

public class InterviewView extends AppCompatActivity implements EventListener<DocumentSnapshot>  {

    Question question;
    DocumentReference ref;
    private FirebaseFirestore mFirestore;
    private DocumentReference interviewRef;
    private ListenerRegistration interviewRegistration;
    private CommentAdapter commentAdapter;
    private RecyclerView commentRecycler;
    BottomSheetBehavior bottomSheetBehavior;
WebView description;
    EditText commentInput;
    ImageView commentSend;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interview_view);

        View bottomSheet = findViewById(R.id.bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);

        setup();
        String interviewId = getIntent().getExtras().getString("ie");
        if (interviewId == null) {
            throw new IllegalArgumentException("Must pass extra " + "ie");
        }

        description= (WebView) findViewById(R.id.description);
        commentRecycler= (RecyclerView) findViewById(R.id.comments);
        commentInput= (EditText) findViewById(R.id.commentInput);
        commentSend= (ImageView) findViewById(R.id.commentSend);
        progressBar= (ProgressBar) findViewById(R.id.progressBar);
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
        interviewRef = mFirestore.collection("ie").document(interviewId);
        Query commentQuery = interviewRef
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

        interviewRef.collection("comments").add(map);
    }

    private void setup() {

        //Handling movement of bottom sheets from sliding
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {

                } else if (newState == BottomSheetBehavior.STATE_EXPANDED) {

                }
            }

            @Override
            public void onSlide(View bottomSheet, float slideOffset) {

            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        commentAdapter.startListening();
        interviewRegistration = interviewRef.addSnapshotListener(this);
    }

    private void updateViews(DocumentReference ref) {

    }

    @Override
    protected void onStop() {
        super.onStop();
        commentAdapter.stopListening();
        if (interviewRegistration != null) {
            interviewRegistration.remove();
            interviewRegistration = null;
        }
    }

    @Override
    public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {

        onInterviewLoaded(documentSnapshot);
    }
    boolean isFirstTime=false;
    private void onInterviewLoaded(DocumentSnapshot documentSnapshot) {


        Log.d("test",documentSnapshot.getString("title"));
        long views=new Double(Double.valueOf(documentSnapshot.getDouble("views"))).longValue();
        description.loadData(documentSnapshot.getString("description"),"text/html", "UTF-8");
        description.setWebViewClient(new MyWebViewClient());
        if(!isFirstTime)
        {views++;
            Map<String,Object> map=new HashMap<>();
            map.put("views",views);
            interviewRef.update(map);
            isFirstTime=true;}
        updateViews(interviewRef);
    }
    private class MyWebViewClient extends WebViewClient {


        @Override
        public void onPageFinished(WebView view, String url) {
            System.out.println("on finish");
            progressBar.setVisibility(View.GONE);
        }

    }

}
