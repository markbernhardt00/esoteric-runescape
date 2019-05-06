package utils;

import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.script.MethodProvider;

public final class SkillUtils {
    private SkillUtils(){}

    public static void chopTree(MethodProvider api, String tree_name){
        RS2Object tree = api.getObjects().closest(obj -> obj != null && obj.getName().equals(tree_name) && api.getMap().canReach(obj));

        if(!api.myPlayer().isAnimating()){
            if(tree != null && tree.interact("Chop down")) {
                new Sleep(() -> api.myPlayer().isAnimating() || !tree.exists(), 5000).sleep();
            }

        }
    }
}
