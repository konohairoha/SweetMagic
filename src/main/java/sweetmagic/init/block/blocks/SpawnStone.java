package sweetmagic.init.block.blocks;

import java.util.Random;

import javax.annotation.Nonnull;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import sweetmagic.init.BlockInit;
import sweetmagic.init.base.BaseModelBlock;
import sweetmagic.init.tile.magic.TileSMSpaner;
import sweetmagic.init.tile.magic.TileSpawnStone;

public class SpawnStone extends BaseModelBlock {

	private final int data;
	private final static AxisAlignedBB AABB = new AxisAlignedBB(0D, 0D, 0D, 1D, 0.4D, 1D);

	public SpawnStone (String name, int data) {
        super(Material.ROCK, name);
        setHardness(4);
        setResistance(9999F);
        setHarvestLevel("pickaxe", 3);
        setSoundType(SoundType.STONE);
        setLightLevel(0.25F);
        this.data = data;
		BlockInit.blockList.add(this);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return this.data == 0 ? AABB : FULL_BLOCK_AABB;
	}

	// 右クリックの処理
	public boolean actionBlock (World world, IBlockState state, BlockPos pos, EntityPlayer player, ItemStack stack) {

		if (this.data == 1 && player.isCreative()) {
			TileSMSpaner tile = (TileSMSpaner) world.getTileEntity(pos);
			int data = tile.data + 1;
			tile.data = data > 8 ? 0 : data;
			tile.setRenderEntity();
			tile.markDirty();
			world.notifyBlockUpdate(pos, state, state, 3);
			return true;
		}

		return false;
	}

	@Override
	public int getExpDrop(IBlockState state, net.minecraft.world.IBlockAccess world, BlockPos pos, int fortune) {
		return 50 + RANDOM.nextInt(50) + RANDOM.nextInt(50);
	}

    @Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {

		switch (this.data) {
		case 0: return new TileSpawnStone();
		case 1: return new TileSMSpaner();
		}

		return new TileSMSpaner();
	}

    public ItemStack getItem(World world, BlockPos pos, IBlockState state) {
        return ItemStack.EMPTY;
    }

    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return null;
    }
}
