package com.learning.medscan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

public class MainScreen extends AppCompatActivity {

    private RadioButton Rb_Medscan;
    private RadioButton Rb_Chats;
    private RadioButton Rb_Profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        Rb_Medscan=findViewById(R.id.rb_medscan);
        Rb_Chats=findViewById(R.id.rb_chats);
        Rb_Profile=findViewById(R.id.rb_profile);
        Rb_Medscan.setChecked(true);
        setup();
        Rb_Medscan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setup();
            }
        });
        Rb_Chats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setup();
            }
        });
        Rb_Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setup();
            }
        });

    }

    private void setup() {
        if(Rb_Medscan.isChecked()){
            Fragment medscan_frag= new frag_main();
            FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.ll_frag,medscan_frag);
            transaction.commit();
        }
        else if(Rb_Chats.isChecked()){
            Fragment chat_frag= new frag_chat();
            FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.ll_frag,chat_frag);
            transaction.commit();
        }else {
            Fragment profile_frag= new frag_profile();
            FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.ll_frag,profile_frag);
            transaction.commit();
        }
    }
}