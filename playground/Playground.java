package playground;

import org.osbot.rs07.api.Inventory;
import org.osbot.rs07.api.filter.Filter;
import org.osbot.rs07.api.model.Entity;
import org.osbot.rs07.api.model.GroundItem;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;
import org.osbot.rs07.utility.ConditionalSleep;

import java.awt.*;

@ScriptManifest(author = "EsotericRS", info = "", logo = "", name = "Playground Script", version = 0.1)
public class Playground extends Script {

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
        /*
        Pseudocode:
        if not in combat and not moving:
            if my inventory is full:
                bury the bones
            else if there are bones on the ground:
                pick them up
            else:
                fight an NPC
        */

        GroundItem ground_bones = getGroundItems().closest(gi -> gi != null && gi.getName().equals("Bones") && getMap().canReach(gi));
        boolean pickable_bones = ground_bones != null;

        //if not in combat and not moving -> do something
        if(!getCombat().isFighting() && !myPlayer().isMoving())
        {
            //if my inventory is full -> bury the bones
            if(getInventory().isFull())
            {
                bury_bones();
            }
            //else if there are bones on the ground -> pick them up
            else if(pickable_bones)
            {
                pickUp("Bones");
            }
            //else fight an NPC
            else
            {
                fightNPC("Goblin");
            }
        }

        //This is the tick rate the script will poll this onLoop() method at
        return (400);
    }

    private void bury_bones() {
        Inventory inv = getInventory();
       if(inv.contains("Bones")){
           if(inv.getItem("Bones").interact("Bones"));
       }
       else {
           inv.deselectItem();
       }
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

    //engages in combat with the most worthy opponent with name NPC_name.
    private void fightNPC(String NPC_name){
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
                if(npc.interact()){
                    new ConditionalSleep(3000, 1000) {
                        @Override
                        public boolean condition() throws InterruptedException {
                            return getCombat().isFighting();
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

    private void travelToEntity(Entity entity)
    {
        //Getting within 4 tiles of an entity -- pretty close for safe interactions
        if(getWalking().walk(entity)){
            new ConditionalSleep(12000, 6000) {
                @Override
                public boolean condition() throws InterruptedException {
                    return entity.getPosition().distance(myPosition()) < 4;
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

    private void pickUp(String ground_item)
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
            else
            {
                travelToEntity(ground_bones);
            }
        }


    }

    @Override
    public void onPaint(Graphics2D graphics2D) {
        graphics2D.drawString("EsotericRS: Playground Script", 15, 15);
    }
}
