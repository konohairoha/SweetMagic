package sweetmagic.init.block.blocks;

import java.util.Random;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.init.base.BaseModelBlock;

public class BlockEncPower extends BaseModelBlock {

	private final float enchantPower;

    public BlockEncPower(String name, float hardness, float resistance, int harvestLevel, float light, float encPower, SoundType sType) {
        super(Material.GLASS, name);
        setHardness(hardness);
        setResistance(resistance);
        setHarvestLevel("pickaxe", harvestLevel);
        setSoundType(sType);
        setLightLevel(light);
		this.enchantPower = encPower;
    }

    @Override
	public float getEnchantPowerBonus(World world, BlockPos pos) {
		return this.enchantPower;
	}

	@Override
	public boolean isSideSolid(IBlockState base_state, IBlockAccess world, BlockPos pos, EnumFacing side) {
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT_MIPPED;
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return Item.getItemFromBlock(this);
	}

    public ItemStack getItem(World world, BlockPos pos, IBlockState state) {
        return new ItemStack(this);
    }
}
