package utils;

import org.osbot.rs07.api.model.Item;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.script.MethodProvider;

public final class SkillUtils {
    private SkillUtils() {
    }


    //pre: Player can reach a range
    //Cooks all food with ingredient_name in ingredient_names
    public static Boolean cookOnRange(MethodProvider api, String[] ingredient_names) {
        RS2Object range = api.getObjects().closest(obj -> obj != null && obj.getName().equals("Range") && api.getMap().canReach(obj));

        for(String ingredient : ingredient_names){
            while(api.getInventory().contains(ingredient)) {
                int before_cooking = api.getSkills().getStatic(Skill.COOKING);
                Item item = api.getInventory().getItem(ingredient);
                if (item != null) {
                    if (item.interact("Use")) {
                        if (range != null) {
                            if (range.interact("Use")) {
                                SleepUtils.sleepUntil(() -> api.getWidgets().get(270, 14) != null, 5000);
                                if (api.getWidgets().get(270, 14) != null) {
                                    if (api.getWidgets().get(270, 14).interact()) {
                                        SleepUtils.sleepUntil(() -> !api.getInventory().contains(ingredient) || before_cooking < api.getSkills().getStatic(Skill.COOKING), 60000);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if(api.getInventory().contains(ingredient)){
                return false;
            }
        }
        return true;
    }



    //Drops all items in players inv containing the substring Burnt
    //TODO: For some reason this is SUPER slow, can I speed this up sometime?
    public static void dropBurntFood(MethodProvider api) {
        for (Item item : api.getInventory().getItems()) {
            int before_capacity = api.getInventory().getCapacity();
            if(item != null && item.getName().contains("Burnt")){
                if(item.interact("Drop")){
                    api.log("Dropping burnt food item...");
                    SleepUtils.sleepUntil(() -> api.getInventory().getCapacity() < before_capacity, 5000);
                    }
                }
            }
        }
    }
