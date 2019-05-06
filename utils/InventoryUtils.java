package utils;

import org.osbot.rs07.api.model.Item;
import org.osbot.rs07.script.MethodProvider;

public final class InventoryUtils {
    private InventoryUtils(){}

    //returns the amount of item with name item_name in players inventory
    public static int countInventoryItem(MethodProvider api, String item_name){

        int rv = 0;
        //iterates through the players inventory searching for items matching item_name
        for(Item item : api.getInventory().getItems()){
            if(item != null){
                if(item.getName().equals(item_name)){
                    rv++;
                }
            }
        }

        return rv;
    }
}
