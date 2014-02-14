package myandroid.security;

import java.io.ByteArrayOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import myandroid.tools.Develop;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

public class Security {

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

	public static void logKeyHash(Context context) {
		try {
			PackageInfo info = context.getPackageManager().getPackageInfo(
					context.getApplicationContext().getPackageName(),
					PackageManager.GET_SIGNATURES);
			for (Signature signature : info.signatures) {
				MessageDigest md = MessageDigest.getInstance("SHA");
				md.update(signature.toByteArray());
				Develop.e(
						new KeyStore(),
						"KeyHash:"
								+ Base64.encodeToString(md.digest(),
										Base64.DEFAULT));
			}
		} catch (NameNotFoundException e) {

		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
