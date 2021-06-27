package com.learning.medscan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    Button bt_send;
    EditText messge_n;
    MessageMember messageMember;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference rootref1,rootref2;
    String sender_d;
    String message;

    MessageAdapter messageAdapter;
    List<MessageMember> memberList;

    public String name;
    public String cud;
    private TextView tv_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        name=getIntent().getStringExtra("Name");
        cud=getIntent().getStringExtra("cuid");

        sender_d=FirebaseAuth.getInstance().getCurrentUser().getUid();

        tv_name=findViewById(R.id.tv_chat_act);
        tv_name.setText(name);

        messge_n=findViewById(R.id.et_message);


        messageMember=new MessageMember();

        recyclerView=findViewById(R.id.rv_message);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(ChatActivity.this));

        bt_send=findViewById(R.id.send_bt);
        bt_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!TextUtils.isEmpty(messge_n.getText().toString())){
                    sendMessage();
                }


            }
        });

        readmessage();
                
    }

    private void readmessage(){
        memberList=new ArrayList<>();
        rootref1=FirebaseDatabase.getInstance().getReference("Chat");
        rootref1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                memberList.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    MessageMember member=dataSnapshot.getValue(MessageMember.class);
                    if(member.getReciever().equals(cud) && member.getSender().equals(sender_d) || member.getReciever().equals(sender_d) && member.getSender().equals(cud)){
                        memberList.add(member);
                    }

                    messageAdapter= new MessageAdapter(ChatActivity.this,memberList);
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void sendMessage() {
        message=messge_n.getText().toString();
        rootref1=FirebaseDatabase.getInstance().getReference();
        messageMember.setSender(sender_d);
        messageMember.setReciever(cud);
        messageMember.setMessage(message);
        rootref1.child("Chat").push().setValue(messageMember);
        messge_n.setText("");


    }

 }
