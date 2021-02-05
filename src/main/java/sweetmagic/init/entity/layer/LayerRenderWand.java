package sweetmagic.init.entity.layer;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.ForgeHooksClient;
import sweetmagic.api.iitem.IWand;
import sweetmagic.util.PlayerHelper;

public class LayerRenderWand extends LayerEffectBase<EntityLivingBase> {

	public LayerRenderWand(RenderLivingBase<?> renderer) {
		super(renderer);
	}

	@Override
	public boolean shouldRender(EntityLivingBase entity, float parTick) {
		return PlayerHelper.isPlayer(entity) && !entity.isSneaking() && !entity.isPlayerSleeping() && !entity.isRiding()&& !entity.isElytraFlying();
	}

	@Override
	public ResourceLocation getTexture(EntityLivingBase entity, float parTick) {
		return null;
	}

	@Override
	public void doRenderLayer(EntityLivingBase entity, float limbSwing, float swingAmount, float parTick, float ageTick, float headYaw, float headPitch, float scale) {
		if (this.shouldRender(entity, parTick)) {
			 GlStateManager.enableBlend();
	         GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	         GlStateManager.color(1F, 1F, 1F, 1F);
	         this.renderWand(scale * 12, entity, parTick);
		}
	}

	// インベントリ内をチェック
	public ItemStack getStack(EntityPlayer player) {

		ItemStack stack = ItemStack.EMPTY;

		// メインハンドに杖を持ってるなら終了
		if (this.checkWand(player.getHeldItemMainhand())) {
			return stack;
		}

		// オフハンドに杖を持ってるなら終了
		else if (this.checkWand(player.getHeldItemOffhand())) {
			return stack;
		}

		// プレイヤーのInventoryの取得
		NonNullList<ItemStack> pInv = player.inventory.mainInventory;

		for (int i = 0; i < pInv.size(); i++) {
			ItemStack invStack = pInv.get(i);

			if (this.checkWand(invStack)) {
				return invStack;
			}
		}

		return stack;
	}

	// 杖かどうか
	public boolean checkWand (ItemStack stack) {
		return stack.getItem() instanceof IWand;
	}

	private void renderWand(float scale, EntityLivingBase entity, float parTick) {

		ItemStack stack = this.getStack((EntityPlayer) entity);
		if (stack.isEmpty() || !this.checkWand(stack)) { return; }

		Minecraft mine = Minecraft.getMinecraft();
		IBakedModel model = mine.getRenderItem().getItemModelWithOverrides(stack, entity.world, entity);

		mine.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		GlStateManager.pushMatrix();

		IWand wand = (IWand) stack.getItem();
		wand.renderWand(scale, entity, parTick);

		mine.getRenderItem().renderItem(stack, ForgeHooksClient.handleCameraTransforms(model, TransformType.NONE, false));
		GlStateManager.popMatrix();
		GL11.glMaterial(GL11.GL_FRONT_AND_BACK, GL11.GL_EMISSION, RenderHelper.setColorBuffer(0f, 0f, 0f, 1f));
	}
}
