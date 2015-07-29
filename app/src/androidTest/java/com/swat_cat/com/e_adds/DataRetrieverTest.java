package com.swat_cat.com.e_adds;

import com.swat_cat.com.e_adds.NetUtils.AddsFetcher;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Dell on 31.05.2015.
 */
public class DataRetrieverTest {
    private static final String TAG = DataRetrieverTest.class.getName();
    @Test
    public void testRetrieveAll(){
        String json = new AddsFetcher().fetchAll();
        Assert.assertNotNull(json);
    }
}
