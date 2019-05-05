package newbie.task;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.script.MethodProvider;

public class Level1Through10 extends Task {

    //Chicken
    private static final Area CHICKEN_COOP_AREA_2 = new Area(new int[][]{{ 3172, 3307 }, { 3172, 3303 }, { 3168, 3300 }, { 3168, 3298 }, { 3169, 3297 }, { 3169, 3296 }, { 3168, 3295 }, { 3168, 3290 }, { 3170, 3288 }, { 3173, 3288 }, { 3174, 3287 }, { 3177, 3287 }, { 3178, 3288 }, { 3184, 3288 }, { 3186, 3290 }, { 3186, 3294 }, { 3187, 3295 }, { 3187, 3298 }, { 3186, 3299 }, { 3186, 3301 }, { 3184, 3303 }, { 3183, 3303 }, { 3182, 3304 }, { 3180, 3304 }, { 3180, 3308 }, { 3172, 3308 }, { 3172, 3304 }});
    private static final Area CHICKEN_COOP_AREA_1 = new Area(new int[][]{{ 3225, 3301 }, { 3225, 3295 }, { 3231, 3295 }, { 3231, 3287 }, { 3237, 3287 }, { 3237, 3290 }, { 3237, 3297 }, { 3238, 3298 }, { 3238, 3300 }, { 3236, 3302 }, { 3225, 3302 }});

    public Level1Through10(MethodProvider api) {
        super(api);
    }

    @Override
    //Attack, Defence, or Strength < 10 means they should be fighting chickens
    public boolean canProcess() {
        boolean inside_skill_range = (api.getSkills().getStatic(Skill.ATTACK) < 10 || api.getSkills().getStatic(Skill.STRENGTH) < 10 || api.getSkills().getStatic(Skill.DEFENCE) < 10);
        return inside_skill_range;
    }

    @Override
    public void process() {
        boolean in_task_area = CHICKEN_COOP_AREA_2.contains(api.myPosition()) || CHICKEN_COOP_AREA_1.contains(api.myPosition());

        if(in_task_area) {
            utils.CombatUtils.fightNPC(api, "Chicken");
        }
        else {
            walkToRandomTaskArea();
        }

        utils.PlayerUtils.handleEnergy(api);
        utils.CombatUtils.handleStyleChange(api, 10);
    }

    //Walks us to a chicken coop
    private void walkToRandomTaskArea(){
        int random_choice = utils.RandomUtils.random(1, 2);
        if(random_choice == 1) {
            api.getWalking().webWalk(CHICKEN_COOP_AREA_1.getRandomPosition());
        }
        else{
            api.getWalking().webWalk(CHICKEN_COOP_AREA_2.getRandomPosition());
        }
    }

    //43:0 - stab 43:1 slash 43:3 block






}