package eu.schulze.app.SyncBrowser;

/** Schlüssel für das ResourceBundle mit den lokalisierten Meldungen. */
public final class MESSAGES {
    /** Meldungen für SyncBrowser in verschiedenen Sprachen. */
    public static final String PATH_MESSAGES = "eu.schulze.app.SyncBrowser.messages";

    // Konstanten für ReaderArgumentsCommandLine.
    /** Prefix für Schlüssel der ReaderArgumentsCommandLine Meldungen. */
    private static final String PREFIX_READER_ARGUMENTS = "reader_args.";
    public static final String RA_OPTION_LOGLEVEL = PREFIX_READER_ARGUMENTS + "option_loglevel";
    public static final String RA_OPTION_FORMATIN = PREFIX_READER_ARGUMENTS + "option_formatin";
    public static final String RA_OPTION_FILEIN = PREFIX_READER_ARGUMENTS + "option_filein";
    public static final String RA_OPTION_FORMATOUT = PREFIX_READER_ARGUMENTS + "option_formatout";
    public static final String RA_OPTION_FILEOUT = PREFIX_READER_ARGUMENTS + "option_fileout";
    public static final String RA_FILEIN_ERRORIO = PREFIX_READER_ARGUMENTS + "filein_errorio";
    public static final String RA_FILEOUT_ERRORIO = PREFIX_READER_ARGUMENTS + "fileout_errorio";

    // Konstanten für ReaderBookmarks Implementierungen.
    /** Prefix für Schlüssel der ReaderBookmarks Meldungen. */
    private static final String PREFIX_READER_BOOKMARKS = "reader_bookmarks.";
    public static final String RB_READ_FILE = PREFIX_READER_BOOKMARKS + "read_file";
    public static final String RB_PROCESS_CHILD = PREFIX_READER_BOOKMARKS + "process_child";
    public static final String RB_TYPE_WRONG = PREFIX_READER_BOOKMARKS + "type_wrong";
    public static final String RB_TYPE_UNKNOWN = PREFIX_READER_BOOKMARKS + "type_unknown";
    public static final String RB_PROCESS_LIST = PREFIX_READER_BOOKMARKS + "process_list";
    public static final String RB_PROCESS_LEAF = PREFIX_READER_BOOKMARKS + "process_leaf";
    public static final String RB_NOPROCESS_ELEMENT = PREFIX_READER_BOOKMARKS + "noprocess_element";

    // Konstanten für WriterBookmark Implementierungen.
    /** Prefix für Schlüssel der WriterBookmarks Meldungen. */
    private static final String PREFIX_WRITER_BOOKMARKS = "writer_bookmarks.";
    public static final String WB_WRITE_FILE = PREFIX_WRITER_BOOKMARKS + "write_file";
    public static final String WB_PROCESS_FOLDER = PREFIX_WRITER_BOOKMARKS + "process_folder";
    public static final String WB_PROCESS_URL = PREFIX_WRITER_BOOKMARKS + "process_url";

    /** Konstruktor. */
    private MESSAGES() {
    }

} // end of class MESSAGES
