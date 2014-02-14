package google.youtube.data;

public class Thumbnails {
	Thumbnail _default;
	Thumbnail medium;
	Thumbnail high;

	public Thumbnail getDefault() {
		return _default;
	}

	public void setDefault(Thumbnail _default) {
		this._default = _default;
	}

	public Thumbnail getMedium() {
		return medium;
	}

	public void setMedium(Thumbnail medium) {
		this.medium = medium;
	}

	public Thumbnail getHigh() {
		return high;
	}

	public void setHigh(Thumbnail high) {
		this.high = high;
	}

}
