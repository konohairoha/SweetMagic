package sweetmagic.init.item.sm.magic;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.api.iitem.IMFTool;
import sweetmagic.init.EnchantInit;
import sweetmagic.init.ItemInit;
import sweetmagic.init.item.sm.sweetmagic.SMItem;
import sweetmagic.util.EventUtil;

public class AetherFlashLight extends SMItem implements IMFTool {

	private int maxMF = 0;
	private final int useMF = 200;

	public AetherFlashLight(String name) {
		super(name, ItemInit.magicList);
        this.setMaxStackSize(1);
		this.setMaxMF(2000);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {

		// MFが足りないら終了
		ItemStack stack = player.getHeldItem(hand);
		if (this.getMF(stack) < this.useMF && !player.isCreative()) { return new ActionResult(EnumActionResult.PASS, stack); }

		// 周囲のえんちちー取得
		AxisAlignedBB aabb = player.getEntityBoundingBox().grow(12.5D);
		List<EntityLiving> entityList = world.getEntitiesWithinAABB(EntityLiving.class, aabb);

		if (!entityList.isEmpty()) {

			boolean flag = false;

			for (EntityLiving entity : entityList) {

				if (!(entity instanceof IMob)) { continue; }

				// ボス以外なら
				if (entity.isNonBoss()) {
					entity.addPotionEffect(new PotionEffect(MobEffects.GLOWING, 600, 0, true, true));
				}

				// ボスなら
				else {
					NBTTagCompound tags = entity.getEntityData();

					// 目くらましを受けていないら
					if (!tags.hasKey("isLight")) {
						EventUtil.tameAIDonmov(entity, 3);
						tags.setBoolean("isLight", true);
					}
				}

				flag = true;
			}

			// クリエ以外ならMF消費とクールタイム付与
			if (!player.isCreative() && flag) {
				this.setMF(stack, this.getMF(stack) - this.useMF);
				player.getCooldownTracker().setCooldown(this, 600);
			}

			return new ActionResult(EnumActionResult.SUCCESS, stack);
		}

		// クリエ以外ならクールタイム付与
		if (!player.isCreative()) {
			player.getCooldownTracker().setCooldown(this, 100);
		}

		return new ActionResult(EnumActionResult.SUCCESS, stack);
	}

	//ツールチップの表示
  	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
  		List<String> textLine = Arrays.<String>asList(this.getTip("tip.aether_flashlight.name").split("<br>"));
  		for (String tip : textLine) {
  	  		tooltip.add(I18n.format(TextFormatting.GOLD + tip));
  		}
  	}

	// エンチャレベル取得
	public int getEnchantLevel (Enchantment enchant, ItemStack stack) {
		return Math.min(EnchantmentHelper.getEnchantmentLevel(enchant, stack), 10);
	}

  	// 最大MFを取得
	@Override
  	public int getMaxMF (ItemStack stack) {
  		return this.maxMF + (this.getEnchantLevel(EnchantInit.maxMFUP, stack) * 5) * (this.maxMF / 100);
  	}

	@Override
	public void setMaxMF(int maxMF) {
		this.maxMF = maxMF;
	}

	@Override
	public boolean showDurabilityBar(ItemStack stack) {
		return this.getMF(stack) < this.getMaxMF(stack);
	}

	public double getDurabilityForDisplay(ItemStack stack) {
		return 1D - ( (double) this.getMF(stack) / (double) this.getMaxMF(stack) );
	}
}
