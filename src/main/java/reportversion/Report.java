package reportversion;

import utils.CryptoUtils;
import utils.FileUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import com.google.gson.Gson;


public class Report {

    private String name;
    private String path;
    private String includeRegex;
    private String excludeRegex;
    private Date createdAt;
    private List<FileValidator> files;

    public Report(String name, String path, String includeRegex, String excludeRegex, Date createdAt) {
        this.name = name;
        this.path = path;
        this.includeRegex = includeRegex;
        this.excludeRegex = excludeRegex;
        this.createdAt = (createdAt == null) ? new Date() : createdAt;
    }

    public Report(String name, String path, String includeRegex, String excludeRegex){
        this(name, path, includeRegex, excludeRegex, null);
    }

    public void generate() throws IOException, NoSuchAlgorithmException {
        this.files = new ArrayList<>();
        List<Path> paths = FileUtils.listAllInDirectory(this.path,
                Optional.ofNullable(this.includeRegex),
                Optional.ofNullable(this.excludeRegex));
        for (Path pathFound : paths) {
            files.add(new FileValidator(pathFound));
        }
    }

    public void checkFiles() throws IOException, NoSuchAlgorithmException {
        for (FileValidator fileValidator : this.files) {
            fileValidator.check();
        }
    }

    public boolean isValid() {
        return this.files.stream().allMatch(file -> file.getValidate());
    }

    public String toString() {
        StringBuilder string = new StringBuilder();
        string.append(String.format("Report : %s\n", this.name));
        string.append(String.format("Created at : %s\n", this.createdAt));
        string.append(String.format("Path: %s\n", this.path));
        string.append(String.format("IncludeRegex: %s\n", this.includeRegex));
        string.append(String.format("ExcludeRegex: %s\n", this.excludeRegex));
        string.append(String.format("Files:\n"));
        for (FileValidator fileValidator : this.files) {
            string.append(String.format("[%b] %s - %s\n", fileValidator.getValidate(), fileValidator.getPath(), fileValidator.getHash()));
        }
        string.append("\n\n");
        string.append(String.format("Report validity : %b", this.isValid()));
        return string.toString();
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public static Report fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, Report.class);
    }

    public String cryptedJson() throws Exception {
        return CryptoUtils.crypt(this.toJson());
    }

    public static Report decryptedJson(String cryptedJson) throws Exception {
        return Report.fromJson(CryptoUtils.decrypt(cryptedJson));
    }
}
