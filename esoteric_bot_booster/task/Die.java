package esoteric_bot_booster.task;

import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.script.MethodProvider;

//This is the 'Die' state -- on met condition the program "dies"
public class Die extends Task {

    public Die(MethodProvider api){
        super(api);
    }

    /*
    Pseudocode:
    if im above Level 30 attack, strength and defense:
        I should process this task
     */
    @Override
    public boolean canProcess() {
        return (api.getSkills().getStatic(Skill.ATTACK) >= 30 && api.getSkills().getStatic(Skill.STRENGTH) >= 30 && api.getSkills().getStatic(Skill.DEFENCE) >= 30);
    }

    /*
    Close the bot process
     */
    @Override
    public void process() {
        api.log("Level 30 attack, strength and defense reached... Congratulations - You REALLY earned it!");

        try {
            api.getBot().getScriptExecutor().stop();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
