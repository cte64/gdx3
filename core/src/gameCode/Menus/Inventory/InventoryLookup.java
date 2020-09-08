package gameCode.Menus.Inventory;

import gameCode.Infrastructure.FileSystem;
import gameCode.Utilities.StringUtils;
import gameCode.Utilities.myPair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class InventoryLookup {

    private class Recipe {
        public ArrayList<myPair<String, Integer>> recipe;
        public String type;
        public Recipe() {
            recipe = new ArrayList<>();
        }
    }


    HashMap<String, Recipe> recipes;


    public InventoryLookup() {

        recipes = new HashMap<>();

        StringUtils fileName = new StringUtils("[type: inventory]");
        StringUtils data = new StringUtils("");
        FileSystem.getFile(fileName, data);
        ArrayList<StringUtils> dataList = StringUtils.getBeforeChar(data.data, '\n');

        for(StringUtils d: dataList) {

            Recipe newRecipe = new Recipe();
            newRecipe.type = StringUtils.getField(d, "type");
            String key = "";

            for(int z = 0; z < 9; z++) {

                String full = StringUtils.getField(d, StringUtils.toString(z));
                String[] fields = full.split(",");

                String type = "";
                int amount = 0;
                if(fields.length == 2) {
                    type = fields[0];
                    amount = StringUtils.stringToInt(fields[1]);
                }

                key += type + ".";
                myPair<String, Integer> newField = new myPair(type, amount);
                newRecipe.recipe.add(newField);
            }

            recipes.put(key, newRecipe);
        }
    }


    public myPair<String, Integer> getCraftedItem(ArrayList<myPair<String, Integer>> crafting) {

        String key = "";
        for(myPair<String, Integer> node: crafting) key += node.first + ".";
        myPair<String, Integer> retVal = new myPair<>("", 0);

        if(recipes.containsKey(key)) {
            ArrayList<Integer> lcm = new ArrayList<Integer>();
            for(int x = 0; x < recipes.get(key).recipe.size(); x++) {
                int craftAmount = crafting.get(x).second;
                int slotAmount = recipes.get(key).recipe.get(x).second;
                if(craftAmount > 0 && slotAmount > 0) {
                    int newLcm = craftAmount / slotAmount;
                    lcm.add(newLcm);
                }
            }
            Collections.sort(lcm);
            retVal.first = recipes.get(key).type;
            retVal.second = lcm.get(0);
        }

        return retVal;
    }

    public ArrayList<myPair<String, Integer>> getRecipe(ArrayList<myPair<String, Integer>> crafting) {
        String key = "";
        for(myPair<String, Integer> node: crafting) key += node.first + ".";
        if(recipes.containsKey(key)) return recipes.get(key).recipe;
        else {
            ArrayList<myPair<String, Integer>> retVal = new ArrayList<>();
            return retVal;
        }
    }
}
