package sweetmagic.init.item.sm.sweetmagic;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.event.SMSoundEvent;
import sweetmagic.init.ItemInit;
import sweetmagic.util.SMUtil;

public class SMLootBag extends SMItem {

	private final int data;

    public SMLootBag(String name, int data) {
		super(name, ItemInit.magicList);
        setMaxStackSize(1);
        this.data = data;
    }

	//右クリックをした際の処理
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {

		ItemStack stack = player.getHeldItem(hand);
		stack.shrink(1);
		BlockPos pos = new BlockPos(player);
		world.playSound(null, pos, SMSoundEvent.ROBE, SoundCategory.PLAYERS, 0.5F, 1.075F);
		if (world.isRemote) { return new ActionResult(EnumActionResult.SUCCESS, stack); }

		// ルートテーブルをリストに入れる
		List<ItemStack> seedList = SMUtil.getOreList("listAllseed");
		Random rand = world.rand;
		player.setActiveHand(hand);

		for (int i = 0; i < 5; i ++) {
			ItemStack seed = seedList.get(rand.nextInt(seedList.size()));
			world.spawnEntity(new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), seed));
		}

		return new ActionResult(EnumActionResult.SUCCESS, stack);
	}

    //ツールチップの表示
  	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
  		String text =  new TextComponentTranslation("tip.seedbag.name", new Object[0]).getFormattedText();
  		tooltip.add(I18n.format(TextFormatting.BLUE + text));
  	}
}