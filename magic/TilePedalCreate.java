package sweetmagic.init.tile.magic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.client.particle.Particle;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import sweetmagic.client.particle.ParticleLay;
import sweetmagic.client.particle.ParticleNomal;
import sweetmagic.client.particle.ParticleTwilightlight;
import sweetmagic.event.SMSoundEvent;
import sweetmagic.init.BlockInit;
import sweetmagic.init.entity.projectile.EntityMagicItem;
import sweetmagic.init.tile.inventory.InventoryWoodChest;
import sweetmagic.util.ParticleHelper;

public class TilePedalCreate extends TileMFBase {

	public final ItemStackHandler handInv = new InventoryWoodChest(this, 1);		// メインハンド
	public final ItemStackHandler inputInv = new InventoryWoodChest(this, 8);	// 投入リスト
	public final ItemStackHandler outPutInv = new InventoryWoodChest(this, 8);	// 出力リスト

	private static final List<Block> blockList = Arrays.<Block> asList(
		BlockInit.aethercrystal_block, BlockInit.divinecrystal_block, BlockInit.purecrystal_block, BlockInit.cosmos_light_block
	);

	public int chargeTime = 0;				// 経過時間
	public boolean isCharge = false;		// 素材がそろったかどうかフラグ
	public boolean isHaveBlock = false;	// ブロックがそろったかどうかフラグ
	public boolean isCrystal = false;		// クリスタルがそろったかどうかフラグ
	public boolean isEncha = false;		// エンチャパワーがそろったかどうかフラグ
	public boolean findPlayer = false;

	public int nowTick = 0;
	public int needChargeTime = 10;

	@Override
	public void update() {

		super.update();

		if (this.isCharge) {
			this.nowTick++;
		}

		if (this.getTime() % 20 != 0) { return; }

		if (!this.isCharge) {
			this.checkBlock();
		}

		if (this.getHandItem().isEmpty() || this.getInputItem(0).isEmpty()) { return; }

		// クラフト中じゃないなら終了
		if (!this.isCharge) { return; }

		if (this.chargeTime < ( this.needChargeTime - 1) ) {
			this.playSound(this.pos, SMSoundEvent.MAGICCRAFT, 1F, 1F);
		}

		this.chargeTime++;

		// パーティクルスポーン
		if (!this.isSever()) {
			this.spawnParticle();
		}

		// クラフトしてないなら終了
		if (this.chargeTime < this.needChargeTime) { return; }

		try {
			// クラフト処理
			this.craftSpawn();
		}

		catch (Throwable e) { }
	}

	// クラフト処理
	public void craftSpawn () {

		// 初期化
		this.chargeTime = 0;
		this.nowTick = 0;
		this.isCharge = false;

		// アイテムスポーン
		if (this.isSever()) {

			for (int i = 0; i < 8; i++) {

				ItemStack stack = this.getoutPutItem(i);
				EntityMagicItem entity = this.getEntityItem(this.pos.up(2), stack.copy());
				entity.setNoDespawn();
				entity.setPickupDelay(8);
				entity.motionX = 0D;
				entity.motionZ = 0D;
				entity.motionY = 0.25D;
				entity.setEffect(true);
				this.world.spawnEntity(entity);

				stack.shrink(stack.getCount());
			}

			ItemStack hand = this.getHandItem();
			hand.shrink(hand.getCount());

			for (int i = 0; i < 8; i++) {
				ItemStack input = this.getInputItem(i);
				input.shrink(input.getCount());
			}

			this.markDirty();
			this.world.notifyBlockUpdate(this.pos, this.getState(this.pos), this.getState(this.pos), 3);
			this.world.playEvent(2003, new BlockPos(this.pos.add(0, 2.5, 0)), 0);
		}

		ParticleHelper.spawnParticle(this.world, this.pos.up(1), EnumParticleTypes.FIREWORKS_SPARK);
		Random rand = this.world.rand;

		for (int i = 0; i < 4; i++) {
			float f1 = (float) (this.pos.getX() - 0.75F + rand.nextFloat() * 1.5F);
			float f2 = (float) (this.pos.getY() + 2F + rand.nextFloat() * 0.5F);
			float f3 = (float) (this.pos.getZ() - 0.75F + rand.nextFloat() * 1.5F);
			this.world.spawnParticle(EnumParticleTypes.FIREWORKS_SPARK, f1, f2, f3, 0, 0, 0);
		}

		this.playSound(this.pos, SoundEvents.ENTITY_PLAYER_LEVELUP, 0.5F, 1F);
	}

	// EntityItemの取得
	public EntityMagicItem getEntityItem (BlockPos pos, ItemStack stack) {
		return new EntityMagicItem(this.world, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, stack);
	}

	// ブロックチェック
	public void checkBlock () {
		this.isEncha = true;
		this.isHaveBlock = this.isCrystal = blockList.contains(this.getBlock(this.pos.down()));

		if (!this.isSever()) {
			this.findPlayer = this.findRangePlayer();
		}
	}
	// 周囲にプレイヤーがいなる場合
	public boolean findRangePlayer () {
		return this.findRangePlayer(8D, 4D, 8D);
	}

	public Block getCrystal () {
		return BlockInit.aethercrystal_block;
	}

	public Block getCrystalAlpha () {
		return BlockInit.aethercrystal_block_alpha;
	}

	public int getData () {
		return 1;
	}

	public boolean quickCraft () {
		return this.needChargeTime < 10;
	}

	public float getEnchantPower () {

		float north = ForgeHooks.getEnchantPower(this.world, this.pos.north(2));
		float south = ForgeHooks.getEnchantPower(this.world, this.pos.south(2));
		float east = ForgeHooks.getEnchantPower(this.world, this.pos.east(2));
		float west = ForgeHooks.getEnchantPower(this.world, this.pos.west(2));

		return north + south + east + west;
	}

