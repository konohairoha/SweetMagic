package sweetmagic.init.block.blocks;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.api.SweetMagicAPI;
import sweetmagic.api.recipe.obmagia.ObMagiaRecipeInfo;
import sweetmagic.event.SMSoundEvent;
import sweetmagic.init.AdvancedInit;
import sweetmagic.init.BlockInit;
import sweetmagic.init.base.BaseFaceBlock;
import sweetmagic.util.ParticleHelper;
import sweetmagic.util.RecipeHelper;
import sweetmagic.util.RecipeUtil;

public class ObMagia extends BaseFaceBlock {

	private static final AxisAlignedBB TOP = new AxisAlignedBB(0D, 0D, 0D, 1D, 0.25D, 1D);
	private static final AxisAlignedBB BOTTOM = new AxisAlignedBB(0D, 0D, 0D, 1D, 1D, 1D);

	public ObMagia(String name, List<Block> list) {
		super(Material.WOOD, name);
        setHardness(0.5F);
        setResistance(1024F);
		this.setSoundType(SoundType.STONE);
		list.add(this);
	}

	@Override
	public boolean canPlaceBlockAt(World world, BlockPos pos) {
		return super.canPlaceBlockAt(world, pos) && super.canPlaceBlockAt(world, pos.up());
	}

	// ブロック設置したときの処理(上のブロックを設置)
	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		world.setBlockState(pos.up(), BlockInit.obmagia_top.getDefaultState().withProperty(FACING, state.getValue(FACING)), 2);
	}

	// ブロックをこわしたとき(下のブロックを指定)
	@Override
	public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
		this.breakBlock(this == BlockInit.obmagia_bottom ? pos.up() : pos.down(), world, true);
	}

	@Override
	public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity) { }

	//右クリックの処理
	public boolean actionBlock (World world, IBlockState state, BlockPos pos, EntityPlayer player, ItemStack stack) {

		if (world.isRemote) { return false; }

		// レシピ情報(ハンドアイテム)がNullの場合レシピ処理をしない
		NonNullList<ItemStack> pInv = player.inventory.mainInventory;
		ObMagiaRecipeInfo recipeInfo = SweetMagicAPI.getObMagiaRecipeInfo(stack, pInv);
		if (!recipeInfo.canComplete) { return false; }

		RecipeUtil recipeUtil = RecipeHelper.recipeSingleCraft(recipeInfo, player, stack);
		AdvancedInit.magic_craft.triggerFor(player);

		// 変換時の音
		this.playerSound(world, pos, SMSoundEvent.WRITE, 1F, 1F);
		this.spawnItem(world, player, recipeUtil.getResult());
		ParticleHelper.spawnParticle(world, pos, EnumParticleTypes.LAVA);

		return true;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return state.getBlock() == BlockInit.obmagia_top ? TOP : BOTTOM;
	}

	// アイテムをドロップ
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return this == BlockInit.obmagia_bottom ? new ItemStack(this).getItem() : ItemStack.EMPTY.getItem();
	}

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		return new ItemStack(BlockInit.obmagia_bottom);
	}

    @Override
	public float getEnchantPowerBonus(World world, BlockPos pos) {
		return 1F;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag advanced) {
		tooltip.add(I18n.format(TextFormatting.GREEN + this.getTip("tip.enchantpower.name") + " : " + 1.0F ));
	}
}
