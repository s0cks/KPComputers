package kpc.common.computer;

import kawa.standard.Scheme;
import kpc.api.ComputerPosition;
import kpc.api.State;
import kpc.api.computer.OperatingSystem;
import kpc.api.fs.FileSystem;
import kpc.common.KPComputers;
import kpc.common.net.KPCPacket;
import kpc.common.utils.INBTTaggable;
import kpc.common.utils.NBTUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;

public final class ServerComputer
implements kpc.api.computer.Computer,
           INBTTaggable{
    private final int id;
    private final Terminal terminal;
    private final Computer computer;
    private final ComputerPosition pos;

    public ServerComputer(ComputerPosition pos, int id){
        this.id = id;
        this.terminal = new Terminal();
        this.computer = new Computer(pos, this.terminal);
        this.pos = pos;
    }

    @Override
    public State state() {
        return this.computer.state();
    }

    @Override
    public int id() {
        return this.id;
    }

    @Override
    public void turnOn(){
        this.computer.turnOn();
    }

    @Override
    public void shutdown(){
        this.computer.shutdown();
    }

    @Override
    public void reboot(){
    }

    @Override
    public void queueEvent(String event, Object... params) {
        this.computer.queueEvent(event, params);
    }

    @Override
    public Terminal terminal() {
        return this.terminal;
    }

    @Override
    public OperatingSystem os() {
        return this.computer.os;
    }

    @Override
    public Scheme scheme() {
        return this.computer.scheme;
    }

    @Override
    public FileSystem fs() {
        return this.computer.fs;
    }

    @Override
    public ComputerPosition pos() {
        return this.pos;
    }

    public void broadcastState(){
        KPCPacket packet = new KPCPacket();
        packet.id = KPCPacket.PACKET_COMPUPDATE;
        packet.dataInt = new int[]{this.id};
        packet.dataNBT = new NBTTagCompound();
        this.writeDescription(packet.dataNBT);
        KPComputers.channel.sendToAll(KPComputers.encode(packet));
    }

    public void sendState(EntityPlayer player){
        KPCPacket packet = new KPCPacket();
        packet.id = KPCPacket.PACKET_COMPUPDATE;
        packet.dataInt = new int[]{this.id};
        packet.dataNBT = new NBTTagCompound();
        this.writeDescription(packet.dataNBT);
        KPComputers.channel.sendTo(KPComputers.encode(packet), (EntityPlayerMP) player);
    }

    public void writeDescription(NBTTagCompound comp) {
        NBTTagCompound termComp = new NBTTagCompound();
        termComp.setInteger("width", this.terminal.width);
        termComp.setInteger("height", this.terminal.height);
        this.terminal.writeToNBT(termComp);
        comp.setTag("terminal", termComp);
    }

    public void broadcastDelete(){
        KPCPacket packet = new KPCPacket();
        packet.id = KPCPacket.PACKET_COMPDELETED;
        packet.dataInt = new int[]{this.id};
        KPComputers.channel.sendToAll(KPComputers.encode(packet));
    }

    public void handlePacket(KPCPacket packet, EntityPlayer sender){
        switch(packet.id){
            case KPCPacket.PACKET_TURNON:{
                this.turnOn();
                break;
            }
            case KPCPacket.PACKET_REBOOT:{
                this.reboot();
                break;
            }
            case KPCPacket.PACKET_SHUTDOWN:{
                this.shutdown();
                break;
            }
            case KPCPacket.PACKET_REQUESTUPDATE:{
                this.sendState(sender);
                break;
            }
            case KPCPacket.PACKET_QUEUEEVENT:{
                String event = packet.dataString[0];
                Object[] args = null;
                if(packet.dataNBT != null){
                    args = NBTUtils.decode(packet.dataNBT);
                }
                this.queueEvent(event, args);
                break;
            }
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound comp) {
        this.computer.readFromNBT(comp);
    }

    @Override
    public void writeToNBT(NBTTagCompound comp) {
        this.computer.writeToNBT(comp);
    }
}