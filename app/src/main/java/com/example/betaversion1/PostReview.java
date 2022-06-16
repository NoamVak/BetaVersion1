package com.example.betaversion1;

import static com.example.betaversion1.FBref.refBooks;
import static com.example.betaversion1.FBref.refReviews;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PostReview extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    AlertDialog.Builder adb;
    final String[] decision={"Take Photo","Open From Library"};
    ImageView iv_BookPic;
    EditText et_BookName,et_Author,et_Pages,et_ReviewContent;
    Spinner sp_Rating,sp_AgeGroup,sp_Genre;
    String[] genre={"Genre","Thriller","Horror","Romance","Fantasy","Children book","Fiction","Sci-Fi","Graphic Novel","Manga"};
    String[] ageGroup={"Age Group","Kids","Teens","Adults"};
    Integer [] rating={0,1,2,3,4,5};
    ArrayList<String> bookImages;
    FirebaseUser user;
    String bookId,uid,strPages,bookName,author,reviewContent;
    int genreIndex=0,ageGroupIndex=0,pages,index=0,which;
    Books book;
    Reviews review;

    private Uri filePath,photoUri;
    private final int PICK_IMAGE_REQUEST = 22;
    private final int CAMERA_REQUEST = 45;

    FirebaseStorage storage;
    StorageReference storageReference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_review);

        iv_BookPic=(ImageView) findViewById(R.id.iv_BookPic);
        et_Author=(EditText) findViewById(R.id.et_Author);
        et_BookName=(EditText) findViewById(R.id.et_BookName);
        et_Pages=(EditText) findViewById(R.id.et_pages);
        et_ReviewContent=(EditText) findViewById(R.id.et_ReviewContent);
        sp_Rating=(Spinner) findViewById(R.id.sp_Rating);
        sp_AgeGroup=(Spinner) findViewById(R.id.sp_AgeGroup);
        sp_Genre=(Spinner)findViewById(R.id.sp_Genre);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        sp_Genre.setOnItemSelectedListener(this);
        sp_AgeGroup.setOnItemSelectedListener(this);
        sp_Rating.setOnItemSelectedListener(this);

        ArrayAdapter<String> genreAdp= new ArrayAdapter<String>(this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,genre);
        sp_Genre.setAdapter(genreAdp);

        ArrayAdapter<String> ageGroupAdp= new ArrayAdapter<String>(this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,ageGroup);
        sp_AgeGroup.setAdapter(ageGroupAdp);

        ArrayAdapter<Integer> ratingAdp= new ArrayAdapter<Integer>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,rating);
        sp_Rating.setAdapter(ratingAdp);

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, getEmail or etc
            uid = user.getUid();
        }

    }

    public void UploadBookPic(View view) {
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
                            Uri photoURI = FileProvider.getUriForFile(PostReview.this,
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
            iv_BookPic.setImageURI(photoUri);
        }
        // checking request code and result code
        // if request code is PICK_IMAGE_REQUEST and
        // resultCode is RESULT_OK
        // then set image in the image view
        else if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                iv_BookPic.setImageBitmap(bitmap);
            }
            catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }

    public void UploadReview(View view) {
        bookId=refBooks.push().getKey();
        bookName=et_BookName.getText().toString();
        author=et_Author.getText().toString();
        strPages=et_Pages.getText().toString();
        reviewContent=et_ReviewContent.getText().toString();
        if(bookName.equals("")||reviewContent.equals("")||author.equals("")||strPages.equals("")||genreIndex==0){
            Toast.makeText(this,"Error: Fields cannot remain empty",Toast.LENGTH_SHORT).show();
        }
        else{
            pages=Integer.parseInt(strPages);
            book=new Books(bookName,author,genreIndex,null,pages,bookId,index);
            review=new Reviews(uid,bookId,reviewContent,index);
            refBooks.child(String.valueOf(genreIndex)).child(author).setValue(book);
            refReviews.child(String.valueOf(index)).setValue(review);

        }

        if(photoUri!=null&&which==1){
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            UploadTask uploadTask = storageReference.child("images/books/" + bookId+"-"+EditProfile.count).putFile(photoUri);
            //StorageReference ref = mStorageRef.child("images/users/" + auth.getCurrentUser().getUid()+"-"+Gallery.count);
            EditProfile.count++;
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    Toast.makeText(PostReview.this, "Successfully Uploaded", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(PostReview.this, "Failed to upload.", Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    progressDialog.setMessage("Uploaded " + (int)progress + "%");
                }
            });
        }
        if (filePath != null&&which==2) {
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            StorageReference ref = storageReference.child("images/books/" +bookId+"-"+EditProfile.count);
            EditProfile.count++;
            ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // Image uploaded successfully
                    // Dismiss dialog
                    progressDialog.dismiss();
                    Toast.makeText(PostReview.this, "Image Uploaded!!", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e)
                {
                    // Error, Image not uploaded
                    progressDialog.dismiss();
                    Toast.makeText(PostReview.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                // Progress Listener for loading
                // percentage on the dialog box
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    progressDialog.setMessage("Uploaded " + (int)progress + "%");
                }
            });


        }

    }

    @Override
    public void onItemSelected(AdapterView<?> adp, View view, int i, long l) {
        if(adp.getId() == R.id.sp_AgeGroup){
            ageGroupIndex=i;
        }
        else if (adp.getId() == R.id.sp_Rating){
            index= rating[i];
        }
        else if(adp.getId() == R.id.sp_Genre){
            genreIndex=i;

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

}