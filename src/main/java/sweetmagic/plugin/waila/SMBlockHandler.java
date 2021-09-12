package sweetmagic.plugin.waila;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

import mcp.mobius.waila.addons.core.HUDHandlerBlocks;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaRegistrar;
import mcp.mobius.waila.api.SpecialChars;
import mcp.mobius.waila.cbcore.LangUtil;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import sweetmagic.SweetMagicCore;
import sweetmagic.api.iitem.IWand;
import sweetmagic.init.BlockInit;
import sweetmagic.init.base.BaseMFBlock;
import sweetmagic.init.block.blocks.BlockFermenter;
import sweetmagic.init.block.blocks.BlockFlourMill;
import sweetmagic.init.block.blocks.BlockFryPan;
import sweetmagic.init.block.blocks.BlockJuiceMaker;
import sweetmagic.init.block.blocks.BlockOven;
import sweetmagic.init.block.blocks.BlockPot;
import sweetmagic.init.block.blocks.BlockStove;
import sweetmagic.init.block.crop.BlockLierRose;
import sweetmagic.init.block.crop.BlockWhitenet;
import sweetmagic.init.block.crop.SweetCrops_Tall;
import sweetmagic.init.block.crop.icrop.ISMCrop;
import sweetmagic.init.tile.cook.TileFermenter;
import sweetmagic.init.tile.cook.TileFlourMill;
import sweetmagic.init.tile.cook.TileJuiceMaker;
import sweetmagic.init.tile.cook.TilePot;
import sweetmagic.init.tile.cook.TileStove;
import sweetmagic.init.tile.magic.TileMFBase;
import sweetmagic.init.tile.magic.TileMFTable;

public class SMBlockHandler extends HUDHandlerBlocks {

	static final List<Block> targetHumList = Lists.newArrayList();

	// 表示する情報
	@Override
	public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {

		Block block = accessor.getBlock();

		// ブロックがないか、コンフィグがtrueではないなら
		if (block == null || !config.getConfig("sweetmagic.showtips")) {
			return currenttip;
		}

		// 作物なら
		else if (block instanceof ISMCrop) {
			ISMCrop crop = (ISMCrop) block;
			this.getView(currenttip, accessor, crop.getMaxBlockState());
		}

		// MFブロックなら
		else if (block instanceof BaseMFBlock) {
			int mf = accessor.getNBTData().getInteger("magiaFlux");
			currenttip.add(String.format("%s", SpecialChars.GREEN + "MF：" +  String.format("%,d", mf)));

			if (block == BlockInit.mftable) {
				String renderStr = "";
				TileMFTable tile = (TileMFTable) accessor.getTileEntity();
				ItemStack stack = tile.getWandItem(0);

				if (!stack.isEmpty()) {
					String name = stack.getItem().getRegistryName().toString();
					renderStr += SpecialChars.getRenderString("waila.stack", "1", name, String.valueOf(stack.getCount()), String.valueOf(stack.getItemDamage()));

					IWand wand = (IWand) stack.getItem();
					renderStr += String.format("%s", SpecialChars.GREEN + String.format("%,d", wand.getMF(stack)) + "MF");
				}
				currenttip.add(renderStr);
			}
		}

		// コンロ
		else if (block instanceof BlockStove) {

			// 必要情報の取得
			TileStove tile = (TileStove) accessor.getTileEntity();
			ItemStack stack = tile.fuelInv.getStackInSlot(0);
			String renderStr = "";

			// ItemStackをレンダーリストに追加
	        currenttip.add(this.getItemStackName(renderStr, stack));
		}

		// 鍋
		else if (block instanceof BlockPot) {

			// 必要情報の取得
			TilePot tile = (TilePot) accessor.getTileEntity();
			List<ItemStack> stackList = new ArrayList<>();
			String renderStr = "";

			// 稼働中
			if (block == BlockInit.pot_on) {
				stackList.add(tile.handItem);
				stackList.addAll(tile.inPutList);
			}

			// 稼働後
			else if (block == BlockInit.pot_re) {
				stackList.addAll(tile.outPutList);
			}

			// リストが空なら終了
			if (stackList.isEmpty()) { return currenttip; }

			// スタックリストの数だけ回してItemStackをレンダーリストに追加
			currenttip.add(this.getItemStackListName(renderStr, stackList));
		}

		// フライパン
		else if (block instanceof BlockFryPan) {

			// 必要情報の取得
			TilePot tile = (TilePot) accessor.getTileEntity();
			List<ItemStack> stackList = new ArrayList<>();
			String renderStr = "";

			// 稼働中
			if (block == BlockInit.frypan_on) {
				stackList.add(tile.handItem);
				stackList.addAll(tile.inPutList);
			}

			// 稼働後
			else if (block == BlockInit.frypan_re) {
				stackList.addAll(tile.outPutList);
			}

			// リストが空なら終了
			if (stackList.isEmpty()) { return currenttip; }

			// スタックリストの数だけ回してItemStackをレンダーリストに追加
			currenttip.add(this.getItemStackListName(renderStr, stackList));
		}

		// オーブン
		else if (block instanceof BlockOven || block instanceof BlockFlourMill) {

			// 必要情報の取得
			TileFlourMill tile = (TileFlourMill) accessor.getTileEntity();
			List<ItemStack> stackList = new ArrayList<>();
			String renderStr = "";

			// 稼働状態なら
			if (block == BlockInit.oven_on || block == BlockInit.flourmill_on) {
				if (block == BlockInit.oven_on) {
					stackList.add(tile.handItem);
				}
				stackList.addAll(tile.inPutList);
			}

			// 稼働後なら
			else if (block == BlockInit.oven_re || block == BlockInit.flourmill_re) {
				stackList.addAll(tile.outPutList);
			}

			// リストが空なら終了
			if (stackList.isEmpty()) { return currenttip; }

			// スタックリストの数だけ回してItemStackをレンダーリストに追加
			currenttip.add(this.getItemStackListName(renderStr, stackList));
		}

		// ジュースメイカー
		else if (block instanceof BlockJuiceMaker) {

			// 必要情報の取得
			TileJuiceMaker tile = (TileJuiceMaker) accessor.getTileEntity();
			List<ItemStack> stackList = new ArrayList<>();
			String renderStr = "";

			// 稼働状態なら
			if (tile.isCooking) {
				stackList.addAll(tile.inPutList);
			}

			// スタックリストの数だけ回してItemStackをレンダーリストに追加
			currenttip.add(this.getItemStackListName(renderStr, stackList));
			stackList.clear();

			// 出力スロットが空でないなら
			if (!tile.getOutPutItem(0).isEmpty()) {

				for (int i = 0; i < 4; i++) {
					ItemStack stack = tile.getOutPutItem(i);

					// 空だったら終了
					if (stack.isEmpty()) { return currenttip; }

					stackList.add(stack);
				}

				// スタックリストの数だけ回してItemStackをレンダーリストに追加
				currenttip.add(this.getItemStackListName(renderStr, stackList));
			}
		}

		// 熟成瓶
		else if (block instanceof BlockFermenter) {

			// 必要情報の取得
			TileFermenter tile = (TileFermenter) accessor.getTileEntity();
			List<ItemStack> stackList = new ArrayList<>();
			String renderStr = "";

			// 稼働状態なら
			if (tile.isWorking && !tile.isFinish) {
				stackList.add(tile.handItem);
				stackList.addAll(tile.inPutList);
			}

			else if (!tile.isWorking && tile.isFinish && !tile.outPutList.isEmpty()) {
				stackList.addAll(tile.outPutList);
			}

			// スタックリストの数だけ回してItemStackをレンダーリストに追加
			currenttip.add(this.getItemStackListName(renderStr, stackList));
			stackList.clear();

		}

		return currenttip;
	}

