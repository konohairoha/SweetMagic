package sweetmagic.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundEvent;
import sweetmagic.SweetMagicCore;
import sweetmagic.event.SMSoundEvent;

public class SoundHelper {

	/**
	 * サウンド定義
	 */

	// スペル送り用音
	public static final int S_NEXT = 1;
	// GUI表示用音
	public static final int S_PAGE = 2;
	// レベルアップ用音
	public static final int S_LEVELUP = 3;
	// ローブ音
	public static final int S_ROBE = 4;
	// ボタン音
	public static final int S_BUT = 5;

	// クライアント対象プレイヤーに音声を再生させる処理
	public static void PlaySoundToPlayer(int sound, float pitch, float volume) {
		SoundEvent play_Sound = null;

		switch (sound) {
		case S_NEXT:
			play_Sound = SMSoundEvent.NEXT;
			break;
		case S_PAGE:
			play_Sound = SMSoundEvent.PAGE;
			break;
		case S_LEVELUP:
			play_Sound = SMSoundEvent.LEVELUP;
			break;
		case S_ROBE:
			play_Sound = SMSoundEvent.ROBE;
			break;
		case S_BUT:
			play_Sound = SoundEvents.UI_BUTTON_CLICK;
			break;
		default:
			// ログ流して処理強制終了
			SweetMagicCore.logger.warn("Sound packet Error");
			return;
		}

		// 対象のプレイヤーのマインクラフトから音声を流す
		Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getRecord(play_Sound, pitch, volume));
	}
}
