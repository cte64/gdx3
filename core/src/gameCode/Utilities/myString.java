package gameCode.Utilities;

import java.util.ArrayList;

public class myString {

    public String data;

    public myString(String newData) {
        data = newData;
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
        if(MathUtils.isNumeric(data))
            return Integer.parseInt(data);
        else return 0;
    }

    public static boolean compareFields(String a, String b, String field) {
        String aField = getField(a, field);
        String bField = getField(b, field);
        if(aField.equals(bField)) return true;
        else return false;
    }

    public static float stringToFloat(String data) {
        if(MathUtils.isNumeric(data))
            return Float.parseFloat(data);
        else return 0.0f;
    }

    public static void compressString(myString cont) {

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


    /*
    public static String getField(myString name, String field) {

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

     */


    public static String getField(String name, String field) {
        myString str = new myString(name);
        return str.getField(field);
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

    public static void setField(myString name, String field, String value) {

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
        myString newString = new myString(name);
        setField(newString, field, value);
        return newString.data;
    }

    public void replaceIndex(int index, char newChar) {
        if(index >= 0 && index < data.length()) {
            data = data.substring(0, index) + newChar + data.substring(index + 1, data.length());
        }
    }
}
