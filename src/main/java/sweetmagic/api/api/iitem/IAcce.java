package sweetmagic.api.iitem;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import sweetmagic.init.item.sm.eitem.SMAcceType;
import sweetmagic.util.PlayerHelper;

public interface IAcce {

	// 効果が発動できるか
	default boolean canUseEffect (World world, EntityPlayer player, ItemStack stack) {
		return player.getCooldownTracker().hasCooldown(stack.getItem());
	}

	// 常に発動したいならここで
	default void onUpdate (World world, EntityPlayer player, ItemStack stack) { }

	// 装備品の効果
	default boolean acceeffect(World world, EntityPlayer player, ItemStack stack) {
		return false;
	}

	// ツールチップ
	List<String> magicToolTip (List<String> toolTip);

	default void debuffRemovalTip(List<String> toolTip) { }

	// アクセサリタイプの取得
	SMAcceType getAcceType ();

	// アクセサリタイプの設定
	void setAcceType(SMAcceType type);

	// 常時発動タイプかどうか
	default boolean isUpdateType () {
		return this.getAcceType() == SMAcceType.UPDATE;
	}

	// ポーション付与
	default void addPotion (EntityLivingBase entiy, Potion potion, int time, int level) {
		PlayerHelper.addPotion(entiy, potion, time, level);
	}

	// ポーション付与
	default void addPotion (EntityLivingBase entiy, Potion potion, int time, int level, boolean flag) {
		PlayerHelper.addPotion(entiy, potion, time, level, flag);
	}

	// えんちちーリスト取得
	default <T extends Entity> List<T> getEntityList (Class <? extends T > classEntity, Entity entity, double x, double  y, double  z) {
		return this.getEntityList(classEntity, entity, x, y, z);
	}

	// えんちちーリスト取得
	default <T extends Entity> List<T> getEntityList (Class <? extends T > classEntity, Entity entity, AxisAlignedBB aabb) {
		return entity.world.getEntitiesWithinAABB(classEntity, aabb);
	}

	// 範囲の取得
	default AxisAlignedBB getAABB (Entity entity, double x, double  y, double  z) {
		return entity.getEntityBoundingBox().grow(x, y, z);
	}

	// サウンド鳴らす
	default void playSound (World world, BlockPos pos, SoundEvent sound, float vol, float pit) {
		world.playSound(null, pos, sound, SoundCategory.NEUTRAL, vol, pit);
	}

	// 重複できるか
	default boolean isDuplication () {
		return false;
	}

	// ポーチから取り出したときの処理
	default void extractPorch (EntityPlayer player) { }

	// tierの取得
	default int getTier () {
		return 1;
	}

	// 作成可能かどうか
	default String canCraft () {
		return "";
	}
}
