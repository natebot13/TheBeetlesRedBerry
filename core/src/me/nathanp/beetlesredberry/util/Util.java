package me.nathanp.beetlesredberry.util;

import com.brashmonkey.spriter.Point;

public class Util {
	public static double distance(Point p1, Point p2) {
        double xDist = p2.x - p1.x;
        double yDist = p2.y - p1.y;
        return Math.sqrt((xDist * xDist) + (yDist * yDist));
    }
	
	public static double angle(Point p1, Point p2) {
        return Math.atan2(p2.y - p1.y, p2.x - p1.x);
    }
}
