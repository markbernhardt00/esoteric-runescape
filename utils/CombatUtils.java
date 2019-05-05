package utils;

import org.osbot.rs07.api.filter.Filter;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.script.MethodProvider;
import org.osbot.rs07.utility.ConditionalSleep;

public final class CombatUtils {

    private CombatUtils(){}

    /*
    This method will return 'problem-free' NPC with getName() = name or if none are available it will return NPC == null (bad-btw)
    NOTE: Worthy defined as existing, reachable, has name = name, is not in dying state (0% hp),
    and not interacted with by somebody else.
    */
    public static NPC getWorthyOpponent(MethodProvider api, String name){
        //This filter will matches with an npc that is reachable, has name = name, has > 0 hp, is not currently
        // being interacted with already, and still exists
        Filter worthyOpponentFilter = new Filter<NPC>(){
            @Override
            public boolean match(NPC npc) {
                return npc != null && api.getMap().canReach(npc) &&
                        npc.getName().equals(name) && npc.getHealthPercent() > 0
                        && npc.getInteracting() == null && npc.exists();
            }

        };

        //gets the nearest opponent worthy of combat! Note: Worthy opponent is defined above
        //Warning might be null! Maybe. Just Maybe.
        NPC worthyOpponent = api.getNpcs().closest(worthyOpponentFilter);

        if(worthyOpponent == null){
            api.log("Uh oh... NPC is null");
        }

        return worthyOpponent;
    }

    public static void fightNPC(MethodProvider api, String NPC_name){
        //if im not in the middle of something...
        if(!api.getCombat().isFighting() && !api.myPlayer().isMoving()) {
            NPC npc = getWorthyOpponent(api, NPC_name);

            //pan to the npc if it is not visible
            if (npc != null) {
                if (!npc.isVisible()) {
                    api.getCamera().toEntity(npc);
                }

                //if npc is too far away to interact with safely
                if (npc.getPosition().distance(api.myPosition()) < 7) {
                    if (npc.interact()) {
                        new ConditionalSleep(3000, 1000) {
                            @Override
                            public boolean condition() throws InterruptedException {
                                return api.getCombat().isFighting();
                            }
                        }.sleep();

                    }
                }
                //web-walk to the npc
                else {
                   api.getWalking().webWalk(npc.getPosition());
                }
            }
            else
            {
                api.log("How can I fight this npc, it is null!");
            }
        }
    }
}
