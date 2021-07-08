package sweetmagic.event;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import sweetmagic.init.PotionInit;

public class LivingPotionEvent {

	// Updateイベント
	@SubscribeEvent
	public void livingEvent (LivingFallEvent event) {

		// 飛躍上昇なら
		EntityLivingBase entity = event.getEntityLiving();
		if (!entity.isPotionActive(MobEffects.JUMP_BOOST)) { return; }

		entity.fallDistance = 0;
	}

	// テレポートイベント
	@SubscribeEvent
	public void teleportEvent(EnderTeleportEvent event) {

		// 重力状態以外なら終了
		EntityLivingBase entity = event.getEntityLiving();
		if (!entity.isPotionActive(PotionInit.gravity)) { return; }

		event.setCanceled(true);
	}

	// ブロック破壊イベント
	@SubscribeEvent
	public void onBlockBreakEvent(BlockEvent.BreakEvent event) {

		// クリエか破壊無効がついてないなら終了
		EntityPlayer player =  event.getPlayer();
		if (!player.isPotionActive(PotionInit.breakblock) || player.isCreative()) { return; }

		// FullCubeかつ採掘速度が1以上の場合はキャンセル
		World world = event.getWorld();
		BlockPos pos = event.getPos();
		IBlockState state = event.getState();
		Block block = state.getBlock();
		if ( ( state.getBlockHardness(world, pos) < 1D && block != Blocks.QUARTZ_BLOCK ) || !block.isFullCube(state) || block == Blocks.MOB_SPAWNER ) { return; }

		event.setCanceled(true);
	}
}
