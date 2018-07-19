package com.jtripled.pumpers;

import com.jtripled.pumpers.block.*;
import com.jtripled.pumpers.tile.TileFluidDuct;
import com.jtripled.pumpers.tile.TilePump;
import com.jtripled.pumpers.tile.TileTank;
import net.minecraft.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 *
 * @author jtripled
 */
@Mod.EventBusSubscriber
public class PumpersBlocks
{
    public static final Block FLUID_DUCT = new BlockFluidDuct();
    public static final Block PUMP = new BlockPump();
    public static final Block TANK = new BlockTank();
    
    @SubscribeEvent
    public static void onRegisterBlocks(RegistryEvent.Register<Block> event)
    {
        event.getRegistry().register(FLUID_DUCT);
        event.getRegistry().register(PUMP);
        event.getRegistry().register(TANK);
        
        GameRegistry.registerTileEntity(TileFluidDuct.class, FLUID_DUCT.getRegistryName().toString());
        GameRegistry.registerTileEntity(TilePump.class, PUMP.getRegistryName().toString());
        GameRegistry.registerTileEntity(TileTank.class, TANK.getRegistryName().toString());
        
        Pumpers.getProxy().registerIgnoredProperties(PUMP, BlockPump.ENABLED);
    }
}
