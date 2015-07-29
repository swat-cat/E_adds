package com.swat_cat.com.e_adds.Controllers;

import android.support.v4.app.Fragment;

/**
 * Created by Dell on 21.06.2015.
 */
public class E_addListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        if(getIntent().hasExtra(StartFragment.CATEGORY_EXTRA)){
            return E_addListFragment.newInstance(getIntent().getIntExtra(StartFragment.CATEGORY_EXTRA,-1));
        }
        else if(getIntent().hasExtra(TextScanActivity.E_ADD_EXTRA)){
            return E_addListFragment.newInstance(getIntent().getStringArrayExtra(TextScanActivity.E_ADD_EXTRA));
        }
        else return null;
    }
}
