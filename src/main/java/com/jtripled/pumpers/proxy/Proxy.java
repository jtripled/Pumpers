package com.jtripled.pumpers.proxy;

import com.jtripled.pumpers.Pumpers;
import com.jtripled.pumpers.container.ContainerFluidDuct;
import com.jtripled.pumpers.container.ContainerPump;
import com.jtripled.pumpers.container.ContainerTank;
import com.jtripled.pumpers.network.MessageBucketCooldown;
import com.jtripled.pumpers.network.MessageFluidCapacity;
import com.jtripled.pumpers.network.MessageFluidContent;
import com.jtripled.pumpers.network.MessageHandlerBucketCooldown;
import com.jtripled.pumpers.network.MessageHandlerFluidCapacity;
import com.jtripled.pumpers.network.MessageHandlerFluidContent;
import com.jtripled.pumpers.tile.TileFluidDuct;
import com.jtripled.pumpers.tile.TilePump;
import com.jtripled.pumpers.tile.TileTank;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

/**
 *
 * @author jtripled
 */
public class Proxy implements IGuiHandler
{
    public void onPreInit(FMLPreInitializationEvent event)
    {
        
    }
    
    public void onInit(FMLInitializationEvent event)
    {
        Pumpers.getNetwork().registerMessage(MessageHandlerBucketCooldown.class, MessageBucketCooldown.class, 0, Side.CLIENT);
        Pumpers.getNetwork().registerMessage(MessageHandlerFluidCapacity.class, MessageFluidCapacity.class, 1, Side.CLIENT);
        Pumpers.getNetwork().registerMessage(MessageHandlerFluidContent.class, MessageFluidContent.class, 2, Side.CLIENT);
        NetworkRegistry.INSTANCE.registerGuiHandler(Pumpers.getInstance(), this);
    }
    
    public void onPostInit(FMLPostInitializationEvent event)
    {
        
    }
    
    public String localize(String unlocalized, Object... args)
    {
        return null;
    }
    
    public void registerItem(RegistryEvent.Register<Item> event, Item item)
    {
        event.getRegistry().register(item);
    }
    
    public void registerBlock(RegistryEvent.Register<Block> event, Block block, Class<? extends TileEntity> tileClass, IProperty... ignoredProperties)
    {
        event.getRegistry().register(block);
        if (tileClass != null)
            GameRegistry.registerTileEntity(tileClass, block.getRegistryName());
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
        return null;
    }

    public <T extends TileEntity> void registerTileRenderer(Class<T> tileClass, TileEntitySpecialRenderer<? super T> tesr)
    {
        
    }
}
