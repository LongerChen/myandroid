package google.map.directions;

import google.map.data.Route;

import java.util.List;

public class Response {
	List<Route> routes;
	String status;

	public List<Route> getRoutes() {
		return routes;
	}

	public void setRoutes(List<Route> routes) {
		this.routes = routes;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
