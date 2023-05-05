package sweetmagic.init.tile.magic;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import sweetmagic.init.block.blocks.FigurineStand;
import sweetmagic.init.entity.monster.ISMMob;

public class TileFigurineStand extends TileSMBase {

	private EntityLivingBase entity = null;
	private EntityLivingBase sub = null;
	private boolean isSub = false;
	private boolean isSpecial = false;

	public void clientUpdate() {
		if (this.entity != null) {
			this.entity.ticksExisted += 1;

			if (this.sub != null) {
				this.sub.ticksExisted += 1;
			}
		}
	}

	// えんちちーの設定
	public void setEntity (EntityLivingBase entity) {
		this.entity = entity;
	}

	// えんちちーの取得
	public EntityLivingBase getEntity () {

		// 生成済みならすぐ返す
		if (this.entity != null) { return this.entity; }

		// フィギアスタンド出なければ終了
		Block block = this.getBlock(this.pos);
		if ( !this.isStand(block) ) { return null; }

		this.entity = ( (FigurineStand) block).getEntity(this, this.world);
		this.entity.ticksExisted = 0;

		// 必殺技モーション中なら変更
		if (this.isSpecial) {
			ISMMob sm = (ISMMob) this.entity;
			sm.setSpecial(true);
		}

		return this.entity;
	}

	// サブエンティティの取得
	public EntityLivingBase getSubEntity () {

		// 生成済みならすぐ返す
		if (this.sub != null || !this.isSub) { return this.sub; }

		Block block = this.getBlock(this.pos);
		if ( !this.isStand(block) ) { return null; }

		this.sub = ( (FigurineStand) block).getSub(this.world, this.pos);
		this.sub.ticksExisted = 0;

		return this.sub;
	}

	// サブエンティティの設定
	public void setSubEntity (EntityLivingBase entity) {
		this.sub = entity;
		this.setSub(entity != null);
	}

	public boolean getSub () {
		return this.isSub;
	}

	public void setSub (boolean isSub) {
		this.isSub = isSub;
	}

	public void setSpecial (boolean isSpecial) {
		this.isSpecial = isSpecial;
	}

	public boolean getSpecial () {
		return this.isSpecial;
	}

	public double getPosY () {

		Block block = this.getBlock(this.pos);
		if ( !this.isStand(block) ) { return 0D; }

		return ( (FigurineStand) block).getPosY(this.world, this.pos);
	}

	private boolean isStand (Block block) {
		return block instanceof FigurineStand;
	}

	@Override
	public NBTTagCompound writeNBT(NBTTagCompound tags) {
		tags.setBoolean("isSub", this.isSub);
		tags.setBoolean("isSpecial", this.isSpecial);
		return tags;
	}

	@Override
	public void readNBT(NBTTagCompound tags) {
        this.isSub = tags.getBoolean("isSub");
        this.isSpecial = tags.getBoolean("isSpecial");
	}
}
