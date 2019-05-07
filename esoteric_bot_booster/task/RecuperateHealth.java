package esoteric_bot_booster.task;

import org.osbot.rs07.script.MethodProvider;

public class RecuperateHealth extends Task{

    public RecuperateHealth(MethodProvider api){
        super(api);
    }

    @Override
    /*
    Pseudocode:
    if my health is below 80%:
        I should process this task
     */
    public boolean canProcess() {
        return api.myPlayer().getHealthPercent() < 80;
    }

    @Override
    public void process() {
        utils.PlayerUtils.eatFood(api, new String[]{"Cooked meat", "Cooked chicken"}, 80);
    }
}
