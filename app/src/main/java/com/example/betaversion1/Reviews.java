package com.example.betaversion1;

public class Reviews {
    public String uid;
    public Books book;
    public String reviewContent;
    public int score;

    public Reviews (String uid,Books book,String reviewContent,int score){
        this.uid=uid;
        this.book=book;
        this.reviewContent=reviewContent;
        this.score=score;
    }

    public Reviews(){

    }

    public String getUid() {
        return uid;
    }

    public Books getBook() {
        return book;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getReviewContent() {
        return reviewContent;
    }

    public void setReviewContent(String reviewContent) {
        this.reviewContent = reviewContent;
    }
}
