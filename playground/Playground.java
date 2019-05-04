package playground;

import org.osbot.rs07.api.Inventory;
import org.osbot.rs07.api.filter.Filter;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.model.Entity;
import org.osbot.rs07.api.model.GroundItem;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;
import org.osbot.rs07.utility.ConditionalSleep;
import javax.swing.*;
import java.lang.reflect.InvocationTargetException;


import java.awt.*;

//TODO: If not enough food, visit bank and get food
// TODO: Areas for Chickens, Goblins
// TODO: Possibly Combat Stance Switch?
// TODO: Visit different areas depending on combat level i.e. chickens until 10, goblins until 20, cows until 30...
// TODO: Badly needs seperation of Concerns

@ScriptManifest(author = "EsotericRS", info = "", logo = "", name = "Playground Script", version = 0.1)
public class Playground extends Script {

    private GUI gui = new GUI();
    private Boolean bury_bones;
    private String food;

    private static final Position[] COW_PEN_POSITION_ARRAY_1 =
            { new Position(3264, 3297, 0),
            new Position(3265, 3255, 0),
            new Position(3253, 3255, 0),
            new Position(3253, 3271, 0),
            new Position(3251,3275,0),
            new Position(3241,3285,0),
            new Position(3244,3290,0),
            new Position(3241,3297,0) };
    private static final Area COW_AREA_1 = new Area(COW_PEN_POSITION_ARRAY_1);

    private static final Position[] COW_PEN_POSITION_ARRAY_2 =
            { new Position(3264, 3297, 0),
                    new Position(3194, 3287, 0),
                    new Position(3211, 3286, 0),
                    new Position(3208, 3300, 0),
                    new Position(3195,3301,0) };
    private static final Area COW_AREA_2 = new Area(COW_PEN_POSITION_ARRAY_2);

    private static final Position[] CHICKEN_COOP_POSITION_ARRAY_2 = { new Position(3183, 3292, 0),
            new Position(3183,3301,0),
            new Position(3171,3300,0),
            new Position(3171, 3290, 0) };

    private static final Area CHICKEN_COOP_AREA_2 = new Area(CHICKEN_COOP_POSITION_ARRAY_2);

    private static final Position[] GOBLIN_SPIDER_POSITION_ARRAY = { new Position(3255, 3239, 0),
            new Position(3244,3241,0),
            new Position(3248,3230,0),
            new Position(3263, 3220, 0) };

    private static final Area GOBLIN_SPIDER_ARRAY = new Area(GOBLIN_SPIDER_POSITION_ARRAY);

    @Override
    //Executes once on script start
    public void onStart() throws InterruptedException {
        try {
            SwingUtilities.invokeAndWait(() -> {
                gui = new GUI();
                gui.open();
            });
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            stop();
            return;
        }
        if (!gui.isStarted()) {
            stop();
            return;
        }

        bury_bones = gui.getIsBonesChecked();
    }

    //JIC user closes the dialog and doesn't click the start button


    @Override
    //Executes once on script exit
    public void onExit() throws InterruptedException {
        if (gui != null) {
            gui.close();
        }
    }

    @Override
    //Executes every 600 milliseconds
    public int onLoop() throws InterruptedException {
        /*
        Pseudocode:
        if hp < 100:
            eat
        if not in combat and not moving:
            if not in the task rea:
                go to the task area
            else if my inventory is full:
                bury the bones
            else if there are buryBones on the ground:
                pick them up
            else:
                fight an NPC
        */

        handleEnergy();

        if(myPlayer().getHealthPercent() < 50)
        {
            eatFood("Lobster");
        }
        //if not in combat and not moving -> do something
        if(!getCombat().isFighting() && !myPlayer().isMoving() && !myPlayer().isUnderAttack())
        {
            //if player is not in cow area
            if(!CHICKEN_COOP_AREA_2.contains(myPosition()))
            {
                log("Not in cow area, web-walking to cow area");
                webWalkToArea(CHICKEN_COOP_AREA_2);
            }
            //if my inventory is full -> bury the buryBones if that option was selected
            else if(getInventory().isFull() &&  bury_bones)
            {
                log("Bury bones because my inventory is full");
                buryBones();
            }
            //else if i am burying bones check for/pick up some bones to bury
            else if(checkForBones() != null && bury_bones)
            {
                log("Pick up some available bones");
                pickUp("Bones");
            }
            //Nothing else on the agenda, so time to fight
            else
            {
                log("Fight a Cow");
                fightNPC("Chicken");
            }
        }

        //High tick-rate, probably too high, we'll see
        return (2400);
    }

