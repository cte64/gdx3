package gameCode.Utilities;


/*
    StringOps();
    std::string toString(int number);
    void compressString(std::string &data);
    std::string getField(const std::string& name, std::string field);
    void setField(std::string& name, std::string field, std::string value);
    void removeField(std::string& name, std::string field);
    void addField(std::string& name, std::string field, std::string value);
    int stringToInt(std::string s);
    std::string getDateAndTime();
 */


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



}
