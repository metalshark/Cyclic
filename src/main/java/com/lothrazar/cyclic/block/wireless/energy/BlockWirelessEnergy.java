package com.lothrazar.cyclic.block.wireless.energy;

import com.lothrazar.cyclic.base.BlockBase;
import com.lothrazar.cyclic.registry.ContainerScreenRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.fluid.FluidState;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockDisplayReader;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class BlockWirelessEnergy extends BlockBase {

  private static final double BOUNDS = 4;
  public static final VoxelShape AABB = Block.makeCuboidShape(BOUNDS, BOUNDS, BOUNDS, 16 - BOUNDS, 16 - BOUNDS, 16 - BOUNDS);

  public BlockWirelessEnergy(Properties properties) {
    super(properties.hardnessAndResistance(1.2F).notSolid());
    this.setHasGui();
  }

  @Override
  protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
    builder.add(LIT);
  }

  @Override
  public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
    return AABB;
  }

  @Override
  public boolean shouldDisplayFluidOverlay(BlockState state, IBlockDisplayReader world, BlockPos pos, FluidState fluidState) {
    return true;
  }

  @Override
  public void registerClient() {
    ScreenManager.registerFactory(ContainerScreenRegistry.wireless_energy, ScreenWirelessEnergy::new);
    RenderTypeLookup.setRenderLayer(this, RenderType.getCutoutMipped());
    //    ClientRegistry.bindTileEntityRenderer(TileRegistry.wireless_transmitter, RenderTransmit::new);
  }

  @Override
  public boolean hasTileEntity(BlockState state) {
    return true;
  }

  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader world) {
    return new TileWirelessEnergy();
  }

  @Override
  public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
    if (state.getBlock() != newState.getBlock()) {
      TileWirelessEnergy tileentity = (TileWirelessEnergy) worldIn.getTileEntity(pos);
      if (tileentity != null && tileentity.gpsSlots != null) {
        InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), tileentity.gpsSlots.getStackInSlot(0));
      }
      worldIn.updateComparatorOutputLevel(pos, this);
    }
    super.onReplaced(state, worldIn, pos, newState, isMoving);
  }
}
