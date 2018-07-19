package com.jtripled.pumpers.tile;

import com.jtripled.pumpers.block.BlockFluidDuct;
import javax.annotation.Nullable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

/**
 *
 * @author jtripled
 */
public class TileFluidDuct extends TileFluidHandler implements ITickable
{
    private EnumFacing previous;
    private int transferCooldown;
    
    public TileFluidDuct()
    {
        super(new FluidTank(Fluid.BUCKET_VOLUME * 1));
        this.previous = EnumFacing.EAST;
        this.transferCooldown = -1;
    }
    
    public EnumFacing getFacing()
    {
        return this.world.getBlockState(this.getPos()).getValue(BlockFluidDuct.FACING);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
    {
        return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY
                && (facing == null || facing == this.getFacing());
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
    {
        return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY
                && (facing == null || facing == this.getFacing())
                ? (T) this : null;
    }
    
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        compound.setInteger("cooldown", transferCooldown);
        compound.setInteger("previous", previous.getIndex());
        return super.writeToNBT(compound);
    }
    
    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        transferCooldown = compound.getInteger("cooldown");
        previous = EnumFacing.getFront(compound.getInteger("previous"));
        super.readFromNBT(compound);
    }

    @Override
    public void update()
    {
        if (this.getWorld() != null && !this.getWorld().isRemote)
        {
            transferCooldown -= 1;
            if (transferCooldown <= 0)
            {
                transferCooldown = 0;
                boolean flag = false;
                if (tank.getFluidAmount() > 0)
                    flag = transferOut();
                if (flag)
                {
                    transferCooldown = 8;
                    this.markDirty();
                }
            }
        }
    }
    
    public boolean transferOut()
    {
        EnumFacing next = getNextFacing(previous, world.getBlockState(pos).getActualState(world, pos));
        if (next == null)
            return false;
        TileEntity testTile = world.getTileEntity(pos.offset(next));
        if (testTile != null && testTile.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, next.getOpposite()))
        {
            previous = next;
            IFluidHandler nextInventory = testTile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, next.getOpposite());
            int amount = nextInventory.fill(drain(400, false), false);
            if (tank.getFluidAmount() > 0 && amount > 0)
            {
                nextInventory.fill(drain(amount, true), true);
                return true;
            }
            return false;
        }
        return false;
    }
    
    private static EnumFacing getNextFacing(EnumFacing previous, IBlockState state)
    {
        EnumFacing[] next;
        switch (previous)
        {
            case DOWN: next = new EnumFacing[] { EnumFacing.UP, EnumFacing.NORTH, EnumFacing.SOUTH, EnumFacing.WEST, EnumFacing.EAST, EnumFacing.DOWN }; break;
            case UP: next = new EnumFacing[] { EnumFacing.NORTH, EnumFacing.SOUTH, EnumFacing.WEST, EnumFacing.EAST, EnumFacing.DOWN, EnumFacing.UP }; break;
            case NORTH: next = new EnumFacing[] { EnumFacing.SOUTH, EnumFacing.WEST, EnumFacing.EAST, EnumFacing.DOWN, EnumFacing.UP, EnumFacing.NORTH }; break;
            case SOUTH: next = new EnumFacing[] { EnumFacing.WEST, EnumFacing.EAST, EnumFacing.DOWN, EnumFacing.UP, EnumFacing.NORTH, EnumFacing.SOUTH }; break;
            case WEST: next = new EnumFacing[] { EnumFacing.EAST, EnumFacing.DOWN, EnumFacing.UP, EnumFacing.NORTH, EnumFacing.SOUTH, EnumFacing.WEST }; break;
            default: next = EnumFacing.values(); break;
        }
        for (EnumFacing face : next)
        {
            if (face != state.getValue(BlockFluidDuct.FACING))
            {
                switch (face)
                {
                    case DOWN: if (state.getValue(BlockFluidDuct.DOWN)) return face; break;
                    case UP: if (state.getValue(BlockFluidDuct.UP)) return face; break;
                    case NORTH: if (state.getValue(BlockFluidDuct.NORTH)) return face; break;
                    case SOUTH: if (state.getValue(BlockFluidDuct.SOUTH)) return face; break;
                    case WEST: if (state.getValue(BlockFluidDuct.WEST)) return face; break;
                    default: if (state.getValue(BlockFluidDuct.EAST)) return EnumFacing.EAST; break;
                }
            }
        }
        return null;
    }
}



/* extends TileBase implements ITileFluidStorage, ITileTransferable
{
    private final FluidTank tank;
    private EnumFacing previous;
    private int transferCooldown;
    
    public TileFluidDuct()
    {
        this.tank = new FluidTank(Fluid.BUCKET_VOLUME * 1);
        this.previous = EnumFacing.EAST;
        this.transferCooldown = -1;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
    {
        return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY
                && (facing == null || facing == getFacing(this));
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
    {
        return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY
                && (facing == null || facing == getFacing(this)) ? (T)this : null;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        writeInternalTank(compound);
        compound.setInteger("previous", previous.getIndex());
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        readInternalTank(compound);
        previous = EnumFacing.getFront(compound.getInteger("previous"));
        super.readFromNBT(compound);
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

    @Override
    public int getTransferCooldown()
    {
        return transferCooldown;
    }

    @Override
    public void setTransferCooldown(int cooldown)
    {
        transferCooldown = cooldown;
    }
    
    @Override
    public boolean canTransferOut()
    {
        return this.getInternalTank().getFluidAmount() > 0;
    }
    
    @Override
    public boolean transferOut()
    {
        EnumFacing next = getNextFacing(previous, world.getBlockState(pos).getActualState(world, pos));
        if (next == null)
            return false;
        TileEntity testTile = world.getTileEntity(pos.offset(next));
        if (testTile != null && testTile.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, next.getOpposite()))
        {
            previous = next;
            IFluidHandler nextInventory = testTile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, next.getOpposite());
            int amount = nextInventory.fill(drain(400, false), false);
            if (tank.getFluidAmount() > 0 && amount > 0)
            {
                nextInventory.fill(drain(amount, true), true);
                return true;
            }
            return false;
        }
        return false;
    }
    
    private static EnumFacing getNextFacing(EnumFacing previous, IBlockState state)
    {
        EnumFacing[] next;
        switch (previous)
        {
            case DOWN: next = new EnumFacing[] { EnumFacing.UP, EnumFacing.NORTH, EnumFacing.SOUTH, EnumFacing.WEST, EnumFacing.EAST, EnumFacing.DOWN }; break;
            case UP: next = new EnumFacing[] { EnumFacing.NORTH, EnumFacing.SOUTH, EnumFacing.WEST, EnumFacing.EAST, EnumFacing.DOWN, EnumFacing.UP }; break;
            case NORTH: next = new EnumFacing[] { EnumFacing.SOUTH, EnumFacing.WEST, EnumFacing.EAST, EnumFacing.DOWN, EnumFacing.UP, EnumFacing.NORTH }; break;
            case SOUTH: next = new EnumFacing[] { EnumFacing.WEST, EnumFacing.EAST, EnumFacing.DOWN, EnumFacing.UP, EnumFacing.NORTH, EnumFacing.SOUTH }; break;
            case WEST: next = new EnumFacing[] { EnumFacing.EAST, EnumFacing.DOWN, EnumFacing.UP, EnumFacing.NORTH, EnumFacing.SOUTH, EnumFacing.WEST }; break;
            default: next = EnumFacing.values(); break;
        }
        for (EnumFacing face : next)
        {
            if (face != state.getValue(IBlockFaceable.FACING))
            {
                switch (face)
                {
                    case DOWN: if (state.getValue(BlockDuct.DOWN)) return face; break;
                    case UP: if (state.getValue(BlockDuct.UP)) return face; break;
                    case NORTH: if (state.getValue(BlockDuct.NORTH)) return face; break;
                    case SOUTH: if (state.getValue(BlockDuct.SOUTH)) return face; break;
                    case WEST: if (state.getValue(BlockDuct.WEST)) return face; break;
                    default: if (state.getValue(BlockDuct.EAST)) return EnumFacing.EAST; break;
                }
            }
        }
        return null;
    }
    
    public static EnumFacing getFacing(TileFluidDuct tile)
    {
        return tile.world.getBlockState(tile.getPos()).getValue(IBlockFaceable.FACING);
    }
}
(*/