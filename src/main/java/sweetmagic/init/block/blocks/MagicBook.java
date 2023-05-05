package sweetmagic.init.block.blocks;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.init.BlockInit;
import sweetmagic.init.base.BaseFaceBlock;

public class MagicBook extends BaseFaceBlock {

	private final static AxisAlignedBB AABB = new AxisAlignedBB(0.05D, 0D, 0.05D, 0.95D, 0.875D, 0.95D);

	public MagicBook(String name) {
		super(Material.WOOD, name);
		setHardness(1.0F);
        setResistance(1024F);
		setSoundType(SoundType.WOOD);
		BlockInit.furniList.add(this);
	}

	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return AABB;
	}

    @Override
	public float getEnchantPowerBonus(World world, BlockPos pos) {
		return 1F;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag advanced) {
		tooltip.add(I18n.format(TextFormatting.GREEN + this.getTip("tip.enchantpower.name") + " : " + 1.0F ));
	}
}
