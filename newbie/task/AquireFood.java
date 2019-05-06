package newbie.task;

import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.script.MethodProvider;

public class AquireFood extends Task {

    private static final Area CHICKEN_COOP_AREA_1 = new Area(new int[][]{{ 3225, 3301 }, { 3225, 3295 }, { 3231, 3295 }, { 3231, 3287 }, { 3237, 3287 }, { 3237, 3290 }, { 3237, 3297 }, { 3238, 3298 }, { 3238, 3300 }, { 3236, 3302 }, { 3225, 3302 }});
    private static final Area COW_AREA_1 = new Area(new int[][]{{ 3193, 3300 }, { 3193, 3286 }, { 3195, 3284 }, { 3196, 3282 }, { 3200, 3282 }, { 3201, 3283 }, { 3206, 3283 }, { 3207, 3284 }, { 3212, 3284 }, { 3213, 3285 }, { 3213, 3289 }, { 3214, 3290 }, { 3214, 3293 }, { 3211, 3295 }, { 3211, 3296 }, { 3210, 3297 }, { 3210, 3302 }, { 3206, 3302 }, { 3204, 3301 }, { 3200, 3301 }, { 3200, 3302 }, { 3195, 3302 }});
    private static final Area RANGE_AREA = new Area(3237, 3197, 3230, 3195);

    public AquireFood(MethodProvider api){
        super(api);
    }

    @Override
    /*
    Pseudocode:
    if im out of food:
            if im not moving or in a fight:
                I should be getting food
    */
    public boolean canProcess() {
        boolean out_of_food = (utils.InventoryUtils.countInventoryItems(api, new String[]{"Cooked meat", "Cooked chicken"}) == 0) && !api.getCombat().isFighting() && !api.myPlayer().isMoving();
        return out_of_food;
    }

    @Override
    //This gets the player food when they are out in the way most available to all players...
    //Mostly put this in here for anti-pattern tbh - I remember I used to cook chickens and beef when i was a f2p freshspawn
    public void process() {
        //Player is of appropriate skill level to easily fight chickens
        boolean chicken_skill = (api.getSkills().getStatic(Skill.ATTACK) < 15 && api.getSkills().getStatic(Skill.STRENGTH) < 15 && api.getSkills().getStatic(Skill.DEFENCE) < 15);
        //Player is of appropriate skill level to easily fight cows
        boolean cow_skill = (api.getSkills().getStatic(Skill.ATTACK) > 14 && api.getSkills().getStatic(Skill.STRENGTH) > 14 && api.getSkills().getStatic(Skill.DEFENCE) > 14);
        boolean has_enough_cookables = utils.InventoryUtils.countInventoryItems(api, new String[]{"Raw beef", "Raw chicken"}) > 12;

        if(chicken_skill && !has_enough_cookables){
            aquireRawChicken();
        }
        else if (cow_skill && !has_enough_cookables) {
            aquireRawBeef();
        }
        else if (has_enough_cookables){
            //walk to range for cooking
            api.getWalking().webWalk(RANGE_AREA.getCentralPosition());

            cookAtRange(new String[]{"Raw beef", "Raw chicken"});
            dropBurntFood();
        }
    }

    private void walkToTaskArea(){

        Area task_area;

        if (api.getSkills().getStatic(Skill.ATTACK) > 14 && api.getSkills().getStatic(Skill.STRENGTH) > 14 && api.getSkills().getStatic(Skill.DEFENCE) > 14){
            task_area = COW_AREA_1;
            api.log("[AquireFood]: Task area is COW_AREA_1");
        }
        else {
            task_area = CHICKEN_COOP_AREA_1;
            api.log("[AquireFood]: Task area is CHICKEN_COOP_AREA_1...");
        }

        api.log("[AquireFood]: Web-walking to task area...");
        api.getWalking().webWalk(task_area);
    }

    private void aquireRawChicken(){
        boolean in_chicken_area = (CHICKEN_COOP_AREA_1.contains(api.myPosition()));
        if(in_chicken_area){
            api.log("[AquireFood]: Aquiring 'Raw chicken'...");
            //Pick up Raw chicken, if there is no Raw chicken you can pick up, hop worlds...
            if(!utils.GroundItemUtils.pickUpItems(api, new String[]{"Raw chicken"})){
                api.log("[AquireFood]: Couldn't find any 'Raw chicken' here...");
                utils.CombatUtils.fightNPC(api, "Chicken");
            }
        }
        else{
            walkToTaskArea();
        }
    }

    private void aquireRawBeef(){
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

    private void cookAtRange(String[] ingredient_names){
        //Is the player inside the range area
        boolean in_range_area = (RANGE_AREA.contains(api.myPosition()));
        if(in_range_area){
            utils.SkillUtils.cookOnRange(api, ingredient_names);
        }
        else{
            api.log("[AquireFood]: Web-walking to RANGE_AREA...");
            api.getWalking().webWalk(RANGE_AREA.getRandomPosition());
        }
    }

    private void dropBurntFood(){
        utils.SkillUtils.dropBurntFood(api);
    }
}
