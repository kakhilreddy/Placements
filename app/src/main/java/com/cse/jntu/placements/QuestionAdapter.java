package com.cse.jntu.placements;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cse.jntu.placements.models.Question;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * RecyclerView adapter for a list of Restaurants.
 */
public class QuestionAdapter extends FireStoreAdapter<QuestionAdapter.ViewHolder> {

    public interface OnQuestionSelectedListener {

        void onQuestionSelected(DocumentSnapshot question);

    }

    private OnQuestionSelectedListener mListener;

    public QuestionAdapter(Query query, OnQuestionSelectedListener listener) {
        super(query);
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(inflater.inflate(R.layout.question, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(getSnapshot(position), mListener);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {


      TextView question;

        TextView username;

        TextView views;

        public ViewHolder(View itemView) {
            super(itemView);
            question= (TextView) itemView.findViewById(R.id.question);
            username= (TextView) itemView.findViewById(R.id.username);
            views= (TextView) itemView.findViewById(R.id.views);
        }

        public void bind(final DocumentSnapshot snapshot,
                         final OnQuestionSelectedListener listener) {

            Question question;
            String question1=snapshot.getString("question");
            String username=snapshot.getString("username");
            long views=new Double(Double.valueOf(snapshot.getDouble("views"))).longValue();
            String id=snapshot.getId();

            question=new Question(id,question1,username,views);
            this.question.setText(question.getQuestion());
            this.username.setText("By "+question.getUsername());
            this.views.setText("Views: "+question.getViews());




            // Click listener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onQuestionSelected(snapshot);
                    }
                }
            });
        }

    }
}