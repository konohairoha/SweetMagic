package sweetmagic.init.block.blocks;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.init.BlockInit;
import sweetmagic.init.base.BaseFaceBlock;

public class BlockLantern extends BaseFaceBlock {

	public final int data;

	public BlockLantern(String name, int data, List<Block> list) {
		super(Material.GLASS, name);
		setHardness(1.0F);
        setResistance(1024F);
		setSoundType(SoundType.METAL);
		this.setLightLevel(1F);
		this.data = data;
		list.add(this);
	}

	//右クリックの処理
	public boolean actionBlock (World world, IBlockState state, BlockPos pos, EntityPlayer player, ItemStack stack) {

		if (!world.isRemote && player.isSneaking()) {
			Block block = this.data == 0 ? BlockInit.lantern_side2 : BlockInit.lantern_side1;
			IBlockState setState = block.getDefaultState();
			world.setBlockState(pos, setState.withProperty(FACING, state.getValue(FACING)), 2);
			this.playerSound(world, pos, SoundEvents.UI_BUTTON_CLICK, 0.5F, world.rand.nextFloat() * 0.1F + 1.2F);
		}

		return true;
	}

	//ツールチップの表示
  	@Override
  	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
	  	String tip = new TextComponentTranslation("tip.lantern_side.name", new Object[0]).getFormattedText();
		tooltip.add(I18n.format(TextFormatting.GREEN + tip));
  	}
}
