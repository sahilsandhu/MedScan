package com.learning.medscan;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Adapter_rv_chat extends RecyclerView.Adapter<Adapter_rv_chat.viewHolder> {

    private Context mcontext;
    private List<User_doctor> muser;
    private boolean frag;

    private FirebaseUser firebaseUser;

    public Adapter_rv_chat(Context mcontext, List<User_doctor> muser, boolean frag) {
        this.mcontext = mcontext;
        this.muser = muser;
        this.frag = frag;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(mcontext).inflate(R.layout.chat_item,parent,false);
        return new Adapter_rv_chat.viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        User_doctor userDoctor=muser.get(position);

        holder.tv_name.setText(userDoctor.Name);
        holder.sp_tv.setText(userDoctor.Specialization);
        Picasso.get().load(userDoctor.Uri).placeholder(R.drawable.doc).into(holder.image);
        holder.bt_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("doctor chat",userDoctor.Id+".."+userDoctor.Name);
                Intent intent=new Intent(mcontext,ChatActivity.class);
                intent.putExtra("Name",userDoctor.Name);
                intent.putExtra("cuid",userDoctor.Id);
                mcontext.startActivity(intent);
            }
        });
    }

    private void onClicked() {
    }

    @Override
    public int getItemCount() {
        return muser.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{

        public TextView tv_name;
        public Button bt_chat;
        public TextView sp_tv;
        public CircleImageView image;
        public viewHolder(@NonNull View itemView) {
            super(itemView);

            tv_name=itemView.findViewById(R.id.user_name_chat);
            bt_chat=itemView.findViewById(R.id.chat);
            sp_tv=itemView.findViewById(R.id.sp_tv);
            image = itemView.findViewById(R.id.prof_img_chat);
        }
    }
}
