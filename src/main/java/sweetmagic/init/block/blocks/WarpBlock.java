package sweetmagic.init.block.blocks;

import java.util.List;
import java.util.Random;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.client.particle.ParticleNomal;
import sweetmagic.handlers.PacketHandler;
import sweetmagic.init.BlockInit;
import sweetmagic.init.base.BaseModelBlock;
import sweetmagic.init.tile.magic.TileWarpBlock;
import sweetmagic.packet.PlayerSoundPKT;
import sweetmagic.util.SoundHelper;

public class WarpBlock extends BaseModelBlock {

	public final int data;

	public WarpBlock (String name, int data, List<Block> list) {
		super(Material.GLASS, name);
		this.data = data;
		list.add(this);
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
		return new AxisAlignedBB(0D, 0D, 0D, 1D, 0.95D, 1D);
	}

    @Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
		return new TileWarpBlock();
	}

	// 右クリックの処理
	@Override
	public boolean actionBlock (World world, IBlockState state, BlockPos pos, EntityPlayer player, ItemStack stack) {

		if (stack.isEmpty()) { return false; }

		// NBTを取得
		NBTTagCompound tags = stack.getTagCompound();
		if (tags == null || !tags.hasKey("posX") || tags.getInteger("dim") != player.dimension) { return false; }

		if (world.isRemote) {
			this.spawnParticl(world, pos, world.rand);
			player.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1, 1);
		}

		else {

			TileWarpBlock tile = (TileWarpBlock) world.getTileEntity(pos);
			tile.posX = tags.getInteger("posX");
			tile.posY = tags.getInteger("posY");
			tile.posZ = tags.getInteger("posZ");

			world.setBlockState(pos, BlockInit.warp_block_on.getDefaultState(), 2);
	        tile.validate();
	        world.setTileEntity(pos, tile);
		}

		return true;
	}

	public void spawnParticl (World world, BlockPos pos, Random rand) {

		for (int i = 0; i < 16; i++) {

			float f1 = pos.getX() + 0.5F;
			float f2 = pos.getY() + 0.25F + rand.nextFloat() * 0.5F;
			float f3 = pos.getZ() + 0.5F;
			float x = (rand.nextFloat() - rand.nextFloat()) * 0.15F;
			float z = (rand.nextFloat() - rand.nextFloat()) * 0.15F;

			Particle effect = new ParticleNomal.Factory().createParticle(0, world, f1, f2, f3, x, 0, z);
			FMLClientHandler.instance().getClient().effectRenderer.addEffect(effect);
		}
	}

	// ブロックの上にいたら
	@Override
	public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity) {

		if (!(entity instanceof EntityLivingBase ) || !entity.isEntityAlive() || !entity.isSneaking()) { return; }

		TileWarpBlock tile = (TileWarpBlock) world.getTileEntity(pos);
		if (tile.getBlock(pos) != BlockInit.warp_block_on) { return; }

		// 座標登録してるなら
		if (tile.isSetPos()) {
			BlockPos tePos = new BlockPos(tile.posX, tile.posY, tile.posZ);
			entity.setPositionAndUpdate(tePos.getX() + 0.5, tePos.getY() + 2, tePos.getZ() + 0.5);

			// クライアント（プレイヤー）へ送りつける
			if (entity instanceof EntityPlayerMP) {
				PacketHandler.sendToPlayer(new PlayerSoundPKT(SoundHelper.S_TELEPORT, 1F, 0.25F), (EntityPlayerMP) entity);
			}
		}

		else {

			for (BlockPos p : BlockPos.getAllInBox(pos.add(-64, -64, -64), pos.add(64, 64, 64))) {

				if (this.checkPos(pos, p)) { continue; }

				Block block = world.getBlockState(p).getBlock();
				if (block != BlockInit.warp_block_on) { continue; }

				TileWarpBlock warp = (TileWarpBlock) world.getTileEntity(p);
				if (warp.isSetPos()) { continue; }

				p = p.north();
				entity.setPositionAndUpdate(p.getX(), p.getY() + 2, p.getZ());

				// クライアント（プレイヤー）へ送りつける
				if (entity instanceof EntityPlayerMP) {
					PacketHandler.sendToPlayer(new PlayerSoundPKT(SoundHelper.S_TELEPORT, 1F, 0.25F), (EntityPlayerMP) entity);
				}
			}
		}
	}

	public boolean checkPos (BlockPos nowPos, BlockPos tePos) {
		return nowPos.getX() == tePos.getX() && nowPos.getY() == tePos.getY() && nowPos.getZ() == tePos.getZ();
	}

	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return Item.getItemFromBlock(BlockInit.warp_block);
	}

	protected ItemStack getSilkTouchDrop(IBlockState state) {
		return new ItemStack(BlockInit.warp_block);
	}

	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World playerIn, List<String> tooltip, ITooltipFlag advanced) {

		String text = new TextComponentTranslation("tip.warp_block_rege.name", new Object[0]).getFormattedText();
		tooltip.add(I18n.format(TextFormatting.GOLD + text));

		String text2 = new TextComponentTranslation("tip.warp_block_teleport.name", new Object[0]).getFormattedText();
		tooltip.add(I18n.format(TextFormatting.GOLD + text2));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {

		if (this != BlockInit.warp_block_on) { return; }

		Random random = world.rand;

		for (int i = 0; i < 4; ++i) {
			world.spawnParticle(EnumParticleTypes.PORTAL,
					pos.getX() + (random.nextDouble() - 0.5D) + 0.5D,
					pos.getY() + random.nextDouble() + 1,
					pos.getZ() + (random.nextDouble() - 0.5D) + 0.5D,
					(random.nextDouble() - 0.5D) * 2.0D,
					-random.nextDouble(),
					(random.nextDouble() - 0.5D) * 2.0D);
		}
	}
}
