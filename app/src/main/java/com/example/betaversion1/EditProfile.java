package com.example.betaversion1;

import static com.example.betaversion1.FBref.refUsers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EditProfile extends AppCompatActivity {

    AlertDialog.Builder adb;
    final String[] decision={"Take Photo","Open From Library"};
    FirebaseUser user;
    String uid,str1,profilePic;
    ArrayList<String> userList=new ArrayList<String>();
    ArrayList<Users> userValues=new ArrayList<Users>();
    Users updateUser;
    int which;

    ImageView iV_profilePic;
    EditText eT_Username1,eT_Bio;
    String username,bio;

    private Uri filePath,photoUri;
    private final int PICK_IMAGE_REQUEST = 22;
    private final int CAMERA_REQUEST = 45;

    public static int count=0;

    FirebaseStorage storage;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        iV_profilePic=(ImageView) findViewById(R.id.iV_profilePic);
        eT_Username1=(EditText)findViewById(R.id.eT_Username1);
        eT_Bio=(EditText)findViewById(R.id.eT_Bio);


        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, getEmail or etc
            uid = user.getUid();
        }


        readUserInfo();
        downloadUserPfp();


    }

    private void downloadUserPfp(){

    }

    private void readUserInfo(){
        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dS) {
                userList.clear();
                userValues.clear();
                for(DataSnapshot data : dS.getChildren()){
                    str1= (String) data.getKey();
                    Users userTmp = data.getValue(Users.class);
                    userValues.add(userTmp);
                    userList.add(str1);
                }
                int index=userList.indexOf(uid);
                eT_Username1.setText(userValues.get(index).getName());
                if(!userValues.get(index).getBio().equals("Null"))
                    eT_Bio.setText(userValues.get(index).getBio());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        refUsers.addValueEventListener(userListener);

    }



    public void editPic(View view) {
        adb= new AlertDialog.Builder(this);

        adb.setTitle("Change your profile picture");
        adb.setItems(decision, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(decision[i].equals("Take Photo")){
                    which=1;
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    //startActivityForResult(intent, 101);

                    //if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    if (true) {
                        // Create the File where the photo should go
                        File photoFile = null;
                        try {
                            photoFile = createImageFile();
                        } catch (IOException ex) {
                            // Error occurred while creating the File
                        }
                        // Continue only if the File was successfully created
                        if (photoFile != null) {
                            Uri photoURI = FileProvider.getUriForFile(EditProfile.this,
                                    "com.example1.android.fileprovider",
                                    photoFile);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                            startActivityForResult(intent, CAMERA_REQUEST);
                        } //
                    }
                }
                else if(decision[i].equals("Open From Library")){
                    which=2;
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(
                            Intent.createChooser(
                                    intent,
                                    "Select Image from here..."),
                            PICK_IMAGE_REQUEST);
                }
            }
        });
        AlertDialog ad=adb.create();
        ad.show();


    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        photoUri = Uri.fromFile(image);
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == CAMERA_REQUEST) {
            iV_profilePic.setImageURI(photoUri);
        }
        // checking request code and result code
        // if request code is PICK_IMAGE_REQUEST and
        // resultCode is RESULT_OK
        // then set image in the image view
        else if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                iV_profilePic.setImageBitmap(bitmap);
            }
            catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }

    public void updateProfile(View view) {
        username=eT_Username1.getText().toString();
        bio=eT_Bio.getText().toString();
        int index=userList.indexOf(uid);

        if(photoUri != null && which==1){
            if(username.equals("")){
                Toast.makeText(EditProfile.this,"Username can't be null",Toast.LENGTH_SHORT).show();
            }
            else if(bio.equals("")){
                updateUser=userValues.get(index);
                updateUser.setName(username);
                updateUser.setBio("Null");
            }
            else{
                updateUser=userValues.get(index);
                updateUser.setName(username);
                updateUser.setBio(bio);
            }
            if(!username.equals("")) {
                ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setTitle("Uploading...");
                progressDialog.show();
                UploadTask uploadTask = storageReference.child("images/users/" + uid + "-" + "profile").putFile(photoUri);
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressDialog.dismiss();
                        Toast.makeText(EditProfile.this, "Successfully Uploaded", Toast.LENGTH_SHORT).show();
                        profilePic="images/users/" + uid + "-" + "profile";
                        updateUser.setImage(profilePic);
                        refUsers.child(uid).setValue(updateUser);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(EditProfile.this, "Failed to upload.", Toast.LENGTH_SHORT).show();
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                        progressDialog.setMessage("Uploaded " + (int) progress + "%");
                    }
                });
            }
        }
        if (filePath != null&&which==2) {
            if(username.equals("")){
                Toast.makeText(EditProfile.this,"Username can't be null",Toast.LENGTH_SHORT).show();
            }
            else if(bio.equals("")){
                updateUser=userValues.get(index);
                updateUser.setName(username);
                updateUser.setBio("Null");
            }
            else{
                updateUser=userValues.get(index);
                updateUser.setName(username);
                updateUser.setBio(bio);

            }
            if(!username.equals("")) {
                ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setTitle("Uploading...");
                progressDialog.show();
                StorageReference ref = storageReference.child("images/users/" + uid + "-" + "profile");
                ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // Image uploaded successfully
                                // Dismiss dialog
                                progressDialog.dismiss();
                                Toast.makeText(EditProfile.this, "Image Uploaded!!", Toast.LENGTH_SHORT).show();
                                profilePic="images/users/" + uid + "-" + "profile";
                                updateUser.setImage(profilePic);
                                refUsers.child(uid).setValue(updateUser);
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                // Error, Image not uploaded
                                progressDialog.dismiss();
                                Toast.makeText(EditProfile.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnProgressListener(
                                new OnProgressListener<UploadTask.TaskSnapshot>() {
                                    // Progress Listener for loading
                                    // percentage on the dialog box
                                    @Override
                                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                        double progress = (100.0 * taskSnapshot.getBytesTransferred() /
                                                taskSnapshot.getTotalByteCount());
                                        progressDialog.setMessage("Uploaded " + (int) progress + "%");
                                    }
                                });
            }
        }

        if(filePath == null&& photoUri == null) {
            if (username.equals("")) {
                Toast.makeText(EditProfile.this, "Username can't be null", Toast.LENGTH_SHORT).show();
            } else if (bio.equals("")) {
                updateUser = userValues.get(index);
                updateUser.setName(username);
                updateUser.setBio("Null");
                refUsers.child(uid).setValue(updateUser);
                finish();
            } else {
                updateUser = userValues.get(index);
                updateUser.setName(username);
                updateUser.setBio(bio);
                refUsers.child(uid).setValue(updateUser);
                finish();

            }
        }
    }
}