package com.example.betaversion1;

public class Reviews {
    public String uid;
    public String bookId;
    public String reviewContent;
    public int rating;

    public Reviews (String uid,String bookId,String reviewContent,int rating){
        this.uid=uid;
        this.bookId=bookId;
        this.reviewContent=reviewContent;
        this.rating=rating;
    }

    public Reviews(){

    }

    public String getUid() {
        return uid;
    }

    public String getBookId() {
        return bookId;
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
