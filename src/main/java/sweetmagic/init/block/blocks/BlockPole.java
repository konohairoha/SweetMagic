package sweetmagic.init.block.blocks;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import sweetmagic.init.BlockInit;
import sweetmagic.init.base.BaseModelBlock;

public class BlockPole extends BaseModelBlock {

	private final int data;
	private static final AxisAlignedBB POLE = new AxisAlignedBB(0.4, 0, 0.4, 0.6, 1, 0.6);
	private static final AxisAlignedBB LANTERN = new AxisAlignedBB(0.25, 0, 0.25, 0.75, 0.65, 0.75);

	public BlockPole (String name, List<Block> list, int data) {
		super(Material.WOOD, name);
        setHardness(0.35F);
        setResistance(1024F);
		this.setSoundType(SoundType.STONE);
		this.data = data;
        setLightLevel(data == 2 ? 1F : 0F);
		list.add(this);
	}

	/**
	 * 0 = 下
	 * 1 = 柱
	 * 2 = ランタン
	 */

	// 設置出来るかどうかの確認
	@Override
	public boolean canPlaceBlockAt(World world, BlockPos pos) {
		return super.canPlaceBlockAt(world, pos) && super.canPlaceBlockAt(world, pos.up())
				 && super.canPlaceBlockAt(world, pos.up(2)) && super.canPlaceBlockAt(world, pos.up(3))
				 && super.canPlaceBlockAt(world, pos.up(4));
	}

	// ブロック設置したときの処理(上のブロックを設置)
	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		for (int i = 1; i < 4; i++)
			world.setBlockState(pos.up(i), BlockInit.pole.getDefaultState(), 2);
		world.setBlockState(pos.up(4), BlockInit.lantern.getDefaultState(), 2);
	}

	// ブロックを壊したときの処理
	public void breakBlock(World world, BlockPos pos, IBlockState state) {

		Block downBlock = world.getBlockState(pos.down()).getBlock();
		if (downBlock instanceof BlockPole) {
			this.breakBlock(pos.down(), world, true);
		}

		Block upBlock = world.getBlockState(pos.up()).getBlock();
		if (upBlock instanceof BlockPole) {
			this.breakBlock(pos.up(), world, true);
		}

        super.breakBlock(world, pos, state);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return this.data == 2 ? this.LANTERN : this.POLE;
	}

	// アイテムをドロップ
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return this.data == 0 ? new ItemStack(this).getItem() : ItemStack.EMPTY.getItem();
	}

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		return new ItemStack(BlockInit.pole_down);
	}
}
