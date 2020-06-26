package gameCode.Utilities;

public class MathUtils {

    public static <A extends Comparable<A>> A clamp(A val, A min, A max) {
        if (val.compareTo(min) < 0) val = min;
        if (val.compareTo(max) > 0) val = max;
        return val;
    }



}
