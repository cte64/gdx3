package gameCode.Utilities;

import com.badlogic.gdx.math.Vector2;
import gameCode.Infrastructure.World;
import gameCode.Infrastructure.Entity;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

public class Coordinates {

    public float x, y;
    public String side;
    public Coordinates(float newX, float newY, String newSide) { x = newX; y = newY; side = newSide; }

    public static Vector2 getPoint(float xUn, float yUn, Entity ent) {

        float xPos = ent.x_pos;
        float yPos = ent.y_pos;

        float xMid = xPos + ent.getWidth()/2;
        float yMid = yPos + ent.getHeight()/2;
        float h = (float)Math.sqrt( Math.pow((xUn - xMid), 2.0f) + Math.pow((yUn - yMid), 2.0f) );

        float pR = (float)Math.atan2( (-yUn + yMid), (xUn - xMid) );
        if(pR < 0) pR += 2*MathUtils.PI;

        float pA = MathUtils.toRad(ent.angle);
        float xN = (float)(xMid + h*Math.cos( pR + pA));
        float yN = (float)(yMid - h*Math.sin( pR + pA));

        xN = MathUtils.clamp(xN, 0.0f, (float)(World.get().getNumPixels() - 1));
        yN = MathUtils.clamp(yN, 0.0f, (float)World.get().getNumPixels() - 1);

        Vector2 retVal = new Vector2(xN, yN);
        return retVal;
    }

    public static Vector2 getPoint2(float xUn, float yUn, Entity ent) {

        if(ent.angle == 0.0) {
            Vector2 retVal = new Vector2(xUn, yUn);
            return retVal;
        }

        float xPos = ent.x_pos;
        float yPos = ent.y_pos;
        float xMid = xPos + ent.getWidth()/2;
        float yMid = yPos + ent.getHeight()/2;
        float h = (float)Math.sqrt( Math.pow((xUn - xMid), 2.0f) + Math.pow((yUn - yMid), 2.0f) );

        float pR = (float)Math.atan2( (-yUn + yMid), (xUn - xMid) );
        if(pR < 0) pR += 2*MathUtils.PI;

        float pA = -MathUtils.toRad(ent.angle);
        float xN = (float)(xMid + h*Math.cos( pR + pA));
        float yN = (float)(yMid + h*Math.sin( pR + pA));

        xN = MathUtils.clamp(xN, 0.0f, (float)(World.get().getNumPixels() - 1));
        yN = MathUtils.clamp(yN, 0.0f, (float)World.get().getNumPixels() - 1);

        Vector2 retVal = new Vector2(xN, yN);
        return retVal;
    }

    public static Vector2 rotatePoint(float xUn, float yUn, float fromX, float fromY, float angle) {

        float h = (float)Math.sqrt( Math.pow((xUn - fromX), 2.0) + Math.pow((yUn - fromY), 2.0) );
        float pR = (float)Math.atan2( (-yUn + fromY), (xUn - fromY) );
        if(pR < 0) pR += 2*MathUtils.PI;

        float pA = MathUtils.toRad(angle);
        float xN = (float)(fromX + h*Math.cos( pR + pA));
        float yN = (float)(fromY + h*Math.sin( pR + pA ));

        xN = MathUtils.clamp(xN, 0.0f, (float)(World.get().getNumPixels() - 1));
        yN = MathUtils.clamp(yN, 0.0f, (float)(World.get().getNumPixels() - 1));

        Vector2 retVal = new Vector2(xN, yN);
        return retVal;
    }

    public static Vector2 rotatePoint2(float xUn, float yUn, float fromX, float fromY, float angle) {

        float h = (float)Math.sqrt( Math.pow((xUn - fromX), 2.0) + Math.pow((yUn - fromY), 2.0) );
        float pR = (float)Math.atan2( (-yUn + fromY), (xUn - fromY) );
        if(pR < 0) pR += 2*MathUtils.PI;

        float pA = MathUtils.toRad(angle);
        float xN = (float)(fromX + h*Math.cos( pR + pA));
        float yN = (float)(fromY + h*Math.sin( pR + pA ));

        xN = MathUtils.clamp(xN, 0.0f, (float)(World.get().getNumPixels() - 1));
        yN = MathUtils.clamp(yN, 0.0f, (float)(World.get().getNumPixels() - 1));

        Vector2 retVal = new Vector2(xN, yN);
        return retVal;
    }

