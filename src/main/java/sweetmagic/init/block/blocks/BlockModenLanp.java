package sweetmagic.init.block.blocks;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.api.enumblock.EnumLocal;
import sweetmagic.api.enumblock.EnumLocal.PropertyLocal;
import sweetmagic.init.BlockInit;
import sweetmagic.init.base.BaseModelBlock;

public class BlockModenLanp extends BaseModelBlock {

	private static final PropertyLocal LOCAL = new PropertyLocal("local", EnumLocal.getLocalList());
	private static final AxisAlignedBB AABB = new AxisAlignedBB(0.7D, 1D, 0.7D, 0.3D, 0D, 0.3D);
	private final int data;

	public BlockModenLanp(String name, int data) {
		super(Material.GLASS, name);
		setSoundType(SoundType.GLASS);
		setHardness(0.25F);
        setResistance(1024F);
		setDefaultState(this.blockState.getBaseState().withProperty(LOCAL, EnumLocal.NOR));
		setLightLevel(1F);
		this.data = data;
		BlockInit.furniList.add(this);
	}

	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return AABB;
	}

	//右クリックの処理
    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing face, float hitX, float hitY, float hitZ) {

		ItemStack stack = player.getHeldItem(hand);
		if (!this.checkBlock(stack)) {

			if (this.data != 0) {

				if (player.isSneaking()) {
					if (!world.isRemote) {

						Block block = this.data == 1 ? BlockInit.walllamp_long : BlockInit.walllamp;
						world.setBlockState(pos, block.getDefaultState(), 2);
			            SoundType sound = this.getSoundType(state, world, pos, player);
			            this.playerSound(world, pos, sound.getPlaceSound(),(sound.getVolume() + 1.0F) / 2.0F, sound.getPitch() * 0.8F);
					}
					return true;
				}
			}

			return false;
		}

    	return this.setBlockSound(world, state, pos, player, stack, 10, true);
    }

	// ブロックチェック
    public boolean checkBlock (ItemStack stack) {
    	Item item = stack.getItem();
    	return item == Item.getItemFromBlock(BlockInit.modenlanp) || item == Item.getItemFromBlock(BlockInit.walllamp) ||
    			item == Item.getItemFromBlock(BlockInit.walllamp_long) || item == Item.getItemFromBlock(BlockInit.glow_lamp);
    }

	@Override
	public boolean isSideSolid(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
		return side == EnumFacing.UP;
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {

		Block under = this.getBlock(world, pos.down());
		boolean top = under instanceof BlockModenLanp || under == BlockInit.glow_lamp || under == BlockInit.magiaflux_core;
		boolean bot = this.getBlock(world, pos.up()) instanceof BlockModenLanp;
		return state.withProperty(LOCAL, EnumLocal.getLocal(top, bot));
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
		return new BlockStateContainer(this, new IProperty[] { LOCAL });
	}

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT_MIPPED;
	}

	public Block getBlock () {
		return this.data == 0 ? this : BlockInit.walllamp;
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return Item.getItemFromBlock(this.getBlock());
	}

    public ItemStack getItem(World world, BlockPos pos, IBlockState state) {
        return new ItemStack(this.getBlock());
    }

	//ツールチップの表示
  	@Override
  	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
		tooltip.add(I18n.format(TextFormatting.GREEN + this.getTip("tip.lantern_side.name")));
  	}
}