	// パーティクルスポーン
	public void spawnParticle () {
		float posX = this.pos.getX() + 0.5F;
		float posY = this.pos.getY() + 0.5F;
		float posZ = this.pos.getZ() + 0.5F;
		Random rand = this.world.rand;

		for(int k = 0; k <= 4; k++) {
			float f1 = (float) posX - 0.5F + rand.nextFloat();
			float f2 = (float) (posY + 0.85F + rand.nextFloat() * 0.75F) + this.nowTick * 0.007F;
			float f3 = (float) posZ - 0.5F + rand.nextFloat();
			this.getParticle().addEffect(ParticleTwilightlight.create(this.world, f1, f2, f3, 0, 0, 0));

			float f4 = (float) posX - 0.5F + rand.nextFloat();
			float f5 = (float) (posY + 0.5F + rand.nextFloat() * 0.75F) + this.nowTick * 0.007F;
			float f6 = (float) posZ - 0.5F + rand.nextFloat();
			List<Integer> color = this.getRGB(rand);
			Particle particle = ParticleLay.create(this.world, f4, f5, f6, 0, 0, 0, color.get(0), color.get(1), color.get(2));
			this.getParticle().addEffect(particle);
		}
	}

    public void spawnParticleRing (World world, double x, double y, double z, double vecX, double vecY, double vecZ, double step) {

		double spped = 0.1D;

        for (double degree = 0D; degree < 2D * Math.PI; degree += step) {
			Particle effect = ParticleNomal.create(world, x + Math.cos(degree), y, z + Math.sin(degree), -Math.cos(degree) * spped, vecY, -Math.sin(degree) * spped);
			this.getParticle().addEffect(effect);
        }
    }

	public List<Integer> getRGB (Random rand) {

		RGB color = null;

		switch (rand.nextInt(7)) {
		case 0:
			color = RGB.RED;
			break;
		case 1:
			color = RGB.B;
			break;
		case 2:
			color = RGB.C;
			break;
		case 3:
			color = RGB.D;
			break;
		case 4:
			color = RGB.E;
			break;
		case 5:
			color = RGB.F;
			break;
		case 6:
			color = RGB.G;
			break;
		}

		return Arrays.asList(color.r, color.g, color.b);
	}

	@Override
	public NBTTagCompound writeNBT(NBTTagCompound tags) {
		super.writeNBT(tags);
		tags.setTag("wand", this.handInv.serializeNBT());
		tags.setTag("ele", this.inputInv.serializeNBT());
		tags.setTag("outPut", this.outPutInv.serializeNBT());
		tags.setInteger("chargeTime", this.chargeTime);
		tags.setInteger("nowTick", this.nowTick);
		tags.setBoolean("isCharge", this.isCharge);
		tags.setBoolean("isHaveBlock", this.isHaveBlock);
		tags.setBoolean("isEncha", this.isEncha);
		tags.setBoolean("isCrystal", this.isCrystal);
		tags.setBoolean("findPlayer", this.findPlayer);
		return tags;
	}

	@Override
	public void readNBT(NBTTagCompound tags) {
		super.readNBT(tags);
		this.handInv.deserializeNBT(tags.getCompoundTag("wand"));
		this.inputInv.deserializeNBT(tags.getCompoundTag("ele"));
		this.outPutInv.deserializeNBT(tags.getCompoundTag("outPut"));
		this.chargeTime = tags.getInteger("chargeTime");
		this.nowTick = tags.getInteger("nowTick");
		this.isCharge = tags.getBoolean("isCharge");
		this.isHaveBlock = tags.getBoolean("isHaveBlock");
		this.isEncha = tags.getBoolean("isEncha");
		this.isCrystal = tags.getBoolean("isCrystal");
		this.findPlayer = tags.getBoolean("findPlayer");
	}

	// ハンドアイテムの取得
	public IItemHandler getHand() {
		return this.handInv;
	}

	// インプットアイテムスロットの取得
	public IItemHandler getInput() {
		return this.inputInv;
	}

	// インプットアイテムスロットの取得
	public IItemHandler getOutPut() {
		return this.outPutInv;
	}

	// ハンドアイテムを取得
	public ItemStack getHandItem() {
		return this.getHand().getStackInSlot(0);
	}

	// インプットスロットのアイテムを取得
	public ItemStack getInputItem(int i) {
		return this.getInput().getStackInSlot(i);
	}

	// インプットスロットのアイテムを取得
	public ItemStack getoutPutItem(int i) {
		return this.getOutPut().getStackInSlot(i);
	}

	// 最大MF量を取得
	@Override
	public int getMaxMF() {
		return 20000;
	}

    // 受信するMF量
	@Override
    public int getUseMF () {
    	return 5000;
    }

	public int getInputCount () {

		int amount = 0;

		for (int i = 0; i < 8; i++) {
			if (this.getInputItem(i).isEmpty()) { continue; }
			++amount;
		}

		return amount;
	}

	public List<ItemStack> getList() {

		List<ItemStack> stackList = new ArrayList<ItemStack>();
		this.putList(stackList, this.getHandItem());

		for (int i = 0; i < 8; i++) {
			this.putList(stackList, this.getInputItem(i));
		}

		return stackList;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getRenderBoundingBox() {
		return INFINITE_EXTENT_AABB;
	}

	public enum RGB {

		RED(255, 138, 147),
		B(255, 196, 138),
		C(255, 255, 138),
		D(147, 255, 138),
		E(138, 255, 234),
		F(138, 183, 255),
		G(255, 138, 238);

		public final int r;
		public final int g;
		public final int b;

		RGB (int r, int g, int b) {
			this.r = r;
			this.g = g;
			this.b = b;
		}
	}
}
