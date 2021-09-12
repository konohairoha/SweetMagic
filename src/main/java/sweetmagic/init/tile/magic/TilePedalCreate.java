package sweetmagic.init.tile.magic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import net.minecraft.client.particle.Particle;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import sweetmagic.client.particle.ParticleLay;
import sweetmagic.client.particle.ParticleTwilightlight;
import sweetmagic.event.SMSoundEvent;
import sweetmagic.init.tile.slot.StackHandler;
import sweetmagic.util.ParticleHelper;

public class TilePedalCreate extends TileMFBase {

	public final ItemStackHandler handInv = new StackHandler(this, 1);		// メインハンド
	public final ItemStackHandler inputInv = new StackHandler(this, 8);	// 投入リスト
	public final ItemStackHandler outPutInv = new StackHandler(this, 8);	// 出力リスト

	public int chargeTime = 0;			// 経過時間
	public boolean isCharge = false;	// 素材がそろったかどうかフラグ
	public int nowTick = 0;

	@Override
	public void update() {

		super.update();

		if (this.isCharge) {
			this.nowTick++;
		}

		if (this.getTime() % 20 != 0 || this.getHandItem().isEmpty() || this.getInputItem(0).isEmpty()) { return; }

		// クラフト中じゃないなら終了
		if (!this.isCharge) { return; }

		if (this.chargeTime < 9) {
			this.playSound(this.pos, SMSoundEvent.MAGICCRAFT, 1F, 1F);
		}

		this.chargeTime++;

		// パーティクルスポーン
		if (this.world.isRemote) {
			this.spawnParticle();
		}


		// クラフトしてないなら終了
		if (this.chargeTime < 10) { return; }

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
		if (!this.world.isRemote) {

			AxisAlignedBB aabb =new AxisAlignedBB(this.pos.add(-7.5D, -3D, -7.5D), this.pos.add(7.5D, 3D, 7.5D));
			List<EntityPlayer> entityList = this.world.getEntitiesWithinAABB(EntityPlayer.class, aabb);
			boolean isInPlayer = !entityList.isEmpty();
			double dX = 0, dY= 0, dZ = 0, dist = 0, vel = 0;

			// 範囲にプレイヤーがいるなら
			if (isInPlayer) {

				float distance = 50F;
				EntityPlayer player = null;

				// リスト分回す
				for (EntityPlayer p : entityList) {

					float dis = this.getDistance(p, this.pos);

					// プレイヤーとの距離が近いなら
					if (distance > dis) {
						distance = dis;
						player = p;
					}
				}

				dX = player.posX - this.pos.getX() + 0.5D;
				dY = player.posY - this.pos.getY() + 1D;
				dZ = player.posZ - this.pos.getZ() + 0.5D;
				dist = Math.sqrt(dX * dX + dY * dY + dZ * dZ);
				vel = 1.0 - dist / 15;
			}

			for (int i = 0; i < 8; i++) {

				ItemStack stack = this.getoutPutItem(i);
				EntityItem entity = new EntityItem(this.world, this.pos.getX() + 0.5D, this.pos.getY() + 1, this.pos.getZ() + 0.5D, stack.copy());
				entity.setNoDespawn();
				entity.setPickupDelay(13);

				if (isInPlayer && vel > 0D) {
					vel *= vel;
					entity.motionX += dX / dist * vel * 0.5;
					entity.motionY += dY / dist * vel * 0.35;
					entity.motionZ += dZ / dist * vel * 0.5;
				}

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

		ParticleHelper.spawnBoneMeal(this.world, this.pos.up(1), EnumParticleTypes.FIREWORKS_SPARK);
		Random rand = this.world.rand;

		for (int i = 0; i < 4; i++) {
			float f1 = (float) (this.pos.getX() - 0.75F + rand.nextFloat() * 1.5F);
			float f2 = (float) (this.pos.getY() + 2F + rand.nextFloat() * 0.5);
			float f3 = (float) (this.pos.getZ() - 0.75F + rand.nextFloat() * 1.5F);
			this.world.spawnParticle(EnumParticleTypes.FIREWORKS_SPARK, f1, f2, f3, 0, 0, 0);
		}

		this.playSound(this.pos, SoundEvents.ENTITY_PLAYER_LEVELUP, 0.5F, 1F);
	}

	public float getDistance (EntityPlayer player, BlockPos pos) {
		return Math.abs((float) (player.posX - pos.getX() + 0.5F + player.posY - pos.getY() + 1F + player.posZ - pos.getZ() + 0.5F) );
	}

	// パーティクルスポーン
	public void spawnParticle () {
		float posX = this.pos.getX() + 0.5F;
		float posY = this.pos.getY() + 0.5F;
		float posZ = this.pos.getZ() + 0.5F;
		Random rand = this.world.rand;

		for(int k = 0; k <= 4; k++) {
			float f1 = (float) posX - 0.5F + rand.nextFloat();
			float f2 = (float) (posY + 0.85F + rand.nextFloat() * 0.75F) + this.nowTick * 0.0075F;
			float f3 = (float) posZ - 0.5F + rand.nextFloat();
			FMLClientHandler.instance().getClient().effectRenderer.addEffect(
					new ParticleTwilightlight.Factory().createParticle(0, this.world, f1, f2, f3, 0, 0, 0));

			float f4 = (float) posX - 0.5F + rand.nextFloat();
			float f5 = (float) (posY + 0.35F + rand.nextFloat() * 0.75F) + this.nowTick * 0.0075F;
			float f6 = (float) posZ - 0.5F + rand.nextFloat();
			List<Integer> color = this.getRGB(rand);
			Particle particle = new ParticleLay.Factory().createParticle(0, this.world, f4, f5, f6, 0, 0, 0, color.get(0), color.get(1), color.get(2));
			FMLClientHandler.instance().getClient().effectRenderer.addEffect(particle);
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
		return 1;
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

		stackList.add(this.getHandItem());

		for (int i = 0; i < 8; i++) {

			ItemStack stack = this.getInputItem(i);
			if (stack.isEmpty()) { continue; }

			stackList.add(stack);
		}

		return stackList;
	}

	public enum RGB {

		RED(255, 138, 147),
		B(255, 196, 138),
		C(255, 255, 138),
		D(147, 255, 138),
		E(138, 255, 234),
		F(138, 183, 255),
		G(255, 138, 238);

		public int r;
		public int g;
		public int b;

		RGB (int r, int g, int b) {
			this.r = r;
			this.g = g;
			this.b = b;
		}
	}
}
