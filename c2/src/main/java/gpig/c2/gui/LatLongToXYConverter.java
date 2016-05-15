package gpig.c2.gui;

import java.awt.Point;

import gpig.common.data.Location;

/**
 * Converts lat long coordinates to x and y for the gui drawings
 */
public class LatLongToXYConverter {
	
	//latlong to utm converter
	private CoordinateConversion coordinateConversion;
	
	//required to determine the scales
	private double imageWidth;
	private double imageHeight;
	private Location mapTopLeftCorner;
	private Location mapBottomRightCorner;
	
	//utm coordinates of both map corners
	private int[] topLeftUTM;
	private int[] bottomRightUTM;
	
	//number of pixels per unit of utm
	private double horizontalScale;
	private double verticalScale;
	
	
	public LatLongToXYConverter(int imageWidth, int imageHeight, Location mapTopLeftCorner,
			Location mapBottomRightCorner) {
		this.imageWidth = imageWidth;
		this.imageHeight = imageHeight;
		this.mapTopLeftCorner = mapTopLeftCorner;
		this.mapBottomRightCorner = mapBottomRightCorner;
		
		coordinateConversion = new CoordinateConversion();
		calculateScales();
		
	}
	
	/**
	 * Correspondence between UTM lenght and pixel lenght
	 */
	private void calculateScales()
	{		
		coordinateConversion = new CoordinateConversion();
		
		topLeftUTM = coordinateConversion.latLon2UTM(mapTopLeftCorner.latitude(), mapTopLeftCorner.longitude());
		bottomRightUTM = coordinateConversion.latLon2UTM(mapBottomRightCorner.latitude(), mapBottomRightCorner.longitude());	
	
		horizontalScale = (imageWidth / (bottomRightUTM[0] - topLeftUTM[0]));
		verticalScale = (imageHeight / (topLeftUTM[1] - bottomRightUTM[1]));
	
	}
	
	/**
	 * Given latlong calculates its x and y
	 */
	public Point convertLocationToPoint(Location location)
	{

		int[] locationUTM = coordinateConversion.latLon2UTM(location.latitude(),location.longitude());
		
		int x = (int)((locationUTM[0] - topLeftUTM[0])*horizontalScale);
		int y = (int)((topLeftUTM[1] - locationUTM[1])*verticalScale);
		
		Point point = new Point(x,y);
		
		return point;	
	}
	
	

}
