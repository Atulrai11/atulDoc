package com.codemaven.xadapp.Model;

public class CenterModel {
    private Long cenId;
    private String cenName;

    public CenterModel(Long cenId, String cenName) {
        this.cenId = cenId;
        this.cenName = cenName;
    }

    public Long getCenId() {
        return cenId;
    }

    public void setCenId(Long cenId) {
        this.cenId = cenId;
    }

    public String getCenName() {
        return cenName;
    }

    public void setCenName(String cenName) {
        this.cenName = cenName;
    }

    @Override
    public String toString() {
        return cenName;
    }
}
