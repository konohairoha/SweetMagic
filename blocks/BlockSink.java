package sweetmagic.init.block.blocks;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.init.BlockInit;
import sweetmagic.init.ItemInit;
import sweetmagic.init.base.BaseFaceBlock;

public class BlockSink extends BaseFaceBlock {

	private static final PropertyBool WATER = PropertyBool.create("water");

	public BlockSink(String name) {
		super(Material.WOOD, name);
		setHardness(0.3F);
        setResistance(1024F);
		setSoundType(SoundType.STONE);
		setDefaultState(this.blockState.getBaseState()
				.withProperty(FACING, EnumFacing.NORTH).withProperty(WATER, false));
		disableStats();
		BlockInit.furniList.add(this);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { FACING, WATER });
	}

	// 右クリックの処理
	public boolean actionBlock (World world, IBlockState state, BlockPos pos, EntityPlayer player, ItemStack stack) {

		// 水の流してる状態の取得
		boolean isWater = state.getValue(WATER);

		// バケツの持っているフラフを取得
		Item item = stack.getItem();
		boolean isBucket = item == Items.BUCKET || item == ItemInit.alt_bucket;

		// 水を流していてバケツを持っているなら
		if (isWater && isBucket ) {
			if (!world.isRemote) {
				this.spawnItem(world, player, new ItemStack(ItemInit.watercup, 4));
			}
            player.playSound(SoundEvents.ITEM_BUCKET_FILL, 1F, 1F);
			return true;
		}

		// 水を流す状態の切り替え
		else if (!isBucket) {
			world.setBlockState(pos, state.withProperty(WATER, !isWater), 3);
            player.playSound(SoundEvents.BLOCK_LEVER_CLICK, 0.5F, isWater ? 1.15F : 1F);
			return true;
		}

		return false;
	}

	// IBlockStateからItemStackのmetadataを生成。ドロップ時とテクスチャ・モデル参照時に呼ばれる
	@Override
	public int getMetaFromState(IBlockState state) {
		return ((EnumFacing) state.getValue(FACING)).getHorizontalIndex() + ( state.getValue(WATER) ? 4 : 0 );
	}

	// ItemStackのmetadataからIBlockStateを生成。設置時に呼ばれる
	@Override
	public IBlockState getStateFromMeta(int meta) {

		boolean isWater = false;

		if (meta >= 4) {
			meta -= 4;
			isWater = true;
		}

		return this.getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta)).withProperty(WATER, isWater);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
		tooltip.add(I18n.format(TextFormatting.GOLD + this.getTip("tip.smsink.name")));
	}
}
