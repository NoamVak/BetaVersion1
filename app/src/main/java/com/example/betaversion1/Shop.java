package com.example.betaversion1;

import static com.example.betaversion1.FBref.refBooks;
import static com.example.betaversion1.FBref.refSales;
import static com.example.betaversion1.FBref.refUsers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class Shop extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {
    ArrayList<String> saleList=new ArrayList<>();
    ArrayList<Sales> saleValues=new ArrayList<>();
    ArrayList<String> bookList=new ArrayList<>();
    ArrayList<Books> bookValues= new ArrayList<>();
    ArrayList<String> userList=new ArrayList<>();
    ArrayList<Users> userValues=new ArrayList<>();
    ArrayList<String> book_name=new ArrayList<>();
    ArrayList<Integer> pages=new ArrayList<>();
    ArrayList<Integer> conditionList= new ArrayList<>();
    ArrayList<String> location=new ArrayList<>();
    ArrayList<String> dateList=new ArrayList<>();
    ArrayList<String> hasImage= new ArrayList<>();
    ArrayList<Integer> price= new ArrayList<>();
    ArrayList<Boolean> status=new ArrayList<>();
    String[] genre={"Genre","Thriller","Horror","Romance","Fantasy","Children book","Fiction","Sci-Fi","Graphic Novel","Manga"};
    String[] ageGroup={"Age Group","Kids","Teens","Adults"};
    String[] filter={"Filter Sales","With image","without image","For Kids","For Teens","For Adults"};
    String[] sort={"Sort Sales","By Price","By Condition"};

    Spinner saleSort,saleFilter;
    ListView shop_ListView;
    TextView tv_bookName,tv_Author,tv_pages,tv_AgeGroup,tv_Genre,tv_PhoneNum,tv_info;
    String str1,bookId,image;
    int sIndex=0,fIndex=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        shop_ListView=(ListView) findViewById(R.id.shop_ListView);
        tv_bookName=(TextView) findViewById(R.id.tv_bookName);
        tv_Author=(TextView) findViewById(R.id.tv_Author);
        tv_pages=(TextView) findViewById(R.id.tv_pages);
        tv_AgeGroup=(TextView) findViewById(R.id.tv_AgeGroup);
        tv_Genre=(TextView) findViewById(R.id.tv_Genre);
        tv_PhoneNum=(TextView) findViewById(R.id.tv_PhoneNum);
        tv_info=(TextView) findViewById(R.id.tv_info);
        saleSort=(Spinner) findViewById(R.id.salesSort);
        saleFilter=(Spinner) findViewById(R.id.saleFilter);

        readBookInfo(sIndex);

        shop_ListView.setOnItemClickListener(this);
        shop_ListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        saleSort.setOnItemSelectedListener(this);
        saleFilter.setOnItemSelectedListener(this);

        ArrayAdapter<String> adpSort=new ArrayAdapter<String>(this
                , androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,sort);
        saleSort.setAdapter(adpSort);
        ArrayAdapter<String> adpFilter=new ArrayAdapter<String>(this
                , androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,filter);
        saleFilter.setAdapter(adpFilter);


    }

    private void displaySales(){
        if(!saleList.isEmpty()){
            book_name.clear();
            pages.clear();
            conditionList.clear();
            location.clear();
            dateList.clear();
            hasImage.clear();
            price.clear();
            status.clear();
            for(int i=0;i<saleValues.size();i++){
                if(saleValues.get(i).getStatus()) {
                    bookId = saleValues.get(i).getBookId();
                    int bIndex = bookList.indexOf(bookId);
                    if (bIndex != -1) {
                        book_name.add(bookValues.get(bIndex).getName());
                        pages.add(bookValues.get(bIndex).getPages());
                        conditionList.add(saleValues.get(i).getCondition());
                        price.add(saleValues.get(i).getPrice());
                        status.add(saleValues.get(i).getStatus());
                        if (!saleValues.get(i).getAddress().equals(""))
                            location.add(saleValues.get(i).getCity() + " - " + saleValues.get(i).getAddress());
                        else location.add(saleValues.get(i).getCity());
                        dateList.add(saleValues.get(i).getDate());
                        if (bookValues.get(bIndex).getImage().equals("Null")) {
                            hasImage.add("Null");
                        } else {
                            image = bookValues.get(bIndex).getImage();
                            hasImage.add(image);
                        }
                    }
                }
            }
            CustomAdapterShop customAdp= new CustomAdapterShop(getApplicationContext(),book_name,pages,conditionList,location,dateList
                    ,hasImage,price,status);
            shop_ListView.setAdapter(customAdp);
            customAdp.notifyDataSetChanged();
        }
    }


    private void readSaleInfo(){
        Query query= refSales.orderByChild("status").equalTo(true);
        ValueEventListener saleListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dS) {
                saleList.clear();
                saleValues.clear();
                for (DataSnapshot data : dS.getChildren()) {
                    str1=(String) data.getKey();
                    Sales saleTmp = data.getValue(Sales.class);
                    saleValues.add(saleTmp);
                    saleList.add(str1);
                }
                displaySales();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        query.addValueEventListener(saleListener);
    }

    private void sortSalePrice(){
        Query query= refSales.orderByChild("price");
        ValueEventListener saleListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dS) {
                saleList.clear();
                saleValues.clear();
                for (DataSnapshot data : dS.getChildren()) {
                    str1=(String) data.getKey();
                    Sales saleTmp = data.getValue(Sales.class);
                    saleValues.add(saleTmp);
                    saleList.add(str1);
                }
                displaySales();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        query.addValueEventListener(saleListener);
    }

    private void sortSaleCond(){
        Query query= refSales.orderByChild("condition");
        ValueEventListener saleListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dS) {
                saleList.clear();
                saleValues.clear();
                for (DataSnapshot data : dS.getChildren()) {
                    str1=(String) data.getKey();
                    Sales saleTmp = data.getValue(Sales.class);
                    saleValues.add(saleTmp);
                    saleList.add(str1);
                }
                displaySales();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        query.addValueEventListener(saleListener);
    }


    private void readBookInfo(int sIndex){
        ValueEventListener bookListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dS) {
                bookList.clear();
                bookValues.clear();
                for (DataSnapshot data : dS.getChildren()) {
                    str1=(String) data.getKey();
                    Books bookTmp = data.getValue(Books.class);
                    bookValues.add(bookTmp);
                    bookList.add(str1);
                }
                switch(sIndex) {
                    case 0:
                        readSaleInfo();
                        break;
                    case 1:
                        sortSalePrice();
                        break;
                    case 2:
                        sortSaleCond();
                        break;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        refBooks.addValueEventListener(bookListener);
    }

    private void bookForKids(int sIndex){
        Query query=refBooks.orderByChild("ageGroup").equalTo(1);
        ValueEventListener VEL=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dS) {
                bookList.clear();
                bookValues.clear();
                for (DataSnapshot data : dS.getChildren()) {
                    str1=(String) data.getKey();
                    Books bookTmp = data.getValue(Books.class);
                    bookValues.add(bookTmp);
                    bookList.add(str1);
                }
                switch(sIndex) {
                    case 0:
                        readSaleInfo();
                        break;
                    case 1:
                        sortSalePrice();
                        break;
                    case 2:
                        sortSaleCond();
                        break;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        query.addValueEventListener(VEL);
    }

    private void bookForTeens(int sIndex){
        Query query=refBooks.orderByChild("ageGroup").equalTo(2);
        ValueEventListener VEL=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dS) {
                bookList.clear();
                bookValues.clear();
                for (DataSnapshot data : dS.getChildren()) {
                    str1=(String) data.getKey();
                    Books bookTmp = data.getValue(Books.class);
                    bookValues.add(bookTmp);
                    bookList.add(str1);
                }
                switch(sIndex) {
                    case 0:
                        readSaleInfo();
                        break;
                    case 1:
                        sortSalePrice();
                        break;
                    case 2:
                        sortSaleCond();
                        break;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        query.addValueEventListener(VEL);
    }

    private void bookForAdults(int sIndex){
        Query query=refBooks.orderByChild("ageGroup").equalTo(3);
        ValueEventListener VEL=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dS) {
                bookList.clear();
                bookValues.clear();
                for (DataSnapshot data : dS.getChildren()) {
                    str1=(String) data.getKey();
                    Books bookTmp = data.getValue(Books.class);
                    bookValues.add(bookTmp);
                    bookList.add(str1);
                }
                switch(sIndex) {
                    case 0:
                        readSaleInfo();
                        break;
                    case 1:
                        sortSalePrice();
                        break;
                    case 2:
                        sortSaleCond();
                        break;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        query.addValueEventListener(VEL);
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item){
        String st=item.getTitle().toString();
        if(st.equals("Home Screen")){
            Intent si=new Intent(Shop.this,MainActivity.class);
            startActivity(si);
            finish();
        }

        return true;
    }



    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        int bIndex=bookList.indexOf(saleValues.get(i).getBookId());
        Books book=bookValues.get(bIndex);
        tv_bookName.setText("Book Name- "+book.getName());
        tv_Author.setText("Written by- "+book.getAuthor());
        tv_pages.setText("Pages- "+String.valueOf(book.getPages()));
        tv_AgeGroup.setText("Recommended for- "+ageGroup[book.getAgeGroup()]);
        tv_Genre.setText("Genre- "+genre[book.getGenre()]);
        tv_PhoneNum.setText("Seller phone- "+saleValues.get(i).getPhoneNum());
        if(saleValues.get(i).getInfo().equals(""))
            tv_info.setText("no additional information");
        else tv_info.setText("info- "+saleValues.get(i).getInfo());

    }

    @Override
    public void onItemSelected(AdapterView<?> adp, View view, int i, long l) {
        if(adp.getId()==R.id.salesSort){
            sIndex=i;
        }
        if(adp.getId()==R.id.saleFilter){
            fIndex=i;
        }

        if(sIndex==0){
            switch (fIndex){
                case 0:
                    readBookInfo(sIndex);
                    break;
                case 3:
                    bookForKids(sIndex);
                    break;
                case 4:
                    bookForTeens(sIndex);
                    break;
                case 5:
                    bookForAdults(sIndex);
                    break;
            }
        }
        else if(sIndex==1){
            switch(fIndex){
                case 0:
                    readBookInfo(sIndex);
                    break;
                case 3:
                    bookForKids(sIndex);
                    break;
                case 4:
                    bookForTeens(sIndex);
                    break;
                case 5:
                    bookForAdults(sIndex);
                    break;
            }
        }
        else if(sIndex==2){
            switch(fIndex){
                case 0:
                    readBookInfo(sIndex);
                    break;
                case 3:
                    bookForKids(sIndex);
                    break;
                case 4:
                    bookForTeens(sIndex);
                    break;
                case 5:
                    bookForAdults(sIndex);
                    break;
            }
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}