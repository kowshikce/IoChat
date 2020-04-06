package com.example.iochat.RestClientService.BaseResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import retrofit2.http.GET;

public class BaseResponse {
    @SerializedName("message")
    @Expose
    public String message;

    public BaseResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
