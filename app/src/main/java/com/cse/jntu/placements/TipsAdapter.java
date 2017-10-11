package com.cse.jntu.placements;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cse.jntu.placements.models.Question;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

/**
 * Created by AKHIL on 10-10-2017.
 */

public class TipsAdapter extends FireStoreAdapter<TipsAdapter.ViewHolder> {
    public interface OnTipSelectedListener {

        void onTipelected(DocumentSnapshot question);

    }

    private TipsAdapter.OnTipSelectedListener mListener;

    public TipsAdapter(Query query, TipsAdapter.OnTipSelectedListener listener) {
        super(query);
        mListener = listener;
    }

    @Override
    public TipsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new TipsAdapter.ViewHolder(inflater.inflate(R.layout.tip, parent, false));
    }

    @Override
    public void onBindViewHolder(TipsAdapter.ViewHolder holder, int position) {
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
                         final TipsAdapter.OnTipSelectedListener listener) {


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
                        listener.onTipelected(snapshot);
                    }
                }
            });
        }

    }
}
