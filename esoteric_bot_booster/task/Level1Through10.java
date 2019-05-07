package esoteric_bot_booster.task;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.script.MethodProvider;

public class Level1Through10 extends Task {

    private static final Area CHICKEN_COOP_AREA_2 = new Area(new int[][]{{ 3172, 3307 }, { 3172, 3303 }, { 3168, 3300 }, { 3168, 3298 }, { 3169, 3297 }, { 3169, 3296 }, { 3168, 3295 }, { 3168, 3290 }, { 3170, 3288 }, { 3173, 3288 }, { 3174, 3287 }, { 3177, 3287 }, { 3178, 3288 }, { 3184, 3288 }, { 3186, 3290 }, { 3186, 3294 }, { 3187, 3295 }, { 3187, 3298 }, { 3186, 3299 }, { 3186, 3301 }, { 3184, 3303 }, { 3183, 3303 }, { 3182, 3304 }, { 3180, 3304 }, { 3180, 3308 }, { 3172, 3308 }, { 3172, 3304 }});
    private static final Area CHICKEN_COOP_AREA_1 = new Area(new int[][]{{ 3225, 3301 }, { 3225, 3295 }, { 3231, 3295 }, { 3231, 3287 }, { 3237, 3287 }, { 3237, 3290 }, { 3237, 3297 }, { 3238, 3298 }, { 3238, 3300 }, { 3236, 3302 }, { 3225, 3302 }});

    public Level1Through10(MethodProvider api) {
        super(api);
    }

    /*
    Pseudocode:
    if have_enough_food:
        if im below level 10 in any combat skill:
            if im not moving or in a fight
                I should process this task
    */
    @Override
    public boolean canProcess() {
        boolean out_of_food = (utils.InventoryUtils.countInventoryItems(api, new String[]{"Cooked meat", "Cooked chicken"}) == 0);
        boolean not_busy = !api.myPlayer().isMoving() && !api.getCombat().isFighting();
        boolean inside_skill_range = (api.getSkills().getStatic(Skill.ATTACK) < 10 || api.getSkills().getStatic(Skill.STRENGTH) < 10 || api.getSkills().getStatic(Skill.DEFENCE) < 10);
        return inside_skill_range && not_busy && !out_of_food;
    }

    /*
    Pseudocode:
    if I have the energy and am not running: TODO: This should be handled elsewhere
        toggle run
    if I'm ready for a stance switch:
        stance switch
    if im in chicken coop 1:
        Fight a chicken
    if im in chicken coop 2:
        Fight a chicken (3/4 chance) or goblin (1/4 chance) NOTE: There are goblins that wander into this chicken coop
    */
    @Override
    public void process() {

        utils.PlayerUtils.handleEnergy(api);
        utils.CombatUtils.handleStyleChange(api, 10);

        boolean in_chicken_coop_area_1 = CHICKEN_COOP_AREA_1.contains(api.myPosition());
        boolean in_chicken_coop_area_2 = CHICKEN_COOP_AREA_2.contains(api.myPosition());

        if(in_chicken_coop_area_1) {
            utils.CombatUtils.fightNPC(api, "Chicken");
        }
        else if(in_chicken_coop_area_2){
            fightRandomChickenCoop2Enemy();
        }
        else {
            walkToRandomTaskArea();
        }


    }

    //Walks us to a chicken coop
    private void walkToRandomTaskArea(){
        int random_choice = utils.RandomUtils.random(1, 2);
        if(random_choice == 1) {
            api.log("[Levels1Through10] Walking to task area: CHICKEN_COOP_AREA_1");
            api.getWalking().webWalk(CHICKEN_COOP_AREA_1.getRandomPosition());
        }
        else{
            api.log("[Levels1Through10] Walking to task area: CHICKEN_COOP_AREA_2");
            api.getWalking().webWalk(CHICKEN_COOP_AREA_2.getRandomPosition());
        }
    }
    //There are goblins that wander into this chicken coop... 3/4 chance to attack a chicken 1/4 chance to attack a goblin
    private void fightRandomChickenCoop2Enemy(){
        int rand_int = utils.RandomUtils.random(0, 4);
        if(rand_int < 4){
            utils.CombatUtils.fightNPC(api, "Chicken");
        }
        else{
            utils.CombatUtils.fightNPC(api, "Goblin");
        }
    }






}