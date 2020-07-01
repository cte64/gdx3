package gameCode.Utilities;

public class MathUtils {


    /*
    float angleBetweenCells(float x1, float y1, float x2, float y2);
    float toRad(float degree);
    float toDeg(float radian);
     */

    public static final float PI = 3.14159265359f;

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

    public static <A extends Comparable<A>> A clamp(A val, A min, A max) {
        if (val.compareTo(min) < 0) val = min;
        if (val.compareTo(max) > 0) val = max;
        return val;
    }



}
