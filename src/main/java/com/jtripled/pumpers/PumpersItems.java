package com.jtripled.pumpers;

import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 *
 * @author jtripled
 */
@Mod.EventBusSubscriber
public class PumpersItems
{
    public static final Item FLUID_DUCT_ITEM = new ItemBlock(PumpersBlocks.FLUID_DUCT).setRegistryName(PumpersBlocks.FLUID_DUCT.getRegistryName()).setUnlocalizedName(PumpersBlocks.FLUID_DUCT.getUnlocalizedName());
    public static final Item PUMP_ITEM = new ItemBlock(PumpersBlocks.PUMP).setRegistryName(PumpersBlocks.PUMP.getRegistryName()).setUnlocalizedName(PumpersBlocks.PUMP.getUnlocalizedName());
    public static final Item TANK_ITEM = new ItemBlock(PumpersBlocks.TANK).setRegistryName(PumpersBlocks.TANK.getRegistryName()).setUnlocalizedName(PumpersBlocks.TANK.getUnlocalizedName());
    
    @SubscribeEvent
    public static void onRegisterItems(RegistryEvent.Register<Item> event)
    {
        Pumpers.getProxy().registerItem(event, FLUID_DUCT_ITEM);
        Pumpers.getProxy().registerItem(event, PUMP_ITEM);
        Pumpers.getProxy().registerItem(event, TANK_ITEM);
    }
}