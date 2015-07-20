package kpc.common.computer;

import kawa.standard.Scheme;
import kpc.api.ComputerPosition;
import kpc.api.Signal;
import kpc.api.State;
import kpc.api.fs.FileSystem;
import kpc.api.fs.io.InputStream;
import kpc.common.computer.api.FileSystemApi;
import kpc.common.computer.api.PkgApi;
import kpc.common.computer.api.PrologApi;
import kpc.common.computer.api.TerminalApi;
import kpc.common.computer.fs.Ext9001FileSystem;
import kpc.common.kawa.get;
import kpc.common.kawa.gets;
import kpc.common.kawa.include;
import kpc.common.kawa.print;
import kpc.common.kawa.println;
import kpc.common.kawa.strings;
import kpc.common.utils.INBTTaggable;
import kpc.common.utils.SchemeFactory;
import net.minecraft.nbt.NBTTagCompound;

import java.io.InputStreamReader;
import java.util.concurrent.ForkJoinPool;

public final class Computer
implements kpc.api.computer.Computer,
           INBTTaggable{
    protected final ForkJoinPool threadPool;
    protected final OperatingSystem os;
    protected final Terminal terminal;
    protected final Scheme scheme;
    protected final FileSystem fs;
    protected final ComputerPosition pos;
    private State state = State.OFF;

    public Computer(ComputerPosition pos, Terminal terminal){
        this.terminal = terminal;
        this.pos = pos;
        this.fs = new Ext9001FileSystem();
        this.os = new OperatingSystem(this);
        this.threadPool = new ForkJoinPool();
        this.scheme = SchemeFactory.create();

        this.scheme.define("fs", new FileSystemApi(this.fs));
        this.scheme.define("os", this.os);
        this.scheme.define("term", new TerminalApi(this.terminal));
        this.scheme.define("include", new include(this.fs));
        this.scheme.define("prolog", new PrologApi(this.fs));
        this.scheme.define("str", new strings());
        this.scheme.define("pkg", new PkgApi(this));

        this.scheme.defineFunction("print", new print(this.terminal));
        this.scheme.defineFunction("println", new println(this.terminal));
        this.scheme.defineFunction("gets", new gets(this.os, this.terminal));
        this.scheme.defineFunction("get", new get(this.os, this.terminal));
    }

    @Override
    public State state() {
        return this.state;
    }

    @Override
    public int id() {
        return -1;
    }

    @Override
    public void turnOn(){
        if(this.state != State.RUNNING){
            this.state = State.RUNNING;
            this.terminal.clear();
            this.terminal.setCursorPos(1, 1);
            this.terminal.write("Loading Bios.scm");
            this.threadPool.execute(
                                           new Runnable() {
                                               @Override
                                               public void run() {
                                                   init();
                                               }
                                           }
            );
        }
    }

    @Override
    public void shutdown() {
        this.state = State.OFF;
    }

    @Override
    public void reboot() {
        this.state = State.OFF;
        this.turnOn();
    }

    public void queueEvent(final String evnt, final Object... args){
        this.threadPool.execute(
                                  new Runnable() {
                                      @Override
                                      public void run() {
                                          synchronized (this) {
                                              Signal signal = new BasicSignal(evnt, args);
                                              synchronized (os){
                                                  os.signal(signal);
                                              }
                                          }
                                      }
                                  }
        );
    }

    @Override
    public kpc.api.computer.Terminal terminal() {
        return this.terminal;
    }

    @Override
    public kpc.api.computer.OperatingSystem os() {
        return this.os;
    }

    @Override
    public Scheme scheme() {
        return this.scheme;
    }

    @Override
    public FileSystem fs() {
        return this.fs;
    }

    @Override
    public ComputerPosition pos() {
        return this.pos;
    }

    private void init(){
        try(InputStream stream = this.fs.openInputStream("/bios.scm")){
            if(stream != null){
                this.scheme.eval(new InputStreamReader(stream.toInputStream()));
            } else{
                throw new NullPointerException("Couldn't resolve bios.scm");
            }
        } catch(Throwable t){
            this.terminal.clear();
            this.terminal.setCursorPos(1, 1);
            this.terminal.write("Error loading bios.scm");
            this.terminal.setCursorPos(1, 2);
            this.terminal.write("Message: " + t.getMessage());
            t.printStackTrace(System.err);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound comp) {
        NBTTagCompound c = comp.getCompoundTag("terminal");
        this.terminal.readFromNBT(c);
        this.state = State.valueOf(comp.getString("state") != null ? comp.getString("state") : "OFF");
    }

    @Override
    public void writeToNBT(NBTTagCompound comp) {
        NBTTagCompound c = new NBTTagCompound();
        this.terminal.writeToNBT(c);
        comp.setTag("terminal", c);
        comp.setString("state", this.state.name());
    }
}