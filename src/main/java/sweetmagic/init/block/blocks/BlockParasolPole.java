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
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.api.iblock.IChangeBlock;
import sweetmagic.init.BlockInit;
import sweetmagic.init.base.BaseModelBlock;

public class BlockParasolPole extends BaseModelBlock implements IChangeBlock {

	private static final PropertyInteger PARASOL = PropertyInteger.create("parasol", 0, 1);
	private static final AxisAlignedBB POLE = new AxisAlignedBB(0.4, 0, 0.4, 0.6, 1, 0.6);
	private final boolean isBase;

	public BlockParasolPole (String name, boolean isBase) {
		super(Material.WOOD, name);
        setHardness(0.2F);
        setResistance(1024F);
		this.setSoundType(SoundType.METAL);
		setDefaultState(this.blockState.getBaseState().withProperty(PARASOL, 0));
        this.isBase = isBase;

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

	// ブロックチェック
    public boolean checkBlock (ItemStack stack) {

    	Item item = stack.getItem();

    	if (item instanceof ItemBlock) {
    		return item == Item.getItemFromBlock(this) || ((ItemBlock) item).getBlock() instanceof BlockAwningTent;
    	}

    	return false;
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

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return Item.getItemFromBlock(BlockInit.parasol_pole);
	}

    public ItemStack getItem(World world, BlockPos pos, IBlockState state) {
        return new ItemStack(BlockInit.parasol_pole);
    }

	@Override
	public void setBlock(World world, BlockPos pos, EntityPlayer player) {

		IBlockState state = world.getBlockState(pos);
		Block block = this.isBase ? BlockInit.parasol_pole : BlockInit.parasol_pole_base;
		world.setBlockState(pos, block.getDefaultState(), 2);

        SoundType sound = this.getSoundType(state, world, pos, player);
        this.playerSound(world, pos, sound.getPlaceSound(),(sound.getVolume() + 1.0F) / 2.0F, sound.getPitch() * 0.8F);
	}

	//ツールチップの表示
  	@Override
  	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
		tooltip.add(I18n.format(TextFormatting.GREEN + this.getTip("tip.lantern_side.name")));
  	}
}
