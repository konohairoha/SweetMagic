package sweetmagic.init.entity.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.MathHelper;

public class EntityAIAnger extends EntityAIBase {

    public final EntityLiving myself;
    private int tickMax = 300;
    private int tick = tickMax;
    private EntityLivingBase target;

    public EntityAIAnger(EntityLiving myselfIn, EntityLivingBase targetIn) {
        this.myself = myselfIn;
        this.target = targetIn;
        setMutexBits(1);
    }

    @Override
    public boolean shouldExecute() {
        return this.tick > 0;
    }

    /// ■中断可能か否か
    @Override
    public boolean isInterruptible() {
        return false;
    }

    @Override
    public void startExecuting(){}

    @Override
    public void resetTask() {
    	this.tick = 0;
    }

    @Override
    public void updateTask() {
    	this.tick = MathHelper.clamp(--this.tick, 0, tickMax);
    	this.myself.setAttackTarget(this.target);
    }

	public void setTarget(EntityLivingBase targetIn) {
		if (this.tick == 0) {
			this.target = targetIn;
			this.tick = tickMax;
		}
	}
}