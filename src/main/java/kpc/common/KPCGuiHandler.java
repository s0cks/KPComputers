package kpc.common;

import cpw.mods.fml.common.network.IGuiHandler;
import kpc.client.gui.GuiComputer;
import kpc.common.container.ContainerComputer;
import kpc.common.tile.TileEntityComputer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public final class KPCGuiHandler
implements IGuiHandler {
    public static final int GUI_TERMINAL = 0xA;

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch(ID){
            case GUI_TERMINAL: return new ContainerComputer();
            default: return null;
        }
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch(ID){
            case GUI_TERMINAL:{
                TileEntityComputer comp = (TileEntityComputer) world.getTileEntity(x, y, z);
                return new GuiComputer(comp);
            }
            default: return null;
        }
    }
}