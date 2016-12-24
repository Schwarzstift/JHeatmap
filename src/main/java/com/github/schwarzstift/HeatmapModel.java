
package com.github.schwarzstift;

import java.util.ArrayList;
import java.util.List;


/**
 * The HeatmapModel provides data for the HeatmapImage.
 *
 * @author ChrisC
 */
class HeatmapModel
{
	private final List<Point>	heatCenterPoints			= new ArrayList<>();
	private double[][]			heatValues;
	private int						heatSpreadingDistance	= 10;
	private final int				width;
	private final int				height;
	
	
	HeatmapModel(int width, int height)
	{
		this.width = width;
		this.height = height;
		heatValues = new double[width][height];
	}
	
	
	void spreadHeatFrom(Point heatCenter)
	{
		heatCenterPoints.add(heatCenter);
		spreadingHeatFromPoint(heatCenter);
	}
	
	
	private void spreadingHeatFromPoint(Point point)
	{
		for (int x = point.getX() - heatSpreadingDistance; x < point.getX() + heatSpreadingDistance; x++)
		{
			for (int y = point.getY() - heatSpreadingDistance; y < point.getY() + heatSpreadingDistance; y++)
			{
				updateHeatValueWithHeatPoint(point, new Point(x, y));
			}
		}
	}
	
	
	private void updateHeatValueWithHeatPoint(Point heatCenter, Point updatedPoint)
	{
		if (isPointInArrayBorders(updatedPoint))
		{
			double distance = heatCenter.distanceTo(updatedPoint);
			heatValues[updatedPoint.getX()][updatedPoint.getY()] += calcAdditionalHeatValue(distance);
		}
	}
	
	
	private boolean isPointInArrayBorders(Point point)
	{
		boolean isPointInXBorder = point.getX() >= 0 && point.getX() < width;
		boolean isPointInYBorder = point.getY() >= 0 && point.getY() < height;
		return isPointInXBorder && isPointInYBorder;
	}
	
	
	private double calcAdditionalHeatValue(double distance)
	{
		double additionalHeat = 0;
		if (distance < heatSpreadingDistance)
		{
			additionalHeat = 1 - distance / heatSpreadingDistance;
		}
		return additionalHeat;
	}
	
	
	double getHeatOn(Point point)
	{
		if (isPointInArrayBorders(point))
		{
			return heatValues[point.getX()][point.getY()];
		}
		return 0;
	}
	
	
	private void refreshModel()
	{
		heatValues = new double[width][height];
		heatCenterPoints.forEach(this::spreadingHeatFromPoint);
	}
	
	
	void reset()
	{
		heatCenterPoints.clear();
		heatValues = new double[width][height];
	}
	
	
	void setHeatSpreadingDistance(int heatSpreadingDistance)
	{
		this.heatSpreadingDistance = heatSpreadingDistance;
		refreshModel();
	}
	
	
	int getHeatSpreadingDistance()
	{
		return heatSpreadingDistance;
	}
	
	
	int getWidth()
	{
		return width;
	}
	
	
	int getHeight()
	{
		return height;
	}
	
	
}
