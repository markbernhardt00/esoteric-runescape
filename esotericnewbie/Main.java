package esotericnewbie;

import esotericnewbie.task.*;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;

import java.util.ArrayList;

@ScriptManifest(author = "EsotericRS", info = "Trains a fresh account to 30 combat!", logo = "https://imgur.com/a/3Gr2vNy", name = "EsotericNewbie", version = 0.95)
public class Main extends Script {

    ArrayList<Task> tasks = new ArrayList<Task>();
    @Override
    public void onStart(){
        utils.WidgetUtils.keepInventoryOpen(this);

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