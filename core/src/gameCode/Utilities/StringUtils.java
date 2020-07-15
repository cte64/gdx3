package gameCode.Utilities;

import java.util.ArrayList;

public class StringUtils {

    public String data;

    public StringUtils(String newData) {
        data = newData;
    }


    public static ArrayList<StringUtils> splitToArr(String data, String splitChar) {
        ArrayList<StringUtils> retVal = new ArrayList<StringUtils>();
        String[] dataArr = data.split(splitChar);
        for(String str: dataArr) { retVal.add(new StringUtils(str)); }
        return retVal;
    }


    public static ArrayList<StringUtils> getBeforeChar(String data, char splitChar) {
        ArrayList<StringUtils> retVal = new ArrayList<StringUtils>();
        String newStr = "";
        for(int x = 0; x < data.length(); x++) {
            char c = data.charAt(x);
            if(c == splitChar) {
                retVal.add(new StringUtils(newStr));
                newStr = "";
            }
            else newStr += c;
        }
        return retVal;
    }

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

    public static String getField(String name, String field) {
        StringUtils str = new StringUtils(name);
        return getField(str, field);
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

        String left = name.data.substring(0, startIndex + 2);
        String right = name.data.substring(endIndex, name.data.length());
        name.data = left + value + right;
    }

    public static String setField(String name, String field, String value) {
        StringUtils newString = new StringUtils(name);
        setField(newString, field, value);
        return newString.data;
    }


}
