package com.jtripled.pumpers;

import com.jtripled.pumpers.block.*;
import com.jtripled.pumpers.tile.TileFluidDuct;
import com.jtripled.pumpers.tile.TilePump;
import com.jtripled.pumpers.tile.TileTank;
import net.minecraft.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

/**
 *
 * @author jtripled
 */
@Mod.EventBusSubscriber
@ObjectHolder(Pumpers.ID)
public class PumpersBlocks
{
    public static final Block FLUID_DUCT = null;
    public static final Block PUMP = null;
    public static final Block TANK = null;
    
    @SubscribeEvent
    public static void onRegisterBlocks(RegistryEvent.Register<Block> event)
    {
        Pumpers.getProxy().registerBlock(event, new BlockFluidDuct(), TileFluidDuct.class);
        Pumpers.getProxy().registerBlock(event, new BlockPump(), TilePump.class, BlockPump.ENABLED);
        Pumpers.getProxy().registerBlock(event, new BlockTank(), TileTank.class);
    }
}
