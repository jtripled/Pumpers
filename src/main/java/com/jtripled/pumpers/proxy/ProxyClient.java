package com.jtripled.pumpers.proxy;

import com.jtripled.pumpers.container.ContainerFluidDuct;
import com.jtripled.pumpers.container.ContainerPump;
import com.jtripled.pumpers.container.ContainerTank;
import com.jtripled.pumpers.gui.GUIFluidDuct;
import com.jtripled.pumpers.gui.GUIPump;
import com.jtripled.pumpers.gui.GUITank;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;

/**
 *
 * @author jtripled
 */
public class ProxyClient extends Proxy
{
    @Override
    public String localize(String unlocalized, Object... args)
    {
        return net.minecraft.client.resources.I18n.format(unlocalized, args);
    }
    
    @Override
    public void registerItem(RegistryEvent.Register<Item> event, Item item)
    {
        super.registerItem(event, item);
        ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "normal"));
    }
    
    @Override
    public void registerBlock(RegistryEvent.Register<Block> event, Block block, Class<? extends TileEntity> tileClass, IProperty... ignoredProperties)
    {
        super.registerBlock(event, block, tileClass, ignoredProperties);
        if (ignoredProperties.length > 0)
            ModelLoader.setCustomStateMapper((Block) block, (new StateMap.Builder()).ignore(ignoredProperties).build());
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

    @Override
    public <T extends TileEntity> void registerTileRenderer(Class<T> tileClass, TileEntitySpecialRenderer<? super T> tesr)
    {
        ClientRegistry.bindTileEntitySpecialRenderer(tileClass, tesr);
    }
}
