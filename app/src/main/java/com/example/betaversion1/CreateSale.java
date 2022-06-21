package com.example.betaversion1;

import static com.example.betaversion1.FBref.refBooks;
import static com.example.betaversion1.FBref.refSales;
import static com.example.betaversion1.FBref.refUsers;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.time.LocalTime;

public class CreateSale extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    AlertDialog.Builder adb;
    final String[] decision={"Take Photo","Open From Library"};
    EditText et_BookName,et_Author,et_Pages,et_price,et_PhoneNum,et_City,et_address,et_saleInfo;
    Spinner sp_AgeGroup,sp_Genre,sp_cond;
    ImageView iv_salePic;
    String[] genre={"Genre","Thriller","Horror","Romance","Fantasy","Children book","Fiction","Sci-Fi","Graphic Novel","Manga"};
    String[] ageGroup={"Age Group","Kids","Teens","Adults"};
    String[] condition={"Condition","Brand New","Like New","Used","Old"};
    FirebaseUser user;
    Sales newSale;
    Books newBook;
    String bookImages="Null";

    Boolean status;
    String date,bookId,uid,strPages,bookName,author,strPrice,phoneNum,city,address,saleInfo;
    int which,genreIndex=0,ageGroupIndex=0,pages,price,condIndex=0;

    private Uri filePath,photoUri;
    private final int PICK_IMAGE_REQUEST = 22;
    private final int CAMERA_REQUEST = 45;

    FirebaseStorage storage;
    StorageReference storageReference;



    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_sale);

        et_BookName=(EditText) findViewById(R.id.et_bookName1);
        et_Author=(EditText) findViewById(R.id.et_Author1);
        et_Pages=(EditText) findViewById(R.id.et_pages1);
        et_price=(EditText) findViewById(R.id.et_price);
        et_PhoneNum=(EditText)findViewById(R.id.et_PhoneNum);
        et_City=(EditText) findViewById(R.id.et_City);
        et_address=(EditText) findViewById(R.id.et_address);
        et_saleInfo=(EditText) findViewById(R.id.et_saleInfo);
        sp_AgeGroup=(Spinner)findViewById(R.id.sp_ageGroup1);
        sp_Genre=(Spinner) findViewById(R.id.sp_genre1);
        sp_cond=(Spinner) findViewById(R.id.sp_cond);
        iv_salePic=(ImageView) findViewById(R.id.iv_salePic);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        sp_Genre.setOnItemSelectedListener(this);
        sp_AgeGroup.setOnItemSelectedListener(this);
        sp_cond.setOnItemSelectedListener(this);

        ArrayAdapter<String> genreAdp= new ArrayAdapter<String>(this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,genre);
        sp_Genre.setAdapter(genreAdp);

        ArrayAdapter<String> ageGroupAdp= new ArrayAdapter<String>(this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,ageGroup);
        sp_AgeGroup.setAdapter(ageGroupAdp);

        ArrayAdapter<String> conditionAdp= new ArrayAdapter<String>(this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,condition);
        sp_cond.setAdapter(conditionAdp);



        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, getEmail or etc
            uid = user.getUid();
        }

    }


    public void addPhoto(View view) {
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
                            Uri photoURI = FileProvider.getUriForFile(CreateSale.this,
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
            iv_salePic.setImageURI(photoUri);
        }
        // checking request code and result code
        // if request code is PICK_IMAGE_REQUEST and
        // resultCode is RESULT_OK
        // then set image in the image view
        else if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                iv_salePic.setImageBitmap(bitmap);
            }
            catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }

    public void postSale(View view) {
        bookName=et_BookName.getText().toString();
        author=et_Author.getText().toString();
        strPages=et_Pages.getText().toString();
        strPrice=et_price.getText().toString();
        phoneNum=et_PhoneNum.getText().toString();
        city=et_City.getText().toString();
        address=et_address.getText().toString();
        saleInfo=et_saleInfo.getText().toString();
        bookId=refBooks.push().getKey();
        date = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        status = true;
        if (photoUri != null && which == 1) {
            if(condIndex==0||bookName.equals("")||author.equals("")||strPages.equals("")||strPrice.equals("")||city.equals("")||genreIndex==0||ageGroupIndex==0)
                Toast.makeText(CreateSale.this, "can't submit if fields are empty", Toast.LENGTH_SHORT).show();
            else {
                pages = Integer.parseInt(strPages);
                price = Integer.parseInt(strPrice);
                bookImages="images/books/" + bookId;
                newBook = new Books(bookName, author, genreIndex, bookImages, pages, bookId, ageGroupIndex);
                newSale = new Sales(date, status, price, condIndex, bookImages, address, phoneNum, city, uid, bookId, saleInfo);
                ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setTitle("Uploading...");
                progressDialog.show();
                UploadTask uploadTask = storageReference.child("images/books/" + bookId ).putFile(photoUri);
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressDialog.dismiss();
                        refBooks.child(bookId).setValue(newBook);
                        refSales.child(date).setValue(newSale);
                        finish();
                        Toast.makeText(CreateSale.this, "Successfully Uploaded", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(CreateSale.this, "Failed to upload.", Toast.LENGTH_SHORT).show();
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
        if (filePath != null && which == 2) {
            if(condIndex==0||bookName.equals("")||author.equals("")||strPages.equals("")||strPrice.equals("")||city.equals("")||genreIndex==0||ageGroupIndex==0)
                Toast.makeText(CreateSale.this, "can't submit if fields are empty", Toast.LENGTH_SHORT).show();
            else {
                pages = Integer.parseInt(strPages);
                price = Integer.parseInt(strPrice);
                bookImages="images/books/" + bookId;
                newBook = new Books(bookName, author, genreIndex, bookImages, pages, bookId, ageGroupIndex);
                newSale = new Sales(date, status, price, condIndex, bookImages, address, phoneNum, city, uid, bookId, saleInfo);
                ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setTitle("Uploading...");
                progressDialog.show();
                StorageReference ref = storageReference.child("images/books/" + bookId);
                ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Image uploaded successfully
                        // Dismiss dialog
                        progressDialog.dismiss();
                        refBooks.child(bookId).setValue(newBook);
                        refSales.child(date).setValue(newSale);
                        Toast.makeText(CreateSale.this, "Image Uploaded!!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Error, Image not uploaded
                        progressDialog.dismiss();
                        Toast.makeText(CreateSale.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    // Progress Listener for loading
                    // percentage on the dialog box
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                        progressDialog.setMessage("Uploaded " + (int) progress + "%");
                    }
                });
            }
        }
        if (filePath == null && photoUri == null) {
            if(condIndex<=0||bookName.equals("")||author.equals("")||strPages.equals("")||strPrice.equals("")||city.equals("")||genreIndex<=0||ageGroupIndex<=0)
                Toast.makeText(CreateSale.this, "can't submit if fields are empty", Toast.LENGTH_SHORT).show();
            else {
                pages = Integer.parseInt(strPages);
                price = Integer.parseInt(strPrice);
                newBook = new Books(bookName, author, genreIndex, bookImages, pages, bookId, ageGroupIndex);
                newSale = new Sales(date, status, price, condIndex, bookImages, address, phoneNum, city, uid, bookId, saleInfo);
                refBooks.child(bookId).setValue(newBook);
                refSales.child(date).setValue(newSale);
                finish();
            }
        }

    }


    @Override
    public void onItemSelected(AdapterView<?> adp, View view, int i, long l) {
        if(adp.getId() == R.id.sp_ageGroup1){
            ageGroupIndex=i;
        }
        else if (adp.getId() == R.id.sp_cond){
            condIndex=i;
        }
        else if(adp.getId() == R.id.sp_genre1){
            genreIndex=i;

        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}