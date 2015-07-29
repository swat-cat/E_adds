package com.swat_cat.com.e_adds.Utils;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.swat_cat.com.e_adds.Entities.Category;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Dell on 16.06.2015.
 */
public class FileUtils {

    private static final String TAG = FileUtils.class.getName();

    private String jsonFilename;
    private String descrCacheFilename;
    private Context context;

    public FileUtils(String jsonFilename,String descrCacheFilename, Context context) {
        this.jsonFilename = jsonFilename;
        this.descrCacheFilename=descrCacheFilename;
        this.context = context;
    }

    public void saveJSON(String json) throws IOException{
        Writer writer = null;
        try {
            OutputStream out = context.openFileOutput(jsonFilename,Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(out);
            writer.write(json);
        }
        finally {
            if (writer != null)
                writer.close();
        }
    }

    public void saveDescrCache(HashMap<Integer,String> cacheMap)throws IOException{
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        fos = context.openFileOutput(descrCacheFilename,Context.MODE_PRIVATE);
        oos = new ObjectOutputStream(fos);
        oos.writeObject(cacheMap);
        fos.close();
        oos.close();
    }

    public HashMap<Integer,String> loadDescrCache(){
        FileInputStream fis = null;
        HashMap<Integer,String> cacheMap = null;
        try {
            fis = context.openFileInput(descrCacheFilename);
            ObjectInputStream ois = new ObjectInputStream(fis);
            cacheMap = (HashMap) ois.readObject();
            ois.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "cache_file loading error");
        } catch (ClassNotFoundException e){
            Log.e(TAG,"Class not founf");
        }
        return cacheMap;
    }

    public String loadJSON() throws IOException{
        StringBuilder jsonString;
        BufferedReader reader = null;
        // Открытие и чтение файла в StringBuilder
        InputStream in = context.openFileInput(jsonFilename);
        reader = new BufferedReader(new InputStreamReader(in));
        jsonString = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            // Line breaks are omitted and irrelevant
            jsonString.append(line);
        }
        if(reader!=null){
            reader.close();
        }
        return jsonString.toString();
    }

    public void saveCategories(ArrayList<Category> categories){
        Gson gson = new GsonBuilder().create();
        String jsonString = gson.toJson(categories);
        HashMap<Integer,String> cacheMap = new HashMap<>();
        for(Category category:categories){
            cacheMap.put(category.getId(),category.getDescription());
        }
        try {
            saveJSON(jsonString);
            saveDescrCache(cacheMap);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG,"Error saving categories files");
        }
    }

    public ArrayList<Category> loadCategories(){
        ArrayList<Category> categories = null;
        HashMap<Integer,String> cacheMap = null;
        Type itemsListType = new TypeToken<ArrayList<Category>>() {}.getType();
        String jsonString;
        try {
            jsonString = loadJSON();
            categories = new GsonBuilder().create().fromJson(jsonString,itemsListType);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG,"Error loading json file");
        }
        cacheMap = loadDescrCache();
        for(int i = 0; i<categories.size();i++){
            categories.get(i).setDescription(cacheMap.get(categories.get(i).getId()));
        }
        return categories;
    }

    public boolean fileExistance(String fname){
        File file = context.getFileStreamPath(fname);
        return file.exists();
    }
}
