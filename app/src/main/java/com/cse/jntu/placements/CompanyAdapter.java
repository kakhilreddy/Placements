package com.cse.jntu.placements;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cse.jntu.placements.models.Question;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

/**
 * Created by AKHIL on 10-10-2017.
 */

public class CompanyAdapter extends FireStoreAdapter<CompanyAdapter.ViewHolder> {

    public interface OnCompanySelectedListener {

        void onCompanySelected(DocumentSnapshot company);

    }

    private OnCompanySelectedListener mListener;

    public CompanyAdapter(Query query,OnCompanySelectedListener listener) {
        super(query);
        mListener = listener;
    }

    @Override
    public CompanyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new CompanyAdapter.ViewHolder(inflater.inflate(R.layout.company, parent, false));
    }

    @Override
    public void onBindViewHolder(CompanyAdapter.ViewHolder holder, int position) {
        holder.bind(getSnapshot(position), mListener);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {


        TextView companyName;

        TextView year;
ImageView imageView;
        TextView emp;
RatingBar ratingBar;
        public ViewHolder(View itemView) {
            super(itemView);
            companyName= (TextView) itemView.findViewById(R.id.companyTitle);
            year= (TextView) itemView.findViewById(R.id.companyFounded);
            emp= (TextView) itemView.findViewById(R.id.companyEmp);
            ratingBar= (RatingBar) itemView.findViewById(R.id.ratingBar);
            imageView= (ImageView) itemView.findViewById(R.id.companyLogo);
        }

        public void bind(final DocumentSnapshot snapshot,
                         final OnCompanySelectedListener listener) {


            String title=snapshot.getString("title");
            String year=snapshot.getString("year");
            long no=new Double(Double.valueOf(snapshot.getDouble("NoOfEmp"))).longValue();
            String id=snapshot.getId();
         Double temp=snapshot.getDouble("rating");
            float rate=temp.floatValue();
ratingBar.setRating(rate);
            this.companyName.setText(title);
            this.year.setText("Founded in "+year);
            this.emp.setText("Employess: "+no);
            Glide.with(itemView).load(snapshot.getString("url")).into(imageView);

            // Click listener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onCompanySelected(snapshot);
                    }
                }
            });
        }

    }

}
