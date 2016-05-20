package gpig.c2.gui.unfolding;

import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PImage;

import java.util.UUID;

import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.AbstractMarker;

import gpig.common.data.ActorType;

/**
 * This marker displays an image at its location.
 */
public class ImageMarker extends AbstractMarker {

	private PImage img;
	private UUID actorId;
	private ActorType actorType;
	private int markerWidth;
	private int markerHeight;

	public ImageMarker(int markerWidth, int markerHeight, UUID id, Location location, ActorType actorType, PImage img) {
		super(location);
		this.img = img;
		this.actorId = id;
		this.actorType = actorType;
		this.markerWidth = markerWidth;
		this.markerHeight = markerHeight;
	}

	@Override
	public void draw(PGraphics pg, float x, float y) {
		pg.pushStyle();
		pg.imageMode(PConstants.CORNER);
		//center the image drawing on the coordinates
		//pg.image(img, x, y, 30, 30);
		pg.image(img, x-markerWidth/2, y-markerHeight/2, markerWidth, markerHeight);
		pg.popStyle();
	}

	@Override
	protected boolean isInside(float checkX, float checkY, float x, float y) {
		boolean inside = false;
		if( checkX > x - markerWidth/2 && checkX < x + markerWidth/2 && checkY > y - markerHeight/2 && checkY < y + markerHeight/2){
			inside = true;
		}
		
		return inside;
	}

	public UUID getActorId() {
		return actorId;
	}

	public ActorType getActorType() {
		return actorType;
	}
}
