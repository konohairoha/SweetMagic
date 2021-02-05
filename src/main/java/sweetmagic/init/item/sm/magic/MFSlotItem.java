package sweetmagic.init.item.sm.magic;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import sweetmagic.api.iitem.ISMItem;
import sweetmagic.init.item.sm.eitem.SMElement;
import sweetmagic.init.item.sm.eitem.SMType;
import sweetmagic.init.item.sm.sweetmagic.SMItem;

public class MFSlotItem extends SMItem implements ISMItem {

	public int time;
	public SMElement ele;
	public SMType type;
	public int tier;
	public int coolTime;
	public int mf;
	public boolean isShirink;
	ResourceLocation icon;

	public MFSlotItem (String name, SMType type, SMElement ele, int tier, int coolTime, int useMF, boolean isShirink) {
		super(name);
		this.setType(type);
		this.setElement(ele);
		this.setTier(tier);
		this.setCoolTime(coolTime);
		this.setUseMF(useMF);
		this.isShirink = isShirink;
		this.icon = new ResourceLocation("sweetmagic","textures/items/" + name + ".png");
	}

	@Override
	public SMElement getElement() {
		return this.ele;
	}

	public void setElement(SMElement ele) {
		this.ele = ele;
	}

	@Override
	public SMType getType() {
		return this.type;
	}

	public void setType(SMType type) {
		this.type = type;
	}

	@Override
	public int getTier() {
		return this.tier;
	}

	public void setTier(int tier) {
		this.tier = tier;
	}

	@Override
	public int getCoolTime() {
		return this.coolTime;
	}

	public void setCoolTime(int time) {
		this.coolTime = time;
	}

	@Override
	public int getUseMF () {
		return this.mf;
	}

	public void setUseMF(int mf) {
		this.mf = mf;
	}

	@Override
	public boolean isShirink() {
		return this.isShirink;
	}

	// テクスチャのリソースを取得
	public ResourceLocation getResource () {
		return this.icon;
	}

	@Override
	public boolean onItemAction(World world, EntityPlayer player, ItemStack stack, Item slotItem) {
		return false;
	}

	// ツールチップ
	public List<String> magicToolTip (List<String> toolTip) {
		return toolTip;
	}
}
