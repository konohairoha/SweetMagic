package sweetmagic.init.item.sm.seed;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.BlockLog;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import sweetmagic.init.item.sm.sweetmagic.SMFoodItem;

public class SMSeed extends SMFoodItem implements IPlantable {

	public IBlockState state;
	public final int data;

	public SMSeed(String name, Block block, int data) {
		super(name);
		this.state = block.getDefaultState();
		this.data = data;
	}

	/**
	 * 0 = 耕した土
	 * 1 = 土
	 * 2 = 岩か土
	 */

    @Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing,float x, float y, float z) {

		ItemStack stack = player.getHeldItem(hand);
		if (!player.canPlayerEdit(pos.offset(facing), facing, stack)) { return EnumActionResult.PASS; }

		if (facing == EnumFacing.UP  && world.isAirBlock(pos.up())) {

			IBlockState state = world.getBlockState(pos);
			Block block = state.getBlock();
			Material m = state.getMaterial();

			switch (this.data) {
			case 0:
				if( !(block instanceof BlockFarmland) && !block.canSustainPlant(state, world, pos, facing, this)) { return EnumActionResult.PASS; }

				this.setBlock(world, player, stack, m, pos);
				return EnumActionResult.SUCCESS;
			case 1:
				if(m == Material.GROUND || m == Material.GRASS || block.canSustainPlant(state, world, pos, facing, this)) {
					this.setBlock(world, player, stack, m, pos);
					return EnumActionResult.SUCCESS;
				}
				return EnumActionResult.PASS;
			case 2:
				if((m == Material.ROCK || m == Material.GROUND || block instanceof BlockLog) && block.isFullBlock(state)) {
					this.setBlock(world, player, stack, m, pos);
					return EnumActionResult.SUCCESS;
				}
				return EnumActionResult.PASS;
			}
		}

        return EnumActionResult.PASS;
	}

    public void setBlock (World world, EntityPlayer player, ItemStack stack, Material material, BlockPos pos) {
		if (!player.capabilities.isCreativeMode) { stack.shrink(1); }
		world.playSound(player, new BlockPos(player), SoundEvents.BLOCK_SNOW_PLACE, SoundCategory.PLAYERS, 0.5F, 1.0F / (world.rand.nextFloat() * 0.4F + 1.2F) + 1 * 0.5F);
		world.setBlockState(pos.up(), this.state, 3);
    }

	@Override
	public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos) {
		return EnumPlantType.Crop;
	}

	@Override
	public IBlockState getPlant(IBlockAccess world, BlockPos pos) {
		return this.state;
	}
}
