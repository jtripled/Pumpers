package com.jtripled.pumpers;

import com.jtripled.pumpers.block.*;
import com.jtripled.pumpers.network.*;
import com.jtripled.voxen.block.IBlockBase;
import com.jtripled.voxen.mod.RegistrationHandler;
import com.jtripled.voxen.mod.Registry;
import net.minecraftforge.fml.relauncher.Side;

/**
 *
 * @author jtripled
 */
public class PumpersRegistry implements Registry
{
    public static final IBlockBase FLUID_DUCT = new BlockFluidDuct();
    public static final IBlockBase PUMP = new BlockPump();
    public static final IBlockBase TANK = new BlockTank();
    
    @Override
    public void onRegisterBlocks(RegistrationHandler handler)
    {
        handler.registerBlock(FLUID_DUCT);
        handler.registerBlock(PUMP);
        handler.registerBlock(TANK);
    }
    
    @Override
    public void onRegisterMessages(RegistrationHandler handler)
    {
        handler.registerMessage(BucketCooldownMessageHandler.class, BucketCooldownMessage.class, Side.CLIENT);
        handler.registerMessage(FluidMessageHandler.class, FluidMessage.class, Side.CLIENT);
        handler.registerMessage(TankResizeMessageHandler.class, TankResizeMessage.class, Side.CLIENT);
    }
}
