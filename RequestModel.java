package com.codemaven.xadapp.Model;

import java.util.List;

public class RequestModel {
    private String id;
    private String catagory;
    private String center;
    private String device_name;
    private String patient_condition;
    private String description;
    private String status;
    private String assigned_device_id;
    private String added_at;
    private String added_by;//user_id
    private String category_name;
    private String donation_center_name;
    private String photo;
    private List<Photo> photoList;

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPhoto() {
        return photo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCatagory() {
        return catagory;
    }

    public void setCatagory(String catagory) {
        this.catagory = catagory;
    }

    public String getCenter() {
        return center;
    }

    public void setCenter(String center) {
        this.center = center;
    }

    public String getDevice_name() {
        return device_name;
    }

    public void setDevice_name(String device_name) {
        this.device_name = device_name;
    }

    public String getPatient_condition() {
        return patient_condition;
    }

    public void setPatient_condition(String patient_condition) {
        this.patient_condition = patient_condition;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAssigned_device_id() {
        return assigned_device_id;
    }

    public void setAssigned_device_id(String assigned_device_id) {
        this.assigned_device_id = assigned_device_id;
    }

    public String getAdded_at() {
        return added_at;
    }

    public void setAdded_at(String added_at) {
        this.added_at = added_at;
    }

    public String getAdded_by() {
        return added_by;
    }

    public void setAdded_by(String added_by) {
        this.added_by = added_by;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getDonation_center_name() {
        return donation_center_name;
    }

    public void setDonation_center_name(String donation_center_name) {
        this.donation_center_name = donation_center_name;
    }

    public List<Photo> getPhotoList() {
        return photoList;
    }

    public void setPhotoList(List<Photo> photoList) {
        this.photoList = photoList;
    }

    public static class Photo{
        private String photo;

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }
    }

}
