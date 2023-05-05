package sweetmagic.init.item.sm.seed;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.BlockLog;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.init.ItemInit;
import sweetmagic.init.item.sm.sweetmagic.SMItem;

public class SMSeed extends SMItem implements IPlantable {

	public IBlockState state;
	private final int data;
	private String name;

	public SMSeed(String name, Block block, int data) {
		super(name, ItemInit.foodList);
		this.state = block.getDefaultState();
		this.data = data;
		this.name = name;
	}

	/**
	 * 0 = 耕した土
	 * 1 = 土
	 * 2 = 岩か土
	 */

    @Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing face, float x, float y, float z) {

		ItemStack stack = player.getHeldItem(hand);
		if (!player.canPlayerEdit(pos.offset(face), face, stack)) { return EnumActionResult.PASS; }

		if (face == EnumFacing.UP  && world.isAirBlock(pos.up())) {

			IBlockState state = world.getBlockState(pos);
			Block block = state.getBlock();
			Material m = state.getMaterial();

			switch (this.data) {
			case 0:
				if( !(block instanceof BlockFarmland) && !block.canSustainPlant(state, world, pos, face, this)) { return EnumActionResult.PASS; }

				this.setBlock(world, player, stack, m, pos);
				return EnumActionResult.SUCCESS;
			case 1:
				if(m == Material.GROUND || m == Material.GRASS || block.canSustainPlant(state, world, pos, face, this)) {
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
    	this.shrinkItem(player, stack);
		this.playSound(world, player, SoundEvents.BLOCK_SNOW_PLACE, 0.5F, 1F / (world.rand.nextFloat() * 0.4F + 1.2F) + 1 * 0.5F);
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

	//ツールチップの表示
  	@Override
  	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
  		if (this.name == "quartz_seed") {
  	  		String tip = new TextComponentTranslation("tip.quartz_seed.name", new Object[0]).getFormattedText();
  			tooltip.add(I18n.format(TextFormatting.GREEN + tip));
  		}
  	}
}
