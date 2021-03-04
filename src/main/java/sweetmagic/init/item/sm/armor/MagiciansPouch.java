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
import net.minecraftforge.items.IItemHandlerModifiable;
import sweetmagic.SweetMagicCore;
import sweetmagic.api.iitem.IAcce;
import sweetmagic.api.iitem.IPouch;
import sweetmagic.handlers.PacketHandler;
import sweetmagic.handlers.SMGuiHandler;
import sweetmagic.init.ItemInit;
import sweetmagic.init.entity.model.ModelPouch;
import sweetmagic.init.tile.inventory.InventoryPouch;
import sweetmagic.packet.PlayerSoundPKT;
import sweetmagic.util.SoundHelper;

public class MagiciansPouch extends ItemArmor implements IPouch {

	private final int data;

	public MagiciansPouch(String name, ArmorMaterial material, int render, EntityEquipmentSlot slot, int data) {
		super(material, render, slot);
        setUnlocalizedName(name);
        setRegistryName(name);
        this.data = data;
		ItemInit.itemList.add(this);
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

	@Override
  	public void openGUI (World world, EntityPlayer player, ItemStack stack) {

		if (!world.isRemote) {
			player.openGui(SweetMagicCore.INSTANCE, SMGuiHandler.MFPOUCH_GUI, world, 0, -1, -1);

			// クライアント（プレイヤー）へ送りつける
			PacketHandler.sendToPlayer(new PlayerSoundPKT(SoundHelper.S_ROBE, 1F, 0.25F), (EntityPlayerMP) player);
		}
  	}

	// 装備してる間機能する内容
	@Override
	public void onArmorTick(World world, EntityPlayer player, ItemStack stack) {

		// インベントリを取得
		InventoryPouch neo = new InventoryPouch(player);
		IItemHandlerModifiable inv = neo.inventory;

		// インベントリの分だけ回す
		for (int i = 0; i < inv.getSlots(); i++) {

			// アイテムを取得し空かアクセサリー以外なら次へ
			ItemStack st = inv.getStackInSlot(i);
			if (st.isEmpty() || !(st.getItem() instanceof IAcce)) { continue; }

			// アクセサリーの取得
			IAcce acce = (IAcce) st.getItem();

			try {
				// アクセサリーのonupdate呼び出し
				acce.onUpdate(world, player, st);
			}

			catch (Throwable e) { }
		}
	}

	//アイテムにダメージを与える処理を無効
	@Override
	public void setDamage(ItemStack stack, int damage) {
		return;
	}
}
