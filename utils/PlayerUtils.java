package utils;

import org.osbot.rs07.api.Inventory;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.model.Item;
import org.osbot.rs07.script.MethodProvider;

public final class PlayerUtils {
    private PlayerUtils(){}

    //Toggles the players run energy when they have atleast 65 energy
    public static void handleEnergy(MethodProvider api){
        if (api.getSettings().getRunEnergy() > 65 && !api.getSettings().isRunning()) {
            api.log("Energy exceeded 65, toggling RUN to ACTIVE");
            api.getSettings().setRunning(true);
        }
    }

    //Returns the closest Position to the player in Position[]
    public static Position findClosestPosition(MethodProvider api, Position[] position_array){
        assert (position_array != null);
        int closest_distance = position_array[0].distance(api.myPosition());
        Position closest_position = position_array[0];
        for(Position position : position_array){
            if (position.distance(api.myPosition()) < closest_distance){
                closest_distance = position.distance(api.myPosition());
                closest_position = position;
            }
        }
        return closest_position;
    }

    public static Boolean eatFood(MethodProvider api, String[] food_names, int percent_hp_threshhold) {

        if (api.myPlayer().getHealthPercent() < percent_hp_threshhold) {
            Inventory player_inventory = api.getInventory();
            int before_eating_capacity = player_inventory.getCapacity();
            for (String food_name : food_names) {
                if (player_inventory.contains(food_name)) {
                    Item food_to_eat = player_inventory.getItem(food_name);
                    if (food_to_eat != null) {
                        api.log("Eating food named: " + food_name);
                        if (food_to_eat.interact("Eat")) {
                            utils.Sleep.sleepUntil(() -> before_eating_capacity < api.getInventory().getCapacity(), 3000);
                        }
                    }
                }
            }
            return before_eating_capacity < api.getInventory().getCapacity();
        }
        else{
            return false;
        }
    }
}