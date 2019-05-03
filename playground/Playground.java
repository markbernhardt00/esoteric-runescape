package playground;

import org.osbot.rs07.api.Combat;
import org.osbot.rs07.api.GroundItems;
import org.osbot.rs07.api.Settings;
import org.osbot.rs07.api.filter.Filter;
import org.osbot.rs07.api.model.Entity;
import org.osbot.rs07.api.model.GroundItem;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;
import org.osbot.rs07.utility.ConditionalSleep;

import java.awt.*;
import java.util.Random;

@ScriptManifest(author = "EsotericRS", info = "", logo = "", name = "Playground Script", version = 0.1)
public class Playground extends Script {
    enum States { SEARCHING_FOR_NPC, MOVING_TO_NPC, FIGHTING_NPC, LOOTING, IDLE, TRAVELING }

    private States state;
    private String ground_items[];



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

        if(state == States.FIGHTING_NPC)
        {
            state = getCombat().isFighting() ? States.FIGHTING_NPC : States.IDLE;
        }

        if(!getCombat().isFighting() && !myPlayer().isMoving()) {
            checkGroundItems("Bones");
        }







        //This is the tick rate the script will poll this onLoop() method at
        //Tick rate is randomized to decrease predictability over time
        return (600 + (int) (Math.random() * (1200 - 600)));
    }

    //returns an opponent worthy of our combat
    private NPC getWorthyOpponent(String name){

        state = States.SEARCHING_FOR_NPC;

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

    //engages in combat with the most worthy opponent with name NPC_name.
    public void fightNPC(String NPC_name){
        //if im not in the middle of something...
        if(!getCombat().isFighting() && !myPlayer().isMoving()) {
            NPC npc = getWorthyOpponent(NPC_name);

            //pan to the npc if it is not visible
            if (!npc.isVisible()) {
                getCamera().toEntity(npc);
            }

            //if npc is too far away to interact with safely
            if (npc.getPosition().distance(myPosition()) < 7)
            {
                state = States.MOVING_TO_NPC;
                if(npc.interact()){
                    new ConditionalSleep(3000, 1000) {
                        @Override
                        public boolean condition() throws InterruptedException {
                            boolean is_fighting = getCombat().isFighting();
                            if(is_fighting)
                            {
                                state=States.FIGHTING_NPC;
                            }
                            return is_fighting;
                        }
                    }.sleep();

                }
            }
            //get closer to the npc
            else {
                travelToEntity(npc);
            }
        }
    }

    public void travelToEntity(Entity entity)
    {

        state = States.TRAVELING;
        //Getting within 4 tiles of an entity -- pretty close for safe interactions
        if(getWalking().walk(entity)){
            new ConditionalSleep(12000, 6000) {
                @Override
                public boolean condition() throws InterruptedException {
                    boolean closeEnough = entity.getPosition().distance(myPosition()) < 4;
                    if(closeEnough){
                        state = States.IDLE;
                    }
                    return closeEnough;
                }
            }.sleep();
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

    private void checkGroundItems(String ground_item)
    {
        GroundItem ground_bones = getGroundItems().closest(gi -> gi != null && gi.getName().equals(ground_item) && getMap().canReach(gi));
        if (ground_bones != null) //if it exists
        {
            boolean closeEnough = ground_bones.getPosition().distance(myPosition()) < 7;
            if (closeEnough) {


                int lastCount = getInventory().getEmptySlotCount();
                if (ground_bones.interact("Take")) {
                    new ConditionalSleep(3000, 1000) {
                        @Override
                        public boolean condition() throws InterruptedException {
                            return getInventory().getEmptySlotCount() < lastCount;
                        }
                    }.sleep();
                }
            }
        }
        else
        {
            travelToEntity(ground_bones);
        }

    }

    @Override
    public void onPaint(Graphics2D graphics2D) {
        graphics2D.drawString("EsotericRS: Playground Script", 15, 15);
    }
}
