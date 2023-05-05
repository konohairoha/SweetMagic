package sweetmagic.init.block.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import sweetmagic.init.BlockInit;
import sweetmagic.init.base.BaseFaceBlock;

public class BlockFaceWood extends BaseFaceBlock {

	private final int data;
	private static final AxisAlignedBB PATH_TREE = new AxisAlignedBB(0D, 0D, 0D, 1D, 0.0125D, 1D);

	public BlockFaceWood (String name, SoundType sound, int data) {
		super(Material.WOOD, name);
        setSoundType(sound);
        setHardness(data == 1 ? 0.1F : 1F);
        setResistance(1024F);
        this.data = data;

        if (this.data != 4) {
    		BlockInit.furniList.add(this);
        }

        else {
    		BlockInit.noTabList.add(this);
            this.setLightLevel(10F);
        }
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {

		switch (this.data) {
		case 0:
			return FULL_BLOCK_AABB;
		case 1:
			return PATH_TREE;
		}

		return FULL_BLOCK_AABB;
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return Item.getItemFromBlock(this.data <= 2 ? this : BlockInit.ventilation_fan);
	}

    @Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing face, float x, float y, float z) {

    	if (this.data <= 2) { return false; }

		if (!world.isRemote) {
			Block block = this.data == 3 ? BlockInit.ventilation_fan_light : BlockInit.ventilation_fan;
			world.setBlockState(pos, block.getDefaultState().withProperty(FACING, state.getValue(FACING)), 2);
			world.playSound(null, pos, SoundEvents.UI_BUTTON_CLICK, SoundCategory.BLOCKS, 0.5F, world.rand.nextFloat() * 0.1F + 1.2F);
		}
		return true;
	}
}
