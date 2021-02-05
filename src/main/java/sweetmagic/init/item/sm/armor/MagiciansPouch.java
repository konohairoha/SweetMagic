package sweetmagic.init.item.sm.armor;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.SweetMagicCore;
import sweetmagic.api.iitem.IPouch;
import sweetmagic.handlers.PacketHandler;
import sweetmagic.handlers.SMGuiHandler;
import sweetmagic.init.ItemInit;
import sweetmagic.init.entity.model.ModelRobe;
import sweetmagic.packet.PlayerSoundPKT;
import sweetmagic.util.SoundHelper;

public class MagiciansPouch extends ItemArmor implements IPouch {

	private final int data;

	public MagiciansPouch(String name, ArmorMaterial material, int render, EntityEquipmentSlot slot, int data) {
		super(material, render, slot);
        setUnlocalizedName(name);
        setRegistryName(name);
        this.data = data;
		ItemInit.noTabList.add(this);
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
		ModelRobe next = new ModelRobe(0.375F, slot.getSlotIndex());
		next.setModelAttributes(model);
		return next;
	}

	@Override
  	public void openGUI (World world, EntityPlayer player, ItemStack stack) {

		if (!world.isRemote) {
			player.openGui(SweetMagicCore.INSTANCE, SMGuiHandler.MFPOUCH_GUI, world, 0, -1, -1);

			// クライアント（プレイヤー）へ送りつける
			PacketHandler.sendToPlayer(new PlayerSoundPKT(SoundHelper.S_ROBE, 1F, 0.25F), (EntityPlayerMP) player);
		}
  	}
}
