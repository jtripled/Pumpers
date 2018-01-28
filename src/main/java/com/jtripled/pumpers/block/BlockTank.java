package com.jtripled.pumpers.block;

import com.jtripled.pumpers.Pumpers;
import com.jtripled.pumpers.container.ContainerTank;
import com.jtripled.pumpers.gui.GUITank;
import com.jtripled.pumpers.network.*;
import com.jtripled.pumpers.render.TESRTank;
import com.jtripled.pumpers.tile.TileTank;
import com.jtripled.voxen.block.BlockBase;
import com.jtripled.voxen.gui.GUIHolder;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

/**
 *
 * @author jtripled
 */
public class BlockTank extends BlockBase implements GUIHolder
{
    public static final PropertyBool UP = PropertyBool.create("up");
    public static final PropertyBool DOWN = PropertyBool.create("down");
    
    public BlockTank()
    {
        super(Pumpers.INSTANCE, "tank", Material.IRON);
        this.setTab(CreativeTabs.REDSTONE);
        this.setItem();
        this.setDefaultState(this.blockState.getBaseState().withProperty(UP, false).withProperty(DOWN, false));
        this.setTileClass(TileTank.class);
        this.setTESRClass(TESRTank.class);
        this.setOpaque(false);
        this.setFullCube(false);
        this.setRenderLayer(BlockRenderLayer.CUTOUT_MIPPED);
    }
    
    /*
     * Block rendering and behaviour.
     */

    @Override
    public boolean isTopSolid(IBlockState state)
    {
        return true;
    }
    
