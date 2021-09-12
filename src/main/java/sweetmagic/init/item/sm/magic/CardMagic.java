package sweetmagic.init.item.sm.magic;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import sweetmagic.init.ItemInit;
import sweetmagic.init.entity.projectile.EntityBaseMagicShot;
import sweetmagic.init.entity.projectile.EntityNomal;
import sweetmagic.init.item.sm.sweetmagic.SMItem;

public class CardMagic extends SMItem {

	private int data;

	public CardMagic (String name, int value, int meta) {
		super(name, ItemInit.magicList);
		this.setMaxStackSize(1);
		this.setMaxDamage(value);
		this.data = meta;
	}

	//右クリックをした際の処理
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		player.setActiveHand(hand);
		return new ActionResult(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}

	//右クリックでチャージした量で射程を伸ばす
	public static float getArrowVelocity(int charge) {
		float f = (float) charge / 20F;
		f = (f * f + f * 2F) / 3F;
		return f > 1F ? 1F : f;
	}

	//右クリックチャージをやめたときに矢を消費せずに矢を射る
	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase living, int timeLeft) {

		if (!(living instanceof EntityPlayer )) { return; }

		EntityPlayer player = (EntityPlayer) living;
		int i = this.getMaxItemUseDuration(stack) - timeLeft;
		float f = getArrowVelocity(i);

		if (!world.isRemote) {
			EntityBaseMagicShot entity = null;
			if (this.data == 0) {
				entity = new EntityNomal(world, player);
			}

			float shot = 1F;
			entity.setDamage(entity.getDamage() + 4);
			if (f == 1f) {
				entity.setDamage(entity.getDamage() + 4);
				shot = 1.5F;
			}

			entity.setHeadingFromThrower(player, player.rotationPitch, player.rotationYaw, 0.0F, 2.5F, 0.0F); //　弾の初期弾速と弾のばらつき
			entity.shoot(entity.motionX, entity.motionY, entity.motionZ, shot, 0); // 射撃速度
			world.spawnEntity(entity);
			stack.damageItem(1, player);
			player.getCooldownTracker().setCooldown(this, 5);
		}
		world.playSound(null, new BlockPos(player), SoundEvents.ENTITY_BLAZE_SHOOT, SoundCategory.NEUTRAL, 0.5F, 0.67F);
	}

	//最大１分間出来るように
	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 72000;
	}

	//右クリックをした際の挙動を弓に
	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
		return EnumAction.BOW;
	}
}
