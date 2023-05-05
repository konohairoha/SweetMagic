package sweetmagic.init.entity.ai;

import java.util.Random;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.world.World;
import sweetmagic.util.EventUtil;

public class EntityAIDonMov extends EntityAIBase {

	public int tickTime = 20;
	private final EntityLiving living;
	private final IAttributeInstance attri;
	private World world;
	private Random rand;

    public EntityAIDonMov(EntityLiving living) {
        this.living = living;
        this.world = living.world;
        this.rand = this.world.rand;
        this.attri = this.living.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
        this.setMutexBits(~0x0);
    }

    @Override
    public boolean shouldExecute() {
        return this.tickTime > 0;
    }

    // 中断可能か否か
    @Override
    public boolean isInterruptible() {
        return false;
    }

    @Override
    public void startExecuting() {
        if (this.attri.hasModifier(EventUtil.modifierDonmove)) {
        	this.attri.removeModifier(EventUtil.modifierDonmove);
        }
        this.attri.applyModifier(EventUtil.modifierDonmove);
    }

    @Override
    public void resetTask() {
    	this.tickTime = 0;
    	this.attri.removeModifier(EventUtil.modifierDonmove);
    }

    // Updates the task
    @Override
    public void updateTask() {
        --this.tickTime;
    }
}
