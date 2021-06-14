package sweetmagic.init.item.sm.armor;

import java.util.List;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.SweetMagicCore;
import sweetmagic.api.iitem.IMFTool;
import sweetmagic.api.iitem.IRobe;
import sweetmagic.handlers.PacketHandler;
import sweetmagic.handlers.SMGuiHandler;
import sweetmagic.init.EnchantInit;
import sweetmagic.init.ItemInit;
import sweetmagic.init.entity.model.ModelRobe;
import sweetmagic.packet.PlayerSoundPKT;
import sweetmagic.util.SoundHelper;

public class MagiciansRobe extends ItemArmor implements IRobe, IMFTool {

	private final int data;
	public int maxMF;

	public MagiciansRobe(String name, ArmorMaterial material, int render, EntityEquipmentSlot slot, int data, int maxMF) {
		super(material, render, slot);
        setUnlocalizedName(name);
        setRegistryName(name);
        this.data = data;
        this.setMaxMF(maxMF);
		ItemInit.magicList.add(this);
	}

	/**
	 * 0 = エーテルローブ
	 * 1 = クロノスローブ
	 * 2 = フェアリーローブ
	 * 3 = ウィンディーネローブ
	 * 4 = イフリートローブ
	 * 5 = サンドリヨンローブ
	 */

	// 特定のアイテムで修復可能に
	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        return false;
    }

	@Override
	@SideOnly(Side.CLIENT)
	public ModelBiped getArmorModel(EntityLivingBase living, ItemStack stack, EntityEquipmentSlot slot, ModelBiped model) {
		ModelRobe next = new ModelRobe(0.375F, slot.getSlotIndex());
		next.setModelAttributes(model);
		return next;
	}

	@Override
  	public void openGUI (World world, EntityPlayer player, ItemStack stack) {

		if (!world.isRemote) {
			player.openGui(SweetMagicCore.INSTANCE, SMGuiHandler.MFROBE_GUI, world, 0, -1, -1);

			// クライアント（プレイヤー）へ送りつける
			PacketHandler.sendToPlayer(new PlayerSoundPKT(SoundHelper.S_ROBE, 1F, 0.25F), (EntityPlayerMP) player);
		}
  	}

	// SMモブのダメージカット率（1だとダメージカット無し）
	@Override
	public float getSMMobDamageCut () {
		return this.data != 0 ? 0.65F : 0.75F;
	}

	// 魔法ダメージカット率（1だとダメージカット無し）
	@Override
	public float getMagicDamageCut () {
		return this.data != 0 ? 0.65F : 0.75F;
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

	// ツールチップの表示
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag advanced) {

		if (this.data != 5) { return; }

		String tip = new TextComponentTranslation("tip.sandryon_robe.name", new Object[0]).getFormattedText();
		tooltip.add(I18n.format(TextFormatting.GREEN  + tip));
	}
}
