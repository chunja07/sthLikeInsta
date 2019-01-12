package com.example.a402_24.day_03_register;

public class Search{

    private String search_img;
    private String search_id;
    private String search_intro;

    public Search(String search_id, String search_intro, String search_img) {
        this.search_img = search_img;
        this.search_id = search_id;
        this.search_intro = search_intro;
    }

    public String getSearch_img() {
        return search_img;
    }

    public void setSearch_img(String search_img) {
        this.search_img = search_img;
    }

    public String getSearch_id() {
        return search_id;
    }

    public void setSearch_id(String search_id) {
        this.search_id = search_id;
    }

    public String getSearch_intro() {
        return search_intro;
    }

    public void setSearch_intro(String search_intro) {
        this.search_intro = search_intro;
    }
}




