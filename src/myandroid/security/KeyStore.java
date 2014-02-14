package myandroid.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import myandroid.tools.Develop;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Base64;

public class KeyStore {
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
