package com.mlst.imageprocessing.rs;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.android.rs.hellocompute.ScriptC_colorizer;

public class ColorizerFragment extends Fragment {
	private Bitmap mBitmapIn;
	private Bitmap mBitmapOut;
	private ImageView out;
	private RenderScript mRS;
	private Allocation mInAllocation;
	private Allocation mOutAllocation;
	private ScriptC_colorizer mScript; 
	private int colorSelection;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.colorizer_layout, null);
		
		Button surprise = (Button)view.findViewById(R.id.surpriseButton);
		surprise.setOnClickListener(new OnClickListener() {
			
			

			@Override
			public void onClick(View v) {
				mScript.set_rand(colorSelection++);
				
				if(colorSelection == 3){
					colorSelection = 0;
				}
				suprise();
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

		return view;
	}

	protected void suprise() {
		mScript.invoke_filter();
		mOutAllocation.copyTo(mBitmapOut);
	}

	private void createScript() {
		mRS = RenderScript.create(getActivity());

		mInAllocation = Allocation.createFromBitmap(mRS, mBitmapIn,
				Allocation.MipmapControl.MIPMAP_NONE, Allocation.USAGE_SCRIPT);
		mOutAllocation = Allocation.createTyped(mRS, mInAllocation.getType());

		mScript = new ScriptC_colorizer(mRS, getResources(), R.raw.colorizer);

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
