package sweetmagic.init.entity.projectile;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntitySittableBlock extends Entity {

	public int blockPosX;
	public int blockPosY;
	public int blockPosZ;

	public EntitySittableBlock(World world) {
		super(world);
		this.noClip = true;
		this.height = 0.01F;
		this.width = 0.01F;
	}

	public EntitySittableBlock(World world, double x, double y, double z, double y0ffset) {
		this(world);
		this.blockPosX = (int) x;
		this.blockPosY = (int) y;
		this.blockPosZ = (int) z;
		this.setPosition(x + 0.5D, y + y0ffset, z + 0.5D);
	}

	public EntitySittableBlock(World world, double x, double y, double z, double y0ffset, int rot, double offset) {
		this(world);
		this.blockPosX = (int) x;
		this.blockPosY = (int) y;
		this.blockPosZ = (int) z;
		this.setPostionConsideringRotation(x + 0.5D, y + y0ffset, z + 0.5D, rot, offset);
	}

	public void setPostionConsideringRotation(double x, double y, double z, int rot, double offset) {
		switch (rot) {
		case 2:
			z += offset;
			break;
		case 0:
			z -= offset;
			break;
		case 3:
			x -= offset;
			break;
		case 1:
			x += offset;
			break;
		}
		this.setPosition(x, y, z);
	}

	@Override
	public double getMountedYOffset() {
		return this.height * 0D;
	}

	@Override
	protected boolean shouldSetPosAfterLoading() {
		return false;
	}

	@Override
	public void onEntityUpdate() {
		if (!this.world.isRemote){
			if (!this.isBeingRidden() || this.world.isAirBlock(new BlockPos(this.blockPosX, this.blockPosY, this.blockPosZ))){
				this.setDead();
				this.world.updateComparatorOutputLevel(this.getPosition(), this.world.getBlockState(getPosition()).getBlock());
			}
		}
	}

	@Override
	protected void entityInit() {}
	@Override
	public void readEntityFromNBT(NBTTagCompound tags) {}
	@Override
	public void writeEntityToNBT(NBTTagCompound tags) {}
}
