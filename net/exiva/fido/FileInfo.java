
package net.exiva.fido;
import java.io.File;

public class FileInfo {
    private int fileID;
    private String fileURL;
    private File fileName;
    public FileInfo(int id, String url, String path) {
        fileID = id;
	fileURL = url;
	fileName = new File(path);
    }
    public int getID() {
        return fileID;
    }
	
    public String getURL() {
        return fileURL;
    }
	
    public File getFile() {
	return fileName;
    }
}
