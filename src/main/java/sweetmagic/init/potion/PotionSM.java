package sweetmagic.init.potion;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import sweetmagic.SweetMagicCore;
import sweetmagic.init.PotionInit;

public class PotionSM extends PotionBase {

	public PotionSM (boolean effect, int color, String name, String dir) {
		super(effect, color, name, dir);
		ForgeRegistries.POTIONS.register(this.setRegistryName(SweetMagicCore.MODID, name));
	}

	@Override
	public void performEffect(EntityLivingBase entity, int amplifier) {

		// クリエなら終了
		if (entity instanceof EntityPlayer && ((EntityPlayer) entity).isCreative()) {
			return;
		}

		// 猛毒状態なら
		if (this == PotionInit.deadly_poison && !entity.isPotionActive(PotionInit.grant_poison)) {

			if (entity.getHealth() > 1F) {
				entity.setHealth(entity.getHealth() - 1);
				entity.world.playSound(null, new BlockPos(entity), SoundEvents.ENTITY_PLAYER_HURT, SoundCategory.NEUTRAL, 1F, 1F);
			}

			if (!(entity instanceof EntityPlayer)) { return; }

			EntityPlayer player = (EntityPlayer) entity;
			player.addExhaustion(amplifier + 3);
		}

		// 燃焼状態
		else if (this == PotionInit.flame) {

			if (entity.getHealth() > 1F) {
				entity.setHealth(entity.getHealth() - 1);
				entity.world.playSound(null, new BlockPos(entity), SoundEvents.ENTITY_PLAYER_HURT, SoundCategory.NEUTRAL, 1F, 1F);
			}

			else {
				entity.attackEntityFrom(DamageSource.WITHER, 1F);
			}
		}
	}

	public boolean isReady(int duration, int amplifier) {
		if (this == PotionInit.deadly_poison || this == PotionInit.flame) {
			int j = 32 >> amplifier;

			if (j > 0) {
				return duration % j == 0;
			} else {
				return true;
			}
        }

		return false;
	}
}
