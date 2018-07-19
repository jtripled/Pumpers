package com.jtripled.pumpers.proxy;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.model.ModelLoader;
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
    public void registerIgnoredProperties(Block block, IProperty... properties)
    {
        ModelLoader.setCustomStateMapper((Block) block, (new StateMap.Builder()).ignore(properties).build());
    }
    
    @Override
    public void registerItemRenderer(Item item, String variant)
    {
        ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), variant));
    }

    @Override
    public <T extends TileEntity> void registerTileRenderer(Class<T> tileClass, TileEntitySpecialRenderer<? super T> tesr)
    {
        ClientRegistry.bindTileEntitySpecialRenderer(tileClass, tesr);
    }
}