    public static ArrayList<Coordinates> getPerimeterCorrds2(Entity ent) {

        ArrayList<Coordinates> data = new ArrayList<Coordinates>();

        //top
        for(int x = (int)ent.x_pos + 1; x < ent.x_pos + ent.getWidth() - 2; x++) {
            Vector2 temp = getPoint(x, ent.y_pos, ent);
            Coordinates s = new Coordinates(temp.x, temp.y, "top");
            data.add(s);
        }

        //bottom
        for(int x = (int)ent.x_pos + 1; x < ent.x_pos + ent.getWidth() - 2; x++) {
            Vector2 temp = getPoint(x, ent.y_pos + ent.getHeight() - 1, ent );
            Coordinates s = new Coordinates(temp.x, temp.y, "bottom");
            data.add(s);
        }

        //left
        for(int y = (int)ent.y_pos + 1; y < ent.y_pos + ent.getHeight() - 2; y++) {
            Vector2 temp = getPoint(ent.x_pos, y, ent );
            Coordinates s = new Coordinates(temp.x, temp.y, "left");
            data.add(s);
        }

        //right
        for(int y = (int)ent.y_pos + 1; y < ent.y_pos + ent.getHeight() - 2; y++) {
            Vector2 temp = getPoint(ent.x_pos + ent.getWidth() - 1, y, ent );
            Coordinates s = new Coordinates(temp.x, temp.y, "right");
            data.add(s);
        }

        return data;
    }

    public static ArrayList<Vector2> getLocatorCellCoord(Entity ent) {
        
        ArrayList<Vector2> corner_coords = new ArrayList<Vector2>();
        if(ent == null) {
            corner_coords.add(new Vector2(0, 0));
            return corner_coords;
        }
        
        //  TOP LEFT
        int topLeftX = (int)((ent.x_pos * World.get().getNumCells()) / World.get().getNumPixels());
        int topLeftY = (int)((ent.y_pos * World.get().getNumCells()) / World.get().getNumPixels());
        topLeftX = MathUtils.clamp(topLeftX, 0, World.get().getNumCells() - 1);
        topLeftY = MathUtils.clamp(topLeftY, 0, World.get().getNumCells() - 1);
        corner_coords.add(new Vector2(topLeftY, topLeftX));

        // TOP RIGHT
        int topRightX = (int)((ent.x_pos + ent.getWidth())/(World.get().tileSize*World.get().xCell));
        int topRightY = (int)((ent.y_pos)/(World.get().tileSize*World.get().xCell));
        topRightX = MathUtils.clamp(topRightX, 0, World.get().getNumCells() - 1);
        topRightY = MathUtils.clamp(topRightY, 0, World.get().getNumCells() - 1);
        corner_coords.add(new Vector2(topRightY, topRightX));

        // BOTTOM LEFT
        int bottomLeftX = (int)((ent.x_pos)/(World.get().tileSize*World.get().xCell));
        int bottomLeftY = (int)((ent.y_pos + ent.getHeight())/(World.get().tileSize*World.get().xCell));
        bottomLeftX = MathUtils.clamp(bottomLeftX, 0, World.get().getNumCells() - 1);
        bottomLeftY = MathUtils.clamp(bottomLeftY, 0, World.get().getNumCells() - 1);
        corner_coords.add(new Vector2(bottomLeftY, bottomLeftX));

        // BOTTOM RIGHT
        int bottomRightX = (int)((ent.x_pos + ent.getWidth())/(World.get().tileSize*World.get().xCell));
        int bottomRightY = (int)((ent.y_pos + ent.getHeight())/(World.get().tileSize*World.get().xCell));
        bottomRightX = MathUtils.clamp(bottomRightX, 0, World.get().getNumCells() - 1);
        bottomRightY = MathUtils.clamp(bottomRightY, 0, World.get().getNumCells() - 1);
        corner_coords.add(new Vector2(bottomRightY, bottomRightX));

        //GET RID OF DUPLICATES
        Set<Vector2> noDup = new LinkedHashSet<Vector2>(corner_coords);
        corner_coords.clear();
        corner_coords.addAll(noDup);

        return corner_coords;
    }
}
