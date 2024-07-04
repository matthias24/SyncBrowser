package eu.schulze.app.SyncBrowser;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;

/**
 * Main Klasse zum synchronisieren bzw. konvertieren von Lesezeichen
 * Dateien.<br>
 * <p>
 */
public final class MainClass {

    /**
     * Konstruktor.
     *
     * @param args Kommandozeilen Argumente.
     */
    public MainClass(final String[] args) {
        final Arguments arguments = ReaderArgumentsCommandLine.getInstance().readArguments(args);
        if (arguments == null) {
            System.exit(0);
        }

        setLogLevel(arguments.logLevel());
        try {
            convertBookmarks(arguments);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(-1);
        }
    }

    /**
     * Setzen des Level der Logausgaben.
     *
     * @param logLevelOptional Optional: Level. Default: INFO
     */
    private void setLogLevel(final Level logLevelOptional) {
        if (logLevelOptional != null) {
            final Logger root = LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
            if (root instanceof ch.qos.logback.classic.Logger logger) {
                logger.setLevel(logLevelOptional);
            }
        }
    }

    /**
     * Konvertieren der Browser Lesezeichen.
     *
     * @param arguments Kommandozeilen Argumente.
     * @throws Exception Falls ein Fehler auftritt.
     */
    private void convertBookmarks(final Arguments arguments) throws Exception {
        final IReaderBookmarks reader = getReaderBookmarks(arguments.formatIn());
        final File fileIn = arguments.fileIn();
//        /Users/matthias/Library/Safari/Bookmarks.plist

        final IWriterBookmarks writer = getWriterBookmarks(arguments.formatOut());
        final File fileOut = arguments.fileOut();
//        /Users/matthias/Library/Application Support/BraveSoftware/Brave-Browser/Default/Bookmarks
//        /Users/matthias/Library/Application Support/Google/Chrome/Default/Bookmarks

        final EBookmarks bookmarks = reader.readFile(fileIn);
        writer.writeFile(fileOut, bookmarks);
    }

    /**
     * Ermitteln des ReaderBookmarks zum lesen der Daten im angegebenen
     * Browserformat.
     *
     * @param format Optional: Browserformat. Default: safari
     * @return ReaderBookmarks.
     * @throws IOException Falls keine Implementierung ermittelt werden konnte.
     */
    private IReaderBookmarks getReaderBookmarks(String format) throws IOException {
        if (format == null) {
            format = "safari";
        }

        final Set<IReaderBookmarks> setReader = FactorySPI.getInstance()
                .getSetServicesRegistered(IReaderBookmarks.class);
        for (final IReaderBookmarks reader : setReader) {
            if (reader.getFormat().equalsIgnoreCase(format)) {
                return reader;
            }
        }

        throw new IOException(
                "IReaderBookmarks Implementierung für Format " + format + " konnte nicht ermittelt werden");
    }

    /**
     * Ermitteln des WriterBookmarks zum schreiben der Daten im angegebenen
     * Browserformat.
     *
     * @param format Optional: Browserformat. Default: chrome
     * @return WriterBookmarks.
     * @throws IOException Falls keine Implementierung ermittelt werden konnte.
     */
    private IWriterBookmarks getWriterBookmarks(String format) throws IOException {
        if (format == null) {
            format = "chrome";
        }

        final Set<IWriterBookmarks> setWriter = FactorySPI.getInstance()
                .getSetServicesRegistered(IWriterBookmarks.class);
        for (final IWriterBookmarks writer : setWriter) {
            if (writer.getFormat().equalsIgnoreCase(format)) {
                return writer;
            }
        }

        throw new IOException(
                "IWriterBookmarks Implementierung für Format " + format + " konnte nicht ermittelt werden");
    }

    /**
     * Main Methode.
     *
     * @param args Kommandozeilen Argumente.
     */
    public static void main(final String[] args) {
//        Locale.setDefault(Locale.ENGLISH);
        new MainClass(args);
    }

} // end of class MainClass
