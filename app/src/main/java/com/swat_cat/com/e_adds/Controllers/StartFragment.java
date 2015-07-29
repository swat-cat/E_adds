package com.swat_cat.com.e_adds.Controllers;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.melnykov.fab.FloatingActionButton;
import com.swat_cat.com.e_adds.Entities.Category;
import com.swat_cat.com.e_adds.NetUtils.AddsFetcher;
import com.swat_cat.com.e_adds.Utils.Constants;
import com.swat_cat.com.e_adds.Utils.FileUtils;
import com.swat_cat.com.e_adds.Utils.Validation;
import com.swat_cat.com.e_adds.R;
import java.util.ArrayList;


/**
 * Created by Dell on 31.05.2015.
 */
public class StartFragment extends Fragment {

    private static final String TAG = StartFragment.class.getName();
    protected static final String CATEGORY_EXTRA = TAG+"category_extra";
    private ArrayList<Category> categories;
    private FileUtils fileUtils;

    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private ProgressDialog progressDialog;
    private CategoryAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
        fileUtils = new FileUtils(Constants.CATEGORIES_JSON_FILE,Constants.DESCR_CACHE_FILE,getActivity());
        if (fileUtils.fileExistance(Constants.CATEGORIES_JSON_FILE)||fileUtils.fileExistance(Constants.DESCR_CACHE_FILE)) {
            categories = fileUtils.loadCategories();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = (View)inflater.inflate(R.layout.start_fragment,container,false);
        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        if (categories==null) {
            new CategoriesAsyncFetcher().execute();
        } else {
            adapter = new CategoryAdapter(categories,getActivity());
            recyclerView.setAdapter(adapter);
            recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(),
                    new RecyclerItemClickListener.OnItemClickListener(){
                        @Override
                        public void onItemClick(View view, int position) {
                            Intent intent = new Intent(getActivity(),E_addListActivity.class);
                            intent.putExtra(CATEGORY_EXTRA, categories.get(position).getId());
                            startActivity(intent);
                        }
                    }));
        }
        fab = (FloatingActionButton)view.findViewById(R.id.fab);
        fab.attachToRecyclerView(recyclerView);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),TextScanActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu,inflater);
        inflater.inflate(R.menu.menu_main, menu);
        SearchManager sm =  (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchMenuItem = menu.findItem(R.id.menu_item_search);
        final SearchView searchViewAction = (SearchView) MenuItemCompat.getActionView(searchMenuItem);
        searchViewAction.setSearchableInfo(sm.getSearchableInfo(getActivity().getComponentName()));
        searchViewAction.setInputType(InputType.TYPE_CLASS_NUMBER);
        searchViewAction.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!Validation.isCodeValid(searchViewAction,true)) {
                    return true;
                } else {
                    return false;
                }
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id){
            case R.id.action_settings:
                return true;
            case R.id.menu_item_search:
                getActivity().onSearchRequested();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showProgressDialog(){
        if (progressDialog==null) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle(R.string.loading);
            progressDialog.setIndeterminate(true);
        }
        progressDialog.show();
    }

    private void hideProgressDialog(){
        if(progressDialog.isShowing()){
            progressDialog.hide();
        }
    }

    public class CategoriesAsyncFetcher extends AsyncTask<Void,Void,ArrayList<Category>> {
        @Override
        protected ArrayList<Category> doInBackground(Void... params) {
            AddsFetcher fetcher = new AddsFetcher();
            categories = fetcher.fetchCategories();
            for(int i = 0 ; i<categories.size(); i++){
                int id = categories.get(i).getId();
                categories.set(i,fetcher.fetchCategoryByCode(id));
            }
            fileUtils.saveCategories(categories);
            return categories;
        }

        @Override
        protected void onPostExecute(ArrayList<Category> categories) {
            adapter = new CategoryAdapter(categories,getActivity());
            recyclerView.setAdapter(adapter);
            hideProgressDialog();
        }

        @Override
        protected void onPreExecute() {
            showProgressDialog();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(progressDialog!=null){
            progressDialog.dismiss();
        }
    }
}
