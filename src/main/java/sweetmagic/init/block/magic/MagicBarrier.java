package sweetmagic.init.block.magic;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.init.BlockInit;
import sweetmagic.init.ItemInit;
import sweetmagic.init.PotionInit;
import sweetmagic.init.base.BaseModelBlock;
import sweetmagic.init.tile.magic.TileMagicBarrier;

public class MagicBarrier extends BaseModelBlock {

	public final int data;

    public MagicBarrier(String name, int data) {
		super(Material.GLASS, name);
		setHardness(data == 0 ? 999999 : 1F);
		setResistance(999999F);
		setSoundType(SoundType.GLASS);
		this.data = data;
		BlockInit.blockList.add(this);
    }

    /**
     * 0 = 鍵あり
     * 1 = 鍵なし
     */

	// ブロックでのアクション
	@Override
	public boolean actionBlock (World world, IBlockState state, BlockPos pos, EntityPlayer player, ItemStack stack) {

		if (world.isRemote || this.data == 1 || stack.getItem() != ItemInit.magickey) { return false; }

//		if (world.isRemote) {
//			player.playSound(SoundEvents.BLOCK_IRON_DOOR_OPEN, 1F, 1F);
//			return true;
//		}
//		player.playSound(SoundEvents.BLOCK_IRON_DOOR_OPEN, 1F, 1F);

		world.playEvent(2001, pos, Block.getStateId(state));
		world.setBlockState(pos, BlockInit.magicbarrier_off.getDefaultState(), 2);
		if (!player.isCreative()) { stack.shrink(1); }

		int range = 64;
        AxisAlignedBB aabb = new AxisAlignedBB(pos.add(-range, -range / 2, -range), pos.add(range, range / 2, range));
		List<EntityPlayer> entityList = world.getEntitiesWithinAABB(EntityPlayer.class, aabb);

		for (EntityPlayer entity : entityList) {
			entity.removePotionEffect(PotionInit.breakblock);
		}

		return true;
	}

	// ブロックを壊したときの処理
	public void breakBlock(World world, BlockPos pos, IBlockState state) {

		for (EnumFacing face : EnumFacing.VALUES) {

			BlockPos facePos =  pos.offset(face);
			Block block = world.getBlockState(facePos).getBlock();
			if (block != BlockInit.magicbarrier_off) { continue; }

			this.breakBlock(facePos, world, true);
		}
	}

	// アイテムをドロップ
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return new ItemStack(BlockInit.magicbarrier_off).getItem();
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return this.data == 0;
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return this.data == 0 ? new TileMagicBarrier() : null;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return this.data == 1;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return this.data == 0 ? BlockRenderLayer.SOLID : BlockRenderLayer.TRANSLUCENT;
	}
}
