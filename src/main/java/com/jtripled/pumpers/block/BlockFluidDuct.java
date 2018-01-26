package com.jtripled.pumpers.block;

import com.jtripled.pumpers.Pumpers;
import com.jtripled.pumpers.container.ContainerFluidDuct;
import com.jtripled.pumpers.gui.GUIFluidDuct;
import com.jtripled.pumpers.tile.TileFluidDuct;
import com.jtripled.voxen.block.BlockDuct;
import com.jtripled.voxen.gui.GUIHolder;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

/**
 *
 * @author jtripled
 */
public class BlockFluidDuct extends BlockDuct implements GUIHolder
{
    public BlockFluidDuct()
    {
        super(Pumpers.INSTANCE, "fluid_duct", Material.IRON);
        this.setTab(CreativeTabs.REDSTONE);
        this.setItem();
        this.setTileClass(TileFluidDuct.class);
    }
    
    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (!world.isRemote)
        {
            this.openGUI(player, world, pos);
        }
        return true;
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

    @Override
    public boolean canConnect(IBlockState state, IBlockAccess world, BlockPos otherPos, EnumFacing otherFacing)
    {
        TileEntity tile = world.getTileEntity(otherPos);
        return tile != null && tile.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, otherFacing);
    }
}
