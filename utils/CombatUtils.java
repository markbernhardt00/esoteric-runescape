package utils;

import org.osbot.rs07.api.filter.Filter;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.script.MethodProvider;
import org.osbot.rs07.utility.ConditionalSleep;

public final class CombatUtils {

    private CombatUtils() {
    }

    /*
    This method will return 'problem-free' NPC with getName() = name or if none are available it will return NPC == null (bad-btw)
    NOTE: Worthy defined as existing, reachable, has name = name, is not in dying state (0% hp),
    and not interacted with by somebody else.
    */
    public static NPC getWorthyOpponent(MethodProvider api, String name) {
        //This filter will matches with an npc that is reachable, has name = name, has > 0 hp, is not currently
        // being interacted with already, and still exists
        Filter worthyOpponentFilter = new Filter<NPC>() {
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

        if (worthyOpponent == null) {
            api.log("Uh oh... NPC is null");
        }

        return worthyOpponent;
    }

    public static NPC fightNPC(MethodProvider api, String NPC_name) {
        //if im not in the middle of something...

        if (!api.getCombat().isFighting() && !api.myPlayer().isMoving()) {
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
                    api.log("Fighting with NPC: " + npc.getName());
                }
                //web-walk to the npc
                else {
                    api.getWalking().webWalk(npc.getPosition());
                }
            } else {
                api.log("How can I fight this npc, it is null!");
            }
            return npc;
        }
        return null;

    }

    //This method will change styles to balance a characters levels up to the given int
    public static void handleStyleChange(MethodProvider api, int level) {
        if (api.getConfigs().get(43) != 0 && api.getSkills().getStatic(Skill.ATTACK) < level) {
            api.log("Combat training milestone: Player is now attack skill level " + String.valueOf(level) + ". Switching to strength training...");
            utils.WidgetUtils.changeCombatStyle(api, "Stab");
        } else if ((api.getConfigs().get(43) == 0 || api.getConfigs().get(43) == 3) && api.getSkills().getStatic(Skill.ATTACK) >= level && api.getSkills().getStatic(Skill.STRENGTH) < level) {
            api.log("Combat training milestone: Player is now strength skill level " + String.valueOf(level) + ". Switching to defence training...");
            utils.WidgetUtils.changeCombatStyle(api, "Slash");
        } else if ((api.getConfigs().get(43) == 1 || api.getConfigs().get(43) == 2 || api.getConfigs().get(43) == 0) && api.getSkills().getStatic(Skill.STRENGTH) >= level && api.getSkills().getStatic(Skill.ATTACK) >= level && api.getSkills().getStatic(Skill.DEFENCE) < level) {
            utils.WidgetUtils.changeCombatStyle(api, "Block");
            api.log("Combat training milestone: Player is now defence skill level " + String.valueOf(level) + ". Moving to the next step in our progression.");
        }
    }
}