package sweetmagic.init.block.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.init.BlockInit;
import sweetmagic.init.base.BaseModelBlock;

public class BlockModenLanp extends BaseModelBlock {

	private static final PropertyBool TOP = PropertyBool.create("top");
	private static final PropertyBool BOT = PropertyBool.create("bot");
	private final static AxisAlignedBB AABB = new AxisAlignedBB(0.7D, 1D, 0.7D, 0.3D, 0D, 0.3D);

	public BlockModenLanp(String name) {
		super(Material.GLASS, name);
		setSoundType(SoundType.GLASS);
		setHardness(0.5F);
        setResistance(1024F);
		setDefaultState(this.blockState.getBaseState()
				.withProperty(TOP, false)
				.withProperty(BOT, false));
		setLightLevel(1F);
		BlockInit.blockList.add(this);
	}

	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return AABB;
	}

	//右クリックの処理
    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing face, float hitX, float hitY, float hitZ) {

        ItemStack stack = player.getHeldItem(hand);

		if (this.checkBlock(stack)) {

			for (int i = 1; i < 4; i++) {

				if (!world.isAirBlock(pos.down(i))) { continue; }

	        	world.setBlockState(pos.down(i), this.getDefaultState(), 3);
	            if (!player.isCreative()) { stack.setCount(stack.getCount() - 1); }

	            SoundType sound = this.getSoundType(state, world, pos.down(i), player);
	            this.playerSound(world, pos.down(i), sound.getPlaceSound(),(sound.getVolume() + 1.0F) / 2.0F, sound.getPitch() * 0.8F);
	        	return true;
			}
		}

    	return false;
    }

    public boolean checkBlock (ItemStack stack) {
    	return stack.getItem() == Item.getItemFromBlock(this);
    }

	@Override
	public boolean isSideSolid(IBlockState baseState, IBlockAccess world, BlockPos pos, EnumFacing side) {
		return side == EnumFacing.UP;
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
		Block block = world.getBlockState(pos.down()).getBlock();
		boolean bot = block == this || block == BlockInit.glow_lamp;
		boolean top = world.getBlockState(pos.up()).getBlock() == this;
		return state.withProperty(TOP, bot).withProperty(BOT, top);
	}

	// 一番下か
	public boolean isBot (IBlockState state) {
		return state == state.withProperty(TOP, false).withProperty(BOT, true) ||
				state == state.withProperty(TOP, false).withProperty(BOT, false);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return 0;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState();
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { TOP, BOT });
	}

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT_MIPPED;
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return Item.getItemFromBlock(this);
	}

    public ItemStack getItem(World world, BlockPos pos, IBlockState state) {
        return new ItemStack(this);
    }
}
