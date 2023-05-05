package sweetmagic.init.item.sm.sweetmagic;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import sweetmagic.SweetMagicCore;
import sweetmagic.handlers.PacketHandler;
import sweetmagic.handlers.SMGuiHandler;
import sweetmagic.init.AdvancedInit;
import sweetmagic.packet.PlayerSoundPKT;
import sweetmagic.util.SoundHelper;

public class SMGuidBook extends SMReturn {

	public SMGuidBook(String name) {
		super(name);
	}

	// クラフト時に進捗達成
	@Override
	public void onCreated(ItemStack stack, World world, EntityPlayer player) {
		if (player instanceof EntityPlayerMP) {
			AdvancedInit.item_use.trigger((EntityPlayerMP) player, stack);
		}
    }

	@Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		player.openGui(SweetMagicCore.INSTANCE, SMGuiHandler.GUIDBOOK, world, 0, -1, -1);
		if (!world.isRemote){
			// クライアント（プレイヤー）へ送りつける
			PacketHandler.sendToPlayer(new PlayerSoundPKT(SoundHelper.S_PAGE, 1F, 0.33F), (EntityPlayerMP) player);
		}

		return new ActionResult(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}
}
