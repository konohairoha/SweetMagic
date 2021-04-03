package sweetmagic.init.base;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BaseModelBlock extends Block {

	public BaseModelBlock (Material material, String name) {
		super(material);
		this.setUnlocalizedName(name);
		this.setRegistryName(name);
	}

	// 右クリックの処理
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ) {
		return this.actionBlock(world, state, pos, player, player.getHeldItem(hand));
	}

	// 右クリックの処理
	public boolean actionBlock (World world, IBlockState state, BlockPos pos, EntityPlayer player, ItemStack stack) {
		return false;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	// フェンスとかにつながないように
	public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing face) {
		return BlockFaceShape.UNDEFINED;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}

	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	// サウンド
	public void playerSound (World world, BlockPos pos, SoundEvent sound, float vol, float pit) {
		world.playSound(null, pos, sound, SoundCategory.BLOCKS, vol, pit);
	}

	// サウンド
	public void playerSound (World world, EntityPlayer player, SoundEvent sound, float vol, float pit) {
		this.playerSound(world, new BlockPos(player), sound, vol, pit);
	}

	// アイテムのスポーン
	public void spawnItem (World world, EntityPlayer player, List<ItemStack> list) {
		BlockPos pos = new BlockPos(player);
		for (ItemStack stack : list) {
			world.spawnEntity(new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), stack));
		}
	}

	// アイテムのスポーン
	public void spawnItem (World world, BlockPos pos, List<ItemStack> list) {
		for (ItemStack stack : list) {
			world.spawnEntity(new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), stack));
		}
	}

	// アイテムのスポーン
	public void spawnItem (World world, EntityPlayer player, ItemStack stack) {
		BlockPos pos = new BlockPos(player);
		world.spawnEntity(new EntityItem(world, pos.getX(), pos.getY() + 0.5, pos.getZ(), stack));
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
}
