package esoteric_bot_booster.task;

import org.osbot.rs07.script.MethodProvider;

public class RecuperateHealth extends Task{

    public RecuperateHealth(MethodProvider api){
        super(api);
    }


    /*
    Pseudocode:
    if my health is below 80%:
        I should process this task
     */
    @Override
    public boolean canProcess() {
        return api.myPlayer().getHealthPercent() < 80;
    }
    /*
    Pseudocode:
    Eat Cooked meat or Cooked chicken if HP falls below 80%
    */
    @Override
    public void process() {
        utils.PlayerUtils.eatFood(api, new String[]{"Cooked meat", "Cooked chicken"}, 80);
    }
}
