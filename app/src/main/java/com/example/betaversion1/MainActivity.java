package com.example.betaversion1;

import static com.example.betaversion1.FBref.refBooks;
import static com.example.betaversion1.FBref.refSales;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity  {
    FirebaseUser user;
    Sales sale, sale2;
    Books book, book2;
    ArrayList<String> lst = new ArrayList<>();
    ArrayList<String> bookList = new ArrayList<String>();
    ArrayList<Books> bookValues = new ArrayList<Books>();
    ArrayList<String> saleList = new ArrayList<String>();
    ArrayList<Sales> saleValues = new ArrayList<Sales>();
    String uid, str1;
    boolean status = true;
    int genre = 2;
    ListView listview, listview1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listview = (ListView) findViewById(R.id.listview);
        listview1 = (ListView) findViewById(R.id.listview1);
        refBooks.push();

        lst.add("926SUu2pP4ODVcspD0gretH7Jzh1-0");
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, getEmail or etc
            uid = user.getUid();
        }
        sale = new Sales("20220607", status, 30, 5, lst, uid, "1", "sale check");
        refSales.child(String.valueOf(status)).setValue(sale);

        book = new Books("Harry Potter", "J.k Rowling", genre, lst, 340, "1", 2, "Book Check");
        refBooks.child(String.valueOf(genre)).setValue(book);

        ArrayAdapter<String> saleAdp = new ArrayAdapter<String>(this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, saleList);

        ValueEventListener saleListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dS) {
                saleList.clear();
                saleValues.clear();
                for (DataSnapshot data : dS.getChildren()) {
                    str1 = (String) data.getKey();
                    Sales saleTmp = data.getValue(Sales.class);
                    saleValues.add(saleTmp);
                    saleList.add(str1);
                }
                saleAdp.notifyDataSetChanged();
                listview.setAdapter(saleAdp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        refSales.addValueEventListener(saleListener);


        ArrayAdapter<String> bookAdp = new ArrayAdapter<String>(this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, bookList);

        ValueEventListener bookListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dS) {
                bookList.clear();
                bookValues.clear();
                for (DataSnapshot data : dS.getChildren()) {
                    str1 = (String) data.getKey();
                    Books bookTmp = data.getValue(Books.class);
                    bookValues.add(bookTmp);
                    bookList.add(str1);
                }
                bookAdp.notifyDataSetChanged();
                listview1.setAdapter(bookAdp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        refBooks.addValueEventListener(bookListener);

    }



}