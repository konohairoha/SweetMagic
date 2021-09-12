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
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
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

		if (world.isRemote) { return; }

		int guiId = 0;

		switch (this.data) {
		case 0:
			guiId = SMGuiHandler.MFF_GUI;
			break;
		case 1:
			guiId = SMGuiHandler.MFF_ADVANCED_GUI;
			break;
		}

		this.openGui(world, player, pos, guiId);
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {

		switch (this.data) {
		case 0: return new TileMFFurnace();
		case 1: return new TileMFFurnaceAdvanced();
		}

		return null;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag advanced) {
		tooltip.add(I18n.format(TextFormatting.GREEN + this.getTip("tip.mffurnace.name")));
		super.addInformation(stack, world, tooltip, advanced);
	}
}
