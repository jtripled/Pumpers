package com.jtripled.pumpers.tile;

import com.jtripled.pumpers.Pumpers;
import com.jtripled.pumpers.network.FluidMessage;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

/**
 *
 * @author jtripled
 */
public interface ITileFluidStorage extends IFluidHandler
{
    public FluidTank getInternalTank();
    
    public BlockPos getInternalTankPos();

    public default NBTTagCompound writeInternalTank(NBTTagCompound compound)
    {
        getInternalTank().writeToNBT(compound);
        return compound;
    }

    public default void readInternalTank(NBTTagCompound compound)
    {
        getInternalTank().readFromNBT(compound);
    }
    
    @Override
    public default IFluidTankProperties[] getTankProperties()
    {
        return getInternalTank().getTankProperties();
    }

    @Override
    public default int fill(FluidStack resource, boolean doFill)
    {
        int filled = getInternalTank().fill(resource, doFill);
        if (!((TileEntity) this).getWorld().isRemote && doFill && filled > 0)
        {
            Pumpers.INSTANCE.getNetwork().sendToAll(new FluidMessage(getInternalTankPos(), getInternalTank().getFluid()));
        }
        return filled;
    }

    @Override
    public default FluidStack drain(FluidStack resource, boolean doDrain)
    {
        FluidStack drained = getInternalTank().drain(resource, doDrain);
        if (!((TileEntity) this).getWorld().isRemote && doDrain && drained != null)
        {
            Pumpers.INSTANCE.getNetwork().sendToAll(new FluidMessage(getInternalTankPos(), getInternalTank().getFluid()));
        }
        return drained;
    }

    @Override
    public default FluidStack drain(int maxDrain, boolean doDrain)
    {
        FluidStack drained = getInternalTank().drain(maxDrain, doDrain);
        if (!((TileEntity) this).getWorld().isRemote && doDrain && drained != null)
        {
            Pumpers.INSTANCE.getNetwork().sendToAll(new FluidMessage(getInternalTankPos(), getInternalTank().getFluid()));
        }
        return drained;
    }
}
