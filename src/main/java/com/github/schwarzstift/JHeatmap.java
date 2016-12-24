
package com.github.schwarzstift;


import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Optional;


/**
 * @author ChrisC
 */
@SuppressWarnings("WeakerAccess")
public class JHeatmap
{
	private final HeatmapModel model;
	private final HeatmapImage image;
	
	private final int				minX;
	private final int				maxX;
	private final int				minY;
	private final int				maxY;
	
	private final double			xScaleFactor;
	private final double			yScaleFactor;
	
	
	/**
	 * This HeatMap collect points and draw them on a Image.
	 * Multiple points on one spot are looking hotter in the image (up to a configurable maximum heat).
	 * The heat is spreading from each point with a configurable linear distance.
	 * To reduce the computational effort all Points will be projected on the Image (with given widthResolution)
	 *
	 * @param maxYInput : highest Y-Value that should be taken as valid heat Center
	 * @param minYInput : lowest Y-Value that should be taken as valid heat Center
	 * @param minXInput : lowest X-Value that should be taken as valid heat Center
	 * @param maxXInput : highest X-Value that should be taken as valid heat Center
	 * @param widthResolution : width resolution of the created BufferedImage (height resolution will be calculated)
	 */
	public JHeatmap(int minXInput, int maxXInput, int minYInput, int maxYInput, int widthResolution)
	{
		this.minX = minXInput;
		this.maxX = maxXInput;
		this.minY = minYInput;
		this.maxY = maxYInput;
		
		int heightResolution = (int) (widthResolution * (maxYInput - minYInput) / (double) (maxXInput - minXInput));
		xScaleFactor = widthResolution / (double) (maxXInput - minXInput);
		yScaleFactor = heightResolution / (double) (maxYInput - minYInput);
		
		model = new HeatmapModel(widthResolution, heightResolution);
		image = new HeatmapImage(model);
	}
	
	
	/**
	 * Reset the HeatMap to background Color
	 */
	public void reset()
	{
		model.reset();
		image.reset();
	}
	
	
	public void setBackgroundColor(Color backgroundColor)
	{
		image.setBackgroundColor(backgroundColor);
	}
	
	
	/**
	 * Spread additional heat from the given xy coordinate on the image
	 *
	 * @param x : the X-Value of the new heat center
	 * @param y : the Y-Value of the new heat center
	 */
	public void drawHeatFromXY(int x, int y)
	{
		Optional<Point> projectedPoint = projectPointToImage(new Point(x, y));
		if (projectedPoint.isPresent())
		{
			model.spreadHeatFrom(projectedPoint.get());
			image.updateHeatColorAround(projectedPoint.get());
		}
		
	}
	
	
	private Optional<Point> projectPointToImage(Point inputPoint)
	{
		if (isPointInRange(inputPoint))
		{
			int newX = (int) ((inputPoint.getX() - minX) * xScaleFactor);
			int newY = (int) ((inputPoint.getY() - minY) * yScaleFactor);
			return Optional.of(new Point(newX, newY));
		}
		return Optional.empty();
	}
	
	
	private boolean isPointInRange(Point point)
	{
		boolean isXInside = point.getX() > minX && point.getX() < maxX;
		boolean isYInside = point.getY() > minY && point.getY() < maxY;
		return isXInside && isYInside;
	}
	
	
	public BufferedImage getImage()
	{
		return image;
	}
	
	
	public void setHeatSpreadingDistance(int spreadingDistance)
	{
		model.setHeatSpreadingDistance(spreadingDistance);
		image.refresh();
	}
	
	
	public void setMaxHeatValue(double maxHeatValue)
	{
		image.setHighestHeatValue(maxHeatValue);
	}
	
}
