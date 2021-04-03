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
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
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

	public BlockParallelInterfere(String name, int data) {
		super(Material.IRON, name);
		setHardness(1.0F);
		setResistance(16F);
		setSoundType(SoundType.STONE);
		disableStats();
		this.data = data;
		BlockInit.blockList.add(this);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return new AxisAlignedBB(0, 0, 0, 1, 0.75, 1);
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
		//xx_xx.langファイルから文字を取得する方法
		TextComponentTranslation text1 = new TextComponentTranslation("tip.parallelinterfere_title.name", new Object[0]);
		TextComponentTranslation text2 = new TextComponentTranslation("tip.parallelinterfere_text.name", new Object[0]);
		tooltip.add(I18n.format(TextFormatting.GOLD + text1.getFormattedText()));
		tooltip.add(I18n.format(TextFormatting.GREEN + text2.getFormattedText()));
	}

    public ItemStack getItem(World world, BlockPos pos, IBlockState state) {
        return ItemStack.EMPTY;
    }

    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return null;
    }

    @Override
	public float getEnchantPowerBonus(World world, BlockPos pos) {
		return 15;
	}
}
