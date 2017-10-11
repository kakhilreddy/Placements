package com.cse.jntu.placements;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.HashMap;
import java.util.Map;

public class CompanyView extends AppCompatActivity  implements EventListener<DocumentSnapshot> {

    private FirebaseFirestore mFirestore;
    private DocumentReference companyRef;
    private ListenerRegistration companyRegistration;
TextView companyTitle;
    TextView year,emp;
    RatingBar ratingBar;
    WebView description;
ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_view);
        String companyId = getIntent().getExtras().getString("company");
        companyTitle= (TextView) findViewById(R.id.companyTitle);
        year= (TextView) findViewById(R.id.companyFounded);
        emp= (TextView) findViewById( R.id.companyEmp);
        logo= (ImageView) findViewById(R.id.companyLogo);
        ratingBar= (RatingBar) findViewById(R.id.ratingBar);
        description= (WebView) findViewById(R.id.description);
        if (companyId == null) {
            throw new IllegalArgumentException("Must pass extra " + "company");
        }
        mFirestore=FirebaseFirestore.getInstance();
        companyRef = mFirestore.collection("companies").document(companyId);
    }

    @Override
    protected void onStart() {
        super.onStart();

        companyRegistration = companyRef.addSnapshotListener(this);
    }
    @Override
    protected void onStop() {
        super.onStop();

        if (companyRegistration != null) {
            companyRegistration.remove();
            companyRegistration = null;
        }
    }

    @Override
    public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
        onTipLoaded(documentSnapshot);
    }

    private void onTipLoaded(DocumentSnapshot documentSnapshot) {
        //   Log.d("test",documentSnapshot.getString("question"));
        companyTitle.setText(documentSnapshot.getString("title"));
        year.setText("Founded in "+documentSnapshot.getString("year"));
 emp.setText("No of Employees: "+documentSnapshot.getDouble("NoOfEmp").intValue());
        ratingBar.setRating(documentSnapshot.getDouble("rating").floatValue());
        description.loadData(documentSnapshot.getString("description"),"text/html", "UTF-8");
        Glide.with(this).load(documentSnapshot.getString("url")).into(logo);

    }
}
