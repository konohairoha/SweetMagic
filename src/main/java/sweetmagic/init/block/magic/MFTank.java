package sweetmagic.init.block.magic;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import sweetmagic.handlers.SMGuiHandler;
import sweetmagic.init.BlockInit;
import sweetmagic.init.base.BaseMFBlock;
import sweetmagic.init.tile.magic.TileMFMMTank;
import sweetmagic.init.tile.magic.TileMFTank;
import sweetmagic.init.tile.magic.TileMFTankAdvanced;

public class MFTank extends BaseMFBlock {

	public final int data;

    public MFTank(String name, int data) {
		super(name);
		this.data = data;
		BlockInit.magicList.add(this);
    }

	// ブロックでのアクション
	@Override
	public void actionBlock (World world, BlockPos pos, EntityPlayer player, ItemStack stack) {
		if (world.isRemote) { return; }
		this.openGui(world, player, pos, SMGuiHandler.MFTANK_GUI);
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {

		switch (this.data) {
		case 0:	return new TileMFTank();
		case 1:	return new TileMFTankAdvanced();
		case 2:	return new TileMFMMTank();
		}

		return null;
	}
}
