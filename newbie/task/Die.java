package newbie.task;

import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.script.MethodProvider;

public class Die extends Task {

    public Die(MethodProvider api){
        super(api);
    }

    @Override
    public boolean canProcess() {
        return (api.getSkills().getStatic(Skill.ATTACK) >= 30 || api.getSkills().getStatic(Skill.STRENGTH) >= 30 || api.getSkills().getStatic(Skill.DEFENCE) >= 30);
    }

    @Override
    public void process() {
        api.getLogoutTab();
        api.getBot().closeSelf();
    }

}
