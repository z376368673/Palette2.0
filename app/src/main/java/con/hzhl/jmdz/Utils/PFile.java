package con.hzhl.jmdz.Utils;

/**
 * Created by apple on 2018/4/26.
 */

public class PFile {

    private String name;
    private String path;

    public PFile(){};
    public PFile(String name, String path) {
        this.name = name;
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
