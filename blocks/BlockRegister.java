package sweetmagic.init.block.blocks;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import sweetmagic.init.BlockInit;
import sweetmagic.init.base.BaseFaceBlock;
import sweetmagic.init.tile.magic.TileRegister;

public class BlockRegister extends BaseFaceBlock {

	public BlockRegister(String name) {
		super(Material.WOOD, name);
		setHardness(0.35F);
		setResistance(1024F);
		setSoundType(SoundType.WOOD);
		BlockInit.furniList.add(this);
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileRegister();
	}
}
