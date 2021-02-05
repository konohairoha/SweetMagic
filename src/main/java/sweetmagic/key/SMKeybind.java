package sweetmagic.key;

import org.lwjgl.input.Keyboard;

public enum SMKeybind {

	OPEN("key.open.name", Keyboard.KEY_H),
	NEXT("key.next.name", Keyboard.KEY_N),
	BACK("key.back.name", Keyboard.KEY_B),
	FAV_1("key.fav_1.name", Keyboard.KEY_O),
	FAV_2("key.fav_2.name", Keyboard.KEY_L);

	public final String keyName;
	public final int defaultKeyCode;

	SMKeybind(String keyName, int defaultKeyCode) {
		this.keyName = keyName;
		this.defaultKeyCode = defaultKeyCode;
	}
}
