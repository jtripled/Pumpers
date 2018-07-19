package com.jtripled.pumpers.network;

import com.jtripled.pumpers.tile.TileFluidHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import static net.minecraftforge.fluids.capability.CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 *
 * @author jtripled
 */
public class MessageHandlerFluidContent implements IMessageHandler<MessageFluidContent, IMessage>
{
    @Override
    public IMessage onMessage(MessageFluidContent message, MessageContext context)
    {
        World world = Minecraft.getMinecraft().player.world;
        TileEntity tile = world.getTileEntity(message.getPos());
        if (tile != null && tile.hasCapability(FLUID_HANDLER_CAPABILITY, null))
        {
            TileFluidHandler handler = (TileFluidHandler) tile.getCapability(FLUID_HANDLER_CAPABILITY, null);
            handler.getTank().drain(Integer.MAX_VALUE, true);
            if (message.getFluidStack() != null)
            {
                handler.getTank().fill(message.getFluidStack(), true);
            }
        }
        return null;
    }
}
