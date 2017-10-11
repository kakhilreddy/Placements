package com.cse.jntu.placements;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

/**
 * Created by AKHIL on 11-10-2017.
 */

public class PrepareAdapter extends FireStoreAdapter<PrepareAdapter.ViewHolder> {

    public interface OnPrepareSelectedListener {

        void onPrepareselected(DocumentSnapshot prepare);

    }

    private PrepareAdapter.OnPrepareSelectedListener mListener;

    public PrepareAdapter(Query query, PrepareAdapter.OnPrepareSelectedListener listener) {
        super(query);
        mListener = listener;
    }

    @Override
    public PrepareAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new PrepareAdapter.ViewHolder(inflater.inflate(R.layout.tip, parent, false));
    }

    @Override
    public void onBindViewHolder(PrepareAdapter.ViewHolder holder, int position) {
        holder.bind(getSnapshot(position), mListener);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {


        TextView tip;



        TextView views;

        public ViewHolder(View itemView) {
            super(itemView);
            tip= (TextView) itemView.findViewById(R.id.tipTitle);

            views= (TextView) itemView.findViewById(R.id.views);
        }

        public void bind(final DocumentSnapshot snapshot,
                         final OnPrepareSelectedListener listener) {


            String title=snapshot.getString("title");

            long views=new Double(Double.valueOf(snapshot.getDouble("views"))).longValue();
            String id=snapshot.getId();


            this.tip.setText(title);

            this.views.setText("Views: "+views);




            // Click listener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onPrepareselected(snapshot);
                    }
                }
            });
        }

    }
}
