package net.javaforge.blog.sshd;

import jline.console.ConsoleReader;
import jline.console.completer.StringsCompleter;
import org.apache.sshd.common.Factory;
import org.apache.sshd.server.Command;
import org.apache.sshd.server.Environment;
import org.apache.sshd.server.ExitCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * @author Maxim Kalina
 * @version $Id$
 */
public class InAppShellFactory implements Factory<Command> {

    public Command create() {
        return new InAppShell();
    }

    private static class InAppShell implements Command, Runnable {

        private static final Logger log = LoggerFactory.getLogger(InAppShell.class);

        public static final boolean IS_MAC_OSX = System.getProperty("os.name").startsWith("Mac OS X");

        private static final String SHELL_THREAD_NAME = "InAppShell";
        private static final String SHELL_PROMPT = "app> ";
        private static final String SHELL_CMD_QUIT = "quit";
        private static final String SHELL_CMD_EXIT = "exit";
        private static final String SHELL_CMD_VERSION = "version";
        private static final String SHELL_CMD_HELP = "help";

        private InputStream in;
        private OutputStream out;
        private OutputStream err;
        private ExitCallback callback;
        private Environment environment;
        private Thread thread;
        private ConsoleReader reader;
        private PrintWriter writer;

        public InputStream getIn() {
            return in;
        }

        public OutputStream getOut() {
            return out;
        }

        public OutputStream getErr() {
            return err;
        }

        public Environment getEnvironment() {
            return environment;
        }

        public void setInputStream(InputStream in) {
            this.in = in;
        }

        public void setOutputStream(OutputStream out) {
            this.out = out;
        }

        public void setErrorStream(OutputStream err) {
            this.err = err;
        }

        public void setExitCallback(ExitCallback callback) {
            this.callback = callback;
        }

        public void start(Environment env) throws IOException {
            environment = env;
            thread = new Thread(this, SHELL_THREAD_NAME);
            thread.start();
        }

        public void destroy() {
            if (reader != null)
                reader.shutdown();
            thread.interrupt();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void run() {
            try {


                reader = new ConsoleReader(in, new FilterOutputStream(out) {
                    @Override
                    public void write(final int i) throws IOException {
                        super.write(i);

                        // workaround for MacOSX!! reset line after CR..
                        if (IS_MAC_OSX && i == ConsoleReader.CR.toCharArray()[0]) {
                            super.write(ConsoleReader.RESET_LINE);
                        }
                    }
                });
                reader.setPrompt(SHELL_PROMPT);
                reader.addCompleter(new StringsCompleter(SHELL_CMD_QUIT,
                        SHELL_CMD_EXIT, SHELL_CMD_VERSION, SHELL_CMD_HELP));
                writer = new PrintWriter(reader.getOutput());

                // output welcome banner on ssh session startup
                writer.println("****************************************************");
                writer.println("*        Welcome to Application Shell.             *");
                writer.println("****************************************************");
                writer.flush();

                String line;
                while ((line = reader.readLine()) != null) {
                    handleUserInput(line.trim());
                }

            } catch (InterruptedIOException e) {
                // Ignore
            } catch (Exception e) {
                log.error("Error executing InAppShell...", e);
            } finally {
                callback.onExit(0);
            }
        }

        private void handleUserInput(String line) throws InterruptedIOException {

            if (line.equalsIgnoreCase(SHELL_CMD_QUIT)
                    || line.equalsIgnoreCase(SHELL_CMD_EXIT))
                throw new InterruptedIOException();

            String response = null;
            if (line.equalsIgnoreCase(SHELL_CMD_VERSION))
                response = "InApp version 1.0.0";
            else if (line.equalsIgnoreCase(SHELL_CMD_HELP))
                response = "Help is not implemented yet...";
            else
                response = "======> \"" + line + "\"";

            writer.println(response);
            writer.flush();
        }
    }
}