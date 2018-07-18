package com.jtripled.pumpers.block;

import com.jtripled.pumpers.container.ContainerFluidDuct;
import com.jtripled.pumpers.gui.GUIFluidDuct;
import com.jtripled.pumpers.tile.TileFluidDuct;
import com.jtripled.voxen.block.BlockDuct;
import com.jtripled.voxen.block.IBlockFaceable;
import com.jtripled.voxen.gui.GUIHolder;
import com.jtripled.voxen.util.Tab;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import static net.minecraftforge.fluids.capability.CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY;

/**
 *
 * @author jtripled
 */
public class BlockFluidDuct extends BlockDuct implements IBlockFaceable.All, GUIHolder
{
    public BlockFluidDuct()
    {
        super("fluid_duct", Material.IRON);
        this.setTab(Tab.REDSTONE);
        this.setItem();
        this.setTileClass(TileFluidDuct.class);
        this.setFullCube(false);
        this.setOpaque(false);
        this.setRenderLayer(BlockRenderLayer.CUTOUT_MIPPED);
        this.setTopSolid(true);
        this.setRenderSides(true);
    }

    @Override
    public boolean canConnect(BlockPos pos, IBlockState state, IBlockAccess world, BlockPos otherPos, IBlockState otherState, EnumFacing face)
    {
        TileEntity tile = world.getTileEntity(otherPos);
        return tile != null && face != state.getValue(FACING)
                && tile.hasCapability(FLUID_HANDLER_CAPABILITY, face);
    }
    
    @Override
    public ContainerFluidDuct getServerGUI(EntityPlayer player, World world, BlockPos pos)
    {
        return new ContainerFluidDuct((TileFluidDuct) world.getTileEntity(pos), player.inventory);
    }
    
    @Override
    public GUIFluidDuct getClientGUI(EntityPlayer player, World world, BlockPos pos)
    {
        return new GUIFluidDuct(getServerGUI(player, world, pos));
    }
}
