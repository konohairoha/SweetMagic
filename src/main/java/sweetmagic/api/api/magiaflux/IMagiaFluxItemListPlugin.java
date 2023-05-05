package sweetmagic.api.magiaflux;

public interface IMagiaFluxItemListPlugin {

	// 保有MFを設定
	public default void setMF(MagiaFluxInfo info) {}

	// MFを設定したアイテムを削除
	public default void deleteMF(MagiaFluxInfo info) {}
}
