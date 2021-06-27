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

import de.hdodenhof.circleimageview.CircleImageView;

public class frag_main extends Fragment{

    TextView brainText,covidText,pneumoniaText,tumourText,malariaText;
    TextView nameText;
    DatabaseReference mRef;
    FirebaseAuth mauth;
    CircleImageView pp;

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
        View layout=inflater.inflate(R.layout.fragment_frag_main, container, false);
        brainText = layout.findViewById(R.id.brainText);
        covidText = layout.findViewById(R.id.covidText);
        pneumoniaText = layout.findViewById(R.id.pneumoniaText);
        tumourText = layout.findViewById(R.id.tumourText);
        malariaText = layout.findViewById(R.id.malariaText);
        nameText = layout.findViewById(R.id.introName);
        pp = layout.findViewById(R.id.ppp);
        settingprofile();

        brainText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCk("BrainTumour");
            }
        });
        covidText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCk("Covid");
            }
        });
        pneumoniaText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCk("Pneumonia");
            }
        });
        tumourText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCk("TumourClassification");
            }
        });
        malariaText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCk("Malaria");
            }
        });
        
        return layout;
    }

    private void settingprofile() {
        mRef = FirebaseDatabase.getInstance().getReference();
        mauth = FirebaseAuth.getInstance();
        mRef.child("users").child(mauth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                nameText.setText("Welcome Back, "+snapshot.child("Name").getValue(String.class)+"!");
                String u=snapshot.child("Uri").getValue(String.class);
                if(!snapshot.child("Uri").getValue(String.class).equals("default")){
                    pp.setImageURI(Uri.parse(u));
                }
                else{
                    if (snapshot.child("Position").getValue(String.class).equals("Doctor")){
                        pp.setImageResource(R.drawable.doc);
                    }
                    else{
                        pp.setImageResource(R.drawable.profile_image);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void onCk(String text) {
        Intent t=new Intent(getContext(),Scan_act.class);
        t.putExtra("ScanType",text);
        startActivity(t);
    }
}