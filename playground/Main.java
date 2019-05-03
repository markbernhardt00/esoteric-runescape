package playground;

import org.osbot.rs07.api.Combat;
import org.osbot.rs07.api.Settings;
import org.osbot.rs07.api.filter.Filter;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;
import org.osbot.rs07.utility.ConditionalSleep;

import java.awt.*;
import java.util.Random;

@ScriptManifest(author = "EsotericRS", info = "", logo = "", name = "Playground Script", version = 0.1)
public class Main extends Script {

    @Override
    //Executes once on script start
    public void onStart() throws InterruptedException {

    }

    @Override
    //Executes once on script exit
    public void onExit() throws InterruptedException {

    }

    @Override
    //Executes every 600 milliseconds
    public int onLoop() throws InterruptedException {

        //run when atleast 75 energy is replenished
        handleRunning(75);

        //fight chickens!
        fightNPC("Chicken");








        //This is the tick rate the script will poll this onLoop() method at
        //Tick rate is randomized to decrease predictability over time
        return (600 + (int) (Math.random() * (1200 - 600)));
    }

    //returns an opponent worthy of our combat
    private NPC getWorthyOpponent(String name){

        //This filter will matches with an npc that is reachable, has name = name, has > 0 hp, is not currently
        // being interacted with already, and still exists
        Filter worthyOpponentFilter = new Filter<NPC>(){
            @Override
            public boolean match(NPC npc) {
                return npc != null && getMap().canReach(npc) &&
                        npc.getName().equals(name) && npc.getHealthPercent() > 0
                        && npc.getInteracting() == null && npc.exists();
            }

        };

        //gets the nearest opponent worthy of combat! Note: Worthy opponent is defined above
        //Warning might be null! Maybe. Just Maybe.
        return getNpcs().closest(worthyOpponentFilter);

    }

    public void fightNPC(String NPC_name){
        //if im not in the middle of something...
        if(!getCombat().isFighting() && !myPlayer().isMoving()) {
            NPC npc = getWorthyOpponent(NPC_name);

            //if npc is visible... attack it
            if (npc.isVisible()) {
                if(npc.interact()){
                    new ConditionalSleep(3000, 1000) {
                        @Override
                        public boolean condition() throws InterruptedException {
                            return getCombat().isFighting();
                        }
                    }.sleep();
                }
            }
            //else pan to the npc so that it is visible
            else
            {
                getCamera().toEntity(npc);
            }

        }
    }

    private void handleRunning(int min_energy)
    {
        //A simple way to enable manage run energy
        //If were not running and have more than 75 run energy, RUN.
        if(!settings.isRunning()) {
            if (settings.getRunEnergy() > min_energy) {
                settings.setRunning(true);
            }
        }
    }

    @Override
    public void onPaint(Graphics2D graphics2D) {
        graphics2D.drawString("EsotericRS: Playground Script", 15, 15);
    }
}
