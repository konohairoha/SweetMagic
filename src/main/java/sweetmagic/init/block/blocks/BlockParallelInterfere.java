package sweetmagic.init.block.blocks;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.SweetMagicCore;
import sweetmagic.event.SMSoundEvent;
import sweetmagic.handlers.SMGuiHandler;
import sweetmagic.init.BlockInit;
import sweetmagic.init.base.BaseFaceBlock;
import sweetmagic.init.tile.magic.TileParallelInterfere;
import sweetmagic.init.tile.magic.TileStardustWish;

public class BlockParallelInterfere extends BaseFaceBlock {

	public final int data;
	public final static AxisAlignedBB AABB = new AxisAlignedBB(0D, 0D, 0D, 1D, 0.75D, 1D);

	public BlockParallelInterfere(String name, int data) {
		super(Material.IRON, name);
		setHardness(0.5F);
		setResistance(1024F);
		setSoundType(SoundType.STONE);
		disableStats();
		this.data = data;
		BlockInit.blockList.add(this);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return AABB;
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {

		switch (this.data) {
		case 0:
			return new TileParallelInterfere();
		case 1:
			return new TileStardustWish();
		}

		return null;
	}

    // 向き変更対応
	public boolean rotateBlock(World world, BlockPos pos, EnumFacing face) {

		TileParallelInterfere tile = (TileParallelInterfere) world.getTileEntity(pos);
		boolean flag = super.rotateBlock(world, pos, face);

		if (tile != null) {
            tile.validate();
            world.setTileEntity(pos, tile);
        }

		return flag;
	}


	//右クリックの処理
	public boolean actionBlock (World world, IBlockState state, BlockPos pos, EntityPlayer player, ItemStack stack) {

		if (world.isRemote) { return true; }

		this.playerSound(world, pos, SMSoundEvent.PAGE, 0.33F, 1F);
		int guiId = 0;

		switch (this.data) {
		case 0:
			guiId = SMGuiHandler.PARALLELINTERFERE_GUI;
			break;
		case 1:
			guiId = SMGuiHandler.STARDUSTWISH;
			break;
		}

		player.openGui(SweetMagicCore.INSTANCE, guiId, world, pos.getX(), pos.getY(), pos.getZ());
		return true;
	}

    public void breakBlock(World world, BlockPos pos, IBlockState state) {
    	TileParallelInterfere tile = (TileParallelInterfere) world.getTileEntity(pos);
		ItemStack stack = new ItemStack(Item.getItemFromBlock(this));
		NBTTagCompound tags = new NBTTagCompound();
		NBTTagCompound tileTags = tile.writeToNBT(new NBTTagCompound());
		tags.setTag("BlockEntityTag", tileTags);
		stack.setTagCompound(tags);
		spawnAsEntity(world, pos, stack);
		world.updateComparatorOutputLevel(pos, state.getBlock());
        super.breakBlock(world, pos, state);
    }

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag advanced) {
		tooltip.add(I18n.format(TextFormatting.GOLD + this.getTip("tip.parallelinterfere_title.name")));
		tooltip.add(I18n.format(TextFormatting.GREEN + this.getTip("tip.parallelinterfere_text.name")));
	}

    public ItemStack getItem(World world, BlockPos pos, IBlockState state) {
        return ItemStack.EMPTY;
    }

    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return null;
    }

    @Override
	public float getEnchantPowerBonus(World world, BlockPos pos) {
		return this.data == 0 ? 15 : 30;
	}
}
