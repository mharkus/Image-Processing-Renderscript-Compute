package com.example.android.rs.hellocompute;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

public class SepiaActivity extends FragmentActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sepia_activity);

		getActionBar().setDisplayHomeAsUpEnabled(true);

		if (savedInstanceState == null) { 
			Fragment fragment = new SepiaFragment();
			getSupportFragmentManager().beginTransaction().add(R.id.detail_container, fragment).commit();
		}
	}
}
