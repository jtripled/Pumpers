package com.jtripled.pumpers.tile;

import com.jtripled.pumpers.Pumpers;
import com.jtripled.pumpers.network.MessageFluidCapacity;
import com.jtripled.pumpers.network.MessageFluidContent;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

/**
 *
 * @author jtripled
 */
public class TileFluidHandler extends TileEntity implements IFluidHandler
{
    protected final FluidTank tank;
    
    public TileFluidHandler(FluidTank tank)
    {
        this.tank = tank;
    }
    
    public FluidTank getTank()
    {
        return tank;
    }
    
    public int getCapacity()
    {
        return tank.getCapacity();
    }
    
    public void setCapacity(int capacity)
    {
        tank.setCapacity(capacity);
        if (tank.getFluidAmount() > capacity)
            this.fill(new FluidStack(this.drain(Integer.MAX_VALUE, true), capacity), true);
        if (!getWorld().isRemote)
        {
            Pumpers.getNetwork().sendToAll(new MessageFluidCapacity(getPos(), tank.getCapacity()));
            if (tank.getFluidAmount() > capacity)
                Pumpers.getNetwork().sendToAll(new MessageFluidContent(getPos(), new FluidStack(tank.getFluid(), tank.getFluidAmount())));
        }
    }
    
    @Override
    public IFluidTankProperties[] getTankProperties()
    {
        return tank.getTankProperties();
    }

    @Override
    public int fill(FluidStack resource, boolean doFill)
    {
        int filled = tank.fill(resource, doFill);
        if (!getWorld().isRemote && doFill && filled > 0)
        {
            Pumpers.getNetwork().sendToAll(new MessageFluidContent(getPos(), tank.getFluid()));
        }
        return filled;
    }

    @Override
    public FluidStack drain(FluidStack resource, boolean doDrain)
    {
        FluidStack drained = tank.drain(resource, doDrain);
        if (!getWorld().isRemote && doDrain && drained != null)
        {
            Pumpers.getNetwork().sendToAll(new MessageFluidContent(getPos(), tank.getFluid()));
        }
        return drained;
    }

    @Override
    public FluidStack drain(int maxDrain, boolean doDrain)
    {
        FluidStack drained = tank.drain(maxDrain, doDrain);
        if (!getWorld().isRemote && doDrain && drained != null)
        {
            Pumpers.getNetwork().sendToAll(new MessageFluidContent(getPos(), tank.getFluid()));
        }
        return drained;
    }
    
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        tank.writeToNBT(compound);
        return super.writeToNBT(compound);
    }
    
    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        tank.readFromNBT(compound);
        super.readFromNBT(compound);
    }
    
    @Override
    public void onDataPacket(NetworkManager network, SPacketUpdateTileEntity packet)
    {
        readFromNBT(packet.getNbtCompound());
    }
    
    @Override
    public SPacketUpdateTileEntity getUpdatePacket()
    {
        return new SPacketUpdateTileEntity(getPos(), 1, getUpdateTag());
    }
    
    @Override
    public NBTTagCompound getUpdateTag()
    {
        return writeToNBT(super.getUpdateTag());
    }
    
    @Override
    public void handleUpdateTag(NBTTagCompound compound)
    {
        readFromNBT(compound);
    }
}
