package sweetmagic.init.item.sm.seed;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemFood;
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
import sweetmagic.init.ItemInit;

public class SMSeedFood extends ItemFood implements IPlantable {

	public IBlockState state;
	public final boolean isSetGrass;

    public SMSeedFood(String name, Block block, int healAmount, float saturation, boolean isSetSeedGrass) {
    	super(healAmount, saturation, false);
    	setUnlocalizedName(name);
        setRegistryName(name);
        setAlwaysEdible();
        this.state = block.getDefaultState();
        this.isSetGrass = isSetSeedGrass;
        ItemInit.foodList.add(this);
    }

    @Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing face, float x, float y, float z) {

		if (face != EnumFacing.UP  || !world.isAirBlock(pos.up())) { return EnumActionResult.FAIL; }

		ItemStack stack = player.getHeldItem(hand);
		if (!player.canPlayerEdit(pos.offset(face), face, stack)) { return EnumActionResult.FAIL; }

		IBlockState state = world.getBlockState(pos);
		Block block = state.getBlock();
		Material material = state.getMaterial();

		if(!this.isSetGrass) {

			if( !(block instanceof BlockFarmland) && !block.canSustainPlant(state, world, pos, face, this)) { return EnumActionResult.PASS; }

			this.setBlock(world, player, stack, material, pos);

		} else if(material == Material.GROUND || material == Material.GRASS || block.canSustainPlant(state, world, pos, face, this)) {
			this.setBlock(world, player, stack, material, pos);
		}
		return EnumActionResult.SUCCESS;
	}

    public void setBlock (World world, EntityPlayer player, ItemStack stack, Material material, BlockPos pos) {
		if (!player.capabilities.isCreativeMode) { stack.shrink(1); }
		world.playSound(player, new BlockPos(player), SoundEvents.BLOCK_SNOW_PLACE, SoundCategory.PLAYERS, 0.5F, 1.0F / (world.rand.nextFloat() * 0.4F + 1.2F) + 1 * 0.5F);
		world.setBlockState(pos.up(), this.state, 3);
    }

    @Override
    public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos){
        return EnumPlantType.Crop;
    }

    @Override
    public IBlockState getPlant(IBlockAccess world, BlockPos pos){
        return this.state;
    }
}
