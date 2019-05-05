package newbie.task;

import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.script.MethodProvider;

public class Level10Through15 extends Task {

    //"Goblin"
    //"Giant spider"
    //"Giant rat"
    private final Area GOBLIN_SPIDER_AREA = new Area(3243, 3234, 3254, 3241);
    private final Area GIANT_RAT_AREA = new Area(new int[][]{{ 3200, 3200 }, { 3193, 3200 }, { 3186, 3208 }, { 3190, 3213 }, { 3199, 3214 }});

    public Level10Through15(MethodProvider api)
    {
        super(api);
    }

    @Override
    public boolean canProcess() {
        boolean respects_upper_bound = (api.getSkills().getStatic(Skill.ATTACK) < 15 || api.getSkills().getStatic(Skill.STRENGTH) < 15 || api.getSkills().getStatic(Skill.DEFENCE) < 15);
        boolean respects_lower_bound = (api.getSkills().getStatic(Skill.ATTACK) > 9 || api.getSkills().getStatic(Skill.STRENGTH) > 9 || api.getSkills().getStatic(Skill.DEFENCE) > 9);
        return respects_upper_bound && respects_lower_bound;
    }

    @Override
    public void process() {
        Position my_pos = api.myPosition();

        boolean in_goblin_spider_area = GOBLIN_SPIDER_AREA.contains(my_pos);
        boolean in_giant_rat_area = GIANT_RAT_AREA.contains(my_pos);
        boolean in_task_area = (in_goblin_spider_area || in_giant_rat_area);

        if(in_goblin_spider_area) {
            //Choose a random enemy between Goblin or Spider to fight
            int rand_int = utils.RandomUtils.random(0, 1);
            if(rand_int == 0) {
                utils.CombatUtils.fightNPC(api, "Goblin");
            }
            else {
                utils.CombatUtils.fightNPC(api, "Giant spider");
            }
        }

        else if (in_giant_rat_area){
            utils.CombatUtils.fightNPC(api, "Giant rat");
        }

        else {
            walkToRandomTaskArea();
        }

        utils.PlayerUtils.handleEnergy(api);
        utils.CombatUtils.handleStyleChange(api, 15);
    }

    private void walkToRandomTaskArea(){
        int random_choice = utils.RandomUtils.random(1, 2);
        if(random_choice == 1) {
            api.getWalking().webWalk(GOBLIN_SPIDER_AREA.getRandomPosition());
        }
        else{
            api.getWalking().webWalk(GIANT_RAT_AREA.getRandomPosition());
        }
    }
}
