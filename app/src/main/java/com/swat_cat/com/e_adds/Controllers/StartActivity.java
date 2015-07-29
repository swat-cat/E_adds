package com.swat_cat.com.e_adds.Controllers;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;

import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.swat_cat.com.e_adds.Entities.E_add;
import com.swat_cat.com.e_adds.NetUtils.AddsFetcher;
import com.swat_cat.com.e_adds.R;

public class StartActivity extends SingleFragmentActivity {
    private static final String TAG = StartActivity.class.getName();
    protected static final String SEARCHED_CODE = StartActivity.class.getName()+"queryCode";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected Fragment createFragment() {
        return new StartFragment();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_fragment;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            int query = Integer.parseInt(intent.getStringExtra(SearchManager.QUERY));
            //PreferenceManager.getDefaultSharedPreferences(this).edit().putInt(SEARCHED_CODE,query);
            Intent i  = new Intent(this,E_addActivity.class);
            i.putExtra(SEARCHED_CODE,String.valueOf(query));
            startActivity(i);
            Log.i(TAG, "Received a new search query: " + query);
        }
    }
}
