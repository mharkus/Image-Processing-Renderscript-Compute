package com.example.android.rs.hellocompute;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class ExampleListActivity extends FragmentActivity
        implements ExampleListFragment.Callbacks {

    private boolean mTwoPane;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example_list);

        if (findViewById(R.id.example_detail_container) != null) {
            mTwoPane = true;
            ((ExampleListFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.example_list))
                    .setActivateOnItemClick(true);
        }
    }

    @Override
    public void onItemSelected(String id) {
        if (mTwoPane) {
        	
        	if("1".equals(id)){
        		ColorizerFragment fragment = new ColorizerFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.example_detail_container, fragment)
                        .commit();

        	}else if("2".equals(id)){
        		MonochromeFragment fragment = new MonochromeFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.example_detail_container, fragment)
                        .commit();
        	}else if("3".equals(id)){
        		FlipFragment fragment = new FlipFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.example_detail_container, fragment)
                        .commit();
        	}else if("4".equals(id)){
        		SepiaFragment fragment = new SepiaFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.example_detail_container, fragment)
                        .commit();
        	}
        } else {
        	if("1".equals(id)){
        		Intent detailIntent = new Intent(this, ColorizerActivity.class);
                startActivity(detailIntent);
        	}else if("2".equals(id)){
        		Intent detailIntent = new Intent(this, MonochromeActivity.class);
                startActivity(detailIntent);
        	}else if("3".equals(id)){
        		Intent detailIntent = new Intent(this, FlipActivity.class);
                startActivity(detailIntent);
        	}else if("4".equals(id)){
        		Intent detailIntent = new Intent(this, SepiaActivity.class);
                startActivity(detailIntent);
        	}
            
        }
    }
}
