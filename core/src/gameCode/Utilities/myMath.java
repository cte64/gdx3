package gameCode.Utilities;

import java.util.Random;

public class myMath {

    public static final float PI = 3.14159265359f;

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
    public static float angle1(float x, float y) {
        float angle = (float)(Math.atan2(y, x)*180.0/PI);
        if(angle < 0) angle += 360.0;
        return angle;
    }
    public static float mag(float x1, float y1, float x2, float y2) {
        float x = x2 - x1;
        float y = y2 - y1;
        float mag = (float)Math.sqrt( Math.pow(x, 2.0) + Math.pow(y, 2.0) );
        return mag;
    }
    public static double getGauss(double median, double sDev) {
        Random ran = new Random();
        double retVal = ran.nextGaussian() * Math.sqrt(sDev) + median;
        return retVal;
    }
    public static float angleBetweenCells(float x1, float y1, float x2, float y2) {
        //remember that y increases in the downward direction and decreases in the up direction
        y1 *= -1.0;
        y2 *= -1.0;
        float x = x1 - x2;
        float y = y1 - y2;
        float ang = angle1(x, y);
        return ang;
    }
    public static float toRad(float degree) {
        float rad = (degree*PI)/180.0f;
        return rad;
    }
    public static float toDeg(float radian) {
        float degree = (radian*180.0f)/PI;
        return degree;
    }
    public static <A extends Comparable<A>> A clamp(A val, A min, A max) {
        if (val.compareTo(min) < 0) val = min;
        if (val.compareTo(max) > 0) val = max;
        return val;
    }

    public static float angleBetween2Points(float xOrig, float yOrig, float x, float y) {
        // this is in Radians!

         float newX = x - xOrig;
         float newY = y - yOrig;

         float angle = (float)(Math.atan2(newY, newX));
         return angle;
    }
}
