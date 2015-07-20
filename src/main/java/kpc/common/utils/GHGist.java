package kpc.common.utils;

import com.google.gson.Gson;
import kpc.api.computer.Computer;
import kpc.api.computer.Terminal;
import kpc.api.fs.FileSystem;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Map;

public final class GHGist{
    private static final Gson GSON = new Gson();
    public static final String GH_API = "https://api.github.com";

    public final String id;
    public final Map<String, GHGistFile> files;

    public static GHGist of(String id){
        try(java.io.InputStream in = new URL(GH_API + "/gists/" + id).openStream()){
            return GSON.fromJson(new InputStreamReader(in), GHGist.class);
        } catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    private GHGist(String id, Map<String, GHGistFile> files){
        this.id = id;
        this.files = files;
    }

    public GHGistIndex index(){
        GHGistFile indexFile = this.files.get("index.json");
        try(InputStream in = new URL(indexFile.raw_url).openStream()){
            return GSON.fromJson(new InputStreamReader(in), GHGistIndex.class);
        } catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    public void install(Computer computer){
        this.index().install(computer.terminal(), computer.fs(), this);
    }

    public static final class GHGistIndex{
        private final Map<String, String> install;
        private final String name;

        public GHGistIndex(Map<String, String> install, String name){
            this.install = install;
            this.name = name;
        }

        public void install(Terminal terminal, FileSystem fs, GHGist gist){
            terminal.write("Installing Package: " + this.name);
            terminal.setCursorPos(1, terminal.getCursorY() + 1);
            int total = 0;
            int count = 0;
            for(Map.Entry<String, String> entry : this.install.entrySet()){
                String path = entry.getValue() + "/" + entry.getKey();

                terminal.write("Installing: " + entry.getKey() + " -> " + path + " (" + gist.files.get(entry.getKey()).size + "b)");
                terminal.setCursorPos(1, terminal.getCursorY() + 1);
                total += gist.files.get(entry.getKey()).size;
                count += 1;

                try(java.io.OutputStream os = fs.openOutputStream(path).toOutputStream();
                    InputStream in = new URL(gist.files.get(entry.getKey()).raw_url).openStream()){

                    int len;
                    byte[] buffer = new byte[8192];
                    while((len = in.read(buffer, 0, 8192)) != -1){
                        os.write(buffer, 0, len);
                    }
                } catch(Exception e){
                    throw new RuntimeException(e);
                }
            }
            terminal.write("Installed " + count + " files (" + total + "b)");
            terminal.setCursorPos(1, terminal.getCursorY() + 1);
        }
    }

    public static final class GHGistFile{
        public final long size;
        public final String raw_url;

        private GHGistFile(long size, String rawUrl) {
            this.size = size;
            this.raw_url = rawUrl;
        }
    }
}