package esoteric_bot_booster.task;

import org.osbot.rs07.api.map.constants.Banks;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.api.ui.EquipmentSlot;
import org.osbot.rs07.script.MethodProvider;
import utils.Sleep;

public class FreshSpawn extends Task{

    public FreshSpawn(MethodProvider api){
        super(api);
    }

    /*
    Pseudocode:
    if the character is detected to be a fresh spawn off tutorial island:
        I should process this task
     */
    @Override
    public boolean canProcess() {
        //This is a lazy way of determining whether or not a char is a fresh spawn. TODO: Less vulnerable solution
        return (utils.InventoryUtils.countInventoryItems(api, new String[]{"Bucket", "Pot", "Bread", "Shortbow"}) == 4);
    }

    @Override
    /*
    Pseudocode:
    equip Bronze sword and Wooden shield
    go to lumbridge upper bank
    drop off tutorial island garbage
     */
    public void process() {
        equipStarterEquipment();
        dropAllToBank();
    }

    //equips starter sword and shield
    private void equipStarterEquipment(){
        if(api.getInventory().contains("Bronze sword")){
            api.log("Equipping a bronze sword");
            if(api.getEquipment().equip(EquipmentSlot.WEAPON, "Bronze sword")){
                Sleep.sleepUntil(() -> api.getEquipment().contains("Bronze sword"), 5000);
            }
            api.log("[FreshSpawn]: Equipped Bronze sword");
        }
        if(api.getInventory().contains("Wooden shield")){
            if(api.getEquipment().equip(EquipmentSlot.WEAPON, "Wooden shield")){
                Sleep.sleepUntil(() -> api.getEquipment().contains("Wooden shield"), 5000);
            }
            api.log("[FreshSpawn]: Equipped Wooden shield");
        }

    }

    //Drops the rest of the fresh spawns starter items off at the bank
    private void dropAllToBank(){
        api.log("[FreshSpawn]: Walking to lumbridge_upper bank");
        api.getWalking().webWalk(Banks.LUMBRIDGE_UPPER);

        RS2Object bank_booth = api.getObjects().closest(obj -> obj != null && obj.getName().equals("Bank booth") && api.getMap().canReach(obj));
        if(bank_booth != null){
            if(bank_booth.interact()){
                utils.Sleep.sleepUntil(() -> api.getBank().isOpen(), 5000);
            }
            if(api.getBank().isOpen()){
                api.getBank().depositAll();
            }
        }
    }
}
