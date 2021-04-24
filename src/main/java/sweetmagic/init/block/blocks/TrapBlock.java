package sweetmagic.init.block.blocks;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import sweetmagic.init.BlockInit;
import sweetmagic.init.PotionInit;
import sweetmagic.init.base.BaseModelBlock;
import sweetmagic.util.PlayerHelper;

public class TrapBlock extends BaseModelBlock {

	private final int data;
	private final static AxisAlignedBB AABB = new AxisAlignedBB(0D, 0D, 0D, 1D, 0.95D, 1D);

	public TrapBlock (String name, int data) {
		super(Material.ROCK, name);
		setHardness(1F);
		setResistance(9999);
		this.data = data;
		BlockInit.blockList.add(this);
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
		return AABB;
	}

	// ブロックの上にいたら
	@Override
	public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity) {

		if (!(entity instanceof EntityPlayer ) || ((EntityPlayer)entity).isCreative()) { return; }

		EntityPlayer player = (EntityPlayer) entity;

		switch (this.data) {
		case 0:
			if (!player.isPotionActive(PotionInit.deadly_poison)) {
				PlayerHelper.addPotion((EntityPlayer) entity, PotionInit.deadly_poison, 300, 1);
			}
			break;
		}
	}

    public ItemStack getItem(World world, BlockPos pos, IBlockState state) {
        return ItemStack.EMPTY;
    }

    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return null;
    }
}
