package sweetmagic.api.magiaflux;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import sweetmagic.init.BlockInit;
import sweetmagic.init.ItemInit;

@SMMagiaFluxItemListPlugin(priority = EventPriority.LOW)
public class MFItemList implements IMagiaFluxItemListPlugin {

	// アイテムにMFを定義する　※こちらでは現在ToolTip経由でMF表示ができないので注意
	@Override
	public void setMF(MagiaFluxInfo info) {

		// 定義例
		info.setMF(new MagiaFluxInfo(new ItemStack(Items.SUGAR), 20));
		info.setMF(new MagiaFluxInfo(new ItemStack(ItemInit.sannyflower_petal), 50));
		info.setMF(new MagiaFluxInfo(new ItemStack(ItemInit.moonblossom_petal), 50));
		info.setMF(new MagiaFluxInfo(new ItemStack(ItemInit.fire_nasturtium_petal), 50));
		info.setMF(new MagiaFluxInfo(new ItemStack(ItemInit.dm_flower), 50));
		info.setMF(new MagiaFluxInfo(new ItemStack(BlockInit.magiclight), 10));
		info.setMF(new MagiaFluxInfo(new ItemStack(ItemInit.prizmium), 500));
		info.setMF(new MagiaFluxInfo(new ItemStack(ItemInit.sugarbell), 50));
		info.setMF(new MagiaFluxInfo(new ItemStack(ItemInit.cotton), 50));
		info.setMF(new MagiaFluxInfo(new ItemStack(Items.STRING), 10));
		info.setMF(new MagiaFluxInfo(new ItemStack(Items.GLOWSTONE_DUST), 50));
		info.setMF(new MagiaFluxInfo(new ItemStack(Blocks.GLOWSTONE), 200));
		info.setMF(new MagiaFluxInfo(new ItemStack(ItemInit.sticky_stuff_petal), 50));
		info.setMF(new MagiaFluxInfo(new ItemStack(ItemInit.clero_petal), 50));
		info.setMF(new MagiaFluxInfo(new ItemStack(ItemInit.stray_soul), 200));
		info.setMF(new MagiaFluxInfo(new ItemStack(ItemInit.electronic_orb), 200));
		info.setMF(new MagiaFluxInfo(new ItemStack(ItemInit.poison_bottle), 200));
		info.setMF(new MagiaFluxInfo(new ItemStack(ItemInit.unmeltable_ice), 200));
		info.setMF(new MagiaFluxInfo(new ItemStack(ItemInit.grav_powder), 200));
		info.setMF(new MagiaFluxInfo(new ItemStack(ItemInit.tiny_feather), 200));
		info.setMF(new MagiaFluxInfo(new ItemStack(ItemInit.witch_tears), 500));
		info.setMF(new MagiaFluxInfo(new ItemStack(ItemInit.ender_shard), 10));
		info.setMF(new MagiaFluxInfo(new ItemStack(Items.ENDER_PEARL), 90));
		info.setMF(new MagiaFluxInfo(new ItemStack(Items.QUARTZ), 25));
		info.setMF(new MagiaFluxInfo(new ItemStack(Blocks.QUARTZ_BLOCK), 100));
		info.setMF(new MagiaFluxInfo(new ItemStack(Items.GHAST_TEAR), 200));
		info.setMF(new MagiaFluxInfo(new ItemStack(BlockInit.magiaflux_block), 1000000));
		info.setMF(new MagiaFluxInfo(new ItemStack(BlockInit.aethercrystal_block), 5400));
		info.setMF(new MagiaFluxInfo(new ItemStack(BlockInit.divinecrystal_block), 72000));
		info.setMF(new MagiaFluxInfo(new ItemStack(BlockInit.purecrystal_block), 900000));
	}

	// MFを定義したアイテム情報を削除
	@Override
	public void deleteMF(MagiaFluxInfo info) {
		// 定義例 ※必要ないのでコメントにしてます
//		info.deleteMF(new MagiaFluxInfo(new ItemStack(Items.SUGAR)));
	}

}
