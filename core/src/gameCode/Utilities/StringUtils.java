package gameCode.Utilities;

public class StringUtils {


    public String data;
    public StringUtils(String newData) { data = newData; }


    public static String toString(int num) { return Integer.toString(num); }

    public static int stringToInt(String data) { return Integer.parseInt(data); }

    public static void compressString(StringUtils cont) {

        if(cont.data.length() <= 0) return;

        char first = cont.data.charAt(0);
        boolean allTheSame = true;

        for(int x = 0; x < cont.data.length(); x++) {
            if(cont.data.charAt(x) != first)
                allTheSame = false;
        }

        if (allTheSame) {
            cont.data = "";
            cont.data += first;
        }
    }

    public static String getField(StringUtils name, String field) {

        int index = name.data.indexOf(field);
        if(index == -1) return "";

        int startIndex = index;
        int endIndex = startIndex;
        int x = index;

        while (x < name.data.length()) {
            if (name.data.charAt(x) == ':') startIndex = x;
            else if (name.data.charAt(x)  == ']') {
                endIndex = x;
                break;
            } x++;
        }

        String retVal = name.data.substring(startIndex + 2, endIndex);
        return retVal;
    }

    public static void setField(StringUtils name, String field, String value) {

        int index = name.data.indexOf("[" + field);
        if(index == -1) {
            name.data += "[" + field + ": " + value + "]";
            return;
        }

        int startIndex = index;
        int endIndex = startIndex;
        int x = index;

        while (x < name.data.length()) {
            if (name.data.charAt(x) == ':') startIndex = x;
            else if (name.data.charAt(x)  == ']') {
                endIndex = x;
                break;
            } x++;
        }

        String replaceThis = name.data.substring(startIndex + 2, endIndex);
        name.data = name.data.replace(replaceThis, value);
    }

}
