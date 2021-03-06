package sweetmagic.init.block.blocks;

import java.util.Random;

import net.minecraft.block.BlockDoor;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.init.BlockInit;
import sweetmagic.init.ItemInit;

public class SMDoor extends BlockDoor {

	public final int data;
	public static final AxisAlignedBB T_SOUTH= new AxisAlignedBB(0D, 0D, 0D, 1D, 2D, 0.1875D);
    public static final AxisAlignedBB T_NORTH = new AxisAlignedBB(0D, 0D, 0.8125D, 1D, 2D, 1D);
    public static final AxisAlignedBB T_WEST = new AxisAlignedBB(0.8125D, 0D, 0D, 1D, 2D, 1D);
    public static final AxisAlignedBB T_EAST = new AxisAlignedBB(0D, 0D, 0D, 0.1875D, 2D, 1D);

	public SMDoor(String name, int meta, Material mate, SoundType sound) {
        super(mate);
        setRegistryName(name);
        setUnlocalizedName(name);
		setHardness(1F);
		setResistance(16F);
		setSoundType(sound);
		this.data = meta;
		BlockInit.noTabList.add(this);
    }

    @Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return state.getValue(HALF) == BlockDoor.EnumDoorHalf.UPPER ? null : this.getItem();
    }
    @Override
    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
        return new ItemStack(this.getItem());
    }

    /**
     * 0 = モダンドア
     * 1 = 2パネドア
     * 2 = 5パネドア
     * 3 = エレガントドア
     * 4 = アーチドア
     * 5 = アーチプラントドア
     * 6 = 木製3ドア
     * 7 = 白木製3ドア
     */

	private Item getItem() {
		switch (this.data) {
		case 0:
			return ItemInit.black_moderndoor;
		case 1:
			return ItemInit.brown_2paneldoor;
		case 2:
			return ItemInit.brown_5paneldoor;
		case 3:
			return ItemInit.brown_elegantdoor;
		case 4:
			return ItemInit.brown_arch_door;
		case 5:
			return ItemInit.brown_arch_plantdoor;
		case 6:
			return ItemInit.woodgold_3;
		case 7:
			return ItemInit.whitewoodgold_3;
		}
		return null;
	}

	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {

		state = state.getActualState(source, pos);

		if ( (this.data == 6 || this.data == 7) && this.isTop(state) ) {

			EnumFacing face = (EnumFacing) state.getValue(FACING);
			boolean flag = !((Boolean) state.getValue(OPEN)).booleanValue();
			boolean flag1 = state.getValue(HINGE) == BlockDoor.EnumHingePosition.RIGHT;

			switch (face) {
			case EAST:
			default:
				return flag ? T_EAST : (flag1 ? T_NORTH : T_SOUTH);
			case SOUTH:
				return flag ? T_SOUTH : (flag1 ? T_EAST : T_WEST);
			case WEST:
				return flag ? T_WEST : (flag1 ? T_SOUTH : NORTH_AABB);
			case NORTH:
				return flag ? NORTH_AABB : (flag1 ? T_WEST : T_EAST);
			}
		}

		return super.getBoundingBox(state, source, pos);

	}

    @Override
    @SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    public boolean isTop (IBlockState state) {
    	return state.getValue(HALF) == BlockDoor.EnumDoorHalf.UPPER;
    }
}