    //Toggles 'Run' in player settings if they have more than 65 energy
    public void handleEnergy(){
        if (getSettings().getRunEnergy() > 65)
            getSettings().setRunning(true);
    }

    //Bury all the bones in the players inventory
    private void buryBones() {
        Inventory inv = getInventory();

       if(inv.contains("Bones")){
           if(inv.getItem("Bones").interact("Bury")){

               //TODO: Improve the speed on this
               new ConditionalSleep(45000, 1000)
               {
                   @Override
                   public boolean condition() throws InterruptedException {
                       inv.getItem("Bones").interact("Bury");
                       return !getInventory().contains("Bones");
                   }
               }.sleep();
           }
       }
       else {
           inv.deselectItem();
       }
    }

    //Walk to the cow pen near lumbridge
    private void webWalkToArea(Area area)
    {
        getWalking().webWalk(area);
    }

    /*
    This method will return 'problem-free' NPC with getName() = name or if none are available it will return NPC == null (bad-btw)
    NOTE: Worthy defined as existing, reachable, has name = name, is not in dying state (0% hp),
    and not interacted with by somebody else.
    */
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
       NPC worthyOpponent = getNpcs().closest(worthyOpponentFilter);

       if(worthyOpponent == null){
           log("Uh oh... NPC is null");
       }

       return worthyOpponent;

    }

    private void eatFood(String food_name)
    {
        Inventory inv = getInventory();

        int before_eating_count = inv.getCapacity();


        if(inv.contains(food_name)){


            if(inv.getItem(food_name).interact("Eat")){

                new ConditionalSleep(3000, 1000)
                {

                    @Override
                    public boolean condition() throws InterruptedException {
                        return (getInventory().getCapacity() < before_eating_count ? true : false);
                    }
                }.sleep();
            }
        }
        else {
            inv.deselectItem();
        }
    }

    //engages in combat with the most worthy opponent with name NPC_name.
    private void fightNPC(String NPC_name){
        //if im not in the middle of something...
        if(!getCombat().isFighting() && !myPlayer().isMoving()) {
            NPC npc = getWorthyOpponent(NPC_name);

            //pan to the npc if it is not visible
            if (npc != null) {
                if (!npc.isVisible()) {
                    getCamera().toEntity(npc);
                }

                //if npc is too far away to interact with safely
                if (npc.getPosition().distance(myPosition()) < 7) {
                    if (npc.interact()) {
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
            else
            {
                log("How can I fight this npc, it is null!");
            }
        }
    }

    //Web walks to entity.getPosition
    private void travelToEntity(Entity entity)
    {
        //Getting within 4 tiles of an entity -- pretty close for safe interactions
        if(getWalking().webWalk(entity.getPosition())){
            new ConditionalSleep(12000, 6000) {
                @Override
                public boolean condition() throws InterruptedException {
                    return entity.getPosition().distance(myPosition()) < 7;
                }
            }.sleep();
        }
    }

    //Returns The closest GroundItem with getName("Bones) or if none are available it will return GroundItem == null
    private GroundItem checkForBones(){
        return getGroundItems().closest(gi -> gi != null && gi.getName().equals("Bones") && gi.getPosition().distance(myPosition()) < 7);
    }

    /*
    This will pick up the closest ground_item with .getName(ground_item),
    It will web-walk to the item and pan the camera if necessary
    */
    private void pickUp(String ground_item)
    {
        GroundItem item = getGroundItems().closest(gi -> gi != null && gi.getName().equals(ground_item) && getMap().canReach(gi));
        if (item != null) //if it exists
        {
            boolean closeEnough = item.getPosition().distance(myPosition()) < 7;
            if (closeEnough) {
                if(!item.isVisible())
                {
                    log("I cant see the bones, so I am panning the camera");
                    getCamera().toEntity(item);
                }
                int lastCount = getInventory().getEmptySlotCount();
                if (item.interact("Take")) {
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
                log("Traveling to the item: " + item);
                travelToEntity(item);
            }
        }


    }

    @Override
    public void onPaint(Graphics2D graphics2D) {
        graphics2D.drawString("EsotericRS: Playground Script", 15, 15);
    }
}
