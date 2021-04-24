package sweetmagic.init.block.magic;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.SweetMagicCore;
import sweetmagic.handlers.SMGuiHandler;
import sweetmagic.init.base.BaseMFFace;
import sweetmagic.init.tile.magic.TileMFFurnace;
import sweetmagic.init.tile.magic.TileMFFurnaceAdvanced;

public class MFFurnace extends BaseMFFace {

	public static boolean setBlock = false;
	private final int data;

    public MFFurnace(String name, int data, List<Block> list) {
        super(name);
        this.data = data;
		list.add(this);
    }

	// ブロックでのアクション
	@Override
	public void actionBlock (World world, BlockPos pos, EntityPlayer player, ItemStack stack) {

		int guiId = 0;

		switch (this.data) {
		case 0:
			guiId = SMGuiHandler.MFF_GUI;
			break;
		case 1:
			guiId = SMGuiHandler.MFF_ADVANCED_GUI;
			break;
		}

		player.openGui(SweetMagicCore.INSTANCE, guiId, world, pos.getX(), pos.getY(), pos.getZ());
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {

		switch (this.data) {
		case 0: return new TileMFFurnace();
		case 1: return new TileMFFurnaceAdvanced();
		}

		return null;
	}

	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		if (!setBlock) {
			super.breakBlock(world, pos, state);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World playerIn, List<String> tooltip, ITooltipFlag advanced) {
		tooltip.add(I18n.format(TextFormatting.GREEN + new TextComponentTranslation("tip.mffurnace.name", new Object[0]).getFormattedText()));
		super.addInformation(stack, playerIn, tooltip, advanced);
	}
}
