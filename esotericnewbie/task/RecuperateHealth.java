package esotericnewbie.task;

import org.osbot.rs07.script.MethodProvider;

public class RecuperateHealth extends Task{

    public RecuperateHealth(MethodProvider api){
        super(api);
    }

    @Override
    public boolean canProcess() {
        return api.myPlayer().getHealthPercent() < 80;
    }

    @Override
    public void process() {
        utils.PlayerUtils.eatFood(api, new String[]{"Cooked meat", "Cooked chicken"}, 80);
    }
}
