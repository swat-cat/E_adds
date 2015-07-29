package com.swat_cat.com.e_adds.Utils;

import android.support.v7.widget.SearchView;
import android.widget.EditText;
import android.widget.TextView;

import com.swat_cat.com.e_adds.Utils.Constants;

import java.util.regex.Pattern;

/**
 * Created by Dell on 14.06.2015.
 */
public class Validation {

    public static boolean isCodeValid(SearchView searchView,boolean required){
        return isValid(searchView,Constants.E_CODE_VALID_REGEXP,Constants.WRONG_CODE_MSG,required);
    }

    // return true if the input field is valid, based on the parameter passed
    public static boolean isValid(SearchView searchView, String regex, String errMsg, boolean required) {
        TextView searchText = null;
        String text = searchView.getQuery().toString().trim();
        // clearing the error, if it was previously set by some other values
        // text required and editText is blank, so return false
        //int searchEditTestId = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        EditText searchEditText = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchEditText.setError(null);
        if ( required && !hasText(searchView) ) return false;

        // pattern doesn't match so returning false
        if (required && !Pattern.matches(regex, text)) {
            searchEditText.setError(errMsg);
            return false;
        }
        return true;
    }

    // check the input field has any text or not
    // return true if it contains text otherwise false
    public static boolean hasText(SearchView searchView) {

        String text = searchView.getQuery().toString().trim();
        EditText searchEditText = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        // length 0 means there is no text
        if (text.length() == 0) {
            searchEditText.setError(Constants.REQUIRED_MSG);
            return false;
        }
        return true;
    }
}
