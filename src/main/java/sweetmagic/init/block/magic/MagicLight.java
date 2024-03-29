package sweetmagic.init.block.magic;

import java.util.List;
import java.util.Random;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.client.particle.ParticleTwilightlight;
import sweetmagic.event.HasItemEvent;
import sweetmagic.init.BlockInit;
import sweetmagic.init.base.BaseModelBlock;

public class MagicLight extends BaseModelBlock {

	private final int data;
	private static final AxisAlignedBB MAGIC = new AxisAlignedBB(0.2D, 0.8D, 0.2D, 0.8D, 0.2D, 0.8D);
	private static final AxisAlignedBB TWILIGHT = new AxisAlignedBB(0D, 0D, 0D, 0D, 0D, 0D);

	public MagicLight(String name, int data, List<Block> list) {
		super(Material.GLASS, name);
		this.setHardness(data == 0 ? 0F : 0.2F);
		this.setResistance(99999F);
		setLightLevel(1F);
		setSoundType(SoundType.STONE);
		this.data = data;
		list.add(this);
	}

	/**
	 * 0 = 魔法の光
	 * 1 = 黄昏の明かり
	 */

	// 光魔法を持ってると当たり判定を出る
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return ( HasItemEvent.hasThisItem || this.data == 1 ) ? MAGIC : TWILIGHT;
	}

	// 右クリックの処理
	@Nonnull
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing face, float x, float y, float z) {

		if (this.data == 1) {

			IBlockState underState = world.getBlockState(pos.down());
			Block under = underState.getBlock();

			if (under == BlockInit.twilight_alstroemeria) {
				return under.onBlockActivated(world, pos.down(), underState, player, hand, face, x, y - 1F, z);
			}
		}

		return super.onBlockActivated(world, pos, state, player, hand, face, x, y, z);
	}

	@Nullable
	public AxisAlignedBB getCollisionBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
		return NULL_AABB;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.TRANSLUCENT;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
		if (this.data == 1) {
			float f1 = pos.getX() + 0.5F + rand.nextFloat() - rand.nextFloat();
			float f2 = pos.getY() + 0.35F + rand.nextFloat() - rand.nextFloat();
			float f3 = pos.getZ() + 0.5F + rand.nextFloat() - rand.nextFloat();
			Particle effect = ParticleTwilightlight.create(world, f1, f2, f3, 0, 0, 0);
			FMLClientHandler.instance().getClient().effectRenderer.addEffect(effect);
		}
	}

    @Override
	public float getEnchantPowerBonus(World world, BlockPos pos) {
		return this.data == 0 ? 0.5F : 3.75F;
	}

	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World playerIn, List<String> tooltip, ITooltipFlag advanced) {

		if (this.data == 0) {
			tooltip.add(I18n.format(TextFormatting.GREEN + this.getTip("tip.enchantpower.name") + " : " + 0.5F ));
			return;
		}

		tooltip.add(I18n.format(TextFormatting.GOLD + this.getTip("tip.sm_twilightlight.name")));
		tooltip.add(I18n.format(TextFormatting.GREEN + this.getTip("tip.enchantpower.name") + " : " + 3.75F ));
	}

	public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer) {
		return false;
	}
}
