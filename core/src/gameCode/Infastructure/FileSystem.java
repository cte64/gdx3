package gameCode.Infastructure;


/*
class FileThing {
    void imageToString(sf::Image* image, std::string& dataString);
    sf::Image stringToImage(std::string& dataString);

    void center(int xIndex, int yIndex);
    bool getUpdate(int x, int y, int select);
    void setUpdate(int x, int y, int select, bool newVal);
    void writeChunk(int xIndex, int yIndex, bool deleteCurrent);
    void readChunk(int xIndex, int yIndex);
    void updateChunks();

public:

    int outerLeft, outerRight, outerTop, outerBottom;      //outer ring
    int middleLeft, middleRight, middleTop, middleBottom;  //middle ring
    int centerLeft, centerRight, centerTop, centerBottom;  //center ring

    std::string getGameSaveDirectory() { return GameSaveDirectory;  }
    std::string getGameSubDirectory() { return gameSubDirectory;  }
    void setGameSubDirectory(std::string newDir) { gameSubDirectory = newDir; }

    void deleteDirectory(std::string directory);
    void createGameDirectory(std::string newDir);
    void saveCurrentChunks();

    void getFile(std::string filename, std::string& data);
    void setFile(std::string filename, std::string& data);

    void update();
    FileThing();
};
 */


import java.util.ArrayList;

public class FileSystem {

    private String GameSaveDirectory;
    private String gameSubDirectory;
    private ArrayList<Boolean> chunkUpdate1;
    private ArrayList<Boolean> chunkUpdate2;



}
