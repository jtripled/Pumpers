package com.jtripled.pumpers;

import com.jtripled.pumpers.block.*;
import com.jtripled.pumpers.tile.TileFluidDuct;
import com.jtripled.pumpers.tile.TilePump;
import com.jtripled.pumpers.tile.TileTank;
import net.minecraft.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

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
        Pumpers.getProxy().registerBlock(event, FLUID_DUCT, TileFluidDuct.class);
        Pumpers.getProxy().registerBlock(event, PUMP, TilePump.class, BlockPump.ENABLED);
        Pumpers.getProxy().registerBlock(event, TANK, TileTank.class);
    }
}
