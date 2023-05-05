package sweetmagic.init.block.blocks;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.api.iblock.IChangeBlock;
import sweetmagic.init.BlockInit;
import sweetmagic.init.base.BaseModelBlock;

public class FencePole extends BaseModelBlock implements IChangeBlock {

	private static final AxisAlignedBB POLE = new AxisAlignedBB(0.4, 0, 0.4, 0.6, 1, 0.6);
	private static final PropertyInteger PARASOL = PropertyInteger.create("parasol", 0, 1);
	private final int data;
	private final boolean isBase;

    public FencePole(String name, int data, boolean isBase) {
		super(Material.GROUND, name);
        this.data = data;
        this.isBase = isBase;
		this.setSoundType(SoundType.WOOD);
		setDefaultState(this.blockState.getBaseState().withProperty(PARASOL, 0));

		if (isBase) {
			BlockInit.noTabList.add(this);
		}

		else {
			BlockInit.furniList.add(this);
		}
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return POLE;
	}

	//右クリックの処理
	public boolean actionBlock (World world, IBlockState state, BlockPos pos, EntityPlayer player, ItemStack stack) {

    	if (player.isSneaking()) {
    		if (!world.isRemote) {
    			this.setBlock(world, pos, player);
    		}
    		return true;
    	}

    	else if (this.checkBlock(stack)) {
	    	return this.setBlockSound(world, state, pos, player, stack, 10, false);
		}

    	return false;
	}

	public Block getBlock () {
		Block block = this;

		if (this.isBase) {
			switch (this.data) {
			case 1:
				block = BlockInit.fence_pole_estor;
				break;
			case 3:
				block = BlockInit.fence_pole_prism;
				break;
			case 5:
				block = BlockInit.fence_pole_pearch;
				break;
			}
		}

		return block;
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return Item.getItemFromBlock(this.getBlock());
	}

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		return new ItemStack(this.getBlock());
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
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
		return state.withProperty(PARASOL, world.getBlockState(pos.up()).getBlock() instanceof BlockAwningTent ? 1 : 0);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { PARASOL });
	}

	// ブロックチェック
    public boolean checkBlock (ItemStack stack) {

    	Item item = stack.getItem();

    	if (item instanceof ItemBlock) {
    		return item == Item.getItemFromBlock(this) || ((ItemBlock) item).getBlock() instanceof BlockAwningTent;
    	}

    	return false;
    }

	@Override
	public void setBlock(World world, BlockPos pos, EntityPlayer player) {

		Block block = null;

		switch (this.data) {
		case 0:
			block = BlockInit.fence_pole_estor_base;
			break;
		case 1:
			block = BlockInit.fence_pole_estor;
			break;
		case 2:
			block = BlockInit.fence_pole_prism_base;
			break;
		case 3:
			block = BlockInit.fence_pole_prism;
			break;
		case 4:
			block = BlockInit.fence_pole_pearch_base;
			break;
		case 5:
			block = BlockInit.fence_pole_pearch;
			break;
		}

		world.setBlockState(pos, block.getDefaultState(), 2);
        SoundType sound = this.getSoundType(block.getDefaultState(), world, pos, player);
        this.playerSound(world, pos, sound.getPlaceSound(),(sound.getVolume() + 1.0F) / 2.0F, sound.getPitch() * 0.8F);
	}

	//ツールチップの表示
  	@Override
  	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
		tooltip.add(I18n.format(TextFormatting.GREEN + this.getTip("tip.lantern_side.name")));
  	}
}
