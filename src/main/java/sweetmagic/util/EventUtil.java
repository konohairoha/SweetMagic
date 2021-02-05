package sweetmagic.util;

import java.util.UUID;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import sweetmagic.init.entity.ai.EntityAIDonMov;

public class EventUtil {

	public static final UUID UUID_donmov  = UUID.fromString("6fd1ce57-8e37-504d-f859-6262b644ef19");
	public static final AttributeModifier modifierDonmove = (new AttributeModifier(UUID_donmov, "donmov", -1.0d, 2)).setSaved(false);

    // 敵AIを動かさない
    public static void tameAIDonmov(EntityLiving target, float power) {

        boolean isLearning = false;
        int tickTime = (int) (power * 20);
        for (EntityAITasks.EntityAITaskEntry entry : target.tasks.taskEntries) {
            if (entry.action instanceof EntityAIDonMov) {
                EntityAIDonMov ai = (EntityAIDonMov)entry.action;
                ai.tickTime = tickTime;
                isLearning = true;
                break;
            }
        }

        if (!isLearning) {
            EntityAIDonMov ai = new EntityAIDonMov(target);
            ai.tickTime = tickTime;
            target.tasks.addTask(0, ai);
        }
    }
}
