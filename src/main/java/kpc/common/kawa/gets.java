package kpc.common.kawa;

import gnu.mapping.Procedure1;
import kpc.api.Signal;
import kpc.api.computer.OperatingSystem;
import kpc.api.computer.Terminal;

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;

public final class gets
extends Procedure1 {
    private final OperatingSystem os;
    private final Terminal terminal;

    public gets(OperatingSystem os, Terminal terminal){
        this.os = os;
        this.terminal = terminal;
    }

    @Override
    public Object apply1(Object arg)
    throws Throwable {
        Object c = this.get();
        if(c == null){
            return arg;
        } else if(c == -1){
            if(arg.toString().length() == 0){
                return this.apply1(arg.toString());
            } else{
                this.terminal.backspace();
                this.terminal.setCursorPos(this.terminal.getCursorX() - 1, this.terminal.getCursorY());
                return this.apply1(((String) arg).substring(0, ((String) arg).length() - 1));
            }
        } else if(c instanceof String){
            this.terminal.write(c.toString());
            this.terminal.setCursorPos(this.terminal.getCursorX() + c.toString().length(), this.terminal.getCursorY());
            return this.apply1(arg.toString() + c.toString());
        } else{
            this.terminal.write(String.valueOf(c));
            this.terminal.setCursorPos(this.terminal.getCursorX() + String.valueOf(c).length(), this.terminal.getCursorY());
            return this.apply1(arg.toString() + ((char) c));
        }
    }

    private Object get(){
        Signal signal = this.os.pull();
        if(signal.name().equals("char")){
            if(signal.args()[0].equals("__enter__")){
                return null;
            } else if(signal.args()[0].equals("__back__")){
                return -1;
            } else if(signal.args()[0].equals("__ctrl__")){
                return "";
            } else if(signal.args()[0].equals("__ctrlv__")){
                try{
                    return Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
                } catch (Exception e) {
                    return "";
                }
            } else{
                return ((String) signal.args()[0]).charAt(0);
            }
        } else{
            return null;
        }
    }
}