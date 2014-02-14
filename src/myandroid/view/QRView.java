package myandroid.view;

import java.io.IOException;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.LuminanceSource;
import com.google.zxing.NotFoundException;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

public class QRView extends SurfaceView implements SurfaceHolder.Callback {
	SurfaceHolder holder;
	Camera camera = null;
	public int previewWidth = 0;
	public int previewHeight = 0;
	Context context;
	QRCodeReader reader;
	

	public QRView(Context context) {
		super(context);
		this.context = context;
		holder = getHolder();
		holder.addCallback(this);
		reader = new QRCodeReader();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		try {
			openCamera();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		closeCamera();

	}

	/**
	 * 開啟相機
	 * 
	 * @throws IOException
	 */
	public void openCamera() throws IOException {
		if (camera == null) {
			camera = Camera.open();
			camera.setPreviewDisplay(holder);
			camera.setDisplayOrientation(90);
			
			Camera.Parameters cp = camera.getParameters();
			cp.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
			Size s = cp.getPictureSize();
			getLayoutParams().height = (int) (((float)s.height / (float)s.width) * context.getResources().getDisplayMetrics().heightPixels);
			
			previewHeight = cp.getPreviewSize().height;
			previewWidth = cp.getPreviewSize().width;
			camera.setPreviewCallback(new PreviewCallback() {

				@Override
				public void onPreviewFrame(byte[] imgData, Camera arg1) {
					LuminanceSource source = new PlanarYUVLuminanceSource(
							imgData, previewWidth, previewHeight,0,0,
							previewWidth, previewHeight, false);
					BinaryBitmap hybridBitmap = new BinaryBitmap(
							new HybridBinarizer(source));
					try {
						Result r = reader.decode(hybridBitmap);
						closeCamera();
						Intent intent = new Intent();
						intent.putExtra("data", r.toString());
						((Activity) context).setResult(0, intent);
						((Activity) context).finish();
					} catch (NotFoundException e) {

					} catch (ChecksumException e) {

					} catch (FormatException e) {

					}

				}
			});
			setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					camera.autoFocus(null);
					
				}
			});
			camera.startPreview();
			camera.autoFocus(null);
		}
	}

	/**
	 * 關閉相機
	 */
	public void closeCamera() {
		if (camera != null) {
			camera.stopPreview();
			camera.setPreviewCallback(null);
			camera.release();
			camera = null;
			setOnClickListener(null);
		}
	}
	
	public void setPreviewCallBack(PreviewCallback callback){
		camera.setPreviewCallback(callback);
	}
	
}
