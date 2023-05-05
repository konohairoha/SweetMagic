package sweetmagic.init.item.sm.sweetmagic;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.init.ItemInit;

public class SMShears extends ItemShears {

	public SMShears(String name) {
		super();
		setUnlocalizedName(name);
        setRegistryName(name);
        setMaxStackSize(1);
        setMaxDamage(800);
        ItemInit.itemList.add(this);
    }

	@Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase entity, EnumHand hand) {

		World world = entity.world;
		if (world.isRemote || !(entity instanceof IShearable)) { return false; }

		IShearable target = (IShearable) entity;
		BlockPos pos = new BlockPos(entity);
		if (!target.isShearable(stack, world, pos)) { return true; }

        List<ItemStack> drops = target.onSheared(stack, world, pos, EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack) + 2);
		Random rand = new Random();

		for (ItemStack stack1 : drops) {
			stack1.setCount(stack.getCount() + world.rand.nextInt(2) + 1);
			EntityItem ent = entity.entityDropItem(stack1, 1.0F);
			ent.motionY += rand.nextFloat() * 0.05F;
			ent.motionX += (rand.nextFloat() - rand.nextFloat()) * 0.1F;
			ent.motionZ += (rand.nextFloat() - rand.nextFloat()) * 0.1F;
		}

		stack.damageItem(1, player);

		return true;
	}

	// ツールチップの表示
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag advanced) {
		String tip = new TextComponentTranslation("tip.alt_shears.name", new Object[0]).getFormattedText();
		tooltip.add(I18n.format(TextFormatting.GREEN + tip));
	}
}
