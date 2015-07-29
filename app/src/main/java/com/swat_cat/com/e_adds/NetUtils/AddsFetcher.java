package com.swat_cat.com.e_adds.NetUtils;

import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.swat_cat.com.e_adds.Entities.Category;
import com.swat_cat.com.e_adds.Entities.E_add;
import com.swat_cat.com.e_adds.Utils.Constants;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;

import java.util.ArrayList;

/**
 * Created by Dell on 31.05.2015.
 */
public class AddsFetcher {
    private static final String TAG = AddsFetcher.class.getName();
    private Header[]headers={
            new BasicHeader("User-Agent", Constants.USER_AGENT),
            new BasicHeader("X-Authorization","EAD-TOKENS apiKey=\"c89b0a895987a710db6ee5c86fc7da24d97e55dd\"")
    };
    //private LruCache<String,Category> categoryLruCache = new LruCache<>(1024*1024*2);
    private GsonBuilder gsonBuilder = new GsonBuilder();

    private String fetchDataWithMethod(String method){
        String url =Uri.parse(Constants.URL_BASE).buildUpon().
                appendEncodedPath(method).toString();
        return fetchDataWithURL(url);
    }

    public String fetchDataWithURL(String url){
        return new HttpRetriever().retrieve(url,headers,null);
    }

    public String fetchAll(){
        return fetchDataWithMethod(Constants.ALL_ADDS_METHOD);
    }

    public ArrayList<Category> fetchCategories(){
        String s = fetchDataWithMethod(Constants.RETRIEVE_CATEGORIES_METHOD);
        Gson gson = gsonBuilder.create();
        JsonParser parser = new JsonParser();
        JsonArray jArray = parser.parse(fetchDataWithMethod(Constants.RETRIEVE_CATEGORIES_METHOD)).getAsJsonArray();
        ArrayList<Category> categories = new ArrayList<Category>();

        for(JsonElement obj : jArray )
        {
            Category category = gson.fromJson( obj , Category.class);
            categories.add(category);
        }
        return categories;
    }

    public ArrayList<E_add> fetchAdditivesByCategory(int categoryId){
        String url =Uri.parse(Constants.URL_BASE).buildUpon().
                appendEncodedPath(Constants.RETRIEVE_E_ADD_BY_CODE_METHOD)
                .appendQueryParameter("category", String.valueOf(categoryId))
                .appendQueryParameter("sort","code").toString();
        String jsonString  = new HttpRetriever().retrieve(url,headers,null);
        Log.w(TAG,jsonString);
        Gson gson = new GsonBuilder().create();
        ArrayList<E_add> additives = new ArrayList<>();
        JsonParser parser = new JsonParser();
        JsonArray jsonArray = parser.parse(jsonString).getAsJsonArray();
        for(JsonElement obj: jsonArray){
            E_add additive = gson.fromJson(obj,E_add.class);
            /*additive = fetchE_addByCode(additive.getCode());*/
            additives.add(additive);
        }
        return additives;
    }

    public Category fetchCategoryByCode(int code){
        //fetchDataWithMethod(Constants.RETRIEVE_CATEGORIES_METHOD + "/" + code);
        Gson gson = gsonBuilder.create();
        Category category= (Category)gson.fromJson(fetchDataWithMethod(Constants.RETRIEVE_CATEGORIES_METHOD + "/" + code),Category.class);
        JsonObject jsonObject = new JsonParser().parse(fetchDataWithURL(category.getUrl())).getAsJsonObject();
        JsonElement element = jsonObject.get("description");
        category.setDescription(element.getAsString());
        return category;
    }

    public E_add fetchE_addByCode(String code){
        String url = fetchDataWithMethod(Constants.RETRIEVE_E_ADD_BY_CODE_METHOD + "/" + code);
        Gson gson = gsonBuilder.create();
        return (E_add)gson.fromJson(url,E_add.class);
    }

}
