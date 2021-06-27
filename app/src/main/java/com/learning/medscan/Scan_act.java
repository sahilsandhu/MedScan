
package com.learning.medscan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.hendraanggrian.appcompat.socialview.Hashtag;
import com.hendraanggrian.appcompat.widget.HashtagArrayAdapter;
import com.hendraanggrian.appcompat.widget.SocialAutoCompleteTextView;
import com.learning.medscan.ml.BactVirusModel;
import com.learning.medscan.ml.BrainTumourClassificationModel;
import com.learning.medscan.ml.CovidModel;
import com.learning.medscan.ml.MalariaModel;
import com.learning.medscan.ml.TumourModel;
import com.theartofdev.edmodo.cropper.CropImage;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.common.ops.NormalizeOp;
import org.tensorflow.lite.support.image.ImageProcessor;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.image.ops.ResizeWithCropOrPadOp;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.List;


public class Scan_act extends AppCompatActivity {

    ImageView imgView,pic1,pic2;
    Bitmap img;
    ImageView close;
    ImageView imageadded;
    TextView post;
    Uri imageUri;
    String imageUrl;
    TextView text1;
    TextView text2;
    SocialAutoCompleteTextView description;
    TextView output;
    Button predict;
    private String sender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_act);

        pic1 = findViewById(R.id.img1);
        pic2 = findViewById(R.id.img2);
        output = findViewById(R.id.output);
        close = findViewById(R.id.close);
        imageadded = findViewById(R.id.image_added);
        imgView = findViewById(R.id.image_added);
        post = findViewById(R.id.submit);
        text1 = findViewById(R.id.text);
        text2 = findViewById(R.id.text2);
        predict = findViewById(R.id.predict);
        //description = findViewById(R.id.description);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Scan_act.this,MainActivity2.class));
                finish();
            }
        });

        predict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(sender.equals("Malaria"))
                {
                    img = Bitmap.createScaledBitmap(img,128,128,true);
                    try {
                        MalariaModel model = MalariaModel.newInstance(getApplicationContext());

                        // Creates inputs for reference.
                        TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 128, 128, 3}, DataType.FLOAT32);
                        TensorImage tensorImage = new TensorImage(DataType.FLOAT32);
                        tensorImage.load(img);
                        ByteBuffer byteBuffer = tensorImage.getBuffer();
                        inputFeature0.loadBuffer(byteBuffer);

                        // Runs model inference and gets result.
                        MalariaModel.Outputs outputs = model.process(inputFeature0);
                        TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

                        // Releases model resources if no longer used.
                        model.close();
                        text1.setText("Malaria Prediction");
                        text2.setText("Accuracy of model is : 99%");
                        pic1.setImageResource(R.drawable.normal1);
                        pic2.setImageResource(R.drawable.normal2);
                        if(outputFeature0.getFloatArray()[0] > outputFeature0.getFloatArray()[1])
                              output.setText("Malaria Infected");
                        else
                            output.setText("Malaria Non-Infected");
                    } catch (IOException e) {
                        // TODO Handle the exception
                    }
                }
                else if(sender.equals("BrainTumour")){
                    //img = Bitmap.createScaledBitmap(img,224,224,true);

                    ImageProcessor imageProcessor = new ImageProcessor.Builder()
                            .add(new ResizeWithCropOrPadOp(224,224))
                            .add(new NormalizeOp(0,255))
                            .build();
                    try {
                        TumourModel model = TumourModel.newInstance(getApplicationContext());

                        // Creates inputs for reference.
                        TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);
                        TensorImage tensorImage = new TensorImage(DataType.FLOAT32);
                        tensorImage.load(img);
                        tensorImage = imageProcessor.process(tensorImage);

                        ByteBuffer byteBuffer = tensorImage.getBuffer();
                        inputFeature0.loadBuffer(byteBuffer);

                        // Runs model inference and gets result.
                        TumourModel.Outputs outputs = model.process(inputFeature0);
                        TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

                        // Releases model resources if no longer used.
                        model.close();
                        text1.setText("Brain Tumour Prediction");
                        text2.setText("Accuracy of model is : 95%");
                        pic1.setImageResource(R.drawable.braintummor);
                        pic2.setImageResource(R.drawable.braintumor);
                        if(outputFeature0.getFloatArray()[0] > outputFeature0.getFloatArray()[1])
                            output.setText("No Tumour");
                        else
                            output.setText("Tumour");
                        output.setText(outputFeature0.getFloatArray()[0]+"\n"+outputFeature0.getFloatArray()[1]);
                    } catch (IOException e) {
                        // TODO Handle the exception
                    }
                }
                else if(sender.equals("Pneumonia")){
                    ImageProcessor imageProcessor = new ImageProcessor.Builder()
                            .add(new ResizeWithCropOrPadOp(200,200))
                            .add(new NormalizeOp(0,255))
                            .build();
                    try {
                        BactVirusModel model = BactVirusModel.newInstance(getApplicationContext());

                        // Creates inputs for reference.
                        TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 200, 200, 3}, DataType.FLOAT32);
                        TensorImage tensorImage = new TensorImage(DataType.FLOAT32);
                        tensorImage.load(img);
                        tensorImage = imageProcessor.process(tensorImage);
                        ByteBuffer byteBuffer = tensorImage.getBuffer();
                        inputFeature0.loadBuffer(byteBuffer);

                        // Runs model inference and gets result.
                        BactVirusModel.Outputs outputs = model.process(inputFeature0);
                        TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

                        // Releases model resources if no longer used.
                        model.close();
                        text1.setText("Pneumonia Prediction");
                        text2.setText("Accuracy of model is : 89%");
                        pic1.setImageResource(R.drawable.normal23);
                        pic2.setImageResource(R.drawable.bnormal8);
                        if(outputFeature0.getFloatArray()[0] > outputFeature0.getFloatArray()[1] && outputFeature0.getFloatArray()[0] > outputFeature0.getFloatArray()[2])
                            output.setText("Normal");
                        else if(outputFeature0.getFloatArray()[1] > outputFeature0.getFloatArray()[0] && outputFeature0.getFloatArray()[1] > outputFeature0.getFloatArray()[2])
                            output.setText("Bacterial");
                        else
                            output.setText("Viral");
                    } catch (IOException e) {
                        // TODO Handle the exception
                    }


                }
                else if(sender.equals("TumourClassification")){
                    //img = Bitmap.createScaledBitmap(img,150,150,true);
                    ImageProcessor imageProcessor = new ImageProcessor.Builder()
                            .add(new ResizeWithCropOrPadOp(150,150))
                            .add(new NormalizeOp(0,255))
                            .build();
                    try {
                        BrainTumourClassificationModel model = BrainTumourClassificationModel.newInstance(getApplicationContext());

                        // Creates inputs for reference.
                        TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 150, 150, 3}, DataType.FLOAT32);
                        TensorImage tensorImage = new TensorImage(DataType.FLOAT32);
                        tensorImage.load(img);
                        tensorImage = imageProcessor.process(tensorImage);
                        ByteBuffer byteBuffer = tensorImage.getBuffer();
                        inputFeature0.loadBuffer(byteBuffer);

                        // Runs model inference and gets result.
                        BrainTumourClassificationModel.Outputs outputs = model.process(inputFeature0);
                        TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

                        // Releases model resources if no longer used.
                        model.close();
                        text1.setText("Brain Tumour Classification");
                        text2.setText("Accuracy of model is : 98%");
                        pic1.setImageResource(R.drawable.glioma);
                        pic2.setImageResource(R.drawable.pituitary);
                        if(outputFeature0.getFloatArray()[0] > outputFeature0.getFloatArray()[1] && outputFeature0.getFloatArray()[0] > outputFeature0.getFloatArray()[2] && outputFeature0.getFloatArray()[0] > outputFeature0.getFloatArray()[3])
                            output.setText("Glioma Tumour");
                        else if(outputFeature0.getFloatArray()[1] > outputFeature0.getFloatArray()[0] && outputFeature0.getFloatArray()[1] > outputFeature0.getFloatArray()[2] && outputFeature0.getFloatArray()[1] > outputFeature0.getFloatArray()[3])
                            output.setText("No Tumour");
                        else if(outputFeature0.getFloatArray()[2] > outputFeature0.getFloatArray()[0] && outputFeature0.getFloatArray()[2] > outputFeature0.getFloatArray()[1] && outputFeature0.getFloatArray()[2] > outputFeature0.getFloatArray()[3])
                            output.setText("Meningma Tumour");
                        else
                            output.setText("Pitutary Tumour");
                    } catch (IOException e) {
                        // TODO Handle the exception
                    }
                }
                else if(sender.equals("Covid")){
                   //img = Bitmap.createScaledBitmap(img,164,164,true);

                    ImageProcessor imageProcessor = new ImageProcessor.Builder()
                            .add(new ResizeWithCropOrPadOp(164,164))
                            .add(new NormalizeOp(0,255))
                            .build();

                try {
                    CovidModel model = CovidModel.newInstance(getApplicationContext());

                    // Creates inputs for reference.
                    TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 164, 164, 3}, DataType.FLOAT32);
                    TensorImage tensorImage = new TensorImage(DataType.FLOAT32);
                    tensorImage.load(img);
                    tensorImage = imageProcessor.process(tensorImage);
                    ByteBuffer byteBuffer = tensorImage.getBuffer();
                    inputFeature0.loadBuffer(byteBuffer);

                    // Runs model inference and gets result.
                    CovidModel.Outputs outputs = model.process(inputFeature0);
                    TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
                    // Releases model resources if no longer used.
                    model.close();
                    text1.setText("Covid-19 Prediction");
                    text2.setText("Accuracy of model is : 97%");
                    pic1.setImageResource(R.drawable.covid);
                    pic2.setImageResource(R.drawable.covidn);
                    if(outputFeature0.getFloatArray()[0] > outputFeature0.getFloatArray()[1] && outputFeature0.getFloatArray()[0] > outputFeature0.getFloatArray()[2])
                        output.setText("Covid-19");
                    else if(outputFeature0.getFloatArray()[1] > outputFeature0.getFloatArray()[0] && outputFeature0.getFloatArray()[1] > outputFeature0.getFloatArray()[2])
                        output.setText("Normal");
                    else
                        output.setText("Pneumonia");


                } catch (IOException e) {
                    // TODO Handle the exception
                }}

            }
        });
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload();
            }
        });
          CropImage.activity().start(Scan_act.this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sender = this.getIntent().getExtras().getString("ScanType");

    }

    private void upload() {
        ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("uploading");
        pd.show();
        if(imageUri!=null)
        {
            StorageReference filePath = FirebaseStorage.getInstance().getReference("Posts").child(System.currentTimeMillis()+"."+getFileExtension(imageUri));
             StorageTask uploadTask = filePath.putFile(imageUri);
             uploadTask.continueWithTask(new Continuation() {
                 @Override
                 public Object then(@NonNull Task task) throws Exception {
                     if(!task.isSuccessful())
                     {
                         throw task.getException();
                     }
                     return filePath.getDownloadUrl();
                 }
             }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                 @Override
                 public void onComplete(@NonNull Task<Uri> task) {
                     Uri downloadUri = task.getResult();
                     imageUrl = downloadUri.toString();

                     DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");
                     String postId = ref.push().getKey();
                     HashMap<String, Object> map = new HashMap<>();
                     map.put("postId",postId);
                     map.put("imageuri",imageUrl);
                     map.put("publisher",FirebaseAuth.getInstance().getCurrentUser().getUid());

                     ref.child(postId).setValue(map);

                     pd.dismiss();
                     startActivity(new Intent(Scan_act.this,MainActivity2.class));
                     finish();
                 }
             }).addOnFailureListener(new OnFailureListener() {
                 @Override
                 public void onFailure(@NonNull Exception e) {
                     Toast.makeText(Scan_act.this,e.getMessage(),Toast.LENGTH_LONG).show();
                 }
             });
        }
    }
    private String getFileExtension(Uri uri)
    {
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(this.getContentResolver().getType(uri));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK)
    {
        CropImage.ActivityResult result  = CropImage.getActivityResult(data);
        imageUri = result.getUri();
        imageadded.setImageURI(imageUri);

        imgView.setImageURI(imageUri);

        try {
            img = MediaStore.Images.Media.getBitmap(this.getContentResolver(),imageUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    else {
        Toast.makeText(this, "Try Again!!", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(Scan_act.this,MainActivity2.class));
        finish();
    }
    }
}

