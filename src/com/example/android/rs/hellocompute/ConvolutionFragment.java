package com.example.android.rs.hellocompute;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;

public class ConvolutionFragment extends Fragment {
	private Bitmap mBitmapIn;
	private Bitmap mBitmapOut;
	private ImageView out;
	private RenderScript mRS;
	private Allocation mInAllocation;
	private Allocation mOutAllocation;
	private ScriptC_convolution mScript;
	private SeekBar factor;
	private SeekBar bias;
	private Spinner filter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.convolution_layout, null);

		filter = (Spinner) view.findViewById(R.id.filters);
		filter.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				applyFilter(arg2);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});

		factor = (SeekBar) view.findViewById(R.id.factor);
		factor.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

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

				mScript.set_factor((float) (progress / 255.0f));
				mScript.set_bias((float) (bias.getProgress() / 255.0f));

				apply();
				out.invalidate();
			}
		});

		bias = (SeekBar) view.findViewById(R.id.bias);
		bias.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

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

				mScript.set_factor((float) (factor.getProgress() / 255.0f));
				mScript.set_bias((float) (progress / 255.0f));

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

		factor.setProgress(0);

		createScript();
		apply();
		out.invalidate();

		return view;
	}

	protected void applyFilter(int index) {

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
		mScript = new ScriptC_convolution(mRS, getResources(),
				R.raw.convolution);

		int rowWidth = mBitmapIn.getWidth();

		int num_rows = mBitmapIn.getHeight();
		int[] row_indices = new int[num_rows];
		for (int i = 0; i < num_rows; i++) {
			row_indices[i] = i * rowWidth;
		}
		Allocation row_indices_alloc = Allocation.createSized(mRS,
				Element.I32(mRS), num_rows, Allocation.USAGE_SCRIPT);
		row_indices_alloc.copyFrom(row_indices);
		
		mScript.bind_gInPixels(mInAllocation);
		mScript.bind_gOutPixels(mOutAllocation);
		mScript.set_mImageWidth(mBitmapIn.getWidth());
		mScript.set_mImageHeight(mBitmapIn.getHeight());

		mScript.set_gIn(row_indices_alloc);
		mScript.set_gOut(row_indices_alloc);
		
		
		mScript.set_gScript(mScript);

	}

	private Bitmap loadBitmap(int resource) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		return BitmapFactory.decodeResource(getResources(), resource, options);
	}
}
