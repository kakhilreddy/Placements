package com.cse.jntu.placements;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cse.jntu.placements.models.Comment;
import com.cse.jntu.placements.models.Question;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by AKHIL on 09-10-2017.
 */

public class CommentAdapter extends FireStoreAdapter<CommentAdapter.ViewHolder> {





    public CommentAdapter(Query query) {
        super(query);

    }

    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new CommentAdapter.ViewHolder(inflater.inflate(R.layout.comment, parent, false));
    }

    @Override
    public void onBindViewHolder(CommentAdapter.ViewHolder holder, int position) {
        holder.bind(getSnapshot(position));
    }

    static class ViewHolder extends RecyclerView.ViewHolder {


        TextView comment;

        TextView username;

        TextView time;

        public ViewHolder(View itemView) {
            super(itemView);
            comment= (TextView) itemView.findViewById(R.id.comment);
            username= (TextView) itemView.findViewById(R.id.username);
            time= (TextView) itemView.findViewById(R.id.time);
        }

        public void bind(final DocumentSnapshot snapshot
                         ) {

            Comment comment;
            String com=snapshot.getString("comment");
            String username=snapshot.getString("username");
           // long views=new Double(Double.valueOf(snapshot.getDouble("views"))).longValue();
            String id=snapshot.getId();
Date date=snapshot.getDate("time");
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            //get current date time with Date()

           // System.out.println(dateFormat.format(date));

            comment=new Comment(id,com,username,null);
            this.comment.setText(comment.getComment());
            this.username.setText("By "+comment.getUsername());
            String str = String.format("Time: "+dateFormat.format(date) );
            this.time.setText(str);





        }

    }
}
