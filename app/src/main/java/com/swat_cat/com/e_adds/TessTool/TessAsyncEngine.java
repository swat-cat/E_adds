package com.swat_cat.com.e_adds.TessTool;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.swat_cat.com.e_adds.Controllers.E_addListActivity;
import com.swat_cat.com.e_adds.Controllers.E_addListFragment;
import com.swat_cat.com.e_adds.Controllers.StartFragment;
import com.swat_cat.com.e_adds.Controllers.TextScanActivity;
import com.swat_cat.com.e_adds.Imaging.Tools;
import com.swat_cat.com.e_adds.Utils.Utils;

import java.util.ArrayList;

/**
 * Created by Fadi on 6/11/2014.
 */
public class TessAsyncEngine extends AsyncTask<Object, Void, String> {

    static final String TAG = "DBG_" + TessAsyncEngine.class.getName();

    private Bitmap bmp;

    private Activity context;


    @Override
    protected String doInBackground(Object... params) {

        try {

            if(params.length < 2) {
                Log.e(TAG, "Error passing parameter to execute - missing params");
                return null;
            }

            if(!(params[0] instanceof Activity) || !(params[1] instanceof Bitmap)) {
                Log.e(TAG, "Error passing parameter to execute(context, bitmap)");
                return null;
            }

            context = (Activity)params[0];

            bmp = (Bitmap)params[1];

            if(context == null || bmp == null) {
                Log.e(TAG, "Error passed null parameter to execute(context, bitmap)");
                return null;
            }

            int rotate = 0;

            if(params.length == 3 && params[2]!= null && params[2] instanceof Integer){
                rotate = (Integer) params[2];
            }

            if(rotate >= -180 && rotate <= 180 && rotate != 0)
            {
                bmp = Tools.preRotateBitmap(bmp, rotate);
                Log.d(TAG, "Rotated OCR bitmap " + rotate + " degrees");
            }

            SharedPreferences preferences = context.getPreferences(Context.MODE_PRIVATE);
            String lang = preferences.getString(TextScanActivity.STATE_SELECTED_NAVIGATION_ITEM,"eng");

            TessEngine tessEngine =  TessEngine.Generate(context);

            bmp = bmp.copy(Bitmap.Config.ARGB_8888, true);

            String result = tessEngine.detectText(bmp,lang);

            Log.d(TAG, result);

            return result;

        } catch (Exception ex) {
            Log.d(TAG, "Error: " + ex + "\n" + ex.getMessage());
        }

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if(s == null || bmp == null || context == null)
            return;
        //TODO
        ArrayList<String> additives = Utils.getAdditives(s);
        ArrayList<String> results = new ArrayList<>();
        Toast.makeText(context,additives.toString(), Toast.LENGTH_LONG).show();
        for(String result:additives){
            results.add(Utils.stripNonDigits(result));
        }
        Intent intent = new Intent(context, E_addListActivity.class);
        intent.putExtra(TextScanActivity.E_ADD_EXTRA,results.toArray(new String[results.size()]));
        context.startActivity(intent);
    }
}
