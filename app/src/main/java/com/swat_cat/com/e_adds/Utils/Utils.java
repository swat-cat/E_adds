package com.swat_cat.com.e_adds.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Dell on 31.05.2015.
 */
public class Utils {
    public static void closeStreamQuietly(InputStream inputStream) {
        try {
            if (inputStream != null) {
                inputStream.close();
            }
        } catch (IOException e) {
            // ignore exception
        }
    }

    public static ArrayList<String> getAdditives(String s){
        ArrayList<String> result = new ArrayList<>();
        Pattern pattern = Pattern.compile(Constants.E_ADD_TESS_REGEXP);
        Matcher matcher = pattern.matcher(s);
        while (matcher.find()){
            result.add(matcher.group());
        }
        return result;
    }

    public static String stripNonDigits(
            final CharSequence input /* inspired by seh's comment */){
        final StringBuilder sb = new StringBuilder(
                input.length() /* also inspired by seh's comment */);
        for(int i = 0; i < input.length(); i++){
            final char c = input.charAt(i);
            if(c > 47 && c < 58){
                sb.append(c);
            }
        }
        return sb.toString();
    }
}