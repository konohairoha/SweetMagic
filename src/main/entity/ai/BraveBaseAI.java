package sweetmagic.init.entity.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.world.World;
import sweetmagic.init.entity.monster.EntityBraveSkeleton;

public class BraveBaseAI extends EntityAIBase {

	protected int spellWarmup;
	protected int spellCooldown;
	public World world;
	public EntityBraveSkeleton brave;
	public final boolean isStop;

	public BraveBaseAI (EntityBraveSkeleton entity, boolean isStop) {
		this.brave = entity;
		this.world = this.brave.world;
		this.isStop = isStop;
	}

	// AIを実行できるか
	public boolean shouldExecute() {
		return this.getTarget() == null ? false : this.brave.ticksExisted >= this.spellCooldown;
	}

	// 実行できるか
	public boolean shouldContinueExecuting() {
		return this.brave.getAttackTarget() != null && this.spellWarmup > 0;
	}

	public void startExecuting() {
		this.spellWarmup = this.getCastWarmupTime();
		this.brave.spellTicks = this.getCastingTime();
		this.spellCooldown = this.brave.ticksExisted + this.getCastingInterval();
		this.setCharge(true);
	}

	// タスク処理
	public void updateTask() {

		--this.spellWarmup;

		if (this.spellWarmup == 0) {
			this.castSpell();
			this.setCharge(false);
		}
	}

	// 特殊行動開始
	protected void castSpell() { }

	// ウォームアップタイム
	protected int getCastWarmupTime() {
		return 10;
	}

	// キャストタイム
	protected int getCastingTime() {
		return this.spellCooldown;
	}

	// インターバル
	protected int getCastingInterval() {
		return 400;
	}

	// ターゲット取得
	public EntityLivingBase getTarget () {
		return this.brave.getAttackTarget();
	}

	// チャージの設定
	public void setCharge (boolean charge) {
		if (this.isStop) {
			this.brave.setSpecial(charge);
		}
	}
}
