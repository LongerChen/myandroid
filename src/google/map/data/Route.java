package google.map.data;

import java.util.List;

public class Route {
	Bounds bounds;
	String copyrights;
	List<Leg> legs;
	Polyline overview_polyline;
	String summary;
	List<String> warnings;
	List<String> waypoint_order;

	public Bounds getBounds() {
		return bounds;
	}

	public void setBounds(Bounds bounds) {
		this.bounds = bounds;
	}

	public String getCopyrights() {
		return copyrights;
	}

	public void setCopyrights(String copyrights) {
		this.copyrights = copyrights;
	}

	public List<Leg> getLegs() {
		return legs;
	}

	public void setLegs(List<Leg> legs) {
		this.legs = legs;
	}

	public Polyline getOverview_polyline() {
		return overview_polyline;
	}

	public void setOverview_polyline(Polyline overview_polyline) {
		this.overview_polyline = overview_polyline;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public List<String> getWarnings() {
		return warnings;
	}

	public void setWarnings(List<String> warnings) {
		this.warnings = warnings;
	}

	public List<String> getWaypoint_order() {
		return waypoint_order;
	}

	public void setWaypoint_order(List<String> waypoint_order) {
		this.waypoint_order = waypoint_order;
	}

}
