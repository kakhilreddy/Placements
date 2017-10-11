package com.cse.jntu.placements;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cse.jntu.placements.models.InterviewExperience;
import com.cse.jntu.placements.models.Question;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

/**
 * Created by AKHIL on 09-10-2017.
 */

public class InterviewAdapter extends FireStoreAdapter<InterviewAdapter.ViewHolder> {

    public interface OnInterviewSelectedListener {

        void onInterviewSelected(DocumentSnapshot interview);

    }

    private InterviewAdapter.OnInterviewSelectedListener mListener;

    public InterviewAdapter(Query query, OnInterviewSelectedListener listener) {
        super(query);
        mListener = listener;
    }

    @Override
    public InterviewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new InterviewAdapter.ViewHolder(inflater.inflate(R.layout.ie, parent, false));
    }

    @Override
    public void onBindViewHolder(InterviewAdapter.ViewHolder holder, int position) {
        holder.bind(getSnapshot(position), mListener);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView title,companyName,username,views,isHired;

        public ViewHolder(View itemView) {
            super(itemView);
            title= (TextView) itemView.findViewById(R.id.title);
            companyName= (TextView) itemView.findViewById(R.id.companyName);
            username= (TextView) itemView.findViewById(R.id.username);
            views= (TextView) itemView.findViewById(R.id.views);
            isHired= (TextView) itemView.findViewById(R.id.isHired);
        }

        public void bind(final DocumentSnapshot snapshot,
                         final OnInterviewSelectedListener listener) {


            InterviewExperience experience;
            String companyName=snapshot.getString("companyName");
            boolean isHired=Boolean.valueOf(snapshot.getBoolean("isHired"));
            String username=snapshot.getString("username");
            long views=new Double(Double.valueOf(snapshot.getDouble("views"))).longValue();

            String id=snapshot.getId();


            String title=snapshot.getString("title");
            experience=new InterviewExperience(id,title,companyName,username,isHired,views,null,null);


            this.title.setText(experience.getTitle());
            this.companyName.setText("Company Name: "+experience.getCompanyName());
            this.isHired.setText("Hired: "+experience.isHired());
            this.views.setText("Views: "+experience.getViews()+"");
            this.username.setText("Created By: "+experience.getUsername());



            // Click listener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onInterviewSelected(snapshot);
                    }
                }
            });
        }

    }
}
