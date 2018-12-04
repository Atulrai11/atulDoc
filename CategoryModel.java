package com.codemaven.xadapp.Model;

public class CategoryModel {
    private Long catId;
    private String catName;

    public CategoryModel(Long catId, String catName) {
        this.catId = catId;
        this.catName = catName;
    }

    public Long getCatId() {
        return catId;
    }

    public void setCatId(Long catId) {
        this.catId = catId;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    @Override
    public String toString() {
        return catName;
    }
}
