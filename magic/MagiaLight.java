package sweetmagic.init.block.magic;

import java.util.List;

import javax.annotation.Nullable;

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
import sweetmagic.config.SMConfig;
import sweetmagic.handlers.CapabilityHandler;
import sweetmagic.init.BlockInit;
import sweetmagic.init.base.BaseMFFace;
import sweetmagic.init.tile.magic.TileMagiaLantern;
import sweetmagic.init.tile.magic.TileMagiaLight;
import sweetmagic.spwanblock.SpawnBlockRegsterContainer;

public class MagiaLight extends BaseMFFace {

	private final int data;
	private static final AxisAlignedBB WRITE = new AxisAlignedBB(0.15D, 0D, 0.15D, 0.85D, 0.85D, 0.85D);
	private static final AxisAlignedBB LIGHT = new AxisAlignedBB(0.25D, 0D, 0.25D, 0.75D, 0.75D, 0.75D);

    public MagiaLight(String name, int data) {
		super(name);
		setLightLevel(1F);
		this.data = data;
		BlockInit.magicList.add(this);
    }

	// ブロックでのアクション
	@Override
	public void actionBlock (World world, BlockPos pos, EntityPlayer player, ItemStack stack) {

		if (world.isRemote) { return; }

		TileMagiaLight tile = (TileMagiaLight) world.getTileEntity(pos);
		TextComponentTranslation tip = new TextComponentTranslation("tip.mf_amount.name");
		player.sendMessage(tip.appendText(String.format("%,d", tile.getMF())));
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {

		switch(this.data) {
		case 0: return new TileMagiaLight();
		case 1: return new TileMagiaLantern();
		}

		return null;
	}

	@Override
	public void onBlockAdded(World world, BlockPos pos, IBlockState state) {

		super.onBlockAdded(world, pos, state);
		SpawnBlockRegsterContainer container = world.getCapability(CapabilityHandler.CONTAINER_REGISTRY, null);
		if(container != null) {
			container.getRegistry().register(pos);
		}
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {

		super.breakBlock(world, pos, state);
		SpawnBlockRegsterContainer container = world.getCapability(CapabilityHandler.CONTAINER_REGISTRY, null);
		if(container != null) {
			container.getRegistry().unregister(pos);
		}
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return this.data == 0 ? WRITE : LIGHT;
	}

	@Override
	public int getMaxMF() {
		switch (this.data) {
		case 0:	  return 1000000;
		case 1:	  return 50000;
		}
		return super.getMaxMF();
	}

	@Override
	public int getTier() {
		return this.data == 0 ? 2 : 1;
	}

	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag advanced) {

		int range = this.data == 0 ? SMConfig.magiaLightRange : SMConfig.magiaLightRange / 4;

		tooltip.add(I18n.format(TextFormatting.RED + this.getTip("tip.sm_redstone.name")));
		tooltip.add(I18n.format(TextFormatting.GOLD + this.getTip("tip.magia_light.name")));
		tooltip.add(I18n.format(TextFormatting.GREEN + this.getTip("tip.magia_light_range.name") + range));
		tooltip.add(I18n.format(""));
		super.addInformation(stack, world, tooltip, advanced);
	}
}
