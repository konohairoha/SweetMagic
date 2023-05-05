package sweetmagic.init.block.blocks;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
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
import sweetmagic.init.tile.chest.TileMagiaStorage;
import sweetmagic.init.tile.chest.TileWoodChest;

public class BlockMagiaStorage extends BaseFaceBlock {

	private final int data;
	private final static AxisAlignedBB AABB = new AxisAlignedBB(0.15D, 0.15D, 0.15D, 0.85D, 0.85D, 0.85D);

	public BlockMagiaStorage(String name, int data) {
		super(Material.GLASS, name);
		setHardness(0.5F);
		setResistance(1024);
		setSoundType(SoundType.GLASS);
		setLightLevel(0.75F);
		this.data = data;
		BlockInit.furniList.add(this);
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileMagiaStorage();
	}

	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return AABB;
	}

	// ブロック設置したときの処理(上のブロックを設置)
	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		TileMagiaStorage tile = (TileMagiaStorage) world.getTileEntity(pos);
		tile.checkRangePlayer();
	}

	// ブロックでのアクション
	@Override
	public boolean actionBlock (World world, IBlockState state, BlockPos pos, EntityPlayer player, ItemStack stack) {

		if (world.isRemote) { return true; }

		this.playerSound(world, pos, SMSoundEvent.STORAGE, 0.5F, 1F);
		player.openGui(SweetMagicCore.INSTANCE, SMGuiHandler.MFSTORAGE_GUI, world, pos.getX(), pos.getY(), pos.getZ());

		return true;
	}

    public void breakBlock(World world, BlockPos pos, IBlockState state) {

    	TileWoodChest tile = (TileWoodChest) world.getTileEntity(pos);
		ItemStack stack = new ItemStack(this);

		if (tile.isSlotEmpty()) {
			spawnAsEntity(world, pos, stack);
			return;
		}

		NBTTagCompound tags = new NBTTagCompound();
		tags.setTag("BlockEntityTag", tile.writeToNBT(new NBTTagCompound()));
		stack.setTagCompound(tags);
		spawnAsEntity(world, pos, stack);
		world.updateComparatorOutputLevel(pos, state.getBlock());
        super.breakBlock(world, pos, state);
	}

    public ItemStack getItem(World world, BlockPos pos, IBlockState state) {
        return ItemStack.EMPTY;
    }

    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return null;
    }

	public int getTier () {
		return this.data;
	}

	public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer) {
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag advanced) {
		tooltip.add(I18n.format(TextFormatting.GOLD + this.getTip("tip.parallelinterfere_title.name")));

		int tier = this.getTier() + 1;
		int limit = 256;

		if (tier == 5) {
			limit = Integer.MAX_VALUE;
		}

		else if (tier != 1) {
			limit *= (int) Math.pow(2, tier);
		}

		tooltip.add(I18n.format(TextFormatting.GOLD + this.getTip("tip.magiastorage.name") + " " + String.format("%,d", limit)));
	}
}
