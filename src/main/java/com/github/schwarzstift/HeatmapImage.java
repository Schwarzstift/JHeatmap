
package com.github.schwarzstift;

import java.awt.*;
import java.awt.image.BufferedImage;


/**
 * The HeatmapImage handel the drawing stuff of the Heatmap.
 *
 * @author ChrisC
 */
class HeatmapImage extends BufferedImage
{
	private final HeatmapModel heatmapModel;
	private Color					backgroundColor	= Color.GRAY;
	private double					highestHeatValue	= 1;
	
	
	/**
	 * @param model : Reference to the corresponding Heatmap Model
	 */
	HeatmapImage(HeatmapModel model)
	{
		super(model.getWidth(), model.getHeight(), BufferedImage.TYPE_INT_RGB);
		heatmapModel = model;
		fillImageWithColor(backgroundColor);
	}
	
	
	void refresh()
	{
		drawImage();
	}
	
	
	void reset()
	{
		fillImageWithColor(backgroundColor);
	}
	
	
	void updateHeatColorAround(Point heatCenter)
	{
		int heatSpreadingDistance = heatmapModel.getHeatSpreadingDistance();
		for (int i = heatCenter.getX() - heatSpreadingDistance; i < heatCenter.getX() + heatSpreadingDistance; i++)
		{
			for (int j = heatCenter.getY() - heatSpreadingDistance; j < heatCenter.getY() + heatSpreadingDistance; j++)
			{
				if (isPointInImage(new Point(i, j)))
				{
					updateColorOnPoint(i, j);
				}
			}
		}
	}
	
	
	private boolean isPointInImage(Point point)
	{
		boolean isPointInXBorder = point.getX() >= 0 && point.getX() < getWidth();
		boolean isPointInYBorder = point.getY() >= 0 && point.getY() < getHeight();
		return isPointInXBorder && isPointInYBorder;
	}
	
	
	private void drawImage()
	{
		for (int x = 0; x < getWidth(); x++)
		{
			for (int y = 0; y < getHeight(); y++)
				updateColorOnPoint(x, y);
		}
	}
	
	
	private void updateColorOnPoint(int x, int y)
	{
		double relativeHeat = heatmapModel.getHeatOn(new Point(x, y)) / highestHeatValue;
		if (relativeHeat > 1)
		{
			relativeHeat = 1;
		}
		int color = new Color((int) (255 * relativeHeat), (int) (255 * (1 - relativeHeat)), 50).getRGB();
		setRGB(x, y, color);
	}
	
	
	private void fillImageWithColor(Color color)
	{
		for (int i = 0; i < getWidth(); i++)
		{
			for (int j = 0; j < getHeight(); j++)
			{
				setRGB(i, j, color.getRGB());
			}
		}
	}
	
	
	void setHighestHeatValue(double highestHeatValue)
	{
		this.highestHeatValue = highestHeatValue;
		drawImage();
	}
	
	
	void setBackgroundColor(Color backgroundColor)
	{
		this.backgroundColor = backgroundColor;
		drawImage();
	}
}