package utils;

import org.osbot.rs07.script.MethodProvider;

public final class PlayerUtils {
    private PlayerUtils(){}

    //Toggles the players run energy when they have atleast 65 energy
    public static void handleEnergy(MethodProvider api){
        if (api.getSettings().getRunEnergy() > 65)
            api.getSettings().setRunning(true);
    }
}
