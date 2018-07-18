package com.jtripled.pumpers.tile;

import com.jtripled.voxen.tile.ITileFluidStorage;
import com.jtripled.voxen.tile.TileBase;
import javax.annotation.Nullable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

/**
 *
 * @author jtripled
 */
public class TileTank extends TileBase implements ITileFluidStorage
{
    public FluidTank tank;
    public TileTank baseTank;
    
    public TileTank()
    {
        this.tank = new FluidTank(Fluid.BUCKET_VOLUME * 16);
        this.baseTank = null;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
    {
        return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY;
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
    {
        return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY ? (T)getBaseTank() : null;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        compound.setInteger("capacity", tank.getCapacity());
        writeInternalTank(compound);
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        tank.setCapacity(compound.getInteger("capacity"));
        readInternalTank(compound);
        super.readFromNBT(compound);
    }
    
    public TileTank getBaseTank()
    {
        if (baseTank == null || world.isRemote)
        {
            BlockPos next = pos;
            TileEntity test = world.getTileEntity(next);
            TileTank base = null;
            while (test instanceof TileTank && next.getY() >= 0)
            {
                base = (TileTank) test;
                next = next.down();
                test = world.getTileEntity(next);
            }
            baseTank = base;
        }
        return baseTank;
    }
    
    @Override
    public FluidTank getInternalTank()
    {
        return tank;
    }
    
    @Override
    public BlockPos getInternalTankPos()
    {
        return pos;
    }
}
