package gameCode.Utilities;

import gameCode.Infastructure.World;
import jdk.internal.net.http.common.Pair;
import gameCode.Infastructure.Entity;
import gameCode.Utilities.MathUtils;

public class Coordinates {


    /*
    public static Pair<Float, Float> getPoint(float xUn, float yUn, Entity ent) {

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


        xN = MathUtils.clamp(xN, 0.0f, (float)(World.getNumPixels() - 1));
        yN = clamp(yN, 0, world.getNumPixels() - 1);

        std::pair<float, float> retVal({xN, yN});
        return retVal;
    }

     */


}
