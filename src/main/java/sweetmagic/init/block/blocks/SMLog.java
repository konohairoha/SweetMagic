package sweetmagic.init.block.blocks;

import net.minecraft.block.BlockLog;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import sweetmagic.api.iblock.ISmeltItemBlock;
import sweetmagic.init.BlockInit;

public class SMLog extends BlockLog implements ISmeltItemBlock {

	private static final PropertyEnum<EnumAxis> LOG_AXIS = PropertyEnum.create("axis", SMLog.EnumAxis.class);
    private IBlockState sapling;

    public SMLog(String name) {
        setRegistryName(name);
        setUnlocalizedName(name);
		setResistance(1024F);
        setDefaultState(this.blockState.getBaseState().withProperty(LOG_AXIS, SMLog.EnumAxis.Y));
        setHarvestLevel("axe", 1);
		BlockInit.blockList.add(this);
    }

    @Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(LOG_AXIS, SMLog.EnumAxis.values()[meta]);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(LOG_AXIS).ordinal();
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, LOG_AXIS);
    }

	// 精錬時間の取得
	@Override
	public int getSmeltTime() {
		return 1200;
	}
}
