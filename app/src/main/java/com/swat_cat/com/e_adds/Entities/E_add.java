package com.swat_cat.com.e_adds.Entities;

import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Dell on 31.05.2015.
 */
public class E_add {
    @SerializedName("id")
    private Integer id;
    @SerializedName("code")
    private String code;
    @SerializedName("last_update")
    private String last_update;
    @SerializedName("name")
    private String name;
    @SerializedName("status")
    private String status;
    @SerializedName("veg")
    private String veg;
    @SerializedName("function")
    private String function;
    @SerializedName("foods")
    private String foods;
    @SerializedName("notice")
    private String warnings;
    @SerializedName("info")
    private String info;
    @SerializedName("url")
    private String url;

    public static E_add parseAdditive(String json){
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(json,E_add.class);
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLast_update() {
        return last_update;
    }

    public void setLast_update(String last_update) {
        this.last_update = last_update;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVeg() {
        return veg;
    }

    public void setVeg(String veg) {
        this.veg = veg;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public String getFoods() {
        return foods;
    }

    public void setFoods(String foods) {
        this.foods = foods;
    }

    public String getWarnings() {
        return warnings;
    }

    public void setWarnings(String warnings) {
        this.warnings = warnings;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return name+'\n'+code+'\n'+status+'\n'+warnings;
    }
}
