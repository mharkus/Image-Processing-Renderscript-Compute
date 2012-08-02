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

import com.example.android.rs.hellocompute.ScriptC_flip;

public class FlipFragment extends Fragment {
	private Bitmap mBitmapIn;
	private ImageView out;
	private RenderScript mRS;
	private Allocation mInAllocation;
	private Allocation mOutAllocation;
	private ScriptC_flip mScript;
	private ImageView in; 

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.flip_layout, null);
		
		Button flipHorizontal = (Button)view.findViewById(R.id.flipHorizontal);
		flipHorizontal.setOnClickListener(new OnClickListener() {
			
			

			@Override
			public void onClick(View v) {
				mScript.set_direction(0);
				flip();
			}
		});
		
		Button flipVertical = (Button)view.findViewById(R.id.flipVertical);
		flipVertical.setOnClickListener(new OnClickListener() {
			
			

			@Override
			public void onClick(View v) {
				mScript.set_direction(1);
				flip();
			}
		});
		
		Button rotateLeft = (Button)view.findViewById(R.id.rotateLeft);
		rotateLeft.setOnClickListener(new OnClickListener() {
			
			

			@Override
			public void onClick(View v) {
				mScript.set_direction(2);
				flip();
			}
		});
		
		Button rotateRight = (Button)view.findViewById(R.id.rotateRight);
		rotateRight.setOnClickListener(new OnClickListener() {
			
			

			@Override
			public void onClick(View v) {
				mScript.set_direction(3);
				flip();
			}
		});
		
		mBitmapIn = loadBitmap(R.drawable.face2);

		in = (ImageView) view.findViewById(R.id.displayin);
		in.setImageBitmap(mBitmapIn);

		createScript();

		return view;
	}

	protected void flip() {
		mScript.invoke_filter();
		mOutAllocation.copyTo(mBitmapIn);
		
		mRS.destroy();
		mInAllocation.destroy();
		mOutAllocation.destroy();
		mScript.destroy();
		
		createScript();
		in.invalidate();
	}

	private void createScript() {
		mRS = RenderScript.create(getActivity());

		mInAllocation = Allocation.createFromBitmap(mRS, mBitmapIn,
				Allocation.MipmapControl.MIPMAP_NONE, Allocation.USAGE_SCRIPT);
		mOutAllocation = Allocation.createTyped(mRS, mInAllocation.getType());

		mScript = new ScriptC_flip(mRS, getResources(), R.raw.flip);
		mScript.set_width(mBitmapIn.getWidth());
		mScript.set_height(mBitmapIn.getHeight());
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
