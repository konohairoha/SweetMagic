package sweetmagic.init.base;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.api.iblock.IMFBlock;
import sweetmagic.init.ItemInit;
import sweetmagic.init.tile.magic.TileMFBase;

public class BaseMFBlock extends BlockContainer {

	public BaseMFBlock (String name) {
		super(Material.GLASS);
		setRegistryName(name);
		setUnlocalizedName(name);
		setSoundType(SoundType.METAL);
		setHardness(0.5F);
		setResistance(1024.0F);
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {

		if (player == null) { return false; }

		ItemStack stack = player.getHeldItem(hand);

		// 何も持ってなかったら終了
		if (stack.isEmpty()) {
			this.actionBlock(world, pos, player, stack);
			return true;
		}

		// NBTを取得
		NBTTagCompound tags = stack.getTagCompound();

		if (tags == null || !tags.hasKey("X")) {
			if (stack.getItem() == ItemInit.mf_stuff) {
				return this.setBlockPos(tags, stack, player, pos);
			}
			this.actionBlock(world, pos, player, stack);
			return true;
		}

		TileEntity tile = (TileEntity) world.getTileEntity(pos);
		if (tile == null || !(tile instanceof IMFBlock)) { return false; }

		IMFBlock mfBlock = (IMFBlock) tile;
		boolean actionFlag = false;

		// 受け取り側かどうか
		if (mfBlock.getReceive()) {

			// NBTがnull以外なら
			BlockPos tilePos = new BlockPos(tags.getInteger("X"), tags.getInteger("Y"), tags.getInteger("Z"));
			mfBlock.addPosList(tilePos);

			if (!world.isRemote) {
				String tip = new TextComponentTranslation("tip.posregi.name", new Object[0]).getFormattedText();
				player.sendMessage(new TextComponentString(tip));
			} else {
				player.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1, 1);
			}
			actionFlag = true;

			tags.removeTag("X");
			tags.removeTag("Y");
			tags.removeTag("Z");
		}

		// 送り側なら座標を登録
		else {
			actionFlag = this.setBlockPos(tags, stack, player, pos);
		}

		return actionFlag;
	}

	// 座標を登録
	public boolean setBlockPos (NBTTagCompound tags, ItemStack stack, EntityPlayer player, BlockPos pos) {

		// NBTが保存したなかったら初期化
		if (tags == null) {
			stack.setTagCompound(new NBTTagCompound());
			tags = stack.getTagCompound();
		}

		tags.setInteger("X", pos.getX());
		tags.setInteger("Y", pos.getY());
		tags.setInteger("Z", pos.getZ());

		player.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1, 1);
		String tip = new TextComponentTranslation("tip.pos.name", new Object[0]).getFormattedText();
		player.sendStatusMessage(new TextComponentTranslation(TextFormatting.GREEN + tip + " : " + " " +
				tags.getInteger("X") + ", " + tags.getInteger("Y") + ", " + tags.getInteger("Z")), true);

		return true;
	}

	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		TileMFBase tile = (TileMFBase) world.getTileEntity(pos);
		ItemStack stack = new ItemStack(Item.getItemFromBlock(this));
		NBTTagCompound tags = new NBTTagCompound();
		NBTTagCompound tileTags = tile.writeToNBT(new NBTTagCompound());
		if (tileTags.hasKey(tile.POST)) { tileTags.removeTag(tile.POST); }
		tags.setTag("BlockEntityTag", tileTags);
		this.saveStackList(tags, tile.getList(), "ItemList");
		stack.setTagCompound(tags);
		stack.getTagCompound().setInteger("mf", tile.getMF());
		spawnAsEntity(world, pos, stack);
		world.updateComparatorOutputLevel(pos, state.getBlock());
        super.breakBlock(world, pos, state);
    }

	// List<ItemStack>をnbt保存
	public NBTTagCompound saveStackList (NBTTagCompound tag, List<ItemStack> stackList, String name) {

		// NULLチェックとListの個数を確認
		if (stackList != null && !stackList.isEmpty()) {

			// nbtのリストを作成
			NBTTagList nbtList = new NBTTagList();
			for (ItemStack stack : stackList) {
				if (!stack.isEmpty()) {

					// アイテムスタックごとに保存
	                NBTTagCompound nbt = new NBTTagCompound();
	                stack.writeToNBT(nbt);
	                nbtList.appendTag(nbt);
				}
			}

			// アイテムスタック
			if (!nbtList.hasNoTags()) {
				tag.setTag(name, nbtList);
			}
		}

		return tag;
	}

	// ブロックでのアクション
	public void actionBlock (World world, BlockPos pos, EntityPlayer player, ItemStack stack) {
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return FULL_BLOCK_AABB;
	}

	@Override
	@Nullable
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
		return this.getCollisionBox(blockState, worldIn, pos);
	}

	public AxisAlignedBB getCollisionBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
		return blockState.getBoundingBox(worldIn, pos);
	}

	@Override
	public EnumPushReaction getMobilityFlag(IBlockState state) {
		return EnumPushReaction.BLOCK;

	}

	public boolean isSolidFace(IBlockState state, BlockPos pos, EnumFacing face) {
		return state.getMaterial().isSolid();
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	// フェンスとかにつながないように
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
		return BlockFaceShape.UNDEFINED;
	}

	@Override
	public boolean isSideSolid(IBlockState base_state, IBlockAccess world, BlockPos pos, EnumFacing side) {
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT_MIPPED;
	}

//	@Override
//	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
//		TileMFBase tile = (TileMFBase) world.getTileEntity(pos);
//		NBTTagCompound tag = stack.getTagCompound();
//		if (tag != null) {
//
////			System.out.println("magiaFlux" + tag.getInteger("magiaFlux"));
//			tile.writeNBT(tag);
//			tile.readNBT(tag);
////			tile.setMF(tag.getInteger("magiaFlux"));
////			System.out.println("mf" + tile.getMF());
////			tile.markDirty();
//		}
//	}

    @Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return null;
	}

	public ItemStack getItem(World world, BlockPos pos, IBlockState state) {
		return ItemStack.EMPTY;
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return null;
	}

	/* === RS === */
	@Override
	public boolean hasComparatorInputOverride(IBlockState state) {
		return true;
	}

	@Override
	public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos) {
		return Container.calcRedstone(worldIn.getTileEntity(pos));
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		return state;
	}

	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World playerIn, List<String> tooltip, ITooltipFlag advanced) {

		NBTTagCompound tags = stack.getTagCompound();
		if (tags != null) {
			String mf = String.format("%,d", tags.getInteger("mf"));
			tooltip.add(I18n.format(TextFormatting.GREEN + mf + "MF"));
		} else {
			tooltip.add(I18n.format(TextFormatting.GREEN + "0MF"));
		}
	}

    @Override
	public float getEnchantPowerBonus(World world, BlockPos pos) {
		return 1;
	}
}