	// ItemStackをrenderStrに変換して返す
	public String getItemStackName (String renderStr, ItemStack stack) {

		if (!stack.isEmpty()) {
			String name = stack.getItem().getRegistryName().toString();
			renderStr += SpecialChars.getRenderString("waila.stack", "1", name, String.valueOf(stack.getCount()), String.valueOf(stack.getItemDamage()));
		}

		return renderStr;
	}

	// ItemStackをrenderStrに変換して返す
	public String getItemStackListName (String renderStr, List<ItemStack> stackList) {

		// スタックリストの数だけ回してItemStackをレンダーリストに追加
		for (ItemStack stack : stackList) {
			if (!stack.isEmpty()) {
				String name = stack.getItem().getRegistryName().toString();
				renderStr += SpecialChars.getRenderString("waila.stack", "1", name, String.valueOf(stack.getCount()), String.valueOf(stack.getItemDamage()));
			}
		}

		return renderStr;
	}

	// 作物のびゅー
	public void getView (List<String> currenttip, IWailaDataAccessor accessor, float maxState) {

		// 成長度の計算
		int meta = accessor.getMetadata();
		float growthValue = meta / maxState * 100F;

		// 最大値未満なら
		if (growthValue < 100F) {
			currenttip.add(String.format("%s : %.0f %%", LangUtil.translateG("hud.msg.growth"), growthValue));
		}

		// 最大値なら
		else {
			currenttip.add(String.format("%s : %s", LangUtil.translateG("hud.msg.growth"), LangUtil.translateG("hud.msg.mature")));
		}
	}

	// NBT読み込み
	@Override
	public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity te, NBTTagCompound tags, World world, BlockPos pos) {
		return te.writeToNBT(tags);
	}

	// 登録処理
	public static void register(IWailaRegistrar registrar) {

		// コンフィグの生成
		registrar.addConfig(SweetMagicCore.MODID, "sweetmagic.showtips", true);

		// リストに入れる
		SMBlockHandler pro = new SMBlockHandler();
		targetHumList.addAll(BlockInit.blockList);
		targetHumList.addAll(BlockInit.noTabList);

		// リストの分だけ回す
		for (Block block : targetHumList) {
			if (!(block instanceof BaseMFBlock) &&
					!(block instanceof SweetCrops_Tall) &&
					!(block instanceof BlockWhitenet) &&
					!(block instanceof BlockStove) &&
					!(block instanceof BlockPot) &&
					!(block instanceof BlockFryPan) &&
					!(block instanceof BlockFlourMill) &&
					!(block instanceof BlockOven) &&
					!(block instanceof BlockLierRose)) {

				registrar.registerBodyProvider(pro, block.getClass());
			}
		}

		// tileの登録
		registerTile(registrar, pro, TileMFBase.class);
		registerTile(registrar, pro, TileStove.class);
		registerTile(registrar, pro, TilePot.class);
		registerTile(registrar, pro, TileFlourMill.class);
	}

	// tileの登録
	public static void registerTile (IWailaRegistrar registrar, SMBlockHandler pro, Class css) {
		registrar.registerBodyProvider(pro, css);
		registrar.registerNBTProvider(pro, css);
	}
}
