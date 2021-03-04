package sweetmagic.init.block.blocks;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import sweetmagic.init.BlockInit;

public class BlockStendGlass extends SMIron {

	public final boolean isOn;
	public final int data;

    public BlockStendGlass(String name, boolean isOn, int data, List<Block> list) {
    	super(Material.REDSTONE_LIGHT, name);
        setHardness(0.7F);
        setResistance(64F);
        setSoundType(SoundType.METAL);
        if (isOn) {
            setLightLevel(1F);
        }
        this.isOn = isOn;
        this.data = data;
        list.add(this);
    }

    /**
     * 0 = off
     * 1 = on
     */

	public void onBlockAdded(World world, BlockPos pos, IBlockState state) {

		if (world.isRemote) { return; }

		if (!this.isOn && world.isBlockPowered(pos)) {
			world.setBlockState(pos, this.getState(), 2);
		}

		else if (this.isOn && !world.isBlockPowered(pos)) {
			world.setBlockState(pos, this.getState(), 2);
		}
	}

	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos) {

		if (world.isRemote) { return; }

		if (!this.isOn && world.isBlockPowered(pos)) {
			world.scheduleUpdate(pos, this, 4);
		}

		else if (this.isOn && !world.isBlockPowered(pos)) {
			world.setBlockState(pos, this.getState(), 2);
		}
	}

	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {

		if (world.isRemote) { return; }

		if (!this.isOn && world.isBlockPowered(pos)) {
			world.setBlockState(pos, this.getState(), 2);
		}
	}

	public Block getBlock (boolean flag) {

		Block block = null;

		switch (this.data) {
		case 0:
			block = flag ? BlockInit.stendglass_lamp_g_on : BlockInit.stendglass_lamp_g_off;
			break;
		case 2:
			block = flag ? BlockInit.stendglass_lamp_on : BlockInit.stendglass_lamp_off;
			break;
		}

		return block;
	}

	public IBlockState getState () {

		Block block = null;

		switch (this.data) {
		case 0:
			block = BlockInit.stendglass_lamp_g_on;
			break;
		case 1:
			block = BlockInit.stendglass_lamp_g_off;
			break;
		case 2:
			block = BlockInit.stendglass_lamp_on;
			break;
		case 3:
			block = BlockInit.stendglass_lamp_off;
			break;
		}

		return block.getDefaultState();
	}

	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return Item.getItemFromBlock(this.getBlock(false));
	}

	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
		return new ItemStack(this.getBlock(true));
	}

	protected ItemStack getSilkTouchDrop(IBlockState state) {
		return new ItemStack(this.getBlock(false));
	}
}
