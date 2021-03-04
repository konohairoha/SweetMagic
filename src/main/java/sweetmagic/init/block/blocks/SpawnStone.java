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

	public final int data;

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
		return this.data == 0 ? new AxisAlignedBB(0, 0, 0, 1, 0.4, 1) : new AxisAlignedBB(0, 0, 0, 1, 1, 1);
	}

	// 右クリックの処理
	public boolean actionBlock (World world, IBlockState state, BlockPos pos, EntityPlayer player, ItemStack stack) {

		if (this.data == 1 && player.isCreative()) {

			TileSMSpaner tile = (TileSMSpaner) world.getTileEntity(pos);
			int data = tile.data + 1;
			tile.data = data > 6 ? 0 : data;
			return true;
		}

		return false;
	}

    @Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {

		switch (this.data) {
		case 0:
			return new TileSpawnStone();
		case 1:
			return new TileSMSpaner();
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
