package com.example.betaversion1;

import static com.example.betaversion1.FBref.refUsers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class EditProfile extends AppCompatActivity {

    AlertDialog.Builder adb;
    final String[] decision={"Take Photo","Open From Library"};
    FirebaseUser user;
    String uid,str1;
    ArrayList<String> userList=new ArrayList<String>();
    ArrayList<Users> userValues=new ArrayList<Users>();
    Users updateUser;

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


        /*storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        auth=FirebaseAuth.getInstance();*/

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, getEmail or etc
            uid = user.getUid();
        }


        readUserInfo();


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

            }
        });
        AlertDialog ad=adb.create();
        ad.show();


    }

    public void updateProfile(View view) {
        username=eT_Username1.getText().toString();
        bio=eT_Bio.getText().toString();
        int index=userList.indexOf(uid);
        if(username.equals("")){
            Toast.makeText(EditProfile.this,"Username can't be null",Toast.LENGTH_SHORT).show();
        }
        else if(bio.equals("")){
            updateUser=userValues.get(index);
            updateUser.setName(username);
            updateUser.setBio("Null");
            refUsers.child(uid).setValue(updateUser);
            finish();
        }
        else{
            updateUser=userValues.get(index);
            updateUser.setName(username);
            updateUser.setBio(bio);
            refUsers.child(uid).setValue(updateUser);
            finish();

        }

    }
}