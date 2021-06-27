package com.learning.medscan;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hendraanggrian.appcompat.widget.SocialAutoCompleteTextView;

import java.util.ArrayList;
import java.util.List;

public class frag_chat extends Fragment {

    private RecyclerView rv_for_chat;
    private List<User_doctor> muser;
    private List<User_doctor> usersp;
    private AutoCompleteTextView searchbar;
    private Adapter_rv_chat adapterRvChat;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_frag_chat, container, false);
        searchbar=view.findViewById(R.id.search_bar);
        rv_for_chat=view.findViewById(R.id.rv_chat);
        rv_for_chat.setHasFixedSize(true);
        rv_for_chat.setLayoutManager(new LinearLayoutManager(getContext()));

        muser=new ArrayList<>();
        usersp=new ArrayList<>();

        adapterRvChat=new Adapter_rv_chat(getContext(),muser,true);

        rv_for_chat.setAdapter(adapterRvChat);
        ReadUsers();
        searchbar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(TextUtils.isEmpty(searchbar.getText().toString())){
                   ReadUsers();
                }
                else {
                    Searchuser(s.toString());
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }

    private void ReadUsers() {

        DatabaseReference dbrefer= FirebaseDatabase.getInstance().getReference().child("users");
        dbrefer.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(TextUtils.isEmpty(searchbar.getText().toString())){
                    muser.clear();
                    for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                        //if((dataSnapshot.child("Position").getValue().toString()).equals("Doctor")){

                            muser.add(dataSnapshot.getValue(User_doctor.class));
                        //}
                    }
                    adapterRvChat.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void Searchuser(String s){
        muser.clear();
        usersp.clear();
        Query query=FirebaseDatabase.getInstance().getReference().child("users").orderByChild("Name").startAt(s).endAt(s+"\uf8ff");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    if((dataSnapshot.child("Position").getValue().toString()).equals("Doctor")){
                        muser.add(dataSnapshot.getValue(User_doctor.class));
                    }

                }
                adapterRvChat.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Query query1=FirebaseDatabase.getInstance().getReference().child("users").orderByChild("Specialization").startAt(s).endAt(s+"\uf8ff");
        query1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    if((dataSnapshot.child("Position").getValue().toString()).equals("Doctor")){
                        usersp.add(dataSnapshot.getValue(User_doctor.class));
                    }
                    muser.addAll(usersp);
                }
                adapterRvChat.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}