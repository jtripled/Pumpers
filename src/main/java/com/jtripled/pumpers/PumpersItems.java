package com.jtripled.pumpers;

import com.jtripled.pumpers.item.ItemBlockFluidDuct;
import com.jtripled.pumpers.item.ItemBlockPump;
import com.jtripled.pumpers.item.ItemBlockTank;
import net.minecraft.item.Item;
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
public class PumpersItems
{
    public static final Item FLUID_DUCT = null;
    public static final Item PUMP_ITEM = null;
    public static final Item TANK_ITEM = null;
    
    @SubscribeEvent
    public static void onRegisterItems(RegistryEvent.Register<Item> event)
    {
        Pumpers.getProxy().registerItem(event, new ItemBlockFluidDuct(PumpersBlocks.FLUID_DUCT));
        Pumpers.getProxy().registerItem(event, new ItemBlockPump(PumpersBlocks.PUMP));
        Pumpers.getProxy().registerItem(event, new ItemBlockTank(PumpersBlocks.TANK));
    }
}