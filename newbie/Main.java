package newbie;

import newbie.task.Level1Through10;
import newbie.task.Task;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;

import java.util.ArrayList;

@ScriptManifest(author = "EsotericRS", info = "Trains a new account to 30 combat!", logo = "", name = "EsotericNewbie", version = 0.1)
public class Main extends Script {

    ArrayList<Task> tasks = new ArrayList<Task>();
    @Override
    public void onStart(){
        tasks.add(new Level1Through10(this));
    }

    @Override
    public int onLoop() throws InterruptedException{
        tasks.forEach(tasks -> tasks.run());
        return 700;
    }

}