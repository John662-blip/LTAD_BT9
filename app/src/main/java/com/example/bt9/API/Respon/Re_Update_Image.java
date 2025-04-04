package com.example.bt9.API.Respon;

import java.util.List;

public class Re_Update_Image {
    private boolean success;
    private String message;
    private List<Result> result;

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public List<Result> getResult() {
        return result;
    }

    public static class Result {
        private String id;
        private String username;
        private String fname;
        private String email;
        private String gender;
        private String images;

        public String getId() {
            return id;
        }

        public String getUsername() {
            return username;
        }

        public String getFname() {
            return fname;
        }

        public String getEmail() {
            return email;
        }

        public String getGender() {
            return gender;
        }

        public String getImages() {
            return images;
        }
    }
}
