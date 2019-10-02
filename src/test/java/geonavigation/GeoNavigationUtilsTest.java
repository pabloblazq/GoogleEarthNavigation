package geonavigation;

import org.junit.Test;

import com.blame.googleearthnavigation.bean.Coordinates;
import com.blame.googleearthnavigation.geonavigation.GeoNavigationUtils;

public class GeoNavigationUtilsTest {

	@Test
	public void testGetDistance() {
		
		Coordinates coordinates1 = new Coordinates(40.46145, -3.64576);
		Coordinates coordinates2 = new Coordinates(40.47492, -3.6454);
		
		System.out.println(GeoNavigationUtils.getDistance(coordinates1, coordinates2));
	}
}
