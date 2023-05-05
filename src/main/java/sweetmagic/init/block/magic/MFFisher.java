package sweetmagic.init.block.magic;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.handlers.SMGuiHandler;
import sweetmagic.init.BlockInit;
import sweetmagic.init.base.BaseMFBlock;
import sweetmagic.init.tile.magic.TileMFFisher;
import sweetmagic.init.tile.magic.TileMFForer;
import sweetmagic.init.tile.magic.TileMFGeneration;
import sweetmagic.init.tile.magic.TileMFSqueezer;

public class MFFisher extends BaseMFBlock {

	private final int data;

    public MFFisher(String name, int data) {
		super(name);
		this.data = data;
		BlockInit.magicList.add(this);
    }

    /**
     * 0 = mfフィッシャー
     * 1 = mfフォーアラー
     * 2 = mfスクイザー
     * 3 = mfジェネレーター
     * 4 = mfハーヴェスター
     */

	// ブロックでのアクション
	@Override
	public void actionBlock (World world, BlockPos pos, EntityPlayer player, ItemStack stack) {
		if (world.isRemote) { return; }

		this.openGui(world, player, pos, SMGuiHandler.MFFISHER_GUI);
	}

	// フェンスとかにつながないように
	public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.SOLID;
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
		case 1:	  return new TileMFForer();
		case 2:	  return new TileMFSqueezer();
		case 3:	  return new TileMFGeneration();
		default: return null;
		}
	}

	public int getData () {
		return this.data;
	}

	@Override
	public int getMaxMF() {
		switch (this.data) {
		case 0:	  return 10000;
		case 1:	  return 10000;
		case 2:	  return 10000;
		case 3:	  return 100000;
		}
		return super.getMaxMF();
	}

	@Override
	public int getTier() {

		switch (this.getData()) {
		case 3: return 2;
		}

		return 1;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag advanced) {

		String tip = "";
		int mf = 0;

		switch (this.data) {
		case 0:
			tip = "tip.mffisher.name";
			mf = 300;
			break;
		case 1:
			tip = "tip.flyishforer.name";
			mf = 800;
			break;
		case 2:
			tip = "tip.mfsqueezer.name";
			mf = 100;
			break;
		case 3:
			tip = "tip.mfgeneration.name";
			mf = 10000;
			break;
		}

		tooltip.add(I18n.format(TextFormatting.RED + this.getTip("tip.sm_redstone.name")));
		tooltip.add(I18n.format(TextFormatting.GREEN + this.getTip(tip)));
		tooltip.add(I18n.format(TextFormatting.GREEN + this.getTip("tip.requiremf.name") + " ： " + String.format("%,d", mf)));
		tooltip.add(I18n.format(""));
		super.addInformation(stack, world, tooltip, advanced);
	}
}
