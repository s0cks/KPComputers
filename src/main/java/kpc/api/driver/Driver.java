package kpc.api.driver;

import kpc.api.computer.Computer;

public interface Driver {
    public String getType();
    public String[] getMethods();
    public Object invoke(String name, Object... args);
    public void onConnect(Computer computer);
    public void onDisconnect(Computer computer);
}