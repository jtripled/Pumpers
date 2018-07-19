package com.jtripled.pumpers;

import com.jtripled.pumpers.render.TESRTank;
import com.jtripled.pumpers.tile.TileTank;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 *
 * @author jtripled
 */
@Mod.EventBusSubscriber
public class PumpersModels
{
    @SubscribeEvent
    public static void onRegisterModels(ModelRegistryEvent event)
    {
        Pumpers.getProxy().registerTileRenderer(TileTank.class, new TESRTank());
    }
}
