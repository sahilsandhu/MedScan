package com.learning.medscan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ThrowOnExtraProperties;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class
  Register extends AppCompatActivity {

    ImageView v;

    private DatabaseReference mRef;
    private FirebaseAuth mAuth;

    private EditText tvdoc_name;
    private EditText tvdoc_age;
    private EditText tvdoc_exp;

    private EditText tvp_name;
    private EditText tvp_age;

    private TextView title;

    private String[] item;
    private String spz;
    private Integer mode;
    private Switch s_mode;
    private Button B_login;
    private CheckBox tnc_Check;
    private Spinner spin;

    private LinearLayout ll_doc;
    private LinearLayout ll_pat;
    ArrayAdapter<String> adapter;
    private String c_uid;

    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        FirebaseApp.initializeApp(Register.this);

        mRef= FirebaseDatabase.getInstance().getReference();
        mAuth= FirebaseAuth.getInstance();
        spin=findViewById(R.id.spinner_specialization);
        v=findViewById(R.id.imageView4);
        tvdoc_name=(EditText) findViewById(R.id.doc_username_register);
        tvdoc_age=(EditText) findViewById(R.id.doc_age_register);
        tvdoc_exp=(EditText) findViewById(R.id.doc_exp_register);
        tvp_name=(EditText) findViewById(R.id.P_username_regis);
        tvp_age=(EditText) findViewById(R.id.P_age_register);
        v=findViewById(R.id.imageView4);
        tnc_Check=(CheckBox) findViewById(R.id.check_TC);
        c_uid=getIntent().getStringExtra("uid");
        pd=new ProgressDialog(this);
        ll_doc=findViewById(R.id.ll_doctor);
        ll_pat=findViewById(R.id.ll_patient);
        title=findViewById(R.id.tv_title_register);
        s_mode=findViewById(R.id.switch_register);
        item= new String[]{"Allergist","Anesthesiologists","Cardiologist","Dermatologist","Gastroenterologists","Neurologist","Gynecologists","Oncologist","Pathologist","Pediatricians","Physiatrists","Others"};
        setupspinner();
        s_mode.setChecked(true);
        Switch_use();
        s_mode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Switch_use();
            }
        });
        B_login=findViewById(R.id.bt_next_registration);
        B_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mode==0){
                    register_doctor();
                }
                else{
                    register_patient();
                }
            }
        });
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spz=item[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                spz="Not specified";
            }
        });

    }

    private void upload() {
            Intent p=new Intent();
            p.setType("image/*");
            p.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(p,1); }




    private void setupspinner() {
        adapter=new ArrayAdapter<>(Register.this,R.layout.support_simple_spinner_dropdown_item,item);
        spin.setAdapter(adapter);
    }

    private void register_patient() {
        String Name=tvp_name.getText().toString();
        String Age=tvp_age.getText().toString();
        String Uri = "default";
        if(Name.isEmpty() || Age.isEmpty()){
            Toast.makeText(Register.this,"Enter all details",Toast.LENGTH_SHORT).show();
        }else if(!tnc_Check.isChecked()){
            Toast.makeText(Register.this,"Please agree to the term and conditions to continue.", Toast.LENGTH_SHORT).show();
        }else{
            pd.setMessage("Please wait");
            pd.show();

            HashMap<String,Object> map=new HashMap<>();
            map.put("Name",Name);
            map.put("Age",Age);
            map.put("Position","Patient");
            map.put("Uri",Uri);

            mRef.child("users").child(c_uid).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        pd.dismiss();
                        startActivity(new Intent(Register.this,login.class));
                        finish();
                    }
                    else{
                        pd.dismiss();
                        Toast.makeText(Register.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }


            });
        }
    }

    private void register_doctor() {
        String Name=tvdoc_name.getText().toString();
        String Age=tvdoc_age.getText().toString();
        String exp=tvdoc_exp.getText().toString();
        String Uri = "default";
        if(Name.isEmpty() || Age.isEmpty() || exp.isEmpty()){
            Toast.makeText(Register.this,"Enter all details",Toast.LENGTH_SHORT).show();
        }else if(!tnc_Check.isChecked()){
            Toast.makeText(Register.this,"Please agree to the term and conditions to continue.", Toast.LENGTH_SHORT).show();
        }else{
            pd.setMessage("Please wait");
            pd.show();
            HashMap<String,Object> map=new HashMap<>();

            map.put("Name",Name);
            map.put("Age",Age);
            map.put("experience",exp);
            map.put("Specialization",spz);
            map.put("Position","Doctor");
            map.put("Uri",Uri);
            mRef.child("users").child(c_uid).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        pd.dismiss();
                        startActivity(new Intent(Register.this,login.class));
                        finish();
                    }
                    else{
                        pd.dismiss();
                        Toast.makeText(Register.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }

    private void Switch_use() {

        if (s_mode.isChecked()){
            ll_pat.setVisibility(View.VISIBLE);
            ll_doc.setVisibility(View.GONE);
            title=findViewById(R.id.tv_title_register);
            title.setText("Register as Patient");
            v.setImageResource(R.drawable.pat);
            mode=1;
        }
        else{
            ll_pat.setVisibility(View.GONE);
            ll_doc.setVisibility(View.VISIBLE);
            title.setText("Register as Doctor");
            v.setImageResource(R.drawable.doc);
            mode=0;
        }
    }





}