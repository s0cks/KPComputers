package kpc.common.computer.api;

import com.google.common.collect.ImmutableMap;
import kpc.api.computer.Computer;
import kpc.common.utils.GHGist;

import java.util.Map;

public final class PkgApi{
    private static final Map<String, PackageManager> pkgManagers = ImmutableMap.<String, PackageManager>builder()
            .put("gist", new GistPackageManager())
            .build();

    private final Computer computer;

    public PkgApi(Computer comp){
        this.computer = comp;
    }

    public Object install(String mode, String id){
        if(!pkgManagers.containsKey(mode)){
            return "Invalid Package Manager: " + mode;
        }

        pkgManagers.get(mode).install(this.computer, id);
        return null;
    }

    private static interface PackageManager{
        public void install(Computer comp, String id);
    }

    private static final class GistPackageManager
    implements PackageManager{
        @Override
        public void install(Computer comp, String id) {
            GHGist.of(id).install(comp);
        }
    }
}