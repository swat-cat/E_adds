package com.swat_cat.com.e_adds.Controllers;

import android.support.v4.app.Fragment;

/**
 * Created by Dell on 12.06.2015.
 */
public class E_addActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        String code = getIntent().getStringExtra(StartActivity.SEARCHED_CODE);
        return E_addFragment.newInstance(code);
    }

}
