package myandroid.security;

import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

public class Base64Tool {

	public String bitmapToBase64(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.JPEG, 50, baos); // bm is the bitmap
															// object
		byte[] b = baos.toByteArray();
		String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
		return encodedImage;
	}

	public Bitmap base64ToBitmap(String str) {
		byte[] data = Base64.decode(str, Base64.DEFAULT);
		Bitmap bm = BitmapFactory.decodeByteArray(data, 0, data.length);
		return bm;
	}
}
