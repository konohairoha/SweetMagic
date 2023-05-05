package sweetmagic.init.item.sm.armor;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.SweetMagicCore;
import sweetmagic.api.iitem.IAcce;
import sweetmagic.api.iitem.IPouch;
import sweetmagic.handlers.PacketHandler;
import sweetmagic.handlers.SMGuiHandler;
import sweetmagic.init.ItemInit;
import sweetmagic.init.entity.model.ModelPorch;
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
		ItemInit.magicList.add(this);
	}

	/**
	 * 0 = マギア
	 * 1 = マスターマギア
	 */

	// 特定のアイテムで修復可能に
	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        return false;
    }

	@Override
	@SideOnly(Side.CLIENT)
	public ModelBiped getArmorModel(EntityLivingBase living, ItemStack stack, EntityEquipmentSlot slot, ModelBiped model) {
		ModelPorch next = new ModelPorch(0.375F, 2);
		next.setModelAttributes(model);
		return next;
	}

	@Override
  	public void openGUI (World world, EntityPlayer player, ItemStack stack) {

		// クライアント（プレイヤー）へ送りつける
		if (!world.isRemote) {
			player.openGui(SweetMagicCore.INSTANCE, SMGuiHandler.MFPOUCH_GUI, world, 0, -1, -1);
			PacketHandler.sendToPlayer(new PlayerSoundPKT(SoundHelper.S_ROBE, 1F, 0.25F), (EntityPlayerMP) player);
		}
  	}

	// 装備してる間機能する内容
	@Override
	public void onArmorTick(World world, EntityPlayer player, ItemStack stack) {

		int gravityCount = 0;
		List<Item> blackList = new ArrayList<>();

		// インベントリを取得
		List<ItemStack> stackList = new InventoryPouch(player).getStackList();
		if (stackList.isEmpty()) { return; }

		try {

			// インベントリの分だけ回す
			for (ItemStack st : stackList) {

				// アクセサリーの取得
				Item item = st.getItem();
				if (blackList.contains(item) || st.isEmpty()) { continue; }

				IAcce acce = (IAcce) item;

				if (item == ItemInit.gravity_pendant) {
					gravityCount++;

					// 3回以上吸い込んでるならパス
					if (gravityCount > 2) { continue; }
				}

				// アクセサリーのonupdate呼び出し
				acce.onUpdate(world, player, st);

				// アップデート系で重複禁止ならブラックリストに入れる
				if (acce.isUpdateType() && !acce.isDuplication()) {
					blackList.add(item);
				}
			}

		}

		catch (Throwable e) { }
	}

	//アイテムにダメージを与える処理を無効
	@Override
	public void setDamage(ItemStack stack, int damage) { }

	// スロット数
	public int getSlotSize () {

		switch (this.data) {
		case 0: return 8;
		case 1: return 16;
		}

		return 8;
	}

	public static boolean hasAcce (EntityPlayer player, Item item) {

        Item porchItem = player.getItemStackFromSlot(EntityEquipmentSlot.LEGS).getItem();

        if (porchItem instanceof IPouch) {

			List<ItemStack> stackList = new InventoryPouch(player).getStackList();

			// インベントリの分だけ回す
			for (ItemStack acce : stackList) {

				// アクセサリーの取得
				if (acce.getItem() != item) { continue; }

				return true;
			}
        }

        return false;
    }
}
