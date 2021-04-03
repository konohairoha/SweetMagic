package sweetmagic.init.tile.magic;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.Particle;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import sweetmagic.client.particle.ParticleNomal;
import sweetmagic.init.enchant.EnchantWand;
import sweetmagic.init.tile.slot.StackHandler;
import sweetmagic.util.EnchantUtil;

public class TileMagiaWrite extends TileMFBase {

	public int maxMagiaFlux = 300000; 	// 最大MF量を設定
	public BlockPos viewPos = this.pos;
	public int smeltTime = 0;
	public boolean isSmelt = false;

	// 杖スロット
	public final ItemStackHandler toolInv = new StackHandler(this, this.getInvSize()) {

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

		if (this.isSmelt) {
			this.tickTime++;
		}

		// 1秒に１度だけ更新
		if (this.getTime() % 12 != 0) { return; }

		// 一定時間経てば処理
		if (this.getToolItem().isEmpty() || this.isMfEmpty() || EnchantmentHelper.getEnchantments(this.getToolItem()).size() <= 0) {
			this.resetData();
			return;
		}

		int useMF = this.getEnchantCost();
		if (useMF <= 0) {
			this.resetData();
		}

		// コスト以上のMFがあるなら
		if (this.getMF() >= useMF) {
			this.smeltTime++;
			this.isSmelt = true;
		}

		// コスト以上ないなら終了
		else {
			this.resetData();
			return;
		}

		// 一定時間が経ったらエンチャレベル書き換え
		if (this.smeltTime >= 50) {
			this.enchantWrite();
		}

		if (this.world.isRemote) {
			this.spawnParticl();
		}

		this.markDirty();
	}

	// データの初期化
	public void resetData () {
		this.smeltTime = 0;
		this.tickTime = 0;
		this.isSmelt = false;
	}

	// エンチャのコストを取得
	public int getEnchantCost () {

		int cost = 0;
		ItemStack stack = this.getToolItem();

		// マップ（エンチャの種類、レベル）の取得
        Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(stack);

		// エンチャの数だけ回す
		for (Entry<Enchantment, Integer> entry : map.entrySet()) {

			// エンチャを取得してコストを追加
			Enchantment enchant = entry.getKey();
			if (!(enchant instanceof EnchantWand) || enchant.getMaxLevel() == 1 ) { continue; }

            final int level = entry.getValue() ;

            if (level > 0 && level < 10) {
                cost += EnchantUtil.calculateNewEnchCost(enchant, level + 1) * 10;
            }
		}

		return cost;
	}

	// エンチャレベルの書き換え
	public void enchantWrite () {

		if (!this.world.isRemote) {

			// アイテムと必要コストの取得
			ItemStack stack = this.getToolItem();
			int useMF = this.getEnchantCost();

			// マップ（エンチャの種類、レベル）の取得
	        Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(stack);

			// エンチャの数だけ回す
			for (Entry<Enchantment, Integer> entry : map.entrySet()) {

				// エンチャがスイートマジック以外のものなら次へ
				Enchantment enchant = entry.getKey();
				if (!(enchant instanceof EnchantWand) || enchant.getMaxLevel() == 1) { continue; }

				// レベルが条件外なら次へ
	            int level = entry.getValue() ;
	            if (level <= 0 || level >= 10) { continue; }

	            // マップに入れなおす
				level++;
				map.put(enchant, Integer.valueOf(level));

				// アイテムにエンチャを再設定
				EnchantmentHelper.setEnchantments(map, stack);
			}

			// コスト分のMFを消費
			this.setMF(this.getMF() - useMF);
			this.sentClient();
			this.world.playEvent(2003, new BlockPos(this.pos.add(0, 1.75, 0)), 0);
		}

		this.playSound(this.pos, SoundEvents.ENTITY_PLAYER_LEVELUP, 1F, 0.5F);
		this.resetData();
	}

	public void spawnParticl () {

		Random rand = this.world.rand;

		for (int i = 0; i < this.smeltTime / 5; i++) {

			float randX = rand.nextFloat() - rand.nextFloat();
			float randY = rand.nextFloat() - rand.nextFloat();
			float randZ = rand.nextFloat() - rand.nextFloat();

			float x = this.pos.getX() + 0.5F + randX;
			float y = this.pos.getY() + 0.75F + randY;
			float z = this.pos.getZ() + 0.5F + randZ;
			float xSpeed = -randX * 0.075F;
			float ySpeed = -randY * 0.075F;
			float zSpeed = -randZ * 0.075F;

			Particle effect = new ParticleNomal.Factory().createParticle(0, this.world, x, y, z, xSpeed, ySpeed, zSpeed);
			FMLClientHandler.instance().getClient().effectRenderer.addEffect(effect);
		}
	}

	@Override
	public NBTTagCompound writeNBT(NBTTagCompound tags) {
		super.writeNBT(tags);
		tags.setTag("wand", this.toolInv.serializeNBT());
		tags.setInteger("smeltTime", this.smeltTime);
		tags.setBoolean("isSmelt", this.isSmelt);
		return tags;
	}

	@Override
	public void readNBT(NBTTagCompound tags) {
		super.readNBT(tags);
		this.toolInv.deserializeNBT(tags.getCompoundTag("wand"));
		this.smeltTime = tags.getInteger("smeltTime");
		this.isSmelt = tags.getBoolean("isSmelt");
	}

	// インベントリの数
	protected int getInvSize() {
		return 1;
	}

	// 杖スロットの取得
	public IItemHandler getTool() {
		return this.toolInv;
	}

	// 杖スロットのアイテムを取得
	public  ItemStack getToolItem() {
		return this.getTool().getStackInSlot(0);
	}

	// 最大MF量を取得
	@Override
	public int getMaxMF() {
		return this.maxMagiaFlux;
	}

    // 送信するMF量
	@Override
    public int getUseMF () {
    	return 5000;
    }
}
