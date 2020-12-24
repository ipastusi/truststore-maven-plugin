package uk.co.automatictester.truststore.maven.plugin.mojo;

public class Truststore {

    private String file;
    private String password;

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
