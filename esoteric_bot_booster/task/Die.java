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
    if im above 30 combat level:
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
        api.log("Level 30 Combat reached... Congratulations - You REALLY earned it!");
        api.getBot().closeSelf();
    }

}
