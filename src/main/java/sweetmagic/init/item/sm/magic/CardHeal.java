package sweetmagic.init.item.sm.magic;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import sweetmagic.event.SMSoundEvent;
import sweetmagic.init.ItemInit;
import sweetmagic.init.item.sm.sweetmagic.SMItem;
import sweetmagic.util.ParticleHelper;

public class CardHeal extends SMItem {

	public CardHeal(String name, int value) {
		super(name, ItemInit.magicList);
		this.setMaxStackSize(1);
		this.setMaxDamage(value);
	}

	//右クリックをした際の処理
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		player.setActiveHand(hand);
		ItemStack stack = player.getHeldItem(hand);
		if (player.getHealth() != player.getMaxHealth()) {
			player.setHealth(player.getHealth() + 6);
			ParticleHelper.spawnHeal(player, EnumParticleTypes.VILLAGER_HAPPY, 16, 1, 4);
			world.playSound(null, new BlockPos(player), SMSoundEvent.HEAL, SoundCategory.NEUTRAL, 0.175F, 1.25F);
			stack.damageItem(1, player);
			return new ActionResult(EnumActionResult.SUCCESS, player.getHeldItem(hand));
		}
		return new ActionResult(EnumActionResult.PASS, player.getHeldItem(hand));
	}

	@Override
	public boolean isEnchantable(ItemStack stack) {
		return false;
	}

	@Override
	public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
		return false;
	}
}
