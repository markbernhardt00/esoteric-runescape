package utils;

import org.osbot.rs07.api.Inventory;
import org.osbot.rs07.api.model.Item;
import org.osbot.rs07.script.MethodProvider;

public final class InventoryUtils {
    private InventoryUtils(){}

    //returns the amount of item with names in item_names array that are in players inventory
    //Not so sure about the complexity on this one... Probably something better out there.
    public static int countInventoryItems(MethodProvider api, String[] item_names){

        int rv = 0;
        //iterates through the players inventory searching for items matching item_name
        for(String item_name : item_names){
            for(Item item : api.getInventory().getItems()){
                if(item != null){
                    if(item.getName().equals(item_name)){
                        rv++;
                    }
                }
            }
        }

        return rv;
    }
}
