package com.swat_cat.com.e_adds;

import com.swat_cat.com.e_adds.Utils.Constants;
import com.swat_cat.com.e_adds.Utils.Utils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Dell on 12.06.2015.
 */
public class PatternMatchingTest {

    public final ArrayList<String> expected = new ArrayList<>();

    @Test
    public void testPatternMatch(){
        Pattern pattern = Pattern.compile(Constants.E_ADD_TESS_REGEXP);
        Matcher matcher1 = pattern.matcher("E100");
        Matcher matcher2= pattern.matcher("E 101");
        Matcher matcher3= pattern.matcher("E135");
        Assert.assertTrue(matcher1.matches());
        Assert.assertTrue(matcher2.matches());
        Assert.assertTrue(matcher3.matches());
    }
    @Before
    public void setUpExpected(){
        expected.add("E100");
        expected.add("E544i");
        expected.add("E 123");
        expected.add("E111");
    }

    @Test
    public void testPaternMatch2(){
        String s = "qwrd E100SDSDskfdkkE544iSDDSdE 123@7dsfE111 ";
        ArrayList<String> strings = new ArrayList<>();
        strings = Utils.getAdditives(s);
        Assert.assertTrue(strings.size()==4);
        for(String substring:strings){
            Assert.assertTrue(expected.contains(substring));
        }
    }
}
