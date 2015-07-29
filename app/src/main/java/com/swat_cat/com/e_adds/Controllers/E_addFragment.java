package com.swat_cat.com.e_adds.Controllers;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.swat_cat.com.e_adds.Entities.E_add;
import com.swat_cat.com.e_adds.NetUtils.AddsFetcher;
import com.swat_cat.com.e_adds.R;

/**
 * Created by Dell on 12.06.2015.
 */
public class E_addFragment extends Fragment {

    private static final String TAG = E_addFragment.class.getName();
    private static final String E_ADD_EXTRA = TAG+" e_add_extra";

    private TextView codeAndName;
    private ImageView statusIcon;
    private TextView statusTextView;
    private TextView functionTextView;
    private TextView warningsTextView;
    private ProgressDialog progressDialog;

    private String code;
    private E_add additive;

    public static Fragment newInstance(String code){
        Bundle args = new Bundle();
        args.putString(E_ADD_EXTRA, code);
        E_addFragment fragment = new E_addFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
        //parseAdditive(getArguments().getString(E_ADD_EXTRA));
        code = getArguments().getString(E_ADD_EXTRA);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = (View)inflater.inflate(R.layout.additive_fragment,container,false);
        codeAndName = (TextView)view.findViewById(R.id.code_name_text);
        statusIcon = (ImageView)view.findViewById(R.id.statusIcon);
        statusTextView = (TextView)view.findViewById(R.id.statusTextView);
        functionTextView = (TextView)view.findViewById(R.id.add_function);
        warningsTextView = (TextView)view.findViewById(R.id.add_warnings);
        new AdditiveAsyncFetcher().execute(code);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setLogo(R.mipmap.ic_launcher);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (NavUtils.getParentActivityName(getActivity()) != null) {
                    NavUtils.navigateUpFromSameTask(getActivity());
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void parseAdditive(String json){
        Gson gson = new GsonBuilder().create();
        additive = gson.fromJson(json,E_add.class);
    }

    private void setDescriptionViews(){
        codeAndName.setText(additive.getCode()+" - "+additive.getName());
        if (additive.getFunction()!=null) {
            functionTextView.setText(additive.getFunction());
        }
        if(additive.getWarnings()!=null){
            warningsTextView.setText(additive.getWarnings());
        }
    }

    private void setStatusViews(){

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

    public class AdditiveAsyncFetcher extends AsyncTask<String,Void,E_add> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected E_add doInBackground(String... params) {
            AddsFetcher fetcher = new AddsFetcher();
            additive = fetcher.fetchE_addByCode(params[0]);
            return additive;
        }


        @Override
        protected void onPostExecute(E_add add) {
            if (additive!=null) {
                setStatusViews();
                setDescriptionViews();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.oops)
                        .setMessage(R.string.empty_code)
                        .setIcon(R.mipmap.ic_launcher)
                        .setCancelable(false)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(getActivity(),StartActivity.class);
                                startActivity(intent);
                            }
                        })
                        .create().show();

            }
            hideProgressDialog();
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
