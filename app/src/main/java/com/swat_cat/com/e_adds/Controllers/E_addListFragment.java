package com.swat_cat.com.e_adds.Controllers;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.swat_cat.com.e_adds.Entities.E_add;
import com.swat_cat.com.e_adds.NetUtils.AddsFetcher;
import com.swat_cat.com.e_adds.R;

import java.util.ArrayList;



/**
 * Created by Dell on 17.06.2015.
 */
public class E_addListFragment extends ListFragment {
    private static final String TAG = E_addListFragment.class.getName();
    private static final String ADDITIVES_LIST_CATEGORY_EXTRA = TAG+" additives_list_by_category";
    private static final String ADDITIVES_LIST_CODES_EXTRA = TAG+" additives_list_by_codes_array";

    private ListView listView;
    private ProgressDialog progressDialog;
    private ArrayList<E_add> additives;

    public static Fragment newInstance(int category) {
        Bundle args = new Bundle();
        args.putInt(ADDITIVES_LIST_CATEGORY_EXTRA, category);
        E_addListFragment fragment = new E_addListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static Fragment newInstance(String[]codes){
        Bundle args = new Bundle();
        args.putStringArray(ADDITIVES_LIST_CODES_EXTRA, codes);
        E_addListFragment fragment = new E_addListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        additives = new ArrayList<>();
        if(getArguments().containsKey(ADDITIVES_LIST_CATEGORY_EXTRA)){
            new AsyncAdditivesByCategoryFetcher().execute(getArguments().getInt(ADDITIVES_LIST_CATEGORY_EXTRA));
        }
        else if(getArguments().containsKey(ADDITIVES_LIST_CODES_EXTRA)){
            String[]codes = getArguments().getStringArray(ADDITIVES_LIST_CODES_EXTRA);
            if(codes.length==0){
                Log.e(TAG,"empty codes array");
            }
            new AsyncAdditivesByCodesFetcher().execute(codes);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.additives_list_fragment,container,false);
        listView = (ListView)view.findViewById(android.R.id.list);
        return view;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        E_add additive = additives.get(position);
        Intent intent = new Intent(getActivity(),E_addActivity.class);
        intent.putExtra(StartActivity.SEARCHED_CODE,additive.getCode());
        startActivity(intent);
    }

    private class AdditivesListAdapter extends ArrayAdapter<E_add>{

        private ImageView statusIcon;
        private TextView statusTextView;

        public AdditivesListAdapter(ArrayList<E_add> additives) {
            super(getActivity(),0, additives);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView==null){
                convertView = getActivity().getLayoutInflater().
                        inflate(R.layout.additives_list_item, null);
            }
            E_add additive = getItem(position);
            TextView listItem = (TextView)convertView.findViewById(R.id.list_item);
            listItem.setText(" "+additive.getCode()+": "+additive.getName());
            /*
            codeName.setText("E "+additive.getCode()+" - "+additive.getName());
            TextView functionTextView = (TextView)convertView.findViewById(R.id.add_function);
            if (additive.getFunction()!=null) {
                functionTextView.setText(additive.getFunction());
            }else functionTextView.setText(R.string.no_data);
            TextView warningsTextView = (TextView)convertView.findViewById(R.id.add_function);
            if(additive.getWarnings()!=null){
                warningsTextView.setText(additive.getWarnings());
            }else warningsTextView.setText(R.string.no_data);
            statusIcon = (ImageView)convertView.findViewById(R.id.statusIcon);
            statusTextView = (TextView)convertView.findViewById(R.id.statusTextView);
            setStatusViews(additive);
            */
            return convertView;
        }

    } private class DetailedAdditivesListAdapter extends ArrayAdapter<E_add>{

        private ImageView statusIcon;
        private TextView statusTextView;

        public DetailedAdditivesListAdapter(ArrayList<E_add> additives) {
            super(getActivity(),0, additives);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView==null){
                convertView = getActivity().getLayoutInflater().
                        inflate(R.layout.detailed_additive_list_item, parent, false);
            }
            E_add additive = getItem(position);
            TextView codeName = (TextView)convertView.findViewById(R.id.code_name_text);
            codeName.setText(additive.getCode()+" - "+additive.getName());
            TextView functionTextView = (TextView)convertView.findViewById(R.id.add_function);
            if (additive.getFunction()!=null) {
                functionTextView.setText(additive.getFunction());
            }else functionTextView.setText(R.string.no_data);
            TextView warningsTextView = (TextView)convertView.findViewById(R.id.add_warnings);
            if(additive.getWarnings()!=null){
                warningsTextView.setText(additive.getWarnings());
            }else warningsTextView.setText(R.string.no_data);
            statusIcon = (ImageView)convertView.findViewById(R.id.statusIcon);
            statusTextView = (TextView)convertView.findViewById(R.id.statusTextView);
            setStatusViews(additive);
            return convertView;
        }

