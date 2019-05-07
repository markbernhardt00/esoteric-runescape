package esoteric_bot_booster;

import esoteric_bot_booster.task.*;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;

import java.util.ArrayList;

@ScriptManifest(author = "EsotericRS", info = "Trains a fresh account to 30 combat!", logo = "https://imgur.com/a/3Gr2vNy", name = "Esoteric Bot Booster", version = 0.95)
public class Main extends Script {

    private ArrayList<Task> tasks = new ArrayList<>();
    @Override
    public void onStart(){
        utils.WidgetUtils.forceInventoryOpen(this);

        tasks.add(new FreshSpawn(this));
        tasks.add(new Die(this));
        tasks.add(new RecuperateHealth(this));
        tasks.add(new AquireFood(this));
        tasks.add(new Level1Through10(this));
        tasks.add(new Level10Through15(this));
        tasks.add(new Levels15Through30(this));
    }

    @Override
    public int onLoop() throws InterruptedException{
        tasks.forEach(tasks -> tasks.run());
        return 700;
    }
}