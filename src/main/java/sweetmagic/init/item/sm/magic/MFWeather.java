package sweetmagic.init.item.sm.magic;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.api.iitem.IElementItem;
import sweetmagic.event.SMSoundEvent;
import sweetmagic.init.item.sm.eitem.SMElement;
import sweetmagic.init.item.sm.eitem.SMType;

public class MFWeather extends MFSlotItem implements IElementItem {

	public int burnTime;
	public int rainTime;

	public MFWeather (String name, int time, SMElement ele, int rainTime) {
		super(name, SMType.AIR, ele, 1, 40, 10, true);
		this.burnTime = time;
		this.setRainTime(rainTime);
	}

	// ツールチップ
	public List<String> magicToolTip (List<String> toolTip) {

		switch (this.rainTime) {
		case 0:
			toolTip.add("tip.magic_fire_nasturtium.name");
			break;
		case 12000:
			toolTip.add("tip.magic_dm.name");
			break;
		}

		return toolTip;
	}

	// 精錬時間
	@Override
	public int getItemBurnTime(ItemStack stack){
		return this.burnTime;
	}

	// 雨を降らす時間を設定
	public void setRainTime (int rainTime) {
		this.rainTime = rainTime;
	}

	// 雨を降らす時間を取得
	public int getRainTime () {
		return this.rainTime;
	}

	@Override
	public boolean onItemAction(World world, EntityPlayer player, ItemStack stack, Item slotItem) {


		if (world instanceof WorldServer) {
			WorldInfo info = world.getMinecraftServer().getWorld(0).getWorldInfo();
			info.setRainTime(this.getRainTime());
			info.setThunderTime(0);
			info.setRaining(this.getRainTime() != 0);
			world.playSound(null, new BlockPos(player), SMSoundEvent.CHANGETIME, SoundCategory.VOICE, 0.35F, 1.0F);
		}

		return true;
	}

	//ツールチップの表示
  	@Override
  	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag){
  		super.addInformation(stack, world, tooltip, flag);

  		if (this.burnTime != 0) {
  		  	//xx_xx.langファイルから文字を取得する方法
  	  		String text = new TextComponentTranslation("tip.burntick.name", new Object[0]).getFormattedText();
  			tooltip.add(I18n.format(TextFormatting.YELLOW + text +" : " + this.burnTime));
  		}
  	}
}