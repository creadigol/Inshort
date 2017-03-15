package com.creadigol.inshort.Model;

/**
 * Created by ADMIN on 09-Jan-17.
 */

public class NewsModel {

    int isBookmarked;
    int like;
    String title;
    String discription;
    String link;
    String image_path;
    String server_image_path;
    long createdTime;
    int isRead;
    int id;
    int s_id;

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public int getIsRead() {
        return isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }

    public int getIsBookmarked() {
        return isBookmarked;
    }

    public void setIsBookmarked(int isBookmarked) {
        this.isBookmarked = isBookmarked;
    }


    public String getServer_image_path() {
        return server_image_path;
    }

    public void setServer_image_path(String server_image_path) {
        this.server_image_path = server_image_path;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getS_id() {
        return s_id;
    }

    public void setS_id(int s_id) {
        this.s_id = s_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public long getCreatedTime() {
        return createdTime;
    }


    public void setCreatedTime(String createdTime) {
        //Log.e("setTransactionDate", "input transactionDate:" + createdTime);

        long dateMilliSeconds = 0;
        try {
            dateMilliSeconds = Long.parseLong(createdTime);
            //  Log.e("setTransactionDate", "input dateMilliSeconds:" + dateMilliSeconds);
            //dateMilliSeconds = dateMilliSeconds * 100;

//            this.createdTime = CommonUtils.getFormatedDate(dateMilliSeconds, "dd MMM yyyy hh:mm aaa");
            this.createdTime = dateMilliSeconds;
        } catch (Exception e) {
            this.createdTime = 0;
        }
        //Log.e("setTransactionDate", "this.transactionDate:" + this.createdTime);

    }

    public void setCreatedTime(long createdTime) {
        //Log.e("setTransactionDate", "input transactionDate:" + createdTime);
        this.createdTime = createdTime;
        //Log.e("setTransactionDate", "this.transactionDate:" + this.createdTime);
    }

}
