package esoteric_bot_booster.task;

import org.osbot.rs07.script.MethodProvider;

public class ManagePlayer extends Task {

    public ManagePlayer(MethodProvider api){
        super(api);
    }

    /*
    Pseudocode:
    if my health is below 80% OR (im not running AND my energy is over 65):
        I should process this task
     */
    @Override
    public boolean canProcess() {
        return api.myPlayer().getHealthPercent() < 80 || (api.getSettings().getRunEnergy() > 65 && !api.getSettings().isRunning());
    }

    /*
    Pseudocode:
    if my health is below 80%:
        Eat some food
    else if im not running and I have over 65 energy:
        Toggle running to on
     */
    @Override
    public void process() {
        if(api.myPlayer().getHealthPercent() < 80) {
            utils.PlayerUtils.eatFood(api, new String[]{"Cooked meat", "Cooked chicken"}, 80);
        }
        else if(api.getSettings().getRunEnergy() > 65 && !api.getSettings().isRunning()){
            utils.PlayerUtils.handleEnergy(api);
        }
    }
}
