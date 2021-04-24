package sweetmagic.api.iitem;

import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import sweetmagic.init.item.sm.eitem.SMElement;
import sweetmagic.init.item.sm.eitem.SMType;
import sweetmagic.util.PlayerHelper;

public interface ISMItem {

	// 属性取得
	SMElement getElement();

	// 属性取得
	SMType getType();

	// tierの取得
	int getTier();

	// クールタイムの取得
	int getCoolTime ();

	// 必要MFを取得
	int getUseMF();

	// アイテムを消費するかどうか
	boolean isShirink ();

	// 魔法発動条件が整っているか
	default boolean canItemMagic (World world, EntityPlayer player, ItemStack stack, NBTTagCompound tags) {
		return true;
	}

	// アイテムのアクション
	boolean onItemAction (World world, EntityPlayer player, ItemStack stack, Item slotItem);

	// テクスチャのリソースを取得
	ResourceLocation getResource ();

	// ツールチップ
	List<String> magicToolTip (List<String> toolTip);

	// 火力取得
	default float getPower (float level) {
		return (level * 0.5F) + (level + 1) / (level / 2) + 1;
	}

	// ポーション効果時間
	default int effectTime (int level) {
		// 基本時間 + (レベル + 補正値)
		return 600 + ((level - 1) * 600);
	}

	// 魔法アイテムの取得
	public static ISMItem getSMItem (Item item) {
		return (ISMItem) item;
	}

	// ポーション付与
	default void addPotion (EntityLivingBase entiy, Potion potion, int time, int level) {
		PlayerHelper.addPotion(entiy, potion, time, level);
	}

	// ポーション付与
	default void addPotion (EntityLivingBase entiy, Potion potion, int time, int level, boolean flag) {
		PlayerHelper.addPotion(entiy, potion, time, level, flag);
	}

	// アップデート
	default void onUpdate (World world, EntityPlayer player, ItemStack stack) { }

	default void playSound(World world, EntityLivingBase entity, SoundEvent sound, float vol, float pitch) {
		world.playSound(null, new BlockPos(entity), sound, SoundCategory.NEUTRAL, vol, pitch);
	}

	default List<EntityLivingBase> getEntityList (World world, EntityPlayer player, double x, double y, double z) {
		return world.getEntitiesWithinAABB(EntityLivingBase.class, player.getEntityBoundingBox().grow(x, y, z));
	}

	default List<EntityLivingBase> getEntityList (World world, BlockPos pos1, BlockPos pos2) {
		return world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(pos1, pos2));
	}

	// サブ属性の取得
	default SMElement getSubElement() {
		return null;
	}

	// サブ属性の設定
	default void setSubElement (SMElement ele) { }
}
