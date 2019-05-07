package esoteric_bot_booster.task;

import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.script.MethodProvider;

public class AquireFood extends Task {

    private static final Area CHICKEN_COOP_AREA_1 = new Area(new int[][]{{ 3225, 3301 }, { 3225, 3295 }, { 3231, 3295 }, { 3231, 3287 }, { 3237, 3287 }, { 3237, 3290 }, { 3237, 3297 }, { 3238, 3298 }, { 3238, 3300 }, { 3236, 3302 }, { 3225, 3302 }});
    private static final Area COW_AREA_1 = new Area(new int[][]{{ 3193, 3300 }, { 3193, 3286 }, { 3195, 3284 }, { 3196, 3282 }, { 3200, 3282 }, { 3201, 3283 }, { 3206, 3283 }, { 3207, 3284 }, { 3212, 3284 }, { 3213, 3285 }, { 3213, 3289 }, { 3214, 3290 }, { 3214, 3293 }, { 3211, 3295 }, { 3211, 3296 }, { 3210, 3297 }, { 3210, 3302 }, { 3206, 3302 }, { 3204, 3301 }, { 3200, 3301 }, { 3200, 3302 }, { 3195, 3302 }});
    private static final Area RANGE_AREA = new Area(3237, 3198, 3229, 3194);
    private static final Position RANGE_POSITION = new Position(3235,3196,0);

    public AquireFood(MethodProvider api){
        super(api);
    }


    /*
    Pseudocode:
    if the player is below 15 combat level:
        acquire raw chicken
        if the players inventory is full and has raw chicken:
            Go to the range in lumbridge and cook the raw chicken
            Drop the burnt chicken
    if the player is above 14 combat level:
        acquire raw beef
        if the players inventory is full and has raw beef:
            Go to the range in lumbridge and cook the raw beef
            Drop the burnt meat
     */
    @Override
    public void process() {
        //Player is of appropriate skill level to easily fight chickens
        boolean chicken_skill = (api.getSkills().getStatic(Skill.ATTACK) < 15 || api.getSkills().getStatic(Skill.STRENGTH) < 15 || api.getSkills().getStatic(Skill.DEFENCE) < 15);
        //Player is of appropriate skill level to easily fight cows
        boolean cow_skill = (api.getSkills().getStatic(Skill.ATTACK) > 14 && api.getSkills().getStatic(Skill.STRENGTH) > 14 && api.getSkills().getStatic(Skill.DEFENCE) > 14);
        boolean stocked_on_cookables = (api.getInventory().getEmptySlotCount() == 0) && (utils.InventoryUtils.countInventoryItems(api, new String[]{"Raw beef", "Raw chicken"}) > 0) ;

        if(chicken_skill && !stocked_on_cookables){
            acquireRawChicken();
        }
        else if (cow_skill && !stocked_on_cookables) {
            aquireRawBeef();
        }
        else if (stocked_on_cookables){
            api.getWalking().webWalk(RANGE_AREA.getCentralPosition());
            cookAtRange(new String[]{"Raw beef", "Raw chicken"});
            utils.SkillUtils.dropBurntFood(api);
        }
    }

    @Override
    /*
    Pseudocode:
    if im out of food:
            if im not moving or in a fight:
                I should process this task
    */
    public boolean canProcess() {
        return (utils.InventoryUtils.countInventoryItems(api, new String[]{"Cooked meat", "Cooked chicken"}) == 0) && !api.getCombat().isFighting() && !api.myPlayer().isMoving();
    }

    //Walks the user to a chicken coop or cow pen depending on what kind of ingredient they need
    private void walkToTaskArea(){

        Area task_area;

        if (api.getSkills().getStatic(Skill.ATTACK) > 14 && api.getSkills().getStatic(Skill.STRENGTH) > 14 && api.getSkills().getStatic(Skill.DEFENCE) > 14){
            task_area = COW_AREA_1;
            api.log("[AcquireFood]: Task area is COW_AREA_1");
        }
        else {
            task_area = CHICKEN_COOP_AREA_1;
            api.log("[AcquireFood]: Task area is CHICKEN_COOP_AREA_1...");
        }

        api.log("[AcquireFood]: Web-walking to task area...");
        api.getWalking().webWalk(task_area);
    }

    //Loots raw chicken from chicken coop, or fights a chicken if there is none already on the ground
    private void acquireRawChicken(){
        utils.WidgetUtils.forceInventoryOpen(api);
        boolean in_chicken_area = (CHICKEN_COOP_AREA_1.contains(api.myPosition()));
        if(in_chicken_area){
            api.log("[AcquireFood]: Acquiring 'Raw chicken'...");
            //Pick up Raw chicken, if there is no Raw chicken you can pick up, hop worlds...
            if(!utils.GroundItemUtils.pickUpItems(api, new String[]{"Raw chicken"})){
                api.log("[AcquireFood]: Couldn't find any 'Raw chicken' here...");
                utils.CombatUtils.fightNPC(api, "Chicken");
            }
        }
        else{
            walkToTaskArea();
        }
    }
    //Loots raw beef from cow pen, or fights a cow if there is none already on the ground
    private void aquireRawBeef(){
        utils.WidgetUtils.forceInventoryOpen(api);
        //Is the player inside the cow area
        boolean in_cow_area = (COW_AREA_1.contains(api.myPosition()));

        if(in_cow_area){
            //Pick up Raw beef, if there is no Raw beef you can pick up, hop worlds...
            if(!utils.GroundItemUtils.pickUpItems(api, new String[]{"Raw beef"})){
                utils.CombatUtils.fightNPC(api, "Cow");
            }
        }
        else{
            walkToTaskArea();
        }
    }

    //Cooks any ingredient with name inside the ingredient_names array
    private void cookAtRange(String[] ingredient_names){
        //Is the player inside the range area
        boolean in_range_area = (RANGE_AREA.contains(api.myPosition()));
        if(in_range_area){
            utils.SkillUtils.cookOnRange(api, ingredient_names);
        }
        else{
            api.log("[AcquireFood]: Web-walking to RANGE_AREA...");
            api.getWalking().webWalk(RANGE_POSITION);
        }
    }
}
