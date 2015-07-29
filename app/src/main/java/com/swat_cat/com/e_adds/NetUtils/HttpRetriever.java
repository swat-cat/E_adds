package com.swat_cat.com.e_adds.NetUtils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGetHC4;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.http.Header;
import org.apache.http.client.config.RequestConfig;
import android.support.annotation.NonNull;

import com.swat_cat.com.e_adds.Utils.Utils;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Dell on 31.05.2015.
 */
public class HttpRetriever {

        private CloseableHttpClient client = HttpClientBuilder.create().build();

        public String retrieve( @NonNull String url,Header[] headers,RequestConfig config) {
            HttpGetHC4 getRequest = new HttpGetHC4(url);
            if(config!=null)getRequest.setConfig(config);
            if(headers!=null)for(Header header:headers)getRequest.addHeader(header);
            try {

                HttpResponse getResponse = client.execute(getRequest);
                final int statusCode = getResponse.getStatusLine().getStatusCode();

                if (statusCode != HttpStatus.SC_OK) {
                    Log.w(getClass().getSimpleName(), "Error " + statusCode + " for URL " + url);
                    return null;
                }

                HttpEntity getResponseEntity = getResponse.getEntity();

                if (getResponseEntity != null) {
                    return EntityUtils.toString(getResponseEntity);
                }

            }
            catch (IOException e) {
                getRequest.abort();
                Log.w(getClass().getSimpleName(), "Error for URL " + url, e);
            }

            return null;

        }

        public InputStream retrieveStream(@NonNull String url,Header[] headers,RequestConfig config) {

            HttpGetHC4 getRequest = new HttpGetHC4(url);
            if(config!=null)getRequest.setConfig(config);
            if(headers!=null)for(Header header:headers)getRequest.addHeader(header);
            try {

                HttpResponse getResponse = client.execute(getRequest);
                final int statusCode = getResponse.getStatusLine().getStatusCode();

                if (statusCode != HttpStatus.SC_OK) {
                    Log.w(getClass().getSimpleName(), "Error " + statusCode + " for URL " + url);
                    return null;
                }

                HttpEntity getResponseEntity = getResponse.getEntity();
                return getResponseEntity.getContent();

            }
            catch (IOException e) {
                getRequest.abort();
                Log.w(getClass().getSimpleName(), "Error for URL " + url, e);
            }

            return null;

        }

        public Bitmap retrieveBitmap(String url,Header[] headers,RequestConfig config) throws Exception {

            InputStream inputStream = null;
            try {
                inputStream = this.retrieveStream(url,headers,config);
                final Bitmap bitmap = BitmapFactory.decodeStream(new FlushedInputStream(inputStream));
                return bitmap;
            }
            finally {
                Utils.closeStreamQuietly(inputStream);
            }

        }

}
