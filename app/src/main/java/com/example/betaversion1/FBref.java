package com.example.betaversion1;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FBref {

    public static FirebaseDatabase FBDB = FirebaseDatabase.getInstance();

    public static FirebaseAuth refAuth= FirebaseAuth.getInstance();

    public static DatabaseReference refReviews = FBDB.getReference("Reviews");
    public static DatabaseReference refUsers = FBDB.getReference("Users");
    public static DatabaseReference refBooks = FBDB.getReference("Books");
    public static DatabaseReference refSales = FBDB.getReference("Sales");
}
