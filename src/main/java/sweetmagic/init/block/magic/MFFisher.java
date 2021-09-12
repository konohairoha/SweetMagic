package sweetmagic.init.block.magic;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.handlers.SMGuiHandler;
import sweetmagic.init.BlockInit;
import sweetmagic.init.base.BaseMFBlock;
import sweetmagic.init.tile.magic.TileFlyishForer;
import sweetmagic.init.tile.magic.TileMFFisher;

public class MFFisher extends BaseMFBlock {

	public final int data;

    public MFFisher(String name, int data) {
		super(name);
		this.data = data;
		BlockInit.magicList.add(this);
    }

	// ブロックでのアクション
	@Override
	public void actionBlock (World world, BlockPos pos, EntityPlayer player, ItemStack stack) {
		if (world.isRemote) { return; }
		this.openGui(world, player, pos, SMGuiHandler.MFFISHER_GUI);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.TRANSLUCENT;
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		switch (this.data) {
		case 0:	  return new TileMFFisher();
		case 1:	  return new TileFlyishForer();
		default: return null;
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag advanced) {

		String tip = "";

		switch (this.data) {
		case 0:
			tip = "tip.mffisher.name";
			break;
		case 1:
			tip = "tip.flyishforer.name";
			break;
		}

		tooltip.add(I18n.format(TextFormatting.GOLD + this.getTip("tip.sm_redstone.name")));
		tooltip.add(I18n.format(TextFormatting.GREEN + this.getTip(tip)));
		super.addInformation(stack, world, tooltip, advanced);
	}
}
