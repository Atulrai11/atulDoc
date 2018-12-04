package com.codemaven.xadapp.Model;

import java.util.List;

public class DonateModel {
    private String id;
    private String deviceId;
    private String user_id;
    private String category_id;
    private String donation_center_id;
    private String device_name;
    private String remarks;
    private String working_status;
    private String contact_to;
    private String mark_donate;
    private String pickup_request;
    private String description;
    private String is_assigned;
    private String added_at;
    private String category_name;
    private String donation_center_name;
    private String images;
    private List<Images>imagesList;

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getDonation_center_id() {
        return donation_center_id;
    }

    public void setDonation_center_id(String donation_center_id) {
        this.donation_center_id = donation_center_id;
    }

    public String getDevice_name() {
        return device_name;
    }

    public void setDevice_name(String device_name) {
        this.device_name = device_name;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getWorking_status() {
        return working_status;
    }

    public void setWorking_status(String working_status) {
        this.working_status = working_status;
    }

    public String getContact_to() {
        return contact_to;
    }

    public void setContact_to(String contact_to) {
        this.contact_to = contact_to;
    }

    public String getMark_donate() {
        return mark_donate;
    }

    public void setMark_donate(String mark_donate) {
        this.mark_donate = mark_donate;
    }

    public String getPickup_request() {
        return pickup_request;
    }

    public void setPickup_request(String pickup_request) {
        this.pickup_request = pickup_request;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIs_assigned() {
        return is_assigned;
    }

    public void setIs_assigned(String is_assigned) {
        this.is_assigned = is_assigned;
    }

    public String getAdded_at() {
        return added_at;
    }

    public void setAdded_at(String added_at) {
        this.added_at = added_at;
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

    public List<Images> getImagesList() {
        return imagesList;
    }

    public void setImagesList(List<Images> imagesList) {
        this.imagesList = imagesList;
    }

    public static class Images{
        private String image;

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }
    }

}
