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
import android.widget.ImageView;
import android.widget.TextView;

public class Prepare extends AppCompatActivity {
    DashBoard.MyAdatper adapter;
    Toolbar toolbar;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prepare_for_company);
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.getSupportActionBar().setTitle("Prepare");
        recyclerView= (RecyclerView) findViewById(R.id.menulist);
        recyclerView.setAdapter(new MyAdatper());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this , new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
if(position==0)
{

Intent intent=new Intent(Prepare.this,PrepareView.class);
    intent.putExtra("prepare",position);
    startActivity(intent);
}
            }
        }));
    }

    class MyAdatper extends RecyclerView.Adapter<Prepare.MenuHolder>
    {

        @Override
        public Prepare.MenuHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v= LayoutInflater.from(getBaseContext()).inflate(R.layout.menu,parent,false);

            return  new Prepare.MenuHolder(v);
        }

        @Override
        public void onBindViewHolder(Prepare.MenuHolder holder, int position) {

            holder.menuname.setText(Constants.subjects[position]);
            holder.icon.setVisibility(View.GONE);


        }

        @Override
        public int getItemCount() {
            return Constants.subjects.length;
        }
    }
    private class MenuHolder extends RecyclerView.ViewHolder {

        TextView menuname;
        ImageView icon;
        public MenuHolder(View itemView) {
            super(itemView);
            Log.d("list","in PH");
            menuname= (TextView) itemView.findViewById(R.id.menuname);
            icon= (ImageView) itemView.findViewById(R.id.menuicon);
        }
    }
}
