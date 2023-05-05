package sweetmagic.util;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import sweetmagic.init.entity.projectile.EntitySittableBlock;

public class SittableUtil {

	public static boolean sitOnBlock(World world, double x, double y, double z, EntityPlayer player, double par6) {
		if (!checkEntity(world, x, y, z, player)) {
			EntitySittableBlock nemb = new EntitySittableBlock(world, x, y, z, par6);
			world.spawnEntity(nemb);
			player.startRiding(nemb);
		}
		return true;
	}

	public static boolean checkEntity(World world, double x, double y, double z, EntityPlayer player) {

		AxisAlignedBB aabb = new AxisAlignedBB(x, y, z, x + 1D, y + 1D, z + 1D).expand(1D, 1D, 1D);
		List<EntitySittableBlock> listEMB = world.getEntitiesWithinAABB(EntitySittableBlock.class, aabb);

		for (EntitySittableBlock mount : listEMB) {
			if (mount.blockPosX == x && mount.blockPosY == y && mount.blockPosZ == z) {
				if (!mount.isBeingRidden()){
					player.startRiding(mount);
				}
				return true;
			}
		}
		return false;
	}

	public static boolean isSomeoneSitting(World world, double x, double y, double z) {

		AxisAlignedBB aabb = new AxisAlignedBB(x, y, z, x + 1D, y + 1D, z + 1D).expand(1D, 1D, 1D);
		List<EntitySittableBlock> listEMB = world.getEntitiesWithinAABB(EntitySittableBlock.class, aabb);

		for (EntitySittableBlock mount : listEMB) {
			if (mount.blockPosX == x && mount.blockPosY == y && mount.blockPosZ == z){
				return mount.isBeingRidden();
			}
		}
		return false;
	}
}
