package com.learning.medscan;

import android.app.Application;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private Context mContext;
    private List<MessageMember> mmember;
    private int MSG_r=1;
    private int MSG_l=0;

    private FirebaseUser user;

    public MessageAdapter(Context mContext, List<MessageMember> mmember) {
        this.mContext = mContext;
        this.mmember = mmember;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(viewType==MSG_r){
            View view=LayoutInflater.from(mContext).inflate(R.layout.send_message,parent,false);
            return new ViewHolder(view);
        }
        else{
            View view=LayoutInflater.from(mContext).inflate(R.layout.rec_message,parent,false);
            return new ViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        MessageMember messageMember=mmember.get(position);
        holder.show_m.setText(messageMember.getMessage());
    }

    @Override
    public int getItemCount() {
        return mmember.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView show_m;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            show_m=itemView.findViewById(R.id.show_message);
        }
    }

    @Override
    public int getItemViewType(int position) {
        user=FirebaseAuth.getInstance().getCurrentUser();
        if (mmember.get(position).getSender().equals(user.getUid())){
            return MSG_r;
        }else{
            return MSG_l;
        }
    }
}
