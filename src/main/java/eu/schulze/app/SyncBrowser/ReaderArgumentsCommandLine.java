package eu.schulze.app.SyncBrowser;

import java.io.File;
import java.util.ResourceBundle;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import ch.qos.logback.classic.Level;

/**
 * Reader für Kommandozeilen Argumente.<br>
 * <p>
 */
public final class ReaderArgumentsCommandLine {
    /** Singleton Instanz. */
    private static final ReaderArgumentsCommandLine m_instance = new ReaderArgumentsCommandLine();
    /** RasourceBundle mit Meldungen in der passenden Sprache. */
    private final ResourceBundle messages = ResourceBundle.getBundle(MESSAGES.PATH_MESSAGES);

    /** Argument Name für Logausgaben Level. */
    private final String argNameLogLevel = Level.ALL + "|" + Level.TRACE + "|" + Level.DEBUG + "|" + Level.INFO + "|"
            + Level.WARN + "|" + Level.OFF;
    /** Optionale Option für Argument Logausgaben Level. */
    private final Option optionLogLevel = Option.builder("log").longOpt("log-level")
            .desc(this.messages.getString(MESSAGES.RA_OPTION_LOGLEVEL)).hasArg().argName(argNameLogLevel).build();
    /** Argument Name für Dateiformat. */
    // TODO Anhand eingelesener Reader und Writer
    private final String argNameFormat = "safari" + "|" + "chrome";
    /** Optionale Option für Argument Dateiformat Eingabedatei. */
    private final Option optionFormatIn = Option.builder("fin").longOpt("format-in")
            .desc(this.messages.getString(MESSAGES.RA_OPTION_FORMATIN)).hasArg().argName(argNameFormat).build();
    /** Option für Eingabedatei mit Lesezeichen. */
    private final Option optionFileIn = Option.builder("in").longOpt("file-in")
            .desc(this.messages.getString(MESSAGES.RA_OPTION_FILEIN)).hasArg().argName("file").type(File.class)
            .required().build();
    /** Optionale Option für Argument Dateiformat Ausgabedatei. */
    private final Option optionFormatOut = Option.builder("fout").longOpt("format-out")
            .desc(this.messages.getString(MESSAGES.RA_OPTION_FORMATOUT)).hasArg().argName(argNameFormat).build();
    /** Option für Ausgabedatei mit Lesezeichen. */
    private final Option optionFileOut = Option.builder("out").longOpt("file-out")
            .desc(this.messages.getString(MESSAGES.RA_OPTION_FILEOUT)).hasArg().argName("file").type(File.class)
            .required().build();
    /** Alle möglichen Optionen. */
    private final Options options = new Options();

    /** Konstruktor. */
    private ReaderArgumentsCommandLine() {
        this.options.addOption(this.optionLogLevel);
        this.options.addOption(this.optionFormatIn);
        this.options.addOption(this.optionFileIn);
        this.options.addOption(this.optionFormatOut);
        this.options.addOption(this.optionFileOut);
    }

    /**
     * Methode zum Ermitteln der Singleton Instanz.
     *
     * @return Instanz.
     */
    public static ReaderArgumentsCommandLine getInstance() {
        return m_instance;
    }

    /**
     * Lesen der angegebenen Kommandozeilen Argumente.
     *
     * @param args Kommandozeilen Argumente.
     * @return Gelesene Argumente, falls kein Fehler aufgetreten ist.
     */
    public Arguments readArguments(final String[] args) {
        final CommandLineParser parser = new DefaultParser();

        try {
            final CommandLine cmd = parser.parse(this.options, args);

            final Level logLevel = getLogLevel(cmd);
            final String formatIn = getFormat(cmd, this.optionFormatIn);
            final File fileIn = getFileIn(cmd);
            final String formatOut = getFormat(cmd, this.optionFormatOut);
            final File fileOut = getFileOut(cmd);

            return new Arguments(logLevel, formatIn, fileIn, formatOut, fileOut);
        } catch (final ParseException ex) {
            System.out.println(ex.getMessage());
            new HelpFormatter().printHelp("syncbrowser", this.options);

            return null;
        }
    }

    /**
     * Ermitteln des Level der Logausgaben.
     *
     * @param cmd CommandLine.
     * @return Level, falls angegeben.
     */
    private Level getLogLevel(final CommandLine cmd) {
        if (cmd.hasOption(this.optionLogLevel)) {
            final String level = cmd.getOptionValue(this.optionLogLevel);

            return Level.valueOf(level.toUpperCase());
        }

        return null;
    }

    /**
     * Ermitteln des Dateiformat der Lesezeichen Datei.
     *
     * @param cmd          CommandLine.
     * @param optionFormat Option des Dateiformat.
     * @return Dateiformat, falls angegeben.
     */
    private String getFormat(final CommandLine cmd, final Option optionFormat) {
        if (cmd.hasOption(optionFormat)) {
            final String format = cmd.getOptionValue(optionFormat);

            return format.toLowerCase();
        }

        return null;
    }

    /**
     * Ermitteln der Eingabedatei mit Lesezeichen.
     *
     * @param cmd CommandLine.
     * @return Eingabedatei.
     * @throws ParseException Falls Datei nicht ermittelt bzw. verwendet werden
     *                        kann.
     */
    private File getFileIn(final CommandLine cmd) throws ParseException {
        if (cmd.hasOption(this.optionFileIn)) {
            final File fileIn = cmd.getParsedOptionValue(this.optionFileIn);
            if (!fileIn.exists() || !fileIn.isFile() || !fileIn.canRead()) {
                final String message = String.format(
                        this.messages.getString(MESSAGES.RA_FILEIN_ERRORIO).replace("{}", "%s"),
                        fileIn.getAbsolutePath());
                throw new ParseException(message);
            }

            return fileIn;
        }

        return null;
    }

    /**
     * Ermitteln der Ausgabedatei mit Lesezeichen.
     *
     * @param cmd CommandLine.
     * @return Ausgabedatei.
     * @throws ParseException Falls Datei nicht ermittelt bzw. verwendet werden
     *                        kann.
     */
    private File getFileOut(final CommandLine cmd) throws ParseException {
        if (cmd.hasOption(this.optionFileOut)) {
            final File fileOut = cmd.getParsedOptionValue(this.optionFileOut);
            if (fileOut.exists() && (!fileOut.isFile() || !fileOut.canWrite())) {
                final String message = String.format(
                        this.messages.getString(MESSAGES.RA_FILEOUT_ERRORIO).replace("{}", "%s"),
                        fileOut.getAbsolutePath());
                throw new ParseException(message);
            }

            return fileOut;
        }

        return null;
    }

} // end of class ReaderArgumentsCommandLine
