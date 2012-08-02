package com.mlst.imageprocessing.rs;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.example.android.rs.hellocompute.ScriptC_sepia;

public class SepiaFragment extends Fragment {
	private Bitmap mBitmapIn;
	private Bitmap mBitmapOut;
	private ImageView out;
	private RenderScript mRS;
	private Allocation mInAllocation;
	private Allocation mOutAllocation;
	private ScriptC_sepia mScript;
	private SeekBar depth;
	private SeekBar intensity; 

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.sepia_layout, null);
		
		depth = (SeekBar) view.findViewById(R.id.depth);
		
		depth.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				
				mScript.set_depth((float)(progress / 255.0));
				mScript.set_intensity((float)(intensity.getProgress() / 255.0));
				
				apply();
				out.invalidate();
			}
		});
		
		intensity = (SeekBar) view.findViewById(R.id.intensity);
		
		intensity.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				
				mScript.set_depth((float)(depth.getProgress() / 255.0));
				mScript.set_intensity((float) (progress / 255.0));
				apply();
				out.invalidate();
				
			}
		});
		
		mBitmapIn = loadBitmap(R.drawable.face2);
		mBitmapOut = loadBitmap(R.drawable.face2);

		ImageView in = (ImageView) view.findViewById(R.id.displayin);
		in.setImageBitmap(mBitmapIn);

		out = (ImageView) view.findViewById(R.id.displayout);
		out.setImageBitmap(mBitmapOut);

		createScript();
		
		depth.setProgress(20);
		intensity.setProgress(50);
		
		apply();
		out.invalidate();

		return view;
	}

	protected void apply() {
		mScript.invoke_filter();
		mOutAllocation.copyTo(mBitmapOut);
	}

	private void createScript() {
		mRS = RenderScript.create(getActivity());

		mInAllocation = Allocation.createFromBitmap(mRS, mBitmapIn,
				Allocation.MipmapControl.MIPMAP_NONE, Allocation.USAGE_SCRIPT);
		mOutAllocation = Allocation.createTyped(mRS, mInAllocation.getType());

		mScript = new ScriptC_sepia(mRS, getResources(), R.raw.sepia);

		mScript.set_gIn(mInAllocation);
		mScript.set_gOut(mOutAllocation);
		mScript.set_gScript(mScript); 
		
	}

	private Bitmap loadBitmap(int resource) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		return BitmapFactory.decodeResource(getResources(), resource, options);
	}
}
