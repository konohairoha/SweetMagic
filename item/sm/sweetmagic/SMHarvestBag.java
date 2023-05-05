package sweetmagic.init.item.sm.sweetmagic;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import sweetmagic.SweetMagicCore;
import sweetmagic.event.SMSoundEvent;
import sweetmagic.handlers.SMGuiHandler;
import sweetmagic.init.ItemInit;
import sweetmagic.init.tile.inventory.InventoryHarvestBag;

public class SMHarvestBag extends SMItem {

	private final int data;

    public SMHarvestBag(String name, int data) {
		super(name, ItemInit.magicList);
        setMaxStackSize(1);
        this.data = data;
    }

    /**
     * 0 = 種収穫袋
     * 1 = 苗木収穫袋
     */

	@Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {

		if (hand == EnumHand.OFF_HAND) {
			return new ActionResult(EnumActionResult.PASS, player.getHeldItem(hand));
		}

		player.openGui(SweetMagicCore.INSTANCE, SMGuiHandler.HARVESTBAG_GUI, world, 0, -1, -1);
		this.playSound(world, player, SMSoundEvent.ROBE, 0.5F, 1.075F);
		return new ActionResult(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}

	@Override
	public EnumActionResult useStack (World world, EntityPlayer player, ItemStack stack, BlockPos pos, EnumFacing face) {

		TileEntity tile = world.getTileEntity(pos);

		// tileのnullチェック
		if (tile != null && tile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, face.getOpposite())) {

			IItemHandler target = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, face.getOpposite());

			// インベントリを取得
			InventoryHarvestBag bag = new InventoryHarvestBag(player, stack);
			IItemHandlerModifiable inv = bag.inventory;

			// インベントリの分だけ回す
			for (int i = 0; i < inv.getSlots(); i++) {

				// 空なら次へ
				ItemStack bagStack = inv.getStackInSlot(i);
				if (bagStack.isEmpty()) { continue; }

				// 対象のスロット分回す
				for (int k = 0; k < target.getSlots(); k++) {

					// スロットのアイテムを取得
					if (!target.insertItem(k, bagStack, true).isEmpty()) { continue; }

					// アイテムを入れる
					target.insertItem(k, bagStack.copy(), false);
					tile.markDirty();
					bagStack.shrink(bagStack.getCount());
					bag.writeBack();
					break;
				}
			}

			this.playSound(world, player, SMSoundEvent.ROBE, 0.5F, 1.075F);
			return EnumActionResult.SUCCESS;

		}

      return EnumActionResult.PASS;
	}

	public int getData () {
		return this.data;
	}

    //ツールチップの表示
  	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {

  		String text =  "";

  		switch (this.data) {
  		case 0:
  			text = "tip.seed_harvest_bag.name";
  			break;
  		case 1:
  			text = "tip.sapling_harvest_bag.name";
  			break;
  		}

  		List<String> textLine = Arrays.<String>asList(this.getTip(text).split("<br>"));

  		for (String tip : textLine) {
  	  		tooltip.add(I18n.format(TextFormatting.GOLD + tip));
  		}
  	}
}
