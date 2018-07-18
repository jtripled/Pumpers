package com.jtripled.pumpers.block;

import com.jtripled.pumpers.container.ContainerPump;
import com.jtripled.pumpers.gui.GUIPump;
import com.jtripled.pumpers.tile.TilePump;
import com.jtripled.voxen.block.BlockBase;
import com.jtripled.voxen.block.IBlockEnableable;
import com.jtripled.voxen.block.IBlockFaceable;
import com.jtripled.voxen.gui.GUIHolder;
import com.jtripled.voxen.util.Tab;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 *
 * @author jtripled
 */
public class BlockPump extends BlockBase implements IBlockFaceable.NoUp, IBlockEnableable, GUIHolder
{
    public BlockPump()
    {
        super("pump", Material.IRON);
        this.setTab(Tab.REDSTONE);
        this.setItem();
        this.setTileClass(TilePump.class);
        this.setFullCube(false);
        this.setOpaque(false);
        this.setRenderLayer(BlockRenderLayer.CUTOUT_MIPPED);
        this.setTopSolid(true);
        this.setRenderSides(true);
    }
    
    @Override
    public ContainerPump getServerGUI(EntityPlayer player, World world, BlockPos pos)
    {
        return new ContainerPump((TilePump) world.getTileEntity(pos), player.inventory);
    }
    
    @Override
    public GUIPump getClientGUI(EntityPlayer player, World world, BlockPos pos)
    {
        return new GUIPump(getServerGUI(player, world, pos));
    }
}
