package sweetmagic.init.block.blocks;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.api.enumblock.EnumLocal;
import sweetmagic.api.enumblock.EnumLocal.PropertyLocal;
import sweetmagic.api.iblock.IChangeBlock;
import sweetmagic.init.BlockInit;

public class BlockPillarStone extends AntiqueBrick implements IChangeBlock {

	protected static final PropertyLocal LOCAL = new PropertyLocal("local", EnumLocal.getLocalList());
	private final int data;
	private final boolean isBase;

    public BlockPillarStone(String name, int data, boolean isBase) {
        super(name, 1F, 1024F, 0, 0);
        this.data = data;
        this.isBase = isBase;
    	setDefaultState(this.blockState.getBaseState().withProperty(LOCAL, EnumLocal.NOR));

		BlockInit.blockList.remove(this);

		if (isBase) {
			BlockInit.noTabList.add(this);
		}

		else {
			BlockInit.furniList.add(this);
		}
    }

	//右クリックの処理
    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing face, float hitX, float hitY, float hitZ) {

    	if (player.isSneaking()) {
    		if (!world.isRemote) {
    			this.setBlock(world, pos, player);
    		}
    		return true;
    	}

		return false;
	}

	// サウンド
	public void playerSound (World world, BlockPos pos, SoundEvent sound, float vol, float pit) {
		world.playSound(null, pos, sound, SoundCategory.BLOCKS, vol, pit);
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
		boolean top = this.checkBlock(world.getBlockState(pos.down()).getBlock());
		boolean bot = this.checkBlock(world.getBlockState(pos.up()).getBlock());
		return this.isBase ? state : state.withProperty(LOCAL, EnumLocal.getLocal(top, bot));
	}

	public boolean checkBlock (Block block) {
		return block instanceof BlockPillarStone || block == BlockInit.smspaner;
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return 0;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState();
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { LOCAL });
	}

	public Block getBlock () {
		return this.data == 1 ? BlockInit.pillar_stone : BlockInit.pillar_stone_w;
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return Item.getItemFromBlock(!this.isBase ? this : this.getBlock());
	}

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		return new ItemStack(!this.isBase ? this : this.getBlock());
	}

	@Override
	public void setBlock(World world, BlockPos pos, EntityPlayer player) {

		Block block = null;

		switch (this.data) {
		case 0:
			block = BlockInit.pillar_stone_base;
			break;
		case 1:
			block = BlockInit.pillar_stone;
			break;
		case 2:
			block = BlockInit.pillar_stone_w_base;
			break;
		case 3:
			block = BlockInit.pillar_stone_w;
			break;
		}

		world.setBlockState(pos, block.getDefaultState(), 2);
        SoundType sound = this.getSoundType(block.getDefaultState(), world, pos, player);
        this.playerSound(world, pos, sound.getPlaceSound(),(sound.getVolume() + 1.0F) / 2.0F, sound.getPitch() * 0.8F);
	}

	//ツールチップの表示
  	@Override
  	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
		tooltip.add(I18n.format(TextFormatting.GREEN + this.getTip("tip.lantern_side.name")));
  	}

	// ツールチップ取得
	public String getTip (String tip) {
		return new TextComponentTranslation(tip, new Object[0]).getFormattedText();
	}
}
