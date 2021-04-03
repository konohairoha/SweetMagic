package sweetmagic.init.block.magic;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.SweetMagicCore;
import sweetmagic.handlers.SMGuiHandler;
import sweetmagic.init.BlockInit;
import sweetmagic.init.base.BaseMFFace;
import sweetmagic.init.tile.chest.TileGravityChest;

public class GravityChest extends BaseMFFace {

	public final int data;

    public GravityChest(String name, int data) {
		super(name);
		this.data = data;
		BlockInit.magicList.add(this);
    }

	// ブロックでのアクション
	@Override
	public void actionBlock (World world, BlockPos pos, EntityPlayer player, ItemStack stack) {
		if (world.isRemote) { return; }
		player.openGui(SweetMagicCore.INSTANCE, SMGuiHandler.GRAVITYCHEST, world, pos.getX(), pos.getY(), pos.getZ());
		this.playerSound(world, pos, SoundEvents.BLOCK_PISTON_CONTRACT, 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		switch (this.data) {
		case 0:
			return new TileGravityChest();
		}
		return null;
	}

	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag advanced) {
		String text = new TextComponentTranslation("tip.gravity_chest.name", new Object[0]).getFormattedText();
		tooltip.add(I18n.format(TextFormatting.GOLD + text));
	}
}
