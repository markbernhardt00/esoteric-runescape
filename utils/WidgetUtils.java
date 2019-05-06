package utils;

import org.osbot.rs07.api.ui.RS2Widget;
import org.osbot.rs07.script.MethodProvider;

public final class WidgetUtils {

    private WidgetUtils(){}

    //This function will drive the player to click on the combat style matching the text in String combat_style
    public static void changeCombatStyle(MethodProvider api, String combat_style)
    {
        //First, get the combat style icon...
        RS2Widget combatStanceIcon = api.getWidgets().get(164, 50);

        //Click on the combat style icon
        if(combatStanceIcon != null){
            combatStanceIcon.interact();
        }

        //Get the combat style widget of the matching combat style
        RS2Widget combatStanceWidget = api.getWidgets().getWidgetContainingText(combat_style);

        //Click on the matching style... switching styles.
        if(combatStanceWidget != null){
            combatStanceWidget.interact();
        }

        //And closing the Combat Style screen
        if(combatStanceIcon != null){
            combatStanceIcon.interact();
        }
    }

    public static void keepInventoryOpen(MethodProvider api){
        RS2Widget inventoryIcon = api.getWidgets().get(164,53);
        RS2Widget fullInventoryView = api.getWidgets().get(164,66);
        if(!fullInventoryView.isVisible() && fullInventoryView != null){
            if(inventoryIcon != null){
                inventoryIcon.interact();
            }
        }
    }
}
