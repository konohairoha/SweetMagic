package sweetmagic.init.item.sm.accessorie;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.SweetMagicCore;
import sweetmagic.api.iitem.IAcce;
import sweetmagic.init.item.sm.eitem.SMAcceType;
import sweetmagic.init.item.sm.sweetmagic.SMItem;

public class SMAcce extends SMItem implements IAcce {

	public final int data;
	public final boolean isDup;
	public SMAcceType type;
	private static final AttributeModifier SPEED_BOOST = new AttributeModifier("speed_boost", 0.15, 0).setSaved(false);

	public SMAcce (String name, SMAcceType type, boolean isDup, int data) {
        super(name);
        this.setAcceType(type);
        this.isDup = isDup;
		this.data = data;
		this.setMaxStackSize(1);
	}

	/**
	 * 0 = 勇者の腕輪
	 * 1 = ウィッチ・スクロール
	 * 2 = 灼熱の宝玉
	 * 3 = 人魚の衣
	 * 4 = 血吸の指輪
	 * 5 = エメラルドピアス
	 * 6 = フォーチュンリング
	 */

	@Override
	public List<String> magicToolTip(List<String> toolTip) {

		switch (this.data) {
		case 0:
			toolTip.add("tip.warrior_bracelet.name");
			break;
		case 1:
			toolTip.add("tip.witch_scroll.name");
			break;
		case 2:
			toolTip.add("tip.scorching_jewel.name");
			break;
		case 3:
			toolTip.add("tip.mermaid_veil.name");
			break;
		case 4:
			toolTip.add("tip.blood_sucking_ring.name");
			break;
		case 5:
			toolTip.add("tip.emelald_pias.name");
			break;
		case 6:
			toolTip.add("tip.fortune_ring.name");
			break;
		}

		return toolTip;
	}

	// 効果が発動できるかどうか
	public boolean canUseEffect (World world, EntityPlayer player, ItemStack stack) {

		switch (this.data) {
		// 血吸の指輪
		case 4:
			return player.getHealth() > 1;
		}

		return true;
	}

	// 常に発動したいならここで
	public void onUpdate(World world, EntityPlayer player, ItemStack stack) {

		switch (this.data) {
		case 2:
			// 灼熱の宝玉
			this.scorchEffect(world, player, stack);
			break;
		case 3:
			// 血吸の指輪
			this.mermaidEffect(world, player, stack);
		case 6:
			// フォーチュンリング
			player.addPotionEffect(new PotionEffect(MobEffects.LUCK, 201, 1, true, false));
			break;
		}
	}

	// 灼熱の宝玉
	public boolean scorchEffect (World world, EntityPlayer player, ItemStack stack) {

		player.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 201, 0, true, false));
		player.setFire(0);

		if (player.isInLava() ) {

			player.fallDistance = 0F;

			if (!player.capabilities.isFlying && SweetMagicCore.proxy.isJumpPressed()) {
				player.motionY += 0.04825;
			}

			if (player.moveForward < 0) {
				player.motionX *= 0.1;
				player.motionZ *= 0.1;
			} else if (player.moveForward >= 0 && player.motionX * player.motionX + player.motionY * player.motionY + player.motionZ * player.motionZ < 2.75D) {
				player.motionX *= 1.875D;
				player.motionZ *= 1.875D;
			}
		}

		return true;
	}

	// 人魚の衣
	public boolean mermaidEffect (World world, EntityPlayer player, ItemStack stack) {

		if (player.isInWater() ) {

			player.setAir(300);
			player.fallDistance = 0F;

			if (!player.capabilities.isFlying && SweetMagicCore.proxy.isJumpPressed()) {
				player.motionY += 0.04825;
			}

			if (player.moveForward < 0) {
				player.motionX *= 0.1;
				player.motionZ *= 0.1;
			} else if (player.moveForward >= 0 && player.motionX * player.motionX + player.motionY * player.motionY + player.motionZ * player.motionZ < 2.75D) {
				player.motionX *= 1.175D;
				player.motionZ *= 1.175D;
			}
		}

		return true;
	}

	// 装備品の効果
	public boolean acceeffect(World world, EntityPlayer player, ItemStack stack) {

		switch (this.data) {
		case 0:
			this.addPotion(player, MobEffects.STRENGTH, 600, 1, true);
			return true;
		case 4:

			float health = player.getHealth();

			// クリエじゃなければ体力を減らす
			if (!player.isCreative()) {
				player.setHealth(health - 0.5F);
			}

			return true;
		}

		return false;
	}

	//ツールチップの表示
  	@Override
  	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {

		String tipEntity = "";

		switch (this.data) {
		case 0:
			tipEntity = new TextComponentTranslation("entity.braveskeleton.name", new Object[0]).getFormattedText();
			break;

		}

  		if (!tipEntity.equals("")) {
  	  		String tip = new TextComponentTranslation("tip.dropboss.name", new Object[0]).getFormattedText() + " " + tipEntity;
  			tooltip.add(I18n.format(TextFormatting.GREEN + tip));
  		}
  	}

	@Override
	public SMAcceType getAcceType() {
		return this.type;
	}

	// アクセサリーの設定
	@Override
	public void setAcceType(SMAcceType type) {
		this.type = type;
	}

	// 効果の重複
	@Override
	public boolean isDuplication () {
		return this.isDup;
	}
}
