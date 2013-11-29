package net.javaforge.blog.sshd;

import org.apache.sshd.server.PasswordAuthenticator;
import org.apache.sshd.server.session.ServerSession;

/**
 * @author Maxim Kalina
 * @version $Id$
 */
public class InAppPasswordAuthenticator implements PasswordAuthenticator {
    @Override
    public boolean authenticate(String username, String password, ServerSession session) {
        return username != null && username.equals(password);
    }
}
