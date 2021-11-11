package sweetmagic.init.block.crop;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import sweetmagic.init.ItemInit;
import sweetmagic.init.block.blocks.PlantPot;
import sweetmagic.util.ParticleHelper;

public class SweetCrops_Tall extends SweetCrops_STAGE5 {

	private static final AxisAlignedBB[] CROPS_AABB = new AxisAlignedBB[] {
		new AxisAlignedBB(0.25D, 0.3D, 0.25D, 0.75D, 0.0D, 0.75D),
		new AxisAlignedBB(0.25D, 0.55D, 0.25D, 0.75D, 0.0D, 0.75D),
		new AxisAlignedBB(0.2D, 0.85D, 0.2D, 0.8D, 0.0D, 0.8D),
		new AxisAlignedBB(0.175D, 1.0D, 0.175D, 0.825D, 0.0D, 0.825D),
		new AxisAlignedBB(0.15D, 1.0D, 0.15D, 0.85D, 0.0D, 0.85D)
	};

    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return CROPS_AABB[this.getNowStateMeta(state)];
    }

	public SweetCrops_Tall(String name, int data, int grnd, float f1) {
		super(name, data, grnd, f1);

		//重要：右クリック回収時のセットする成長段階をセット
		this.RC_SetStage = 1;
	}

	/**
	 *  0 = コーン
	 *　1 = とめと
	 *　2 = ナス
	 */

	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ){

		if (world.isRemote) { return true; }

		ItemStack stack = player.getHeldItem(hand);

		if (!this.isSickle(world, player, pos, stack)) {
			this.onRicghtClick(world, player, state, pos, stack);
		}
		return true;
	}

	// 右クリック
	public void onRicghtClick (World world, EntityPlayer player, IBlockState state, BlockPos pos, ItemStack stack) {

		// 最大成長かどうか
		if (this.isMaxAge(state)) {

			Random rand = world.rand;

			// 作物をドロップ
			this.doDropItem(world, pos, state, player, stack, rand, this.RC_SetStage);

			//成長段階をセット
			world.setBlockState(pos, this.growStage(world, state, this.RC_SetStage), 2);

			// 2段目かどうかで判断して上か下の座標を取得
			BlockPos pos2 = !this.checkTopBlock(world, pos) ? pos.down() : pos.up();
			IBlockState state2 = world.getBlockState(pos2);

			// 上か下したが同じ作物以外かつ最大成長じゃないならなら終了
			if (state2.getBlock() != this && !this.isMaxAge(state2)) { return; }

			// 作物をドロップ
			this.doDropItem(world, pos2, state2, player, stack, rand, this.RC_SetStage);

			// 上のブロックの処理
			if (!this.checkTopBlock(world, pos)) {
				this.breakBlock(world, pos2.up(), true);
				world.setBlockState(pos2, this.growStage(world, state2, this.RC_SetStage), 2);
			}

			// 下のブロックの処理
			else {
				world.setBlockState(pos2, this.growStage(world, state2, this.RC_SetStage), 2);
				this.breakBlock(world, pos2, true);
			}

		} else {

			ItemStack stackB = new ItemStack(Items.DYE, 1, 15);

			if (ItemStack.areItemsEqual(stack, stackB)) {

				// パーティクルスポーン
				ParticleHelper.spawnBoneMeal(world, pos, EnumParticleTypes.VILLAGER_HAPPY);
				if (!player.isCreative()) { stack.shrink(1); }

				// 成長処理
				world.setBlockState(pos, this.growStage(world, state, this.getNowStateMeta(state) + 1), 2);

				// 成長度が3以上かつ２段階目のブロックかつ上が空気である
				if (this.getNowStateMeta(state) >= 2 && this.checkTopBlock(world, pos) && world.getBlockState(pos.up()).getBlock() == Blocks.AIR) {
					world.setBlockState(pos.up(), this.getDefaultState(), 2);
				}
			}
		}
	}

	// 作物ドロップ
	public void doDropItem (World world, BlockPos pos, IBlockState state, EntityPlayer player, ItemStack hand, Random rand, int setstage) {
		// 作物ドロップ
    	EntityItem drop = this.getDropItem(world, player, hand, this.getCrop(), rand.nextInt(2) + 1);
    	world.spawnEntity(drop);

		// 音を鳴らす
		this.playCropSound(world, rand, pos);
	}


	// ブロック破壊処理
	public boolean breakBlock(World world, BlockPos pos, boolean dropBlock) {

        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
		if (block.isAir(state, world, pos)) { return false; }

		world.playEvent(2001, pos, Block.getStateId(state));
        block.dropBlockAsItem(world, pos, state, 0);

        return world.setBlockState(pos, Blocks.AIR.getDefaultState(), 2);
    }

	//最大成長段階の取得（int用）上書き忘れ注意
	@Override
	public int getMaxBlockState() {
		return 4;
	}

	//土、草、耕地に置いても壊れないようにする
	@Override
	public boolean canBlockStay(World world, BlockPos pos, IBlockState state) {

		IBlockState state2 = world.getBlockState(pos.down());
		Block block = state2.getBlock();

		if ( (this.getNowStateMeta(state2) > 1 && block == this) || block instanceof PlantPot || block instanceof BlockFarmland) {
			return true;
		}

		else if (this.stayGrnd == 1 && this.canSustainBush(state2) && block != this) {
			return false;
		}

        IBlockState soil = world.getBlockState(pos.down());
        return (world.getLight(pos) >= 8 || world.canSeeSky(pos)) && soil.getBlock().canSustainPlant(soil, world, pos.down(), EnumFacing.UP, this);
	}

	public boolean canSustainBush(IBlockState state) {
		Material mate = state.getMaterial();
		return mate == Material.GROUND || mate == Material.GRASS ;
	}

	//=====================記述変更終わり==============================

	//超必須メソッド　これがないとStatic参照ができずうまくBlockStateをやりくりしにくい
	//このメソッドが受け持つ役割は基本的に作物の成長段階を外からいじるときに使う。
	public static IBlockState withStage(World world, IBlockState state, int age) {
		return state.getBlock().getDefaultState().withProperty(getSweetState(), age);
	}

	//最大成長段階の取得（IBlockState用）
	public IBlockState getGrownState() {
		return this.getDefaultState().withProperty(this.getSweetState(), 4);
	}

	//ドロップする種
	@Override
	protected Item getSeed() {

		switch (this.metaCrop) {
		case 0:	return ItemInit.corn_seed;
		case 1:	return ItemInit.tomato;
		case 2:	return ItemInit.eggplant_seed;
		}

		return null;
	}

	//ドロップする作物
	@Override
	protected Item getCrop() {

		switch (this.metaCrop) {
		case 0:	return ItemInit.corn;
		case 1:	return ItemInit.tomato;
		case 2:	return ItemInit.eggplant;
		}

		return null;
	}

	// 上のブロックかどうかをチェックするメソッド
	public boolean checkTopBlock (World world, BlockPos pos) {
		return !(world.getBlockState(pos.down()).getBlock() instanceof SweetCrops_Tall);
	}

	//自然成長に必須。　ランダムTick更新処理の書き直しをするときはここをオーバーライドすること
	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {

		super.updateTick(world, pos, state, rand);

		// 成長度が3以上かつ２段階目のブロックかつ上が空気である
		if (this.getNowStateMeta(state) >= 2 && this.checkTopBlock(world, pos) && world.getBlockState(pos.up()).getBlock() == Blocks.AIR) {
			world.setBlockState(pos.up(), this.getDefaultState(), 2);
		}
	}

	// ブロックをこわしたとき(下のブロックを指定)
	@Override
	public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
		if (this.checkTopBlock(world, pos)) { this.breakBlock(world, pos.up(), true); }
	}
}
