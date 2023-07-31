package io.github.cottonmc.templates.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Waterloggable;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

public class WaterloggableTemplateBlock extends TemplateBlock implements Waterloggable {
	public WaterloggableTemplateBlock(Settings settings) {
		super(settings);
		
		setDefaultState(getDefaultState().with(Properties.WATERLOGGED, false));
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder.add(Properties.WATERLOGGED));
	}
	
	@Nullable
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		//not calling TemplateInteractionUtil.modifyPlacementState because we extend TemplateBlock which already does
		BlockState sup = super.getPlacementState(ctx);
		if(sup != null) sup = sup.with(Properties.WATERLOGGED, ctx.getWorld().getFluidState(ctx.getBlockPos()).isOf(Fluids.WATER));
		return sup;
	}
	
	@Override
	public FluidState getFluidState(BlockState state) {
		return state.get(Properties.WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
	}
	
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState otherState, WorldAccess world, BlockPos pos, BlockPos moved) {
		if(state.get(Properties.WATERLOGGED)) world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
		return super.getStateForNeighborUpdate(state, direction, otherState, world, pos, moved);
	}
}
