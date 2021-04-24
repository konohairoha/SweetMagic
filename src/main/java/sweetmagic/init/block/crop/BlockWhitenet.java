package sweetmagic.init.block.crop;

import java.util.Random;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import sweetmagic.init.ItemInit;
import sweetmagic.util.ParticleHelper;
import sweetmagic.util.SMUtil;
import sweetmagic.util.SweetState;

public class BlockWhitenet extends SweetCrops_STAGE4 {

	public BlockWhitenet(String name, int n1, int n2, float f1) {
		super(name, n1, n2, f1);
		this.setSoundType(SoundType.PLANT);
		this.disableStats();
		//必ずメソッド含めすべてのBlockStateの上書きをすること。SweetStateのプロパティ入りBlockStateであるSTAGE(任意の数字)を使う。
		this.setDefaultState(this.blockState.getBaseState().withProperty(SweetState.STAGE4, 0));
	}

	// ドロップする種
	@Override
	protected Item getSeed() {
		return ItemInit.whitenet_seed;
	}

	// ドロップする作物
	@Override
	protected Item getCrop() {
		return ItemInit.whitenet;
	}

	// 自然成長に必須。　ランダムTick更新処理の書き直しをするときはここをオーバーライドすること
	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {

		super.updateTick(world, pos, state, rand);
		if (!world.isAreaLoaded(pos, 1)) { return; }

		int i = this.getNowStateMeta(state);
		if (i < this.getMaxBlockState()) {
			float f = SMUtil.getGrowthChance(this, world, pos, 2F);
			if (ForgeHooks.onCropsGrowPre(world, pos, state, rand.nextInt((int) (this.growValue / f) + 1) == 0)) {
				world.setBlockState(pos, this.withStage(world, state, i + 1), 2);
				ForgeHooks.onCropsGrowPost(world, pos, state, world.getBlockState(pos));
			}
		}
	}

	//土、草、耕地に置いても壊れないようにする
	@Override
	public boolean canBlockStay(World world, BlockPos pos, IBlockState state) {
		return this.canPlaceBlockAt(world, pos);
	}

	@Override
	public boolean canPlaceBlockAt(World world, BlockPos pos) {
		Material m = world.getBlockState(pos.down()).getMaterial();
		return m == Material.ROCK || m == Material.GROUND || m == Material.WOOD;
	}

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

		int age = this.getNowStateMeta(state);

		if (age >= this.getMaxBlockState()) {

			Random rand = new Random();
			world.spawnEntity(this.getDropItem(world, player, stack, this.getCrop(), rand.nextInt(2) + 1));
			world.setBlockState(pos, this.withStage(world, state, 0), 1); //スティッキースタッフの成長段階を3下げる
			this.playCropSound(world, rand, pos);
		}

		else {

			ItemStack stackB = new ItemStack(Items.DYE, 1, 15);
			if (ItemStack.areItemsEqual(stack, stackB)) {
            	ParticleHelper.spawnBoneMeal(world, pos, EnumParticleTypes.VILLAGER_HAPPY);
				if (!player.capabilities.isCreativeMode) { stack.shrink(1); }
				world.setBlockState(pos, this.withStage(world, state, this.getNowStateMeta(state) + 1), 2);
			}
		}
	}
}
