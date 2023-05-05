package sweetmagic.init.item.sm.sweetmagic;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import com.google.common.collect.Multimap;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.api.iblock.IChangeBlock;
import sweetmagic.client.particle.ParticleNomal;
import sweetmagic.init.ItemInit;
import sweetmagic.util.SMUtil;

public class MagicianWand extends SMItem {

	public MagicianWand (String name) {
		super(name, ItemInit.magicList);
	}

	@Override
	public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot slot) {
        Multimap<String, AttributeModifier> map = super.getItemAttributeModifiers(slot);
		if (slot == EntityEquipmentSlot.MAINHAND) {
			map.put(EntityPlayer.REACH_DISTANCE.getName(), new AttributeModifier(SMUtil.TOOL_REACH, "modifier", 4D, 0));
		}
        return map;
    }

	@Override
	public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {

		if (player.isSneaking()) {

			if (!world.isRemote) {
				IBlockState state = world.getBlockState(pos);
				Block block = state.getBlock();

				if (block instanceof IChangeBlock) {
					((IChangeBlock) block).setBlock(world, pos, player);
				}

				else {
					block.rotateBlock(world, pos, side.getOpposite());
				}

	            SoundType sound = state.getBlock().getSoundType(state, world, pos, player);
	            this.playerSound(world, pos, sound.getPlaceSound(),(sound.getVolume() + 1.0F) / 2F, sound.getPitch() * 0.8F);
			}

			else {
				this.spawnParticl(world, pos, world.rand);
			}

			return EnumActionResult.SUCCESS;
		}

		return super.onItemUseFirst(player, world, pos, side, hitX, hitY, hitZ, hand);
	}

	// サウンド
	public void playerSound (World world, BlockPos pos, SoundEvent sound, float vol, float pit) {
		world.playSound(null, pos, sound, SoundCategory.BLOCKS, vol, pit);
	}

	public void spawnParticl (World world, BlockPos pos, Random rand) {

		for (int i = 0; i < 6; i++) {

			float f1 = pos.getX() + 0.5F;
			float f2 = pos.getY() + 0.25F + rand.nextFloat() * 0.5F;
			float f3 = pos.getZ() + 0.5F;
			float x = (rand.nextFloat() - rand.nextFloat()) * 0.15F;
			float z = (rand.nextFloat() - rand.nextFloat()) * 0.15F;

			Particle effect = ParticleNomal.create(world, f1, f2, f3, x, 0, z);
			FMLClientHandler.instance().getClient().effectRenderer.addEffect(effect);
		}
	}


	// ツールチップの表示
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag advanced) {
		tooltip.add(I18n.format(TextFormatting.GREEN + this.getTip("tip.magician_wand.name")));
	}
}
