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
	// 盾破壊音
	public static final int S_BREAK = 6;
	// エンチャント解除音
	public static final int S_ENCHREMO = 7;
	// レベルダウン用音
	public static final int S_LEVELDOUWN = 8;
	// 食べた用音
	public static final int S_SHRINK = 9;
	// 食べた用音
	public static final int S_TELEPORT = 10;
	// リジェネ解除音
	public static final int S_REMOVE = 11;

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
		case S_BREAK:
			play_Sound = SoundEvents.ITEM_SHIELD_BREAK;
			break;
		case S_ENCHREMO:
			play_Sound = SoundEvents.ITEM_ARMOR_EQIIP_ELYTRA;
			break;
		case S_LEVELDOUWN:
			play_Sound = SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP;
			break;
		case S_SHRINK:
			play_Sound = SoundEvents.ENTITY_ITEM_PICKUP;
			break;
		case S_TELEPORT:
			play_Sound = SoundEvents.ENTITY_ENDERMEN_TELEPORT;
			break;
		case S_REMOVE:
			play_Sound = SoundEvents.ENTITY_ITEM_BREAK;
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
