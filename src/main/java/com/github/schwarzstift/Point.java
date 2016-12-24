
package com.github.schwarzstift;

/**
 * @author ChrisC
 */
class Point
{
	private final int	x;
	private final int	y;
	
	
	Point(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	
	int getX()
	{
		return x;
	}
	
	
	int getY()
	{
		return y;
	}
	
	
	double distanceTo(Point point)
	{
		return Math.sqrt(Math.pow(point.getX() - (double) x, 2) + Math.pow(point.getY() - (double) y, 2));
	}
	
}