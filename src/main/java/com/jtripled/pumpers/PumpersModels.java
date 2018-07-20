package com.jtripled.pumpers;

import com.jtripled.pumpers.render.TESRTank;
import com.jtripled.pumpers.tile.TileTank;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 *
 * @author jtripled
 */
@Mod.EventBusSubscriber
public class PumpersModels
{
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void onRegisterModels(ModelRegistryEvent event)
    {
        Pumpers.getProxy().registerTileRenderer(TileTank.class, new TESRTank());
    }
}
