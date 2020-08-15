package gameCode.Utilities;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class Misc {

    public static String getDate() {


        Date date = new Date(); // your date
        // Choose time zone in which you want to interpret your Date
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Paris"));
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        // etc.
        String retVal = month + "/" + day + "/" + year;
        return retVal;
    }
}
