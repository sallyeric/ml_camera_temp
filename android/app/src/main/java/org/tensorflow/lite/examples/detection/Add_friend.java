package org.tensorflow.lite.examples.detection;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class Add_friend extends AppCompatActivity {



    private StorageReference mStorageRef = FirebaseStorage.getInstance().getReference("Images");
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    // get current user info
    private FirebaseFirestore mFireStoreRef = FirebaseFirestore.getInstance();      // PLACE reference

    private final FirebaseUser user = fAuth.getCurrentUser();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_friend);
        GridView mgridView = (GridView) findViewById(R.id.Add_friend_gridview);
        final Image_Adapter_clickable_add_profile ia = new Image_Adapter_clickable_add_profile(this);
        mgridView.setAdapter(ia);
        mgridView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView parent, View v, int position, long id){
                ia.callImageViewer(position,1);
                finish();
            }
        });

        EditText editName = findViewById(R.id.Add_friend_name);
        Button btn = findViewById(R.id.btn_friend_change);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inputintent = getIntent();

                String imgPath = inputintent.getExtras().getString("filepath");
                Uri uri= Uri.parse(imgPath);

                String name = editName.getText().toString();


                //여기서 디비에 전달
                uploadImage(uri, name);
//                Intent intent = new Intent(getApplicationContext(),tab1.class);
//
//                startActivity(intent);
                finish();
            }
        });

        ImageButton button = findViewById(R.id.back2);
//        downloadData();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    void uploadImage(Uri uri, String name)
    {
        //이미지 저장
        SimpleDateFormat s = new SimpleDateFormat("ddMMyyyyhhmmss");
        String format = s.format(new Date());
        String fileName = "image_" + format + ".jpg";


        InputStream stream = null;

        try{
            stream = new FileInputStream(uri.toString());
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
        }

        if(stream == null)
        {
            return;
        }



        // get image url
        final StorageReference riversRef = mStorageRef.child(fileName);
        final UploadTask uploadTask = riversRef.putStream(stream);
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if(!task.isSuccessful()){
                    //throw.task.getException();
                }
                return riversRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if(task.isSuccessful()){
                    Uri downloadUri = task.getResult();
                    String imageUrl = String.valueOf(downloadUri);
                    uploadData(downloadUri, name);
                }else{

                }
            }
        });
    }
    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }


    void uploadData(Uri imgOfUrl, String name)
    {
        String userEmail = user.getEmail();


        HashMap<String, Object> data = new HashMap<>();
        data.put("profileUrl", imgOfUrl.toString());
        data.put("name", name);

        mFireStoreRef.collection("Users")
                .document(userEmail)
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Success", "DocumentSnapshot successfully updated!");
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("Failure", "Error updating document", e);
            }
        });





    }
}
