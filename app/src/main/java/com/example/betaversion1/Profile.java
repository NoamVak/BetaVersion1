package com.example.betaversion1;

import static com.example.betaversion1.FBref.refAuth;
import static com.example.betaversion1.FBref.refUsers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class Profile extends AppCompatActivity {

    String str1;
    ImageView profilePic;
    TextView tvUsername,tvBio;
    FirebaseUser user;
    String uid,pfpPath;
    ArrayList<String> userList=new ArrayList<String>();
    ArrayList<Users> userValues=new ArrayList<Users>();
    FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profilePic=(ImageView) findViewById(R.id.profilePic);
        tvUsername=(TextView) findViewById(R.id.tvUsername);
        tvBio=(TextView)findViewById(R.id.tvBio);

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, getEmail or etc
            uid = user.getUid();
        }

        storage= FirebaseStorage.getInstance();

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
                tvUsername.setText(userValues.get(index).getName());
                String bio=userValues.get(index).getBio();
                if(!bio.equals("Null"))
                    tvBio.setText(userValues.get(index).getBio());
                else tvBio.setText("");
                if(!userValues.get(index).getImage().equals("Null")) {
                    pfpPath = userValues.get(index).getImage();
                    downloadUserPfp(pfpPath);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        refUsers.addValueEventListener(userListener);

    }

    private void downloadUserPfp(String s){
        StorageReference storageRef = storage.getReference();
        StorageReference imageRef= storageRef.child(s);
        final long ONE_MEGABYTE = 3150* 3150;

        imageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bMap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                profilePic.setImageBitmap(bMap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Profile.this,"not Working",Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item){
        String st=item.getTitle().toString();
        if(st.equals("Home Screen")){
            Intent si=new Intent(Profile.this,MainActivity.class);
            startActivity(si);
        }

        if(st.equals("Sign Out")){
            refAuth.signOut();
            SharedPreferences settings=getSharedPreferences("PREFS_NAME",MODE_PRIVATE);
            SharedPreferences.Editor editor=settings.edit();
            editor.putBoolean("stayConnect",false);
            editor.commit();
            Intent si=new Intent (Profile.this,SignUp.class);
            startActivity(si);
            finish();
        }
        return true;
    }


    public void EditProfile(View view) {
        Intent si=new Intent(Profile.this,EditProfile.class);
        startActivity(si);
    }

    public void postReview(View view) {
        Intent si=new Intent(Profile.this,PostReview.class);
        startActivity(si);
    }
}