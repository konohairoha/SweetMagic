package sweetmagic.init.tile.magic;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.Particle;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import sweetmagic.client.particle.ParticleNomal;
import sweetmagic.handlers.PacketHandler;
import sweetmagic.init.tile.slot.StackHandler;
import sweetmagic.packet.TileMFBlockPKT;

public class TileToolRepair extends TileMFBase {

	public int maxMagiaFlux = 200000; 	// 最大MF量を設定
	public BlockPos viewPos = this.pos;

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

		// 一定時間経てば処理
		if (this.getTime() % 20 != 0 || this.isMfEmpty()) { return; }

		if (!this.world.isRemote) {

			// MFが空ではないなら杖にMFを入れる
			this.repairTool();

			this.markDirty();
		}

		else {

			if (!this.getWandItem(0).isEmpty()) {
				this.spawnParticl(0.125F, 0F);
			}

			if (!this.getWandItem(1).isEmpty()) {
				this.spawnParticl(0F, 0.125F);
			}

			if (!this.getWandItem(2).isEmpty()) {
				this.spawnParticl(-0.125F, 0F);
			}

			if (!this.getWandItem(3).isEmpty()) {
				this.spawnParticl(0F, -0.125F);
			}
		}
	}

	// MFを消費してツールの耐久値を回復
	public void repairTool () {

		// スロット分回す
		for (int i = 0; i < this.getTool().getSlots(); i++) {

			// 杖スロットが空以外なら次へ
			ItemStack stack = this.getWandItem(i);
			if(stack.isEmpty() || stack.getItemDamage() == 0){ continue; }

			// 消費MFを取得してそれ未満なら次へ
			int maxDamage = stack.getMaxDamage();
			int useMF = Math.min(100000, (int) ((maxDamage * 0.1F) *(maxDamage * 0.1F) * 0.1F));
			if (this.getMF() < useMF) { continue; }

			// 耐久値の回復とMFの消費
			stack.setItemDamage(stack.getItemDamage() - 10);
			this.setMF(this.getMF() - useMF);
			PacketHandler.sendToClient(new TileMFBlockPKT (0, 0, this.getMF(), this.getTilePos()));

			// MFが空なら終了
			if (this.isMfEmpty()) { break; }
		}
	}

	public void spawnParticl (float pX, float pZ) {

		Random rand = this.world.rand;

		for (int i = 0; i < 2; i++) {

			float randX = (rand.nextFloat() - rand.nextFloat()) * 0.1F;
			float randY = (rand.nextFloat() - rand.nextFloat()) * 0.1F;
			float randZ = (rand.nextFloat() - rand.nextFloat()) * 0.1F;

			float x = this.pos.getX() + 0.5F + randX;
			float y = this.pos.getY() + 0.525F + randY;
			float z = this.pos.getZ() + 0.5F + randZ;
			float xSpeed = pX * 0.25F;
			float ySpeed = 0.1F * 0.4F;
			float zSpeed = pZ * 0.25F;

			Particle effect = new ParticleNomal.Factory().createParticle(0, this.world, x, y, z, xSpeed, ySpeed, zSpeed, 123, 255, 71);
			FMLClientHandler.instance().getClient().effectRenderer.addEffect(effect);
		}
	}

	@Override
	public NBTTagCompound writeNBT(NBTTagCompound tags) {
		super.writeNBT(tags);
		tags.setTag("wand", this.toolInv.serializeNBT());
		return tags;
	}

	@Override
	public void readNBT(NBTTagCompound tags) {
		super.readNBT(tags);
		this.toolInv.deserializeNBT(tags.getCompoundTag("wand"));
	}

	// インベントリの数
	public int getInvSize() {
		return 4;
	}

	// 杖スロットの取得
	public IItemHandler getTool() {
		return this.toolInv;
	}

	// 杖スロットのアイテムを取得
	public  ItemStack getWandItem(int i) {
		return this.getTool().getStackInSlot(i);
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
