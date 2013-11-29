package net.javaforge.blog.sshd;

import org.apache.sshd.SshServer;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;

public class App {
    public static void main(String[] args) throws Throwable {
        SshServer sshd = SshServer.setUpDefaultServer();
        sshd.setPort(5222);
        sshd.setKeyPairProvider(new SimpleGeneratorHostKeyProvider("hostkey.ser"));
        sshd.setPasswordAuthenticator(new InAppPasswordAuthenticator());
        sshd.setShellFactory(new InAppShellFactory());
        sshd.start();
    }
}
