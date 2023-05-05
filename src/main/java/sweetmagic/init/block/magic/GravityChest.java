package sweetmagic.init.block.magic;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.handlers.SMGuiHandler;
import sweetmagic.init.BlockInit;
import sweetmagic.init.base.BaseMFFace;
import sweetmagic.init.tile.magic.TileGravityChest;
import sweetmagic.init.tile.magic.TileMFBase;

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
		this.openGui(world, player, pos, SMGuiHandler.GRAVITYCHEST);
		this.playerSound(world, pos, SoundEvents.BLOCK_PISTON_CONTRACT, 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		switch (this.data) {
		case 0: return new TileGravityChest();
		}
		return null;
	}

    public void breakBlock(World world, BlockPos pos, IBlockState state) {

    	TileGravityChest tile = (TileGravityChest) world.getTileEntity(pos);

		if (tile.isSlotEmpty() && tile.isMfEmpty() && tile.range == 5) {
			spawnAsEntity(world, pos, new ItemStack(this));
			return;
		}

        super.breakBlock(world, pos, state);
	}

	public ItemStack setTagStack (TileMFBase tile, ItemStack stack, NBTTagCompound tags) {

		ItemStack stack2 = super.setTagStack(tile, stack, tags);
		NBTTagCompound tagStack= stack2.getTagCompound();
		tagStack.setInteger("range", ( (TileGravityChest)tile).range);

		return stack2;
	}

	@Override
	public int getMaxMF() {
		return 100000;
	}

	// フェンスとかにつながないように
	public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.SOLID;
	}

	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag advanced) {
		tooltip.add(I18n.format(TextFormatting.RED + this.getTip("tip.sm_redstone.name")));
		tooltip.add(I18n.format(TextFormatting.GOLD + this.getTip("tip.gravity_chest.name")));

		int range = 5;

		if (stack.hasTagCompound() && stack.getTagCompound().hasKey("range")) {
			range = stack.getTagCompound().getInteger("range");
		}

		tooltip.add(I18n.format(TextFormatting.GOLD + this.getTip("tip.sm_range.name") + "：" + TextFormatting.GREEN + range));
		tooltip.add(I18n.format(""));
		super.addInformation(stack, world, tooltip, advanced);
	}
}
