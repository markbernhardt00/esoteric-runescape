package newbie;

import newbie.task.Level10Through20;
import newbie.task.Level1Through10;
import newbie.task.Levels20Through30;
import newbie.task.Task;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;

import java.util.ArrayList;

@ScriptManifest(author = "EsotericRS", info = "Trains a new account to 30 combat!", logo = "", name = "EsotericNewbie", version = 0.2)
public class Main extends Script {

    ArrayList<Task> tasks = new ArrayList<Task>();
    @Override
    public void onStart(){

        tasks.add(new Level1Through10(this));
        tasks.add(new Level10Through20(this));
        tasks.add(new Levels20Through30(this));
    }

    @Override
    public int onLoop() throws InterruptedException{
        tasks.forEach(tasks -> tasks.run());
        return 700;
    }

}