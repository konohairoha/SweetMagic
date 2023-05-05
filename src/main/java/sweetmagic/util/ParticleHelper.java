package sweetmagic.util;

import net.minecraft.client.particle.ParticleManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.client.FMLClientHandler;

public class ParticleHelper {

	public static void spawnHeal(Entity entity, EnumParticleTypes type, int count, double speed, int... particleArgs) {
		if (entity.world instanceof WorldServer) {
			((WorldServer) entity.world).spawnParticle(type, entity.posX, entity.posY + entity.height * 0.5, entity.posZ, count, entity.width * 0.5, entity.height * 0.5, entity.width * 0.5, speed, particleArgs);
		}
	}

	public static void spawnParticle(World world, BlockPos pos, EnumParticleTypes type, int count, double speed) {
		if (world instanceof WorldServer) {
			((WorldServer) world).spawnParticle(type, pos.getX(), pos.getY(), pos.getZ(), count, 0, 0, 0, speed, 0);
		}
	}

	public static void spawnParticle(World world, BlockPos pos, EnumParticleTypes type, double x, double y, double z) {
		if (world instanceof WorldServer) {
			((WorldServer) world).spawnParticle(type, pos.getX(), pos.getY(), pos.getZ(), x, y, z, 0, 0, 0);
		}
	}

	public static void spawnParticle(World world, BlockPos pos, EnumParticleTypes type) {
		if (world instanceof WorldServer) {
			((WorldServer) world).spawnParticle(type, pos.getX() + 0.5F, pos.getY() + 0.33F, pos.getZ() + 0.5F, 8, 0.25, 0.1, 0.25, 0, 0);
		}
	}

	public static void spawnParticle(World world, BlockPos pos, EnumParticleTypes type, int value) {
		if (world instanceof WorldServer) {
			((WorldServer) world).spawnParticle(type, pos.getX() + 0.5F, pos.getY() + 0.33F, pos.getZ() + 0.5F, value, 0.25, 0.1, 0.25, 0, 0);
		}
	}

	public static ParticleManager spawnParticl () {
		return FMLClientHandler.instance().getClient().effectRenderer;
	}
}
