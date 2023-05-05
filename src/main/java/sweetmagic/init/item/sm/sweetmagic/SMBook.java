package sweetmagic.init.item.sm.sweetmagic;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.SweetMagicCore;
import sweetmagic.api.iitem.IAcce;
import sweetmagic.handlers.PacketHandler;
import sweetmagic.handlers.SMGuiHandler;
import sweetmagic.init.item.sm.eitem.SMAcceType;
import sweetmagic.key.ClientKeyHelper;
import sweetmagic.key.SMKeybind;
import sweetmagic.packet.PlayerSoundPKT;
import sweetmagic.util.SoundHelper;

public class SMBook extends SMReturn implements IAcce {

	protected final int data;

	public SMBook(String name, int data) {
		super(name);
		this.data = data;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {

		// クライアント（プレイヤー）へ送りつける
		if (!world.isRemote) {
			player.openGui(SweetMagicCore.INSTANCE, SMGuiHandler.SMBOOK_GUI, world, 0, -1, -1);
			PacketHandler.sendToPlayer(new PlayerSoundPKT(SoundHelper.S_PAGE, 1F, 0.33F), (EntityPlayerMP) player);
		}

		return new ActionResult(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}

	//ツールチップの表示
  	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
  		super.addInformation(stack, world, tooltip, flag);
		tooltip.add(I18n.format(TextFormatting.GREEN + this.getTip("tip.magicbook.name") ));
  	}

	@Override
	public List<String> magicToolTip(List<String> toolTip) {
		toolTip.add(TextFormatting.RED + ClientKeyHelper.getKeyName(SMKeybind.BACK) + TextFormatting.GOLD + this.getTip("tip.magicbook_acc.name"));
		return toolTip;
	}

	@Override
	public SMAcceType getAcceType() {
		return SMAcceType.TERMS;
	}

	@Override
	public void setAcceType(SMAcceType type) { }

	// tierの取得
	public int getTier () {
		return this.data + 1;
	}
}
