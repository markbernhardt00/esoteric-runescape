package esoteric_bot_booster.task;

import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.script.MethodProvider;

//TODO: Maybe a 23-30 Task - split up the cow areas 15-30 is a long time for one location...
public class Levels15Through30 extends Task{
    private static final Area COW_AREA_1 = new Area(new int[][]{{ 3193, 3300 }, { 3193, 3286 }, { 3195, 3284 }, { 3196, 3282 }, { 3200, 3282 }, { 3201, 3283 }, { 3206, 3283 }, { 3207, 3284 }, { 3212, 3284 }, { 3213, 3285 }, { 3213, 3289 }, { 3214, 3290 }, { 3214, 3293 }, { 3211, 3295 }, { 3211, 3296 }, { 3210, 3297 }, { 3210, 3302 }, { 3206, 3302 }, { 3204, 3301 }, { 3200, 3301 }, { 3200, 3302 }, { 3195, 3302 }});
    private static final Area COW_AREA_2 = new Area(new int[][]{{ 3242, 3297 }, { 3244, 3282 }, { 3254, 3277 }, { 3256, 3268 }, { 3254, 3256 }, { 3264, 3256 }, { 3263, 3297 }});

    public Levels15Through30(MethodProvider api) {
        super(api);
    }


    /*
    Pseudocode:
    if have_enough_food:
        if im above level 14 and below level 30 in any combat skill:
            if im not moving or in a fight
                I should process this task
    */
    @Override
    public boolean canProcess() {
        //redundant declarations for readability only...
        boolean out_of_food = (utils.InventoryUtils.countInventoryItems(api, new String[]{"Cooked meat", "Cooked chicken"}) == 0);
        boolean not_busy = !api.myPlayer().isMoving() && !api.getCombat().isFighting();
        boolean respects_upper_bound = (api.getSkills().getStatic(Skill.ATTACK) < 30 || api.getSkills().getStatic(Skill.STRENGTH) < 30 || api.getSkills().getStatic(Skill.DEFENCE) < 30);
        boolean respects_lower_bound = (api.getSkills().getStatic(Skill.ATTACK) > 14 && api.getSkills().getStatic(Skill.STRENGTH) > 14 && api.getSkills().getStatic(Skill.DEFENCE) > 14);
        return respects_upper_bound && respects_lower_bound && not_busy && !out_of_food;
    }

    /*
    Pseudocode:
    if I'm ready for a stance switch:
        stance switch
    if im in a cow area:
        Fight a cow
    else:
        Go to a random cow area
    */
    @Override
    public void process() {
        utils.CombatUtils.handleStyleChange(api, 30);

        boolean in_task_area = COW_AREA_1.contains(api.myPosition()) || COW_AREA_2.contains(api.myPosition());

        //TODO: Maybe kill the odd farmer when hes available...
        if(in_task_area) {
            utils.CombatUtils.fightNPC(api, "Cow");
        }
        else {
            walkToRandomTaskArea();
        }
    }

    //Walks the player to a random cow area to promote behavioral diversity
    private void walkToRandomTaskArea(){
        int random_choice = utils.RandomUtils.random(1, 2);
        if(random_choice == 1) {
            api.log("[Levels15Through30] Walking to COW_AREA_1");
            api.getWalking().webWalk(COW_AREA_1.getRandomPosition());
        }
        else{
            api.log("[Levels15Through30] Walking to COW_AREA_2");
            api.getWalking().webWalk(COW_AREA_2.getRandomPosition());
        }
    }
}
