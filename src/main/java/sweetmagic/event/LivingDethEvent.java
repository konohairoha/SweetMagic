package sweetmagic.event;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import sweetmagic.init.PotionInit;
import sweetmagic.util.ParticleHelper;

public class LivingDethEvent {

	@SubscribeEvent
	public void onLivingDeathEvent(LivingDeathEvent event) {

		if (event.getEntityLiving() == null) { return; }

		// ポーションがアクティブじゃないなら
		EntityLivingBase living = event.getEntityLiving();
		if (!living.isPotionActive(PotionInit.refresh_effect)) { return; }

		// ポーションレベルが0なら終了
		int power = living.getActivePotionEffect(PotionInit.refresh_effect).getAmplifier();
		if (power == 0) { return; }

		// 死亡キャンセル
		event.setCanceled(true);

		if (living instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) living;
			if (player.capabilities.isCreativeMode && player.getName().equals("Konohairoha")) {
				living.setHealth(living.getMaxHealth());
				return;
			}
		}

		living.setHealth(living.getMaxHealth() / 2);
		living.removePotionEffect(PotionInit.refresh_effect);

		// パーティクルスポーン
		World world = living.world;
		ParticleHelper.spawnHeal(living, EnumParticleTypes.VILLAGER_HAPPY, 16, 1, 4);
		ParticleHelper.spawnHeal(living, EnumParticleTypes.FIREWORKS_SPARK, 64, world.rand.nextGaussian() * 0.1, 4);
		world.playSound(null, new BlockPos(living), SoundEvents.ITEM_TOTEM_USE, SoundCategory.NEUTRAL, 0.25F, 1F);
	}
}
