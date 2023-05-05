package sweetmagic.init.item.sm.sweetmagic;

import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import sweetmagic.init.ItemInit;

public class SMItem extends Item {

	public SMItem(String name) {
        setUnlocalizedName(name);
        setRegistryName(name);
        ItemInit.itemList.add(this);
    }

	public SMItem(String name, List<Item> list) {
        setUnlocalizedName(name);
        setRegistryName(name);
        list.add(this);
    }

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing face, float x, float y, float z) {
		return this.useStack(world, player, player.getHeldItem(hand), pos, face);
	}

	public EnumActionResult useStack (World world, EntityPlayer player, ItemStack stack, BlockPos pos, EnumFacing face) {
		return EnumActionResult.PASS;
	}

	public String getTip (String tip) {
		return new TextComponentTranslation(tip).getFormattedText();
	}

	// アイテムスポーン
	public void spawnItem (World world, EntityLivingBase entity, ItemStack stack) {
		world.spawnEntity(new EntityItem(world, entity.posX, entity.posY + 0.5D, entity.posZ, stack));
	}

	// アイテムスポーン
	public void spawnItem (World world, BlockPos pos, ItemStack stack) {
		world.spawnEntity(new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), stack));
	}

	// 音流し
	public void playSound (World world, EntityPlayer player, SoundEvent sound, float vol, float pith) {
        world.playSound(player, player.getPosition(), sound, SoundCategory.PLAYERS, vol, pith);
	}

	// アイテム減らし
	public void shrinkItem (EntityPlayer player, ItemStack stack) {
		if (!player.isCreative()) { stack.shrink(1); }
	}
}
