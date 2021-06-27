package com.learning.medscan;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class frag_profile extends Fragment {

    private DatabaseReference mRef;
    private FirebaseAuth mauth;
    private String Name;
    private String Age;
    private  String position;

    private Button logout;

    private ImageView p;

    private TextView tv_Name;
    private TextView tv_age;
    private TextView tv_pos;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRef = FirebaseDatabase.getInstance().getReference();
        mauth = FirebaseAuth.getInstance();
        View layout=inflater.inflate(R.layout.fragment_frag_profile, container, false);
        tv_Name=(TextView) layout.findViewById(R.id.tv_prof_name_age);
        tv_age = (TextView) layout.findViewById(R.id.tv_prof_age);
        tv_pos=(TextView) layout.findViewById(R.id.tv_prof_position);
        p=layout.findViewById(R.id.ppp);
        logout=layout.findViewById(R.id.bt_logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout_func();
            }
        });
        settingprofile();
        return layout;
    }

    private void logout_func() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getContext(),MainActivity.class));
    }

    private void settingprofile() {
        mRef = FirebaseDatabase.getInstance().getReference();
        mauth = FirebaseAuth.getInstance();
        mRef.child("users").child(mauth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tv_Name.setText(snapshot.child("Name").getValue(String.class));
                tv_pos.setText(snapshot.child("Position").getValue(String.class));
                tv_age.setText(snapshot.child("Age").getValue(String.class));
                String u=snapshot.child("Uri").getValue(String.class);
                if(!snapshot.child("Uri").getValue(String.class).equals("default")){
                    p.setImageURI(Uri.parse(u));
                }
                else{
                    if (snapshot.child("Position").getValue(String.class).equals("Doctor")){
                        p.setImageResource(R.drawable.doc);
                    }
                    else{
                        p.setImageResource(R.drawable.profile_image);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}