package esoteric_bot_booster.task;

import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.script.MethodProvider;

public class Level10Through15 extends Task {

    private static final Area GOBLIN_SPIDER_AREA = new Area(new int[][]{{ 3241, 3250 }, { 3242, 3240 }, { 3245, 3233 }, { 3250, 3228 }, { 3256, 3230 }, { 3257, 3239 }, { 3251, 3251 }});
    private static final Area GIANT_RAT_AREA = new Area(new int[][]{{ 3200, 3200 }, { 3193, 3200 }, { 3186, 3208 }, { 3190, 3213 }, { 3199, 3214 }});

    public Level10Through15(MethodProvider api)
    {
        super(api);
    }

    @Override
    /*
    Pseudocode:
    if have_enough_food:
        if im above level 9 and below level 15 in any combat skill:
            if im not moving or in a fight
                I should process this task
    */
    public boolean canProcess() {
        boolean out_of_food = (utils.InventoryUtils.countInventoryItems(api, new String[]{"Cooked meat", "Cooked chicken"}) == 0);
        boolean not_busy = !api.myPlayer().isMoving() && !api.getCombat().isFighting();
        boolean respects_upper_bound = (api.getSkills().getStatic(Skill.ATTACK) < 15 || api.getSkills().getStatic(Skill.STRENGTH) < 15 || api.getSkills().getStatic(Skill.DEFENCE) < 15);
        boolean respects_lower_bound = (api.getSkills().getStatic(Skill.ATTACK) > 9 && api.getSkills().getStatic(Skill.STRENGTH) > 9 && api.getSkills().getStatic(Skill.DEFENCE) > 9);
        return respects_upper_bound && respects_lower_bound && not_busy && !out_of_food;
    }

    @Override
    /*
    Pseudocode:
    if I have the energy and am not running: TODO: This should be handled elsewhere
        toggle run
    if I'm ready for a stance switch:
        stance switch
    if im in a the goblin_spider area:
        Fight random(goblin, spider)
    else if im in the giant_rat_area:
        Fight a giant rat
    else:
        Go to the appropriate task area (goblin_spider area for < 13 combat level, giant_rat_area for >= 13 combat level)
    */
    public void process() {

        utils.PlayerUtils.handleEnergy(api);
        utils.CombatUtils.handleStyleChange(api, 15);

        Position my_pos = api.myPosition();
        Area task_area = (api.getSkills().getStatic(Skill.ATTACK) < 13 || api.getSkills().getStatic(Skill.STRENGTH) < 13 || api.getSkills().getStatic(Skill.DEFENCE) < 13) ? GOBLIN_SPIDER_AREA : GIANT_RAT_AREA;

        boolean in_goblin_spider_area = GOBLIN_SPIDER_AREA.contains(my_pos);
        boolean in_giant_rat_area = GIANT_RAT_AREA.contains(my_pos);

        if(in_goblin_spider_area) {
            fightRandomNPC();
        }
        else if (in_giant_rat_area){
            utils.CombatUtils.fightNPC(api, "Giant rat");
        }
        else {
            api.log("[Levels10Through15]: Outside of task area - walking to task area...");
            api.getWalking().webWalk(task_area.getRandomPosition());
        }


    }

    //Choose a random enemy between Goblin or Spider to fight
    private void fightRandomNPC(){
        int rand_int = utils.RandomUtils.random(1, 2);

        if(rand_int == 1) {
            utils.CombatUtils.fightNPC(api, "Goblin");
        }
        else{
            utils.CombatUtils.fightNPC(api, "Giant spider");
        }
    }

}
