package sweetmagic.util;

import javax.annotation.Nonnull;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

public class TeleportUtil extends Teleporter {

    public TeleportUtil(WorldServer world, double x, double y, double z) {
        super(world);
        this.worldServer = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    private final WorldServer worldServer;
    private double x, y, z;

    @Override
    public void placeInPortal(@Nonnull Entity entity, float rotationYaw) {
        this.worldServer.getBlockState(new BlockPos((int) this.x, (int) this.y, (int) this.z));
		entity.setPosition(this.x, this.y, this.z);
		entity.motionX = 0.0f;
		entity.motionY = 0.0f;
		entity.motionZ = 0.0f;
    }

	public static void teleportToDimension(EntityPlayer player, int dimension, BlockPos pos) {
		teleportToDimension(player, dimension, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
	}

    public static void teleportToDimension(EntityPlayer player, int dimension, double x, double y, double z) {

        int oldDim = player.getEntityWorld().provider.getDimension();
        if (!(player instanceof EntityPlayerMP)) { return; }
        EntityPlayerMP playerMP = (EntityPlayerMP) player;
        WorldServer worldServer = ((EntityPlayerMP) player).getEntityWorld().getMinecraftServer().getWorld(dimension);
        player.addExperienceLevel(0);

		if (worldServer == null || worldServer.getMinecraftServer() == null) {
			throw new IllegalArgumentException("Dimension: " + dimension + " doesn't exist!");
        }

        worldServer.getMinecraftServer().getPlayerList().transferPlayerToDimension(playerMP, dimension, new TeleportUtil(worldServer, x, y, z));
        player.setPositionAndUpdate(x, y, z);
        if (oldDim == 1) {
            player.setPositionAndUpdate(x, y, z);
            worldServer.spawnEntity(player);
            worldServer.updateEntityWithOptionalForce(player, false);
        }
    }
}
