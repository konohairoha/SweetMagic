package sweetmagic.plugin;

import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.IWailaRegistrar;
import mcp.mobius.waila.api.WailaPlugin;
import sweetmagic.plugin.waila.SMBlockHandler;

@WailaPlugin
public class SMWailaPlugin implements IWailaPlugin {

	// レジスターの読み込み
	@Override
	public void register(IWailaRegistrar registrar) {
		SMBlockHandler.register(registrar);
	}
}
