package com.blame.googleearthnavigation.scrapers;

import com.blame.googleearthnavigation.bean.Coordinates;

public class MapCoordinatesDotNetScraper extends HTTPScraper {

	protected static final String URL = "https://www.mapcoordinates.net/admin/component/edit/Vpc_MapCoordinates_Advanced_GoogleMapCoords_Component/Component/json-get-elevation";
	
	public MapCoordinatesDotNetScraper() {
		super();
		
		this.url = URL;
		this.method = "POST";
	}

	public int getElevation(Coordinates coordinates) {
		
		this.parameters = "latitude=" + coordinates.getLatitude() + "&longitude=" + coordinates.getLongitude();
		def response = getJSONObject();
		return response.elevation;
	}
}