        private void setStatusViews(E_add additive){
            if(additive.getStatus()==null&&additive.getWarnings()==null){
                statusIcon.setImageResource(R.mipmap.attention);
                statusTextView.setText(getResources().getStringArray(R.array.statuses)[1]);
            }
            else if(additive.getWarnings()!=null){
                statusIcon.setImageResource(R.mipmap.delete_sign);
                statusTextView.setText(getResources().getStringArray(R.array.statuses)[2]);
            }
            else if(additive.getStatus().equalsIgnoreCase("safe")){
                statusIcon.setImageResource(R.mipmap.checkmark);
                statusTextView.setText(getResources().getStringArray(R.array.statuses)[0]);
            }
            else if(additive.getStatus().toLowerCase().contains("dangerous")
                    ||additive.getStatus().toLowerCase().contains("not permitted")
                    ||additive.getStatus().toLowerCase().contains("banned")){
                statusIcon.setImageResource(R.mipmap.poison);
                statusTextView.setText(getResources().getStringArray(R.array.statuses)[3]);
            }
            else{
                statusIcon.setImageResource(R.mipmap.attention);
                statusTextView.setText(getResources().getStringArray(R.array.statuses)[1]);
            }
        }
    }



    private void showProgressDialog(){
        if (progressDialog==null) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle(R.string.loading);
            progressDialog.setMessage(getResources().getString(R.string.loading_data));
            progressDialog.setIndeterminate(true);
        }
        progressDialog.show();
    }

    private void hideProgressDialog(){
        if(progressDialog.isShowing()){
            progressDialog.hide();
        }
    }

    private class AsyncAdditivesByCategoryFetcher extends AsyncTask<Integer,Void,ArrayList<E_add>>{
        @Override
        protected ArrayList<E_add> doInBackground(Integer... params) {
            AddsFetcher fetcher = new AddsFetcher();
            additives = fetcher.fetchAdditivesByCategory(params[0]);
            return additives;
        }

        @Override
        protected void onPostExecute(ArrayList<E_add> e_adds) {
            super.onPostExecute(e_adds);
            AdditivesListAdapter adapter = new AdditivesListAdapter(e_adds);
            listView.setAdapter(adapter);
            hideProgressDialog();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }
    }

    private class AsyncAdditivesByCodesFetcher extends AsyncTask<String[],Void,ArrayList<E_add>>{
        @Override
        protected ArrayList<E_add> doInBackground(String[]... params) {
            AddsFetcher fetcher = new AddsFetcher();
            for(String code:params[0]){
                E_add additive = fetcher.fetchE_addByCode(code);
                if(additive==null){
                    continue;
                }
                additives.add(additive);
            }
            return additives;
        }

        @Override
        protected void onPostExecute(ArrayList<E_add> e_adds) {
            super.onPostExecute(e_adds);
            DetailedAdditivesListAdapter adapter = new DetailedAdditivesListAdapter(e_adds);
            listView.setAdapter(adapter);
            hideProgressDialog();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
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
