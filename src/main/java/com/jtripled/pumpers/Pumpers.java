package com.jtripled.pumpers;

import com.jtripled.pumpers.container.ContainerFluidDuct;
import com.jtripled.pumpers.container.ContainerPump;
import com.jtripled.pumpers.container.ContainerTank;
import com.jtripled.pumpers.gui.GUIFluidDuct;
import com.jtripled.pumpers.gui.GUIPump;
import com.jtripled.pumpers.gui.GUITank;
import com.jtripled.pumpers.network.MessageBucketCooldown;
import com.jtripled.pumpers.network.MessageFluidCapacity;
import com.jtripled.pumpers.network.MessageFluidContent;
import com.jtripled.pumpers.network.MessageHandlerBucketCooldown;
import com.jtripled.pumpers.network.MessageHandlerFluidCapacity;
import com.jtripled.pumpers.network.MessageHandlerFluidContent;
import com.jtripled.pumpers.proxy.Proxy;
import com.jtripled.pumpers.tile.TileFluidDuct;
import com.jtripled.pumpers.tile.TilePump;
import com.jtripled.pumpers.tile.TileTank;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

/**
 *
 * @author jtripled
 */
@Mod(modid = Pumpers.ID, name = Pumpers.NAME, version = Pumpers.VERSION)
@Mod.EventBusSubscriber
public class Pumpers implements IGuiHandler
{
    public static final String ID = "pumpers";
    public static final String NAME = "Pumpers";
    public static final String VERSION = "1.0";
    public static final String DEPENDS = "";
    
    @Mod.Instance(ID)
    public static Pumpers INSTANCE;
    
    @SidedProxy(serverSide = "com.jtripled." + ID + ".proxy.ProxyServer", clientSide = "com.jtripled." + ID + ".proxy.ProxyClient")
    public static Proxy PROXY;
    
    public static final SimpleNetworkWrapper NETWORK = new SimpleNetworkWrapper(ID);
    
    public static Pumpers getInstance()
    {
        return INSTANCE;
    }

    public static String getID()
    {
        return ID;
    }

    public static String getName()
    {
        return NAME;
    }

    public static String getVersion()
    {
        return VERSION;
    }
    
    public static Proxy getProxy()
    {
        return PROXY;
    }
    
    public static SimpleNetworkWrapper getNetwork()
    {
        return NETWORK;
    }
    
    @Mod.EventHandler
    public void onPreInit(FMLPreInitializationEvent event)
    {
        
    }
    
    @Mod.EventHandler
    public void onInit(FMLInitializationEvent event)
    {
        NETWORK.registerMessage(MessageHandlerBucketCooldown.class, MessageBucketCooldown.class, 0, Side.CLIENT);
        NETWORK.registerMessage(MessageHandlerFluidCapacity.class, MessageFluidCapacity.class, 1, Side.CLIENT);
        NETWORK.registerMessage(MessageHandlerFluidContent.class, MessageFluidContent.class, 2, Side.CLIENT);
        NetworkRegistry.INSTANCE.registerGuiHandler(this, this);
    }
    
    @Mod.EventHandler
    public void onPostInit(FMLPostInitializationEvent event)
    {
        
    }

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        switch (ID)
        {
            case 0:
                return new ContainerFluidDuct((TileFluidDuct) world.getTileEntity(new BlockPos(x, y, z)), player.inventory);
            case 1:
                return new ContainerPump((TilePump) world.getTileEntity(new BlockPos(x, y, z)), player.inventory);
            case 2:
                return new ContainerTank((TileTank) world.getTileEntity(new BlockPos(x, y, z)), player.inventory);
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        switch (ID)
        {
            case 0:
                return new GUIFluidDuct((ContainerFluidDuct) getServerGuiElement(ID, player, world, x, y, z));
            case 1:
                return new GUIPump((ContainerPump) getServerGuiElement(ID, player, world, x, y, z));
            case 2:
                return new GUITank((ContainerTank) getServerGuiElement(ID, player, world, x, y, z));
        }
        return null;
    }
}
