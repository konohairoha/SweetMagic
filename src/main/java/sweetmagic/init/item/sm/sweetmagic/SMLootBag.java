package sweetmagic.init.item.sm.sweetmagic;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
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
        setMaxStackSize(64);
        this.data = data;
    }

    /**
     * 0 = 種袋
     * 1 = 卵袋
     * 2 = アクセ袋
     */

	//右クリックをした際の処理
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {

		ItemStack stack = player.getHeldItem(hand);
		stack.shrink(1);
		BlockPos pos = new BlockPos(player);
		world.playSound(null, pos, SMSoundEvent.ROBE, SoundCategory.PLAYERS, 0.5F, 1.075F);
		if (world.isRemote) { return new ActionResult(EnumActionResult.SUCCESS, stack); }

		Random rand = world.rand;

		switch (this.data) {
		case 0:
			// 鉱石辞書からランダムにアイテムを引っ張る
			this.getOreLoot(world, pos, player, hand, rand, "listAllseed", 5);
			break;
		case 1:
			this.spawnItem(world, pos, new ItemStack(Items.EGG , rand.nextInt(8) + 4));
			break;
		case 2:
			// 鉱石辞書からランダムにアイテムを引っ張る
			this.getOreLoot(world, pos, player, hand, rand, "magicAccessori", 1);
			break;
		}

		return new ActionResult(EnumActionResult.SUCCESS, stack);
	}

	// 鉱石辞書からランダムにアイテムを引っ張る
	public void getOreLoot (World world, BlockPos pos, EntityPlayer player, EnumHand hand, Random rand, String ore, int value) {

		// ルートテーブルをリストに入れる
		List<ItemStack> seedList = SMUtil.getOreList(ore);
//		player.setActiveHand(hand);

		for (int i = 0; i < value; i ++) {
			this.spawnItem(world, pos, seedList.get(rand.nextInt(seedList.size())));
		}
	}

	// アイテムスポーン
	public void spawnItem (World world, BlockPos pos, ItemStack stack) {
		world.spawnEntity(new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), stack));
	}

    //ツールチップの表示
  	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {

  		String text =  "";

  		switch (this.data) {
  		case 0:
  			text = "tip.seedbag.name";
  			break;
  		case 1:
  			text = "tip.eggbag.name";
  			break;
  		case 2:
  			text = "tip.accebag.name";
  			break;
  		}

  		tooltip.add(I18n.format(TextFormatting.BLUE + new TextComponentTranslation(text, new Object[0]).getFormattedText()));
  	}
}