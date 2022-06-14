package com.example.betaversion1;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class EditProfile extends AppCompatActivity {

    ImageView iV_profilePic;
    EditText eT_Username1,eT_Bio;
    String username,bio;

    private Uri filePath,photoUri;
    private final int PICK_IMAGE_REQUEST = 22;
    private final int CAMERA_REQUEST = 45;

    public static int count=0;

    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        iV_profilePic=(ImageView) findViewById(R.id.iV_profilePic);
        eT_Username1=(EditText)findViewById(R.id.eT_Username1);
        eT_Bio=(EditText)findViewById(R.id.eT_Bio);


        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        auth=FirebaseAuth.getInstance();
    }

    public void editPic(View view) {


    }

    public void updateProfile(View view) {
    }
}