package com.jtripled.pumpers.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

/**
 *
 * @author jtripled
 */
public class MessageFluidContent implements IMessage
{
    private BlockPos pos;
    private Fluid fluid;
    private int amount;

    public MessageFluidContent()
    {

    }

    public MessageFluidContent(BlockPos pos, FluidStack fluid)
    {
        this(pos, fluid == null ? null : fluid.getFluid(), fluid == null ? 0 : fluid.amount);
    }
    
    public MessageFluidContent(BlockPos pos, Fluid fluid, int amount)
    {
        this.pos = pos;
        this.fluid = fluid;
        this.amount = amount;
    }

    public BlockPos getPos()
    {
        return pos;
    }
    
    public Fluid getFluid()
    {
        return fluid;
    }
    
    public int getAmount()
    {
        return amount;
    }
    
    public FluidStack getFluidStack()
    {
        return fluid == null ? null : new FluidStack(fluid, amount);
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
        amount = buf.readInt();
        if (amount > 0)
        {
            String name = ByteBufUtils.readUTF8String(buf);
            fluid = FluidRegistry.getFluid(name);
        }
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(pos.getX());
        buf.writeInt(pos.getY());
        buf.writeInt(pos.getZ());
        buf.writeInt(amount);
        ByteBufUtils.writeUTF8String(buf, fluid == null ? "null" : fluid.getName());
    }
}
