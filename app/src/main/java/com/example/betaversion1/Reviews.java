package com.example.betaversion1;

public class Reviews {
    public String uid;
    public Books book;
    public String reviewContent;
    public int rating;

    public Reviews (String uid,Books book,String reviewContent,int rating){
        this.uid=uid;
        this.book=book;
        this.reviewContent=reviewContent;
        this.rating=rating;
    }

    public Reviews(){

    }

    public String getUid() {
        return uid;
    }

    public Books getBook() {
        return book;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getReviewContent() {
        return reviewContent;
    }

    public void setReviewContent(String reviewContent) {
        this.reviewContent = reviewContent;
    }
}
