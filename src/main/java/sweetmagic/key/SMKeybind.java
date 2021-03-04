package sweetmagic.key;

import org.lwjgl.input.Keyboard;

public enum SMKeybind {

	OPEN("key.open.name", Keyboard.KEY_H),
	NEXT("key.next.name", Keyboard.KEY_N),
	BACK("key.back.name", Keyboard.KEY_B),
	POUCH("key.pouch.name", Keyboard.KEY_P);

	public final String keyName;
	public final int defaultKeyCode;

	SMKeybind(String keyName, int defaultKeyCode) {
		this.keyName = keyName;
		this.defaultKeyCode = defaultKeyCode;
	}
}
