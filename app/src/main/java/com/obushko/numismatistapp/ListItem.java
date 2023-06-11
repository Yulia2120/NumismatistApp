package com.obushko.numismatistapp;

public class ListItem {

   private String urlImage;
    private String title;
    private String price;
    private boolean currentTheme;

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public boolean getCurrentTheme() {
       return currentTheme;
    }

}
