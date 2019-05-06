package newbie.task;

import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.script.MethodProvider;

public class Level10Through20 extends Task {

    private final Area GOBLIN_SPIDER_AREA = new Area(new int[][]{{ 3241, 3250 }, { 3242, 3240 }, { 3245, 3233 }, { 3250, 3228 }, { 3256, 3230 }, { 3257, 3239 }, { 3251, 3251 }});
    private final Area GIANT_RAT_AREA = new Area(new int[][]{{ 3200, 3200 }, { 3193, 3200 }, { 3186, 3208 }, { 3190, 3213 }, { 3199, 3214 }});

    public Level10Through20(MethodProvider api)
    {
        super(api);
    }

    @Override
    public boolean canProcess() {
        boolean not_busy = !api.myPlayer().isMoving() && !api.getCombat().isFighting();
        boolean respects_upper_bound = (api.getSkills().getStatic(Skill.ATTACK) < 15 || api.getSkills().getStatic(Skill.STRENGTH) < 15 || api.getSkills().getStatic(Skill.DEFENCE) < 15);
        boolean respects_lower_bound = (api.getSkills().getStatic(Skill.ATTACK) > 9 && api.getSkills().getStatic(Skill.STRENGTH) > 9 && api.getSkills().getStatic(Skill.DEFENCE) > 9);
        return respects_upper_bound && respects_lower_bound && not_busy;
    }

    @Override
    public void process() {
        Position my_pos = api.myPosition();

        //basically task
        Area task_area = (api.getSkills().getStatic(Skill.ATTACK) < 15 || api.getSkills().getStatic(Skill.STRENGTH) < 15 || api.getSkills().getStatic(Skill.DEFENCE) < 15) ? GOBLIN_SPIDER_AREA : GIANT_RAT_AREA;

        boolean in_goblin_spider_area = GOBLIN_SPIDER_AREA.contains(my_pos);
        boolean in_giant_rat_area = GIANT_RAT_AREA.contains(my_pos);

        if(in_goblin_spider_area) {
            //Choose a random enemy between Goblin or Spider to fight
            int rand_int = utils.RandomUtils.random(1, 2);
            if(rand_int == 1) {
                utils.CombatUtils.fightNPC(api, "Goblin");
            }
            else{
                utils.CombatUtils.fightNPC(api, "Giant spider");
            }
            utils.CombatUtils.handleStyleChange(api, 15);
        }
        else if (in_giant_rat_area){
            utils.CombatUtils.fightNPC(api, "Giant rat");
            utils.CombatUtils.handleStyleChange(api, 20);
        }
        else {
            api.log("Outside of task area - walking to task area...");
            api.getWalking().webWalk(task_area.getRandomPosition());
        }

        utils.PlayerUtils.handleEnergy(api);
    }

}
