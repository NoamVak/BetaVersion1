package com.example.betaversion1;

import static com.example.betaversion1.FBref.refAuth;
import static com.example.betaversion1.FBref.refUsers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class SignUp extends AppCompatActivity {

    EditText eT_username,eT_email,eT_password;
    TextView tV;
    String username,email,password,uid;
    private FirebaseAuth mAuth;
    Users users;
    boolean registered,stayConnect;
    CheckBox cBstayconnect;
    Button btn;
    ToggleButton tB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        tV=(TextView)findViewById(R.id.tV);
        eT_username=(EditText) findViewById(R.id.eT_username);
        eT_email=(EditText) findViewById(R.id.eT_email);
        eT_password=(EditText) findViewById(R.id.eT_password);
        btn=(Button) findViewById(R.id.btn);
        tB=(ToggleButton)findViewById(R.id.tB);
        cBstayconnect=(CheckBox)findViewById(R.id.cBstayconnect);

        registered=true;
        stayConnect=false;

        regoption();

    }

    /**
     * On activity start - Checking if user already logged in.
     * If logged in & asked to be remembered - pass on.
     * <p>
     */

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences settings=getSharedPreferences("PREFS_NAME",MODE_PRIVATE);
        boolean isChecked=settings.getBoolean("stayConnect",false);
        Intent si = new Intent(SignUp.this,MainActivity.class);
        if (mAuth.getCurrentUser()!=null && isChecked) {
            stayConnect=true;
            startActivity(si);
            finish();
        }
    }

    private void regoption(){
        String str="Already have an account?";
        if(tB.isChecked()){
            eT_username.setVisibility(View.VISIBLE);
            tV.setText(str);
            registered=false;
            logoption();
        }
    }

    private void logoption(){
        String str="Don't have an account?";
        if(!tB.isChecked()){
            eT_username.setVisibility(View.INVISIBLE);
            tV.setText(str);
            registered=true;
            regoption();
         }
    }

    public void logOrreg(View view) {
        if(registered){
            email=eT_email.getText().toString();
            password=eT_password.getText().toString();
            if(email.equals("")||password.equals("")){
                Toast.makeText(SignUp.this,"Error! : password or email can't be empty",Toast.LENGTH_SHORT).show();
            }
            else {

                final ProgressDialog pd = ProgressDialog.show(this, "Login", "Connecting...", true);
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                pd.dismiss();
                                if (task.isSuccessful()) {
                                    SharedPreferences settings = getSharedPreferences("PREFS_NAME", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = settings.edit();
                                    editor.putBoolean("stayConnect", cBstayconnect.isChecked());
                                    editor.commit();
                                    Log.d("MainActivity", "signinUserWithEmail:success");
                                    Toast.makeText(SignUp.this, "Login Success", Toast.LENGTH_SHORT).show();
                                    Intent si = new Intent(SignUp.this, MainActivity.class);
                                    startActivity(si);
                                    finish();
                                } else {
                                    Log.d("MainActivity", "signinUserWithEmail:fail");
                                    Toast.makeText(SignUp.this, "e-mail or password are wrong!", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        }
        else{
            email=eT_email.getText().toString();
            password=eT_password.getText().toString();
            username=eT_username.getText().toString();
            if(username.equals("")){
                Toast.makeText(SignUp.this,"You didn't enter a username",Toast.LENGTH_SHORT).show();
            }
            else if(email.equals("")||password.equals("")){
                Toast.makeText(SignUp.this,"Error! : password or email can't be empty",Toast.LENGTH_SHORT).show();
            }
            else {
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d("TAG", "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    updateUI(user);
                                    uid = mAuth.getUid();
                                    Toast.makeText(SignUp.this, uid, Toast.LENGTH_SHORT).show();
                                    users = new Users(username, "Null",email, uid, "Null");
                                    refUsers.child(uid).setValue(users);
                                    Intent si = new Intent(SignUp.this, MainActivity.class);
                                    startActivity(si);
                                    finish();

                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w("TAG", "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(SignUp.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    Toast.makeText(SignUp.this, email + " " + password, Toast.LENGTH_SHORT).show();
                                    updateUI(null);
                                }

                            }
                        });
            }

        }

    }
    public void option(View view) {
    if(tB.isChecked())regoption();
    else logoption();
}

    public void updateUI(FirebaseUser u) {
        if(u==null) Toast.makeText(SignUp.this, "failed", Toast.LENGTH_SHORT).show();
        else Toast.makeText(SignUp.this, "successful", Toast.LENGTH_SHORT).show();
    }


}