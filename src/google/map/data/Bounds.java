package google.map.data;

public class Bounds {
	LatLng northeast;
	LatLng southwest;

	public LatLng getNortheast() {
		return northeast;
	}

	public void setNortheast(LatLng northeast) {
		this.northeast = northeast;
	}

	public LatLng getSouthwest() {
		return southwest;
	}

	public void setSouthwest(LatLng southwest) {
		this.southwest = southwest;
	}
}
