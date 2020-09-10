package gameCode.Utilities;

import java.util.ArrayList;

public class myString {

    public String data;

    //Member methods =========================================================================
    public myString(String newData) {
        data = newData;
    }

    public String getField(String field) {

        int index = data.indexOf(field);
        if(index == -1) return "";

        int startIndex = index;
        int endIndex = startIndex;
        int x = index;

        while (x < data.length()) {
            if (data.charAt(x) == ':') startIndex = x;
            else if (data.charAt(x)  == ']') {
                endIndex = x;
                break;
            } x++;
        }

        String retVal = data.substring(startIndex + 2, endIndex);
        return retVal;

    }

    public void setField(String field, String value) {

        int index = data.indexOf("[" + field);
        if(index == -1) {
            data += "[" + field + ": " + value + "]";
            return;
        }

        int startIndex = index;
        int endIndex = startIndex;
        int x = index;

        while (x < data.length()) {
            if (data.charAt(x) == ':') startIndex = x;
            else if (data.charAt(x)  == ']') {
                endIndex = x;
                break;
            } x++;
        }
        String left = data.substring(0, startIndex + 2);
        String right = data.substring(endIndex, data.length());
        data = left + value + right;
    }

    public void replaceIndex(int index, char newChar) {
        if(index >= 0 && index < data.length()) {
            data = data.substring(0, index) + newChar + data.substring(index + 1, data.length());
        }
    }


    //Static Utility Methods =================================================================
    public static float stringToFloat(String data) {
        if(myMath.isNumeric(data))
            return Float.parseFloat(data);
        else return 0.0f;
    }

    public static boolean compareFields(String a, String b, String field) {
        String aField = getField(a, field);
        String bField = getField(b, field);
        if(aField.equals(bField)) return true;
        else return false;
    }

    public static String getField(String name, String field) {
        myString str = new myString(name);
        return str.getField(field);
    }

    public static String setField(String data, String field, String value) {
        myString wrapper = new myString(data);
        wrapper.setField(field, value);
        return wrapper.data;
    }

    public static ArrayList<myString> getBeforeChar(String data, char splitChar) {
        ArrayList<myString> retVal = new ArrayList<myString>();
        String newStr = "";
        for(int x = 0; x < data.length(); x++) {
            char c = data.charAt(x);
            if(c == splitChar) {
                retVal.add(new myString(newStr));
                newStr = "";
            }
            else newStr += c;
        }
        return retVal;
    }

    public static String toString(int num) { return Integer.toString(num); }

    public static int stringToInt(String data) {
        if(myMath.isNumeric(data))
            return Integer.parseInt(data);
        else return 0;
    }
}
