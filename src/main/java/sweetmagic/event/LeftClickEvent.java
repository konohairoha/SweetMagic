package sweetmagic.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import sweetmagic.SweetMagicCore;
import sweetmagic.api.iitem.IWand;
import sweetmagic.handlers.PacketHandler;
import sweetmagic.packet.WandLeftClickPKT;

@Mod.EventBusSubscriber(value = Side.CLIENT, modid = SweetMagicCore.MODID)
public class LeftClickEvent {

    @SubscribeEvent
    public static void onLeftClick(PlayerInteractEvent.LeftClickEmpty event) {

        EntityPlayer player = event.getEntityPlayer();
        if (!player.isSneaking()) { return; }

        ItemStack stack = player.getHeldItemMainhand();
        if (!(stack.getItem() instanceof IWand)) { return; }

        ChangeSlot(stack);
    }

    @SubscribeEvent
    public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {

        if (event.isCanceled()) { return; }

        EntityPlayer player = event.getEntityPlayer();
        if (!player.isSneaking()) { return; }

        ItemStack stack = player.getHeldItemMainhand();
        if (!(stack.getItem() instanceof IWand)) { return; }

        event.setCanceled(true);
        ChangeSlot(stack);
    }

    public static void ChangeSlot (ItemStack stack) {

    	IWand wand = (IWand) stack.getItem();
    	NBTTagCompound tags = wand.getNBT(stack);
    	boolean flag = tags.getBoolean(IWand.FAVFLAG);

    	// お気に入りに登録してるスロットの取得
    	int slot = !flag ? tags.getInteger(IWand.FAV1): tags.getInteger(IWand.FAV2);

    	// お気に入り切り替えフラグの切り替え
    	tags.setBoolean(IWand.FAVFLAG, !flag);

    	// スロットの切り替え
		tags.setInteger(IWand.SLOT, slot);

    	PacketHandler.sendToServer(new WandLeftClickPKT(!flag, slot));
    }
}
