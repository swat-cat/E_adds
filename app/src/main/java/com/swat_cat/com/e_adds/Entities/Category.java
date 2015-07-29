package com.swat_cat.com.e_adds.Entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Dell on 31.05.2015.
 */
public class Category {
    @SerializedName("id")
    private Integer id;
    @SerializedName("name")
    private String name;
    @SerializedName("category")
    private Integer category;
    @SerializedName("last_update")
    private String last_update;
    @SerializedName("additives")
    private Integer addsAmount;
    @SerializedName("url")
    private String url;
    transient private String description;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public String getLast_update() {
        return last_update;
    }

    public void setLast_update(String last_update) {
        this.last_update = last_update;
    }

    public Integer getAddsAmount() {
        return addsAmount;
    }

    public void setAddsAmount(Integer addsAmount) {
        this.addsAmount = addsAmount;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String toString() {
        return name+'\n'+description;
    }
}
