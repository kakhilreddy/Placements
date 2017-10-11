package com.cse.jntu.placements;

import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cse.jntu.placements.models.Question;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TipView extends AppCompatActivity implements EventListener<DocumentSnapshot> {


    private FirebaseFirestore mFirestore;
    private DocumentReference tipRef;
    private ListenerRegistration tipRegistration;
    private boolean isFirstTime=false;
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
        setContentView(R.layout.activity_tip_view);
        View bottomSheet = findViewById(R.id.bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        String tipId = getIntent().getExtras().getString("tip");
        if (tipId == null) {
            throw new IllegalArgumentException("Must pass extra " + "tip");
        }
        mFirestore=FirebaseFirestore.getInstance();
        tipRef = mFirestore.collection("tips").document(tipId);
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
        Query commentQuery = tipRef
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

        tipRef.collection("comments").add(map);
    }
    @Override
    protected void onStart() {
        super.onStart();
commentAdapter.startListening();
        tipRegistration = tipRef.addSnapshotListener(this);
    }
    @Override
    protected void onStop() {
        super.onStop();
commentAdapter.stopListening();
        if (tipRegistration != null) {
            tipRegistration.remove();
            tipRegistration = null;
        }
    }

    @Override
    public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
        onTipLoaded(documentSnapshot);
    }

    private void onTipLoaded(DocumentSnapshot documentSnapshot) {
     //   Log.d("test",documentSnapshot.getString("question"));
        long views=new Double(Double.valueOf(documentSnapshot.getDouble("views"))).longValue();
        description.loadData(documentSnapshot.getString("description"),"text/html", "UTF-8");
        description.setWebViewClient(new TipView.MyWebViewClient());

        if(!isFirstTime)
        {views++;
            Map<String,Object> map=new HashMap<>();
            map.put("views",views);
            tipRef.update(map);
            isFirstTime=true;}
    }
    private class MyWebViewClient extends WebViewClient {


        @Override
        public void onPageFinished(WebView view, String url) {
            System.out.println("on finish");
            progressBar.setVisibility(View.GONE);
        }

    }
}
