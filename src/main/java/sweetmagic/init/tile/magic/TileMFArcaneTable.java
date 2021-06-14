package sweetmagic.init.tile.magic;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import sweetmagic.init.EnchantInit;
import sweetmagic.init.base.BaseMFFace;
import sweetmagic.init.tile.slot.StackHandler;

public class TileMFArcaneTable extends TileMFBase {

	public int maxMagiaFlux = 4000000; 	// 最大MF量を設定
	public int charmTime = 0;
	public boolean isCharm = false;
	public int healTime = 0;
	public boolean isHeal = false;
	public int needMF = 200000;

	// 杖スロット
	public final ItemStackHandler wandInv = new StackHandler(this, this.getInvSize()) {

        @Override
		public void onContentsChanged(int slot) {
            markDirty();
            IBlockState state = world.getBlockState(pos);
            world.markAndNotifyBlock(pos, world.getChunkFromBlockCoords(pos), state, state, 1 | 2);
        }
    };

	@Override
	public void update() {

		super.update();

		// 1秒に１度だけ更新
		if (this.getTime() % 10 != 0) { return; }

		// チャームのエンチャント付与中なら
		if (this.isCharm) {
			this.charmTime++;
		}

		// ヒールのエンチャント付与中なら
		if (this.isHeal) {
			this.healTime++;
		}

		// エーテルチャームを付けれるかチェック
		if (this.checkCharm()) {
			this.resetCharmData();
		}

		else {
			this.isCharm = true;
		}

		// エーテルヒールを付けれるかチェック
		if (this.checkHeal()) {
			this.resetHealData();
		}

		else {
			this.isHeal = true;
		}

		if (this.charmTime >= 50) {
			this.addEnchant(this.getWandItem(0), EnchantInit.aetherCharm);
			this.resetCharmData();
		}

		if (this.healTime >= 50 && this.canEnchant()) {
			this.addEnchant(this.getWandItem(1), EnchantInit.mfRecover);
			this.resetHealData();
		}

		if (this.getTime() % 10 == 0 && this.world.isRemote && ( this.isCharm || this.isHeal )) {
			this.spawnParticl();
		}
	}

	// 必要MF以上あるかどうか
	public boolean canEnchant () {
		return this.getMF() >= this.needMF;
	}

	// エーテルチャームを付けれるかチェック
	public boolean checkCharm () {
		ItemStack stack = this.getWandItem(0);
		return stack.isEmpty() || !this.canEnchant() || this.checkEncha(EnchantInit.aetherCharm, stack);
	}

	// チャームをリセット
	public void resetCharmData () {
		this.isCharm = false;
		this.charmTime = 0;
	}

	// エーテルヒールを付けれるかチェック
	public boolean checkHeal () {
		ItemStack stack = this.getWandItem(1);
		return stack.isEmpty() || !this.canEnchant() || stack.getMaxDamage() < 1 || this.checkEncha(EnchantInit.mfRecover, stack);
	}

	// チャームをリセット
	public void resetHealData () {
		this.isHeal = false;
		this.healTime = 0;
	}

	// 既にエンチャがついてないか確認
	public boolean checkEncha (Enchantment enchant, ItemStack stack) {
		return EnchantmentHelper.getEnchantmentLevel(enchant, stack) > 0;
	}

	// エンチャント
	public void addEnchant (ItemStack stack, Enchantment enchant) {
		if (!this.world.isRemote) {
			stack.addEnchantment(enchant, 1);
			this.setMF(this.getMF() - this.needMF);
			this.sentClient();
			this.playSound(this.pos, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, 1F, this.world.rand.nextFloat() * 0.1F + 0.9F);
		}
	}

	// パーティクルスポーン
	public void spawnParticl () {

		Random rand = this.world.rand;
		int count = Math.max(this.charmTime, this.healTime) / 4;

		for (int i = 0; i < count; i++) {

			float x = this.pos.getX() + 0.6F;
			float y = this.pos.getY() + 1.75F;
			float z = this.pos.getZ() + 0.3F;
			float xSpeed = (rand.nextFloat() - rand.nextFloat()) * 2F;
			float ySpeed = (rand.nextFloat() - rand.nextFloat()) * 0.1F;
			float zSpeed = (rand.nextFloat() - rand.nextFloat()) * 2F;

			this.world.spawnParticle(EnumParticleTypes.ENCHANTMENT_TABLE, x, y, z, xSpeed, ySpeed, zSpeed);
		}
	}

	@Override
	public NBTTagCompound writeNBT(NBTTagCompound tags) {
		super.writeNBT(tags);
		tags.setTag("wand", this.wandInv.serializeNBT());
		tags.setInteger("charmTime", this.charmTime);
		tags.setBoolean("isCharm", this.isCharm);
		tags.setInteger("healTime", this.healTime);
		tags.setBoolean("isHeal", this.isHeal);
		return tags;
	}

	@Override
	public void readNBT(NBTTagCompound tags) {
		super.readNBT(tags);
		this.wandInv.deserializeNBT(tags.getCompoundTag("wand"));
		this.charmTime = tags.getInteger("charmTime");
		this.isCharm = tags.getBoolean("isCharm");
		this.healTime = tags.getInteger("healTime");
		this.isHeal = tags.getBoolean("isHeal");
	}

	// インベントリの数
	protected int getInvSize() {
		return 2;
	}

	// 杖スロットの取得
	public IItemHandler getWand() {
		return this.wandInv;
	}

	// 杖スロットのアイテムを取得
	public  ItemStack getWandItem(int i) {
		return this.getWand().getStackInSlot(i);
	}

	// 最大MF量を取得
	@Override
	public int getMaxMF() {
		return this.maxMagiaFlux;
	}

    // 送信するMF量
	@Override
    public int getUseMF () {
    	return 10000;
    }

	// 向きの取得
	public EnumFacing getFace () {
		return this.getState(this.pos).getValue(BaseMFFace.FACING);
	}
}
