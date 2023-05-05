package sweetmagic.init.block.blocks;


import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.IInteractionObject;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.init.BlockInit;
import sweetmagic.init.base.BaseFaceBlock;
import sweetmagic.init.tile.container.ContainerChoppingBoard;
import sweetmagic.util.FaceAABB;

public class ChoppingBoard extends BaseFaceBlock {

	private final static AxisAlignedBB[] AABB = new FaceAABB(0.0625D, 0D, 0.25D, 0.75D, 0.0625D, 0.75D).getRotatedBounds();

	public ChoppingBoard (String name) {
		super(Material.WOOD, name);
        setHardness(0.25F);
		BlockInit.furniList.add(this);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return AABB[state.getValue(FACING).rotateYCCW().getHorizontalIndex()];
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing face, float x, float y, float z) {

		if (world.isRemote) { return true; }

		player.displayGui(new ChoppingBoard.InterfaceCraftingTable(world, pos));
		player.addStat(StatList.CRAFTING_TABLE_INTERACTION);
		return true;
	}

	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag advanced) {
		tooltip.add(I18n.format(TextFormatting.GOLD + this.getTip("tip.sm_chopping_board.name")));
		super.addInformation(stack, world, tooltip, advanced);
	}

	public static class InterfaceCraftingTable implements IInteractionObject {

		private final World world;
		private final BlockPos pos;

		public InterfaceCraftingTable(World world, BlockPos pos) {
			this.world = world;
			this.pos = pos;
		}

		public String getName() {
			return null;
		}

		public boolean hasCustomName() {
			return false;
		}

		public ITextComponent getDisplayName() {
			return new TextComponentTranslation(BlockInit.chopping_board.getUnlocalizedName() + ".name", new Object[0]);
		}

		public Container createContainer(InventoryPlayer pInv, EntityPlayer player) {
			return new ContainerChoppingBoard(pInv, this.world, this.pos);
		}

		public String getGuiID() {
			return "minecraft:crafting_table";
		}
	}

}
