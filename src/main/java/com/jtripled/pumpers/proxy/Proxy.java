package com.jtripled.pumpers.proxy;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;

/**
 *
 * @author jtripled
 */
public class Proxy
{
    public String localize(String unlocalized, Object... args)
    {
        return null;
    }
    
    public void registerIgnoredProperties(Block block, IProperty... properties)
    {
        
    }
    
    public void registerItemRenderer(Item item, String variant)
    {
        
    }

    public <T extends TileEntity> void registerTileRenderer(Class<T> tileClass, TileEntitySpecialRenderer<? super T> tesr)
    {
        
    }
}
