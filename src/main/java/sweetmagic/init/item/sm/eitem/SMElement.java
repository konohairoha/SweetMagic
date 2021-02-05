package sweetmagic.init.item.sm.eitem;

public enum SMElement {

	FLAME,		// 火
	FROST,		// 氷
	CYCLON,		// 風
	LIGHTNING,	// 雷
	EARTH,		// 土
	SHINE,		// 光
	DARK,		// 闇
	WATER,		// 水
	GRAVITY,	// 重力
	BLAST,		// 爆発
	TOXIC,		// 毒
	TIME;		// 時間

	SMElement() {}

	// Stringから属性を返す
	public static SMElement getElement (String ele) {

		SMElement element = null;

		switch (ele) {
		case "FLAME":
			element = FLAME;
			break;
		case "FROST":
			element = FROST;
			break;
		case "CYCLON":
			element = CYCLON;
			break;
		case "LIGHTNING":
			element = LIGHTNING;
			break;
		case "EARTH":
			element = EARTH;
			break;
		case "SHINE":
			element = SHINE;
			break;
		case "DARK":
			element = DARK;
			break;
		case "WATER":
			element = WATER;
			break;
		case "GRAVITY":
			element = GRAVITY;
			break;
		case "BLAST":
			element = BLAST;
			break;
		case "TOXIC":
			element = TOXIC;
			break;
		case "TIME":
			element = TIME;
			break;
		}

		return element;
	}
}
