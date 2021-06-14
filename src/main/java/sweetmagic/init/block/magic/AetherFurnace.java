package sweetmagic.init.block.magic;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.SweetMagicCore;
import sweetmagic.handlers.SMGuiHandler;
import sweetmagic.init.BlockInit;
import sweetmagic.init.base.BaseMFBlock;
import sweetmagic.init.tile.magic.TileAetherFurnace;
import sweetmagic.init.tile.magic.TileMFBase;

public class AetherFurnace extends BaseMFBlock {

	private static final AxisAlignedBB TOP = new AxisAlignedBB(0.2, 0, 0.2, 0.8, 1, 0.8);
	private static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	private final boolean isTop;
	private final boolean isAdvanced;

    public AetherFurnace(String name, List<Block> list, boolean isTop, boolean isAdvanced) {
        super(name);
        this.isTop = isTop;
        this.isAdvanced = isAdvanced;
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
		list.add(this);
    }

    /**
     * 0 = 上
     * 1 = 下
     * 2 = 改良型上
     * 3 = 改良型下
     */

	// ブロックでのアクション
	@Override
	public void actionBlock(World world, BlockPos pos, EntityPlayer player, ItemStack stack) {

		if (this.isTop) {

			TileEntity tile = world.getTileEntity(pos.down());
			if (!(tile instanceof TileAetherFurnace)) { return; }

			player.openGui(SweetMagicCore.INSTANCE, SMGuiHandler.AETHER_GUI, world, pos.getX(), pos.getY() - 1, pos.getZ());
		}

		else {
			player.openGui(SweetMagicCore.INSTANCE, SMGuiHandler.AETHER_GUI, world, pos.getX(), pos.getY(), pos.getZ());
		}
	}

    @Override
	public boolean hasTileEntity(IBlockState state) {
		return !this.isTop;
	}

	@Override
	public boolean canPlaceBlockAt(World world, BlockPos pos) {
		return super.canPlaceBlockAt(world, pos) && super.canPlaceBlockAt(world, pos.up());
	}

	// ブロック設置したときの処理(上のブロックを設置)
	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {

		// 下なら
		if (!this.isTop) {

			Block block;

			// 通常型なら
			if (!this.isAdvanced) {
				block = BlockInit.aether_furnace_top;
			}

			// 改良型なら
			else {
				block = BlockInit.advanced_aether_furnace_top;
			}

			world.setBlockState(pos.up(), block.getDefaultState().withProperty(FACING, state.getValue(FACING)), 2);
		}

		TileMFBase tile = (TileMFBase) world.getTileEntity(pos);
		NBTTagCompound tag = stack.getTagCompound();
		if (tag != null) {
			tag.removeTag("mf");
			tile.writeToNBT(tag);
		}
	}

	// ブロックをこわしたとき(下のブロックを指定)
	@Override
	public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
		this.breakBlock(!this.isTop ? pos.up() : pos.down(), world, true);
        world.removeTileEntity(!this.isTop ? pos.up() : pos.down());
	}

	@Override
	public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity) { }

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return TOP;
	}

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		return new ItemStack(!this.isAdvanced ? BlockInit.aether_furnace_bottom : BlockInit.advanced_aether_furnace_bottom);
	}

	// ブロック破壊処理
	public boolean breakBlock(BlockPos pos, World world, boolean dropBlock) {

		IBlockState state = world.getBlockState(pos);
		Block block = state.getBlock();
		if (block.isAir(state, world, pos)) { return false; }

		world.playEvent(2001, pos, Block.getStateId(state));

		if (dropBlock) {
			block.dropBlockAsItem(world, pos, state, 0);
		}

		return world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
	}

	public void breakBlock(World world, BlockPos pos, IBlockState state) {

		if (this.isTop) { return; }

		super.breakBlock(world, pos, state);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {

		if (this.isTop) { return null; }

    	return new TileAetherFurnace();
	}

	public ItemStack getItem(World world, BlockPos pos, IBlockState state) {
		return ItemStack.EMPTY;
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return null;
	}

	// getOppositeでプレイヤーの向きを取得
	@Override
	public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
		return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
	}

	// IBlockStateからItemStackのmetadataを生成。ドロップ時とテクスチャ・モデル参照時に呼ばれる
	@Override
	public int getMetaFromState(IBlockState state) {
		return ((EnumFacing) state.getValue(FACING)).getHorizontalIndex();
	}

	// ItemStackのmetadataからIBlockStateを生成。設置時に呼ばれる
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta));
	}

	// 初期BlockStateContainerの生成
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { FACING });
	}

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag advanced) {

		String tip = "";

		if (this.isAdvanced) {
			tip = "tip.advanced_aether_furnace.name";
		}

		else {
			tip = "tip.aether_furnace.name";
		}

		tooltip.add(I18n.format(TextFormatting.GREEN + new TextComponentTranslation(tip, new Object[0]).getFormattedText()));
		super.addInformation(stack, world, tooltip, advanced);
	}
}
