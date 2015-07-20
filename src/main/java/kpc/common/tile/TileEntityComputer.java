package kpc.common.tile;

import kpc.api.ComputerPosition;
import kpc.api.computer.Computer;
import kpc.common.KPComputers;
import kpc.common.KPGuiHandler;
import kpc.common.computer.ClientComputer;
import kpc.common.computer.ServerComputer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public final class TileEntityComputer
extends TileEntity{
    private int id;

    @Override
    public void writeToNBT(NBTTagCompound comp){
        super.writeToNBT(comp);
        comp.setInteger("compId", this.id);
        ServerComputer computer = this.getServerComputer();
        if(computer != null){
            computer.writeToNBT(comp);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound comp){
        super.readFromNBT(comp);
        this.id = comp.getInteger("compId");
        ServerComputer computer = this.getServerComputer();
        if(computer != null){
            computer.readFromNBT(comp);
        }
    }

    public void interact(EntityPlayer player){
        if(!player.worldObj.isRemote){
            this.createServerComputer().turnOn();
            player.openGui(KPComputers.instance, KPGuiHandler.GUI_TERMINAL, this.worldObj, this.xCoord, this.yCoord, this.zCoord);
        }
    }

    public Computer getComputer(){
        if(this.worldObj.isRemote){
            return this.getClientComputer();
        }

        return this.getServerComputer();
    }

    public ServerComputer getServerComputer(){
        return KPComputers.serverComputerRegistry.get(this.id);
    }

    public ClientComputer getClientComputer(){
        return KPComputers.clientComputerRegistry.get(this.id);
    }

    public Computer createComputer(){
        if(this.worldObj.isRemote){
            return this.createClientComputer();
        }

        return this.createServerComputer();
    }

    public ClientComputer createClientComputer(){
        if(this.worldObj.isRemote){
            if(this.id >= 0){
                if(!KPComputers.clientComputerRegistry.contains(this.id)){
                    KPComputers.clientComputerRegistry.register(this.id, new ClientComputer(new ComputerPosition(this.worldObj, this.xCoord, this.yCoord, this.zCoord), this.id));
                }

                return KPComputers.clientComputerRegistry.get(this.id);
            }
        }

        return null;
    }

    public ServerComputer createServerComputer(){
        if(!this.worldObj.isRemote){
            boolean changed = false;
            if(this.id <= 0){
                this.id = KPComputers.serverComputerRegistry.nextId();
                changed = true;
            }

            if(!KPComputers.serverComputerRegistry.contains(this.id)){
                ServerComputer computer = new ServerComputer(new ComputerPosition(this.worldObj, this.xCoord, this.yCoord, this.zCoord), this.id);
                KPComputers.serverComputerRegistry.register(this.id, computer);
                changed = true;
            }

            if(changed){
                this.updateBlock();
            }

            return KPComputers.serverComputerRegistry.get(this.id);
        }

        return null;
    }

    public void updateBlock(){
        this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
        this.worldObj.markTileEntityChunkModified(this.xCoord, this.yCoord, this.zCoord, this);
    }

    public int id(){
        return this.id;
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
}