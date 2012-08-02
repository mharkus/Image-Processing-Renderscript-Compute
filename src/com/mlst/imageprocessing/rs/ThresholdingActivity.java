package com.mlst.imageprocessing.rs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

public class ThresholdingActivity extends FragmentActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.threshold_activity);

		getActionBar().setDisplayHomeAsUpEnabled(true);

		if (savedInstanceState == null) { 
			Fragment fragment = new ThresholdingFragment();
			getSupportFragmentManager().beginTransaction().add(R.id.detail_container, fragment).commit();
		}
	}
}