    @Override
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }
    
    /*
     * Block state.
     */

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {DOWN, UP});
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return 0;
    }
    
    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        return state.withProperty(UP, world.getTileEntity(pos.up()) instanceof TileTank)
                .withProperty(DOWN, world.getTileEntity(pos.down()) instanceof TileTank);
    }
    
    /*
     * Registration.
     */
    
    @Override
    public ContainerTank getServerGUI(EntityPlayer player, World world, BlockPos pos)
    {
        return new ContainerTank((TileTank) world.getTileEntity(pos), player.inventory);
    }
    
    @Override
    public GUITank getClientGUI(EntityPlayer player, World world, BlockPos pos)
    {
        return new GUITank(getServerGUI(player, world, pos));
    }
    
    /*
     * Updating neighboring tanks.
     */
    
    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state)
    {
        FluidTank lostTank = ((TileTank) world.getTileEntity(pos)).tank;
        super.breakBlock(world, pos, state);
        TileEntity tileUp = world.getTileEntity(pos.up());
        TileEntity tileDown = world.getTileEntity(pos.down());
        boolean up = tileUp != null && tileUp instanceof TileTank;
        boolean down = tileDown != null && tileDown instanceof TileTank;
        if (up && down)
        {
            // Split Tank
            TileTank baseTank = (TileTank) tileUp;
            baseTank.baseTank = baseTank;
            baseTank.tank = new FluidTank(16000);
            BlockPos next = baseTank.getPos().up();
            TileEntity test = world.getTileEntity(next);
            TileTank tank;
            while (test != null && test instanceof TileTank)
            {
                tank = (TileTank) test;
                tank.baseTank = null;
                baseTank.tank.setCapacity(baseTank.tank.getCapacity() + 16000);
                next = next.up();
                test = world.getTileEntity(next);
            }
            if (!world.isRemote)
            {
                Pumpers.INSTANCE.getNetwork().sendToAll(new TankResizeMessage(baseTank.getPos(), baseTank.tank.getCapacity()));
            }
            baseTank = ((TileTank) tileDown).getBaseTank();
            baseTank.baseTank = baseTank;
            baseTank.tank.setCapacity(16000);
            next = baseTank.getPos().up();
            test = world.getTileEntity(next);
            while (test != null && test instanceof TileTank)
            {
                tank = (TileTank) test;
                tank.baseTank = null;
                baseTank.tank.setCapacity(baseTank.tank.getCapacity() + 16000);
                next = next.up();
                test = world.getTileEntity(next);
            }
            int toDrain = baseTank.tank.getFluidAmount() - baseTank.tank.getCapacity();
            if (toDrain > 0)
                ((TileTank) tileUp).fill(baseTank.drain(toDrain, true), true);
            if (!world.isRemote)
            {
                Pumpers.INSTANCE.getNetwork().sendToAll(new TankResizeMessage(baseTank.getPos(), baseTank.tank.getCapacity()));
                Pumpers.INSTANCE.getNetwork().sendToAll(new FluidMessage(baseTank.getPos(), baseTank.tank.getFluid()));
            }
        }
        else if (up)
        {
            // Shrink Bottom
            TileTank baseTank = (TileTank) tileUp;
            baseTank.baseTank = baseTank;
            baseTank.tank = lostTank;
            baseTank.tank.setCapacity(baseTank.tank.getCapacity() - 16000);
            int toDrain = baseTank.tank.getFluidAmount() - baseTank.tank.getCapacity();
            if (toDrain > 0)
                baseTank.drain(toDrain, true);
            BlockPos next = baseTank.getPos().up();
            TileEntity test = world.getTileEntity(next);
            TileTank tank;
            while (test != null && test instanceof TileTank)
            {
                tank = (TileTank) test;
                tank.baseTank = null;
                next = next.up();
                test = world.getTileEntity(next);
            }
            if (!world.isRemote)
            {
                Pumpers.INSTANCE.getNetwork().sendToAll(new TankResizeMessage(baseTank.getPos(), baseTank.tank.getCapacity()));
                Pumpers.INSTANCE.getNetwork().sendToAll(new FluidMessage(baseTank.getPos(), baseTank.tank.getFluid()));
            }
        }
        else if (down)
        {
            // Shrink Top
            TileTank baseTank = ((TileTank) tileDown).getBaseTank();
            baseTank.tank.setCapacity(baseTank.tank.getCapacity() - 16000);
            int toDrain = baseTank.tank.getFluidAmount() - baseTank.tank.getCapacity();
            if (toDrain > 0)
                baseTank.drain(toDrain, true);
            if (!world.isRemote)
            {
                Pumpers.INSTANCE.getNetwork().sendToAll(new TankResizeMessage(baseTank.getPos(), baseTank.tank.getCapacity()));
            }
        }
    }

    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState state)
    {
        TileEntity tileUp = world.getTileEntity(pos.up());
        TileEntity tileDown = world.getTileEntity(pos.down());
        boolean up = tileUp != null && tileUp instanceof TileTank;
        boolean down = tileDown != null && tileDown instanceof TileTank;
        if (up && down)
        {
            // Merge Tank
            TileTank mergeTank = (TileTank) world.getTileEntity(pos);
            TileTank baseTank = mergeTank.getBaseTank();
            TileTank upTank = (TileTank) tileUp;
            BlockPos next = pos;
            TileEntity test = world.getTileEntity(next);
            TileTank tank;
            FluidStack out = upTank.tank.drain(upTank.tank.getFluidAmount(), true);
            while (test != null && test instanceof TileTank)
            {
                tank = (TileTank) test;
                tank.baseTank = null;
                baseTank.tank.setCapacity(baseTank.tank.getCapacity() + 16000);
                next = next.up();
                test = world.getTileEntity(next);
            }
            if (out != null)
                baseTank.fill(out, true);
            if (!world.isRemote)
            {
                Pumpers.INSTANCE.getNetwork().sendToAll(new TankResizeMessage(baseTank.getPos(), baseTank.tank.getCapacity()));
                Pumpers.INSTANCE.getNetwork().sendToAll(new FluidMessage(baseTank.getPos(), baseTank.tank.getFluid()));
            }
        }
        else if (up)
        {
            // Extend Down
            TileTank baseTank = (TileTank) world.getTileEntity(pos);
            TileTank formerBaseTank = (TileTank) tileUp;
            baseTank.tank = formerBaseTank.tank;
            formerBaseTank.tank = new FluidTank(16000);
            baseTank.tank.setCapacity(baseTank.tank.getCapacity() + 16000);
            BlockPos next = formerBaseTank.getPos();
            TileEntity test = world.getTileEntity(next);
            TileTank tank;
            while (test != null && test instanceof TileTank)
            {
                tank = (TileTank) test;
                tank.baseTank = null;
                next = next.up();
                test = world.getTileEntity(next);
            }
            if (!world.isRemote)
            {
                Pumpers.INSTANCE.getNetwork().sendToAll(new TankResizeMessage(baseTank.getPos(), baseTank.tank.getCapacity()));
                Pumpers.INSTANCE.getNetwork().sendToAll(new FluidMessage(formerBaseTank.getPos(), formerBaseTank.tank.getFluid()));
                Pumpers.INSTANCE.getNetwork().sendToAll(new FluidMessage(baseTank.getPos(), baseTank.tank.getFluid()));
            }
        }
        else if (down)
        {
            // Extend Up
            TileTank baseTank = ((TileTank) world.getTileEntity(pos)).getBaseTank();
            baseTank.tank.setCapacity(baseTank.tank.getCapacity() + 16000);
            if (!world.isRemote)
            {
                Pumpers.INSTANCE.getNetwork().sendToAll(new TankResizeMessage(baseTank.getPos(), baseTank.tank.getCapacity()));
            }
        }
        else
        {
            TileTank tile = (TileTank) world.getTileEntity(pos);
            tile.baseTank = tile;
        }
    }
}
