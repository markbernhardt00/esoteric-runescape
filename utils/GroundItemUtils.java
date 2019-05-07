package utils;

import org.osbot.rs07.api.model.GroundItem;
import org.osbot.rs07.script.MethodProvider;
import org.osbot.rs07.utility.ConditionalSleep;

public final class GroundItemUtils {
    private GroundItemUtils(){}

    //Returns true if the player can Pick up items with names in items_array, and also picks them up
    public static Boolean pickUpItems(MethodProvider api, String[] items_array) {
        boolean rv = false;
        for (String item_name : items_array) {
            GroundItem item = api.getGroundItems().closest(gi -> gi != null && gi.getName().equals(item_name) && api.getMap().canReach(gi));
            if (item != null) //if it exists
            {
                boolean closeEnough = item.getPosition().distance(api.myPosition()) < 7;
                if (closeEnough) {
                    rv = true;
                    api.log("Picking up item named: " + item_name);
                    if (!item.isVisible()) {
                        api.log("I cant see the item, so I am panning the camera");
                        api.getCamera().toEntity(item);
                    }
                    int lastCount = api.getInventory().getEmptySlotCount();
                    if (item.interact("Take")) {
                        new ConditionalSleep(3000, 1000) {
                            @Override
                            public boolean condition() throws InterruptedException {
                                return api.getInventory().getEmptySlotCount() < lastCount;
                            }
                        }.sleep();
                    }
                } else if ((item.getPosition().distance(api.myPosition()) < 12) && (item.getPosition().distance(api.myPosition()) > 7))
            {
                    api.log("The item: " + item.getName() + " is too far away, so I'm moving closer");
                    api.getWalking().webWalk(item.getPosition());
                    rv = true;
                }
            }
        }
        return rv;
    }
}
