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
    public void placeInPortal(@Nonnull Entity entity, float rotYaw) {
        this.worldServer.getBlockState(new BlockPos((int) this.x, (int) this.y, (int) this.z));
		entity.setPosition(this.x, this.y, this.z);
		entity.motionX = 0F;
		entity.motionY = 0F;
		entity.motionZ = 0F;
    }

	public static void teleportToDimension(EntityPlayer player, int dimension, BlockPos pos) {
		teleportToDimension(player, dimension, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
	}

    public static void teleportToDimension(EntityPlayer player, int dim, double x, double y, double z) {

        if (!(player instanceof EntityPlayerMP)) { return; }

        EntityPlayerMP playerMP = (EntityPlayerMP) player;
        WorldServer worldServer = playerMP.getEntityWorld().getMinecraftServer().getWorld(dim);

		if (worldServer == null || worldServer.getMinecraftServer() == null) {
			throw new IllegalArgumentException("Dimension: " + dim + " doesn't exist!");
        }

        worldServer.getMinecraftServer().getPlayerList().transferPlayerToDimension(playerMP, dim, new TeleportUtil(worldServer, x, y, z));
        player.setPositionAndUpdate(x, y, z);
        player.addExperienceLevel(0);
    }
}
