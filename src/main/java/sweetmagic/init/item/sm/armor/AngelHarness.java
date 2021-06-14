package sweetmagic.init.item.sm.armor;

import com.google.common.collect.Multimap;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.SweetMagicCore;
import sweetmagic.api.iitem.IHarness;
import sweetmagic.api.iitem.ISMArmor;
import sweetmagic.init.EnchantInit;
import sweetmagic.init.ItemInit;
import sweetmagic.init.PotionInit;
import sweetmagic.init.entity.model.ModelPouch;

public class AngelHarness extends ItemArmor implements IHarness, ISMArmor {

	public final int data;
	public int maxMF;
	public int tickTime = 0;
    private static final AttributeModifier MOVESPEED = new AttributeModifier("ArmorSpeed", 0.1892D, 2);

	public AngelHarness(String name, ArmorMaterial material, int render, EntityEquipmentSlot slot, int data, int maxMF) {
		super(material, render, slot);
        setUnlocalizedName(name);
        setRegistryName(name);
        this.data = data;
        this.setMaxMF(maxMF);
//        this.moveSpeed = new AttributeModifier("ArmorSpeed", 0.1892D, 2);
		ItemInit.magicList.add(this);
	}

	/**
	 * 0 = エンジェルハーネス
	 * 1 = エーテルブーツ
	 */

	@Override
	public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot slot) {
		Multimap<String, AttributeModifier> map = super.getItemAttributeModifiers(slot);
		if (this.data == 1 && slot == EntityEquipmentSlot.FEET) {
			map.put(SharedMonsterAttributes.MOVEMENT_SPEED.getName(), MOVESPEED);
		}
		return map;
	}

	// 特定のアイテムで修復可能に
	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        return false;
    }

	@Override
	@SideOnly(Side.CLIENT)
	public ModelBiped getArmorModel(EntityLivingBase living, ItemStack stack, EntityEquipmentSlot slot, ModelBiped model) {
		ModelPouch next = new ModelPouch(0.375F, slot.getIndex());
		next.setModelAttributes(model);
		return next;
	}

	// 装備してる間機能する内容
	@Override
	public void onArmorTick(World world, EntityPlayer player, ItemStack stack) {

		player.fallDistance = 0;

		// 飛行中なら終了
		if (player.capabilities.isFlying || this.getMF(stack) <= 0) { return; }

		// エンジェルハーネスなら
		if (this.data == 0) {

			// MFが１以上かつジャンプしてるなら
			if (SweetMagicCore.proxy.isJumpPressed()) {

				if (player.isPotionActive(PotionInit.breakblock)) {
					if (!player.capabilities.isFlying) {
						player.motionY += 0.035F;
					}
				}

				else {
					player.motionY += 0.2F;
					this.tickTime++;
				}
			}

			// スニークしてないなら
			else if (!player.isSneaking()) {
				player.motionY *= 0.825;
			}
		}

		else {
			player.addPotionEffect(new PotionEffect(PotionInit.resistance_blow, 201, 0));
		}

		// ダッシュボタン押してるなら
		if (SweetMagicCore.proxy.isDushPressed() && Math.abs(player.motionX) + Math.abs(player.motionZ) < 2) {
			player.motionX *= 1.1F;
			player.motionZ *= 1.1F;
			this.tickTime++;
		}

		// 4tickに一回MFを消費
		if (this.tickTime >= 4) {
			this.tickTime -= 4;
			this.setMF(stack, this.getMF(stack) - 1);
		}
	}

	// エンチャントエフェクト描画
	@Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack) {
	    return false;
    }

	@Override
	public boolean showDurabilityBar(ItemStack stack) {
		return this.getMF(stack) != this.getMaxMF(stack);
	}

	public double getDurabilityForDisplay(ItemStack stack) {
		return 1D - ( (double) this.getMF(stack) / (double) this.getMaxMF(stack) );
	}

	//アイテムにダメージを与える処理を無効
	@Override
	public void setDamage(ItemStack stack, int damage) { }

	@Override
	public int getMaxMF(ItemStack stack) {
		int addMaxMF = (this.getEnchantLevel(EnchantInit.maxMFUP, stack) * 5) * (this.maxMF / 100);
  		return this.maxMF + addMaxMF;
	}

	@Override
	public void setMaxMF(int maxMF) {
		this.maxMF = maxMF;
	}
}
