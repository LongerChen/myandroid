package myandroid.sensor;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

public class MyGPS implements LocationListener {
	Context context;
	LocationManager locationManager;
	OnLocateListener onLocateListener;
	Location location = null;

	public MyGPS(Context context) {
		this.context = context;
		locationManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE); // 取得系統定位服務
	}

	/**
	 * 開始定位
	 */
	public void startLocate(OnLocateListener onCompeleteListener) {
		this.onLocateListener = onCompeleteListener;
		if (checkGPS()) {

			for (String provider : locationManager.getProviders(true))
				locationManager.requestLocationUpdates(provider, 0, 0, this);
		} else {
			Toast.makeText(context, "請開啟定位服務", Toast.LENGTH_LONG).show();
			context.startActivity(new Intent(
					Settings.ACTION_LOCATION_SOURCE_SETTINGS)); // 開啟設定頁面
		}

	}

	/**
	 * 停止定位
	 */
	public void stopLocate() {
		locationManager.removeUpdates(this);
	}

	/**
	 * 檢查以下Provider可用性： <li>LocationManager.GPS_PROVIDER - 精確性佳(5-50米)，費時<li>
	 * LocationManager.NETWORK_PROVIDER - 精確性弱(500-1000米)，快速<li>
	 * LocationManager.PASSIVE_PROVIDER - 很少用
	 * 
	 * @return boolean
	 */
	public boolean checkGPS() {

		return locationManager.getProviders(true).size() >= 2;
	}

	/**
	 * 取得最佳Provider
	 * 
	 * @return String Provider
	 */
	public String getBestProvider() {

		Criteria criteria = new Criteria(); // 資訊提供者選取標準
		String bestProvider = LocationManager.GPS_PROVIDER; // 最佳資訊提供者
		bestProvider = locationManager.getBestProvider(criteria, true); // 選擇精準度最高的提供者
		return bestProvider;
	}

	/**
	 * 取得最後定位點
	 * 
	 * @return Location
	 */
	public Location getLastKnownLocation() {
		List<String> providers = locationManager.getProviders(true);
		for (String provider : providers) {
			Location tempLocation = locationManager
					.getLastKnownLocation(provider);
			if (location == null)
				location = tempLocation;
			else {
				if (tempLocation.getTime() > location.getTime())
					location = tempLocation;
				else if (tempLocation.getTime() == location.getTime())
					if (tempLocation.getAccuracy() > tempLocation.getAccuracy())
						location = tempLocation;
			}
		}
		return location;
	}

	/**
	 * 取得最後定位點
	 * 
	 * @param provider
	 *            <li>LocationManager.GPS_PROVIDER <li>
	 *            LocationManager.NETWORK_PROVIDER <li>
	 *            LocationManager.PASSIVE_PROVIDER
	 * @return Location
	 */
	public Location getLastKnownLocation(String provider) {
		return locationManager.getLastKnownLocation(provider);
	}

	/**
	 * 
	 * 比較精確度
	 * 
	 * @param l1
	 *            點位1
	 * @param l2
	 *            點位2
	 * @return Location
	 */
	public Location getBetterAccuracy(Location l1, Location l2) {
		return l1.getAccuracy() < l2.getAccuracy() ? l1 : l2;
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		Log.e("onStatusChanged", provider);
	}

	@Override
	public void onProviderEnabled(String provider) {
		Log.e("ProviderEnabled", provider);
	}

	@Override
	public void onProviderDisabled(String provider) {
		Log.e("ProviderDisabled", provider);
		if (onLocateListener != null)
			onLocateListener.onLocate(null);
	}

	@Override
	public void onLocationChanged(Location location) {
		if (this.location == null)
			this.location = location;
		else
			this.location = getBetterAccuracy(this.location, location);
		if (onLocateListener != null)
			onLocateListener.onLocate(this.location);
	}

	public interface OnLocateListener {
		public void onLocate(Location location);
	}

}
