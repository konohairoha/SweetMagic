package sweetmagic.init.block.blocks;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.SweetMagicCore;
import sweetmagic.handlers.SMGuiHandler;
import sweetmagic.init.BlockInit;
import sweetmagic.init.base.BaseModelBlock;
import sweetmagic.init.tile.chest.TileRattanBasket;

public class BlockRattanBasket extends BaseModelBlock {

	public final int data;

	public BlockRattanBasket(String name, int data) {
		super(Material.WOOD, name);
		setHardness(0.1F);
		setResistance(256F);
		setSoundType(SoundType.WOOD);
		this.data = data;
		BlockInit.blockList.add(this);
	}

	/**
	 * 0 =
	 */

	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return new AxisAlignedBB(0.25D, 0.62D, 0.25D, 0.75D, 0D, 0.75D);
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileRattanBasket();
	}

	// ブロックでのアクション
	@Override
	public boolean actionBlock (World world, IBlockState state, BlockPos pos, EntityPlayer player, ItemStack stack) {
		if (world.isRemote) { return true; }
		player.openGui(SweetMagicCore.INSTANCE, SMGuiHandler.RATTAMBASKET, world, pos.getX(), pos.getY(), pos.getZ());
		return true;
	}

	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World playerIn, List<String> tooltip, ITooltipFlag advanced) {
		String text = new TextComponentTranslation("tip.rattanbasket.name", new Object[0]).getFormattedText();
		tooltip.add(I18n.format(TextFormatting.GOLD + text));
	}
}
