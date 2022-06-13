package com.example.betaversion1;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FBref {

    public static FirebaseDatabase FBDB = FirebaseDatabase.getInstance();

    public static FirebaseAuth reAuth= FirebaseAuth.getInstance();

    public static DatabaseReference refUsers = FBDB.getReference("Users");
    public static DatabaseReference refBooks = FBDB.getReference("Books");
    public static DatabaseReference refSales = FBDB.getReference("Sales");
}
