package com.jtripled.pumpers;

import com.jtripled.voxen.mod.ModBase;
import com.jtripled.voxen.mod.Registry;
import com.jtripled.voxen.network.Network;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 *
 * @author jtripled
 */
@Mod(modid = Pumpers.ID, name = Pumpers.NAME, version = Pumpers.VERSION)
@Mod.EventBusSubscriber
public class Pumpers extends ModBase
{
    @Mod.Instance(Pumpers.ID)
    public static Pumpers INSTANCE;
    
    public static final String ID = "pumpers";
    public static final String NAME = "Pumpers";
    public static final String VERSION = "1.0";
    
    public static final Network NETWORK = new Network(ID);

    @Override
    public String getID()
    {
        return ID;
    }

    @Override
    public String getName()
    {
        return NAME;
    }

    @Override
    public String getVersion()
    {
        return VERSION;
    }
    
    @Override
    public Network getNetwork()
    {
        return NETWORK;
    }
    
    @Override
    public Registry createRegistry()
    {
        return new PumpersRegistry();
    }
    
    @Mod.EventHandler
    public void onPreInit(FMLPreInitializationEvent event)
    {
        preInit(event);
    }
    
    @Mod.EventHandler
    public void onInit(FMLInitializationEvent event)
    {
        init(event);
    }
    
    @Mod.EventHandler
    public void onPostInit(FMLPostInitializationEvent event)
    {
        postInit(event);
    }
}
