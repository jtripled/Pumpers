package com.jtripled.pumpers.block;

import com.jtripled.pumpers.container.ContainerTank;
import com.jtripled.pumpers.gui.GUITank;
import com.jtripled.pumpers.render.TESRTank;
import com.jtripled.pumpers.tile.TileTank;
import com.jtripled.voxen.block.BlockBase;
import com.jtripled.voxen.block.IBlockConnectable;
import com.jtripled.voxen.gui.GUIHolder;
import com.jtripled.voxen.util.Tab;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

/**
 *
 * @author jtripled
 */
public class BlockTank extends BlockBase implements IBlockConnectable.Vertical, GUIHolder
{
    public BlockTank()
    {
        super("tank", Material.IRON);
        this.setTab(Tab.REDSTONE);
        this.setItem();
        this.setTileClass(TileTank.class);
        this.setTESRClass(TESRTank.class);
        this.setOpaque(false);
        this.setFullCube(false);
        this.setRenderLayer(BlockRenderLayer.CUTOUT_MIPPED);
        this.setTopSolid(true);
    }

    @Override
    public boolean canConnect(BlockPos pos, IBlockState state, IBlockAccess world, BlockPos otherPos, IBlockState otherState, EnumFacing face)
    {
        return otherState.getBlock() instanceof BlockTank;
    }
    
    @Override
    public ContainerTank getServerGUI(EntityPlayer player, World world, BlockPos pos)
    {
        return new ContainerTank(((TileTank) world.getTileEntity(pos)).getBaseTank(), player.inventory);
    }
    
    @Override
    public GUITank getClientGUI(EntityPlayer player, World world, BlockPos pos)
    {
        return new GUITank(getServerGUI(player, world, pos));
    }
    
    @Override
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }
    
    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState state)
    {
        TileTank baseTank = null;
        TileTank newTank = (TileTank) world.getTileEntity(pos);
        TileEntity tileUp = world.getTileEntity(pos.up());
        TileEntity tileDown = world.getTileEntity(pos.down());
        boolean up = tileUp instanceof TileTank;
        boolean down = tileDown instanceof TileTank;
        
        // Merge Tanks
        if (up && down)
        {
            baseTank = newTank.getBaseTank();
            TileTank aboveTank = (TileTank) tileUp;
            baseTank.setCapacity(baseTank.getCapacity() + aboveTank.getCapacity() + Fluid.BUCKET_VOLUME * 16);
            baseTank.fill(aboveTank.drain(Integer.MAX_VALUE, true), true);
            aboveTank.setCapacity(Fluid.BUCKET_VOLUME * 16);
            while (tileUp instanceof TileTank)
            {
                ((TileTank) tileUp).baseTank = null;
                tileUp = world.getTileEntity(tileUp.getPos().up());
            }
        }
        
        // Extend Down
        else if (up)
        {
            baseTank = newTank;
            TileTank aboveTank = (TileTank) tileUp;
            baseTank.setCapacity(aboveTank.getCapacity() + Fluid.BUCKET_VOLUME * 16);
            baseTank.fill(aboveTank.drain(Integer.MAX_VALUE, true), true);
            aboveTank.setCapacity(Fluid.BUCKET_VOLUME * 16);
            while (tileUp instanceof TileTank)
            {
                ((TileTank) tileUp).baseTank = null;
                tileUp = world.getTileEntity(tileUp.getPos().up());
            }
        }
        
        // Extend Up
        else if (down)
        {
            baseTank = newTank.getBaseTank();
            baseTank.setCapacity(baseTank.getCapacity() + Fluid.BUCKET_VOLUME * 16);
        }
    }
    
    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state)
    {
        TileTank baseTank = null;
        TileTank lostTank = (TileTank) world.getTileEntity(pos);
        TileEntity tileUp = world.getTileEntity(pos.up());
        TileEntity tileDown = world.getTileEntity(pos.down());
        boolean up = tileUp instanceof TileTank;
        boolean down = tileDown instanceof TileTank;
        
        // Split Tanks
        if (up && down)
        {
            baseTank = lostTank.getBaseTank();
            int oldCapacity = baseTank.getCapacity();
            int newCapacity = (lostTank.getPos().getY() - baseTank.getPos().getY()) * Fluid.BUCKET_VOLUME * 16;
            TileTank aboveTank = (TileTank) tileUp;
            FluidStack drained = baseTank.drain(baseTank.drain(Integer.MAX_VALUE, false).amount - newCapacity, true);
            baseTank.setCapacity(newCapacity);
            aboveTank.setCapacity(oldCapacity - newCapacity - Fluid.BUCKET_VOLUME * 16);
            aboveTank.fill(drained, true);
            while (tileUp instanceof TileTank)
            {
                ((TileTank) tileUp).baseTank = null;
                tileUp = world.getTileEntity(tileUp.getPos().up());
            }
        }
        
        // Shrink Bottom
        else if (up)
        {
            baseTank = (TileTank) tileUp;
            baseTank.setCapacity(lostTank.getCapacity() - Fluid.BUCKET_VOLUME * 16);
            baseTank.fill(lostTank.drain(Integer.MAX_VALUE, true), true);
            while (tileUp instanceof TileTank)
            {
                ((TileTank) tileUp).baseTank = null;
                tileUp = world.getTileEntity(tileUp.getPos().up());
            }
        }
        
        // Shrink Top
        else if (down)
        {
            baseTank = lostTank.getBaseTank();
            baseTank.setCapacity(baseTank.getCapacity() - Fluid.BUCKET_VOLUME * 16);
        }
        
        super.breakBlock(world, pos, state);
    }
}
