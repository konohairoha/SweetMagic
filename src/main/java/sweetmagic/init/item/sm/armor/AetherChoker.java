package sweetmagic.init.item.sm.armor;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.api.iitem.IChoker;
import sweetmagic.api.iitem.IMFTool;
import sweetmagic.api.iitem.ISMArmor;
import sweetmagic.init.EnchantInit;
import sweetmagic.init.ItemInit;
import sweetmagic.init.entity.model.ModelPouch;

public class AetherChoker extends ItemArmor implements IChoker, ISMArmor {

	public final int data;
	public int maxMF;
	public int tickTime = 0;
	public static final EntityEquipmentSlot[] ARMORSLOT = new EntityEquipmentSlot[] { EntityEquipmentSlot.CHEST, EntityEquipmentSlot.LEGS, EntityEquipmentSlot.FEET, EntityEquipmentSlot.MAINHAND };

	public AetherChoker(String name, ArmorMaterial material, int render, EntityEquipmentSlot slot, int data, int maxMF) {
		super(material, render, slot);
        setUnlocalizedName(name);
        setRegistryName(name);
        this.data = data;
        this.setMaxMF(maxMF);
		ItemInit.magicList.add(this);
	}

	/**
	 * 0 = ヘルメット
	 * 1 = チェストプレート
	 * 2 = レギンス
	 * 3 = ブーツ
	 */

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

		// MFが空なら終了
		if (this.isEmpty(stack)) { return; }

		this.tickTime++;
		if (this.tickTime % 200 != 0) { return; }

		// スロット分回す
		for (EntityEquipmentSlot slot : ARMORSLOT) {

			ItemStack armor = player.getItemStackFromSlot(slot);
			Item item = armor.getItem();
			if (!(item instanceof IMFTool)) { continue; }

			IMFTool tool =  (IMFTool) item;
			if (tool.isMaxMF(armor)) { continue; }

			// 消費MFを取得
			int useMF = Math.min(this.getHealValue(), this.getMF(stack));
			tool.insetMF(armor, useMF);
			this.setMF(stack, this.getMF(stack) - useMF);

			// MFが空なら終了
			if (this.isEmpty(stack)) { return; }
		}
	}

	public int getHealValue () {

		switch (this.data) {
		case 0: return 100;
		case 1: return 500;
		case 2: return 2000;
		}

		return 0;
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
