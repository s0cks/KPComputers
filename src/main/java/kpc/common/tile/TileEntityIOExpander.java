package kpc.common.tile;

import kpc.api.computer.Computer;
import kpc.api.driver.Driver;
import kpc.stdlib.Colors;
import mrtjp.projectred.api.IBundledTile;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public final class TileEntityIOExpander
extends TileEntity
implements Driver,
           IBundledTile{
    private final byte[] signals = new byte[Colors.MAX];

    @Override
    public void readFromNBT(NBTTagCompound comp){
        super.readFromNBT(comp);
        for(int i = 0; i < Colors.MAX; i++){
            this.signals[i] = comp.getByte("signal_" + i);
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound comp){
        super.writeToNBT(comp);
        for(int i = 0; i < Colors.MAX; i++){
            comp.setByte("signal_" + i, this.signals[i]);
        }
    }

    @Override
    public String getType() {
        return "ioExpander";
    }

    @Override
    public String[] getMethods() {
        return new String[]{
            "set", "get"
        };
    }

    @Override
    public Object invoke(String name, Object... args) {
        switch(name){
            case "set":{
                int slot = Integer.valueOf(args[0].toString());
                this.signals[slot] = (this.signals[slot] == 255 ? 0 : (byte) 255);
                this.worldObj.notifyBlockChange(this.xCoord, this.yCoord, this.zCoord, worldObj.getBlock(this.xCoord, this.yCoord, this.zCoord));
                return slot;
            }
            case "get":{
                int slot = Integer.valueOf(args[0].toString());
                return this.signals[slot];
            }
        }
        return null;
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound tag = new NBTTagCompound();
        writeToNBT(tag);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 0, tag);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        this.readFromNBT(pkt.func_148857_g());
    }

    @Override
    public void onConnect(Computer computer) {

    }

    @Override
    public void onDisconnect(Computer computer) {

    }

    @Override
    public byte[] getBundledSignal(int dir) {
        return this.signals;
    }

    @Override
    public boolean canConnectBundled(int side) {
        return true;
    }
}