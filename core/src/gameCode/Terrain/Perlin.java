package gameCode.Terrain;

import java.util.ArrayList;

public class Perlin {

    public Perlin(int lowestPoint, int layerC, int octaves, int stretch, ArrayList<Float> total) {

        ArrayList<Float> random = new ArrayList<Float>();
        ArrayList<Float> lines = new ArrayList<Float>();

        //initialize ===================================================================================================
        for (int x = 0; x < layerC; x++) {
            random.add( (float)(Math.random() * stretch) );
            total.add(0.0f);
        }

        for (int y = 0; y < octaves; y++) {
            for (int x = 0; x < layerC; x++)
                lines.add(0.0f);
        }

        //make lines ===================================================================================================
        for (int y = 0; y < octaves; y++) {

            int increm = (int)Math.pow(2, y);
            int leftIndex, rightIndex;

            for (int x = 0; x < layerC; x++) {
                leftIndex = (x / increm) * increm;
                rightIndex = leftIndex + increm;
                if (rightIndex >= layerC - 1) rightIndex = layerC - 1;
                float percentage = (x % increm) / (float)increm;
                float interPol = random.get(leftIndex) + (random.get(rightIndex) - random.get(leftIndex)) * percentage;
                lines.set(layerC * y + x, interPol / (float)(lowestPoint + stretch));
            }
        }

        //make total =======================================================================================================
        for (int y = 0; y < octaves; y++) {
            int power = (int)Math.pow(2, octaves - y - 1);
            for (int x = 0; x < layerC; x++) {
                float scaler = 1.0f / (float)power;
                float newVal = total.get(x) + scaler * lines.get((y * layerC) + x);
                total.set(x, newVal);
            }
        }

        float largest = 0.0f;
        for (int x = 0; x < layerC; x++) { if (total.get(x) > largest) largest = total.get(x); }
        for (int x = 0; x < layerC; x++) { float norm = total.get(x) / largest; total.set(x, norm); }
        for (int x = 0; x < layerC; x++) { total.set(x, (total.get(x) * stretch) + lowestPoint); }
    }
}
