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
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.SweetMagicCore;
import sweetmagic.handlers.SMGuiHandler;
import sweetmagic.init.AdvancedInit;
import sweetmagic.init.BlockInit;
import sweetmagic.init.ItemInit;
import sweetmagic.init.base.BaseFaceBlock;
import sweetmagic.init.tile.chest.TileShoeBox;
import sweetmagic.init.tile.chest.TileWoodChest;
import sweetmagic.util.FaceAABB;

public class BlockWoodChest extends BaseFaceBlock {

	public final int data;
	public boolean isSet = false;
	private final static AxisAlignedBB[] AABB = new FaceAABB(0D, 0D, 0.5D, 1D, 1D, 1D).getRotatedBounds();
	private final static AxisAlignedBB[] SHOE = new FaceAABB(0.1D, 0D, 0.225D, 0.9D, 0.33D, 0.775D).getRotatedBounds();
	private final static AxisAlignedBB[] SHOEPILING = new FaceAABB(0.1D, 0D, 0.225D, 0.9D, 1D, 0.775D).getRotatedBounds();
	private final static AxisAlignedBB[] POST = new FaceAABB(0.4375D, 0D, 0.25D, 0.875D, 1.21875D, 0.75D).getRotatedBounds();

	public BlockWoodChest(String name, int data) {
		super(Material.WOOD, name);
		setHardness(0.5F);
		setResistance(1024);
		setSoundType(SoundType.WOOD);
		this.data = data;
		BlockInit.furniList.add(this);
	}

	/**
	 * 0 = 	ウッドチェスト
	 * 1 = トレジャーチェスト
	 * 2 = キッチンテーブル
	 * 3 = モダンシェルフ
	 * 4 = シューズボックス
	 * 5 = 積み上げたシューズボックス
	 * 6 = ポスト
	 * 7 = 木材ポスト
	 */

	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {

		switch (this.data) {
		case 3: return AABB[state.getValue(FACING).rotateYCCW().getHorizontalIndex()];
		case 4: return SHOE[state.getValue(FACING).getHorizontalIndex()];
		case 5: return SHOEPILING[state.getValue(FACING).getHorizontalIndex()];
		case 6: return POST[state.getValue(FACING).getHorizontalIndex()];
		case 7: return POST[state.getValue(FACING).getHorizontalIndex()];
		}

		return FULL_BLOCK_AABB;
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {

		if (this.data == 4) {
			return new TileShoeBox();
		}

		return new TileWoodChest();
	}

	// 右クリックの処理
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing face, float hitX, float hitY, float hitZ) {

		if (this.data == 2 && face == EnumFacing.UP && player.getHeldItem(hand).getItem() == Items.BUCKET) {
			if (!world.isRemote) {
				this.spawnItem(world, player, new ItemStack(ItemInit.watercup, 9));
			}
            player.playSound(SoundEvents.ITEM_BUCKET_FILL, 1.0F, 1.0F);
			return true;
		}

		return super.onBlockActivated(world, pos, state, player, hand, face, hitX, hitY, hitZ);
	}

	// ブロックでのアクション
	@Override
	public boolean actionBlock (World world, IBlockState state, BlockPos pos, EntityPlayer player, ItemStack stack) {

		if (world.isRemote) { return true; }

		int guiId = 0;
		SoundEvent sound = null;
		float pitch = world.rand.nextFloat() * 0.1F + 0.9F;

		switch (this.data) {
		case 0:
			sound = SoundEvents.BLOCK_PISTON_CONTRACT;
			guiId = SMGuiHandler.WOODCHEST;
			break;
		case 1:
			sound = SoundEvents.BLOCK_IRON_DOOR_OPEN;
			guiId = SMGuiHandler.WOODCHEST;
			break;
		case 2:
		case 3:
			sound = SoundEvents.BLOCK_PISTON_CONTRACT;
			guiId = SMGuiHandler.KICHEN_CHEST_GUI;
			break;
		case 4:
			sound = SoundEvents.BLOCK_SHULKER_BOX_OPEN;
			guiId = SMGuiHandler.SHOEBOX_GUI;
			pitch = 1.25F;
			break;
		case 5:
			sound = SoundEvents.BLOCK_SHULKER_BOX_OPEN;
			guiId = SMGuiHandler.WOODCHEST;
			pitch = 1.25F;
			break;
		case 6:
		case 7:
			sound = SoundEvents.BLOCK_WOODEN_TRAPDOOR_OPEN;
			guiId = SMGuiHandler.WOODCHEST;
			break;
		}

		this.playerSound(world, pos, sound, 0.5F, pitch);
		player.openGui(SweetMagicCore.INSTANCE, guiId, world, pos.getX(), pos.getY(), pos.getZ());

		// 神殿のトレジャーチェストなら開く
		if (this.data == 1 && world.getBlockState(pos.down()).getBlock() == BlockInit.gorgeous_lamp && player instanceof EntityPlayerMP) {
			AdvancedInit.item_use.trigger((EntityPlayerMP) player, new ItemStack(state.getBlock()));
		}

		return true;
	}

    public void breakBlock(World world, BlockPos pos, IBlockState state) {

    	if (this.isSet) { return; }

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

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag advanced) {
		tooltip.add(I18n.format(TextFormatting.GOLD + this.getTip("tip.parallelinterfere_title.name")));
	}
}
