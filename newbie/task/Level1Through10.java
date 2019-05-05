package newbie.task;
import org.osbot.rs07.api.Combat;
import org.osbot.rs07.api.filter.Filter;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.model.Entity;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.script.MethodProvider;
import org.osbot.rs07.utility.ConditionalSleep;
import utils.CombatUtils;

import java.util.Random;

public class Level1Through10 extends Task {

    private static final Area CHICKEN_COOP_AREA_2 = new Area(new int[][]{{ 3172, 3307 }, { 3172, 3303 }, { 3168, 3300 }, { 3168, 3298 }, { 3169, 3297 }, { 3169, 3296 }, { 3168, 3295 }, { 3168, 3290 }, { 3170, 3288 }, { 3173, 3288 }, { 3174, 3287 }, { 3177, 3287 }, { 3178, 3288 }, { 3184, 3288 }, { 3186, 3290 }, { 3186, 3294 }, { 3187, 3295 }, { 3187, 3298 }, { 3186, 3299 }, { 3186, 3301 }, { 3184, 3303 }, { 3183, 3303 }, { 3182, 3304 }, { 3180, 3304 }, { 3180, 3308 }, { 3172, 3308 }, { 3172, 3304 }});
    private static final Area CHICKEN_COOP_AREA_1 = new Area(new int[][]{{ 3225, 3301 }, { 3225, 3295 }, { 3231, 3295 }, { 3231, 3287 }, { 3237, 3287 }, { 3237, 3290 }, { 3237, 3297 }, { 3238, 3298 }, { 3238, 3300 }, { 3236, 3302 }, { 3225, 3302 }});

    public Level1Through10(MethodProvider api) {
        super(api);
    }

    @Override
    //Attack, Defence, or Strength < 10 means they should be fighting chickens
    public boolean canProcess() {
        boolean inside_skill_range = (api.getSkills().getStatic(Skill.ATTACK) < 10 && api.getSkills().getStatic(Skill.STRENGTH) < 10 && api.getSkills().getStatic(Skill.DEFENCE) < 10);
        return inside_skill_range;
    }

    @Override
    public void process() {
        if(CHICKEN_COOP_AREA_2.contains(api.myPosition()) || CHICKEN_COOP_AREA_1.contains(api.myPosition())) {
            utils.CombatUtils.fightNPC(api, "Chicken");
        }
        else {
            walkToTaskArea();
        }

        handleStyleChange();
    }

    //This method will swap styles all the way up to level 10, allowing even distribution of combat skill points
    private void handleStyleChange(){
        if(api.getConfigs().get(43) != 0 && api.getSkills().getStatic(Skill.ATTACK) <= 10)
        {
            utils.WidgetUtils.changeCombatStyle(api,"Stab");
        }
        else if((api.getConfigs().get(43) == 0 || api.getConfigs().get(43) == 3) && api.getSkills().getStatic(Skill.ATTACK) >= 10)
        {
            utils.WidgetUtils.changeCombatStyle(api,"Slash");
        }
        else if((api.getConfigs().get(43) == 1 || api.getConfigs().get(43) == 2) && api.getSkills().getStatic(Skill.STRENGTH) >= 10)
        {
            utils.WidgetUtils.changeCombatStyle(api,"Block");
        }
    }

    //Walks us to a chicken coop
    private void walkToTaskArea(){
        int random_choice = utils.RandomUtils.random(1, 2);
        if(random_choice == 1) {
            api.getWalking().webWalk(CHICKEN_COOP_AREA_1.getRandomPosition());
            System.out.println("hello");
        }
        else{
            api.getWalking().webWalk(CHICKEN_COOP_AREA_2.getRandomPosition());
        }
    }

    //43:0 - stab 43:1 slash 43:3 block






}