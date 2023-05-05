package sweetmagic.init.item.sm.sweetmagic;

import sweetmagic.api.iitem.IElementItem;
import sweetmagic.init.item.sm.eitem.SMElement;

public class SMElementItem extends SMDropItem implements IElementItem {

	private SMElement ele;

	public SMElementItem (String name, SMElement ele) {
		super(name);
		this.setElement(ele);
	}

	@Override
	public SMElement getElement() {
		return this.ele;
	}

	@Override
	public void setElement(SMElement ele) {
		this.ele = ele;
	}
}
