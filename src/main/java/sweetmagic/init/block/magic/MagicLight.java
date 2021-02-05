package sweetmagic.init.block.magic;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.client.particle.ParticleTwilightlight;
import sweetmagic.event.HasItemEvent;
import sweetmagic.init.base.BaseModelBlock;

public class MagicLight extends BaseModelBlock {

	public final int data;

	public MagicLight(String name, int data, List<Block> list) {
		super(Material.GLASS, name);
		this.setHardness(0.2F);
		this.setResistance(99999F);
		setLightLevel(1F);
		this.data = data;
		list.add(this);
	}

	/**
	 * 0 = 魔法の光
	 * 1 = 黄昏の明かり
	 */

	// 光魔法を持ってると当たり判定を出る
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return HasItemEvent.hasThisItem || this.data == 1 ? new AxisAlignedBB(0.2D, 0.8D, 0.2D, 0.8D, 0.2D, 0.8D) : new AxisAlignedBB(0D, 0D, 0D, 0D, 0D, 0D);
	}

	@Nullable
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
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
			Particle effect = new ParticleTwilightlight.Factory().createParticle(0, world, f1, f2, f3, 0, 0, 0);
			FMLClientHandler.instance().getClient().effectRenderer.addEffect(effect);
		}
	}

    @Override
	public float getEnchantPowerBonus(World world, BlockPos pos) {
		return this.data == 0 ? 0.5F : 3.75F;
	}

	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World playerIn, List<String> tooltip, ITooltipFlag advanced) {

		if (this.data == 0) { return; }

  		String tip= new TextComponentTranslation("tip.sm_twilightlight.name", new Object[0]).getFormattedText();
		tooltip.add(I18n.format(TextFormatting.GOLD + tip));
	}

}
