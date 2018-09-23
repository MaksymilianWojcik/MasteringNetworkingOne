package com.example.mwojcik.retrofitone.retrofit;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SimpleModel {

    @SerializedName("page")
    private int page;
    @SerializedName("per_page")
    private int per_page;
    @SerializedName("total")
    private int total;
    @SerializedName("total_pages")
    private int total_pages;
    @SerializedName("data")
    private List<SimpleData> simpleDataList;

    public SimpleModel(int page, int per_page, int total, int total_pages, List<SimpleData> simpleDataList) {
        this.page = page;
        this.per_page = per_page;
        this.total = total;
        this.total_pages = total_pages;
        this.simpleDataList = simpleDataList;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPer_page() {
        return per_page;
    }

    public void setPer_page(int per_page) {
        this.per_page = per_page;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }

    public List<SimpleData> getSimpleDataList() {
        return simpleDataList;
    }

    public void setSimpleDataList(List<SimpleData> simpleDataList) {
        this.simpleDataList = simpleDataList;
    }

    @Override
    public String toString() {
        return "page: " + getPage() + ", per_page: " + getPer_page() + ", total: "
                + getTotal() + ", total_pages: " + getTotal_pages() + ", simpleDataList: " + getSimpleDataList().toString();
    }
}
