package sweetmagic.init.block.magic;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.init.BlockInit;
import sweetmagic.init.base.BaseMFBlock;
import sweetmagic.init.tile.magic.TileMFPot;

public class MFPot extends BaseMFBlock {

	public final int data;

    public MFPot(String name, int data) {
        super(name);
        this.data = data;
		BlockInit.blockList.add(this);
    }

	// ブロックでのアクション
	@Override
	public void actionBlock (World world, BlockPos pos, EntityPlayer player, ItemStack stack) {
		if (world.isRemote) { return; }
		TileMFPot tile = (TileMFPot) world.getTileEntity(pos);
		player.sendMessage(new TextComponentString("MF量：" + tile.getMF()));
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return new AxisAlignedBB(0.4D, 0.0D, 0.4D, 0.6D, 0.5D, 0.6D);
	}

	@Override
	public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
		return new TileMFPot();
	}

	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World playerIn, List<String> tooltip, ITooltipFlag advanced) {

		super.addInformation(stack, playerIn, tooltip, advanced);

		String tip = "";

		switch (this.data) {
		case 0:
			tip = "tip.mfpot_dm.name";
			break;
		case 1:
			tip = "tip.mfpot_tw.name";
			break;
		}

		String text = new TextComponentTranslation(tip, new Object[0]).getFormattedText();
		tooltip.add(I18n.format(TextFormatting.GOLD + text));
	}
}
