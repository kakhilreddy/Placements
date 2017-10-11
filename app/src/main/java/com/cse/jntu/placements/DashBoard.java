package com.cse.jntu.placements;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.cse.jntu.placements.models.*;

import java.util.ArrayList;
import java.util.List;

import viethoa.com.snackbar.BottomSnackBarMessage;

public class DashBoard extends AppCompatActivity implements View.OnClickListener {
RecyclerView recyclerView;
    List<menu> list;
    MyAdatper adapter;
    Toolbar toolbar;
    TextView uname,uemail;
    ImageView uprofile;

    LinearLayout linearLayout;
FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.getSupportActionBar().setTitle("Placement Portal");
recyclerView= (RecyclerView) findViewById(R.id.menulist);
list=new ArrayList<>();
auth=FirebaseAuth.getInstance();

        uname= (TextView) findViewById(R.id.uname);
        uemail= (TextView) findViewById(R.id.uemail);
        SharedPreferences preferences=getSharedPreferences("profile",MODE_PRIVATE);
final FirebaseUser user=auth.getCurrentUser();
      if(getSharedPreferences("resend",MODE_PRIVATE).getBoolean("flag",false))
      {
          user.reload().addOnSuccessListener(new OnSuccessListener<Void>() {
              @Override
              public void onSuccess(Void aVoid) {

                  if(!user.isEmailVerified())
                  {
                      linearLayout= (LinearLayout) findViewById(R.id.verify);
                      linearLayout.setVisibility(View.VISIBLE);
                  }
              }
          });
      }
else        if(!user.isEmailVerified())
        {

             linearLayout= (LinearLayout) findViewById(R.id.verify);
            linearLayout.setVisibility(View.VISIBLE);
        }
       uname.setText(preferences.getString("name",null));
        uemail.setText(preferences.getString("email",null));

ImageView profilePic= (ImageView) findViewById(R.id.uprofile);
        if(user.getPhotoUrl()!=null)
        Glide.with(this).load(user.getPhotoUrl()).into(profilePic);






      adapter=new MyAdatper();

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
  if(position==0)
  {
startActivity(new Intent(DashBoard.this,InterviewExperiences.class));
  }else if(position==1)
  {
      startActivity(new Intent(DashBoard.this,tips.class));

  }else if(position==2)
  {  startActivity(new Intent(DashBoard.this,QA.class));

  }else if(position==3)
  {
      startActivity(new Intent(DashBoard.this,Prepare.class));
  }else if(position==4)
  {
      startActivity(new Intent(DashBoard.this,Company.class));
  }
  else if(position==5)
  {
      startActivity(new Intent(DashBoard.this,About.class));
  }
else
{
    FirebaseAuth mAuth=FirebaseAuth.getInstance();
    mAuth.signOut();
    startActivity(new Intent(DashBoard.this,MainActivity.class));
    finish();
}
                    }
                })
        );
    }

    @Override
    public void onClick(View view) {

        int id=view.getId();
        switch ((id))
        {
            case R.id.resend:
SharedPreferences preferences=getSharedPreferences("resend",MODE_PRIVATE);
                SharedPreferences.Editor editor=preferences.edit();
                editor.putBoolean("flag",true);
                editor.commit();
                auth.getCurrentUser().sendEmailVerification()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    BottomSnackBarMessage bottomSnackBarMessage = new BottomSnackBarMessage(DashBoard.this);
                                    bottomSnackBarMessage.showSuccessMessage("Verification email sent");
                                    Log.d("email", "Email sent.");

                                   linearLayout.setVisibility(View.GONE);
                                }
                            }
                        });
                };


    }

    class MyAdatper extends RecyclerView.Adapter<MenuHolder>
    {

        @Override
        public MenuHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v=LayoutInflater.from(getBaseContext()).inflate(R.layout.menu,parent,false);

            return  new MenuHolder(v);
        }

        @Override
        public void onBindViewHolder(MenuHolder holder, int position) {

            holder.menuname.setText(Constants.menu[position]);
            holder.icon.setImageResource(Constants.menuIds[position]);


        }

        @Override
        public int getItemCount() {
            return 7;
        }
    }
    private class MenuHolder extends RecyclerView.ViewHolder {

        TextView menuname;
ImageView icon;
        public MenuHolder(View itemView) {
            super(itemView);
Log.d("list","in MH");
            menuname= (TextView) itemView.findViewById(R.id.menuname);
            icon= (ImageView) itemView.findViewById(R.id.menuicon);
        }
    }
}
