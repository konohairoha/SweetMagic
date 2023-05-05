package sweetmagic.init.block.blocks;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.init.BlockInit;
import sweetmagic.init.base.BaseFaceBlock;
import sweetmagic.util.FaceAABB;

public class BlockBlinder extends BaseFaceBlock {

	private final static AxisAlignedBB[] WALL = new FaceAABB(0D, 0D, 0.9375D, 1D, 1D, 1D).getRotatedBounds();
	private static final PropertyBool CLOSED = PropertyBool.create("close");
	protected static final PropertyInteger TYPE = PropertyInteger.create("type", 0, 4);
	private final int data;

	public BlockBlinder(String name, int data) {
		super(Material.WOOD, name);
		setHardness(0F);
		setResistance(1024F);
		setSoundType(SoundType.CLOTH);
		setDefaultState(this.blockState.getBaseState()
				.withProperty(FACING, EnumFacing.NORTH).withProperty(CLOSED, false).withProperty(TYPE, 0));
		this.data = data;
		BlockInit.furniList.add(this);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { FACING, CLOSED, TYPE });
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {

		IBlockState upState = world.getBlockState(pos.up());
		if (upState.getBlock() != this) { return state; }

		if (upState.getValue(CLOSED)) {
			return state.withProperty(TYPE, 1);
		}

		else {

			boolean down = this.getBlock(world, pos.down()) == this;
			int type = down ? 2 : 3;
			return state.withProperty(TYPE, type);
		}

	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return WALL[state.getValue(FACING).rotateYCCW().getHorizontalIndex()];
	}

	// 右クリックの処理
	public boolean actionBlock (World world, IBlockState state, BlockPos pos, EntityPlayer player, ItemStack stack) {

		// 水の流してる状態の取得
		boolean isClosed = state.getValue(CLOSED);

		SoundEvent sound = isClosed ? SoundEvents.BLOCK_CLOTH_BREAK : SoundEvents.BLOCK_CLOTH_PLACE;
		this.playerSound(world, pos, sound, 1F, 1F);

		world.setBlockState(pos, state.withProperty(CLOSED, !isClosed), 3);

		this.setBlinderOpen(world, pos, !isClosed, true);
		this.setBlinderOpen(world, pos, !isClosed, false);

		return true;
	}

	public void setBlinderOpen (World world, BlockPos pos, boolean isClosed, boolean isUp) {

		for (int i = 0; i <= 10; i++) {

			IBlockState state = world.getBlockState(isUp ? pos.up(i) : pos.down(i));
			if (state.getBlock() != this) { return; }

			world.setBlockState(isUp ? pos.up(i) : pos.down(i), state.withProperty(CLOSED, isClosed), 3);
		}
	}

	// IBlockStateからItemStackのmetadataを生成。ドロップ時とテクスチャ・モデル参照時に呼ばれる
	@Override
	public int getMetaFromState(IBlockState state) {
		return super.getMetaFromState(state) + (!state.getValue(CLOSED) ? 5 : 0);
	}

	// ItemStackのmetadataからIBlockStateを生成。設置時に呼ばれる
	@Override
	public IBlockState getStateFromMeta(int meta) {

		boolean isClose = false;

		if (meta > 4) {
			meta -= 5;
			isClose = true;
		}

		return super.getStateFromMeta(meta).withProperty(CLOSED, !isClose);
	}

	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag advanced) {
		tooltip.add(I18n.format(TextFormatting.GOLD + this.getTip("tip.blinder_opne.name")));
	}
}
