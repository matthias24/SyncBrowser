package eu.schulze.app.SyncBrowser;

import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dd.plist.NSArray;
import com.dd.plist.NSDictionary;
import com.dd.plist.NSObject;
import com.dd.plist.NSString;
import com.dd.plist.PropertyListParser;

/**
 * Reader für Browser Lesezeichen in Safari.<br>
 * <p>
 */
public final class ReaderBookmarksSafari implements IReaderBookmarks {
    /** Schlüssel für WEB_BOOKMARK_TYPE. */
    private static final String KEY_WEB_BOOKMARK_TYPE = "WebBookmarkType";
    /** Wert für WEB_BOOKMARK_TYPE List. */
    private static final String VALUE_WEB_BOOKMARK_TYPE_LIST = "WebBookmarkTypeList";
    /** Wert für WEB_BOOKMARK_TYPE Leaf. */
    private static final String VALUE_WEB_BOOKMARK_TYPE_LEAF = "WebBookmarkTypeLeaf";
    /** Wert für WEB_BOOKMARK_TYPE Proxy. */
    private static final String VALUE_WEB_BOOKMARK_TYPE_PROXY = "WebBookmarkTypeProxy";

    /** Schlüssel für UUID der List bzw. des Leaf. */
    private static final String KEY_UUID = "WebBookmarkUUID";
    /** Schlüssel für Name der List. */
    private static final String KEY_TITLE_FOLDER = "Title";
    /** Schlüssel für Kindobjekte der List. */
    private static final String KEY_CHILDREN = "Children";

    /** Schlüssel für Name des Leaf. */
    private static final String KEY_TITLE_URL = "title";
    /** Schlüssel für URL des Leaf. */
    private static final String KEY_URL = "URLString";
    /** Schlüssel für Metadaten des Leaf. */
    private static final String KEY_METADATA_URL = "URIDictionary";

    /** Logger. */
    private final Logger logger = LoggerFactory.getLogger(getClass());
    /** RasourceBundle mit Meldungen in der passenden Sprache. */
    private final ResourceBundle messages = ResourceBundle.getBundle(MESSAGES.PATH_MESSAGES);

    /** WEB_BOOKMARK_TYPE des NSDictionary. */
    private enum WEB_BOOKMARK_TYPE {
        /** List, die ein Folder Objekt darstellt. */
        LIST,
        /** Leaf, das ein URL Objekt darstellt. */
        LEAF,
        /** Unbekanntes Objekt, das nicht verarbeitet wird. */
        UNKNOWN
    }

    /** Konstruktor. */
    public ReaderBookmarksSafari() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFormat() {
        return "safari";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EBookmarks readFile(final File file) throws Exception {
        this.logger.atDebug().log(this.messages.getString(MESSAGES.RB_READ_FILE), file);

        final NSObject object = PropertyListParser.parse(file);
        if (object instanceof NSDictionary dictionary) {
            final NSObject children = dictionary.get(KEY_CHILDREN);
            if (children instanceof NSArray array) {
                // Wurzelordner ermitteln.
                final EFolder rootfolders = new EFolder(UUID.randomUUID(), "root");
                for (final NSObject child : array.getArray()) {
                    readAndAddChild(child, rootfolders);
                }

                // Wurzelordner zu Bookmarks addieren.
                return toBookmarks(rootfolders);
            } else {
                final String message = String.format(
                        this.messages.getString(MESSAGES.RB_NOPROCESS_ELEMENT).replace("{}", "%s"), KEY_CHILDREN,
                        object);
                throw new IOException(message);
            }
        } else {
            final String message = String.format(
                    this.messages.getString(MESSAGES.RB_NOPROCESS_ELEMENT).replace("{}", "%s"), "PList", object);
            throw new IOException(message);
        }
    }

    /**
     * Ordner mit allen Wurzelordnern zu Bookmarks addieren.
     *
     * @param rootfolders Ordner mit Wurzelordnern.
     * @return Bookmarks.
     */
    private EBookmarks toBookmarks(final EFolder rootfolders) {
        final EBookmarks bookmarks = new EBookmarks();
        for (final IChild rootfolder : rootfolders.getChildren()) {
            if (rootfolder instanceof EFolder folder) {
                final String name = rootfolder.getName();
                if ("BookmarksBar".equalsIgnoreCase(name)) {
                    bookmarks.setRootfolder(EBookmarks.KEY_BOOKMARKS_BAR, folder);
                } else if ("BookmarksMenu".equalsIgnoreCase(name)) {
                    bookmarks.setRootfolder(EBookmarks.KEY_BOOKMARKS_MENU, folder);
                } else if ("com.apple.ReadingList".equalsIgnoreCase(name)) {
                    bookmarks.setRootfolder(EBookmarks.KEY_READINGLIST, folder);
                }
            } else {
                this.logger.atWarn().log(this.messages.getString(MESSAGES.RB_NOPROCESS_ELEMENT), "EFolder", rootfolder);
            }
        }

        return bookmarks;
    }

    /**
     * Lesen des Kindobjekt, falls dieses ein NSDictionary ist und addieren zum
     * Folder.
     *
     * @param object       NSObject.
     * @param resultFolder Folder zum addieren des Kindobjekt.
     */
    private void readAndAddChild(final NSObject object, final EFolder resultFolder) {
        this.logger.atTrace().log(this.messages.getString(MESSAGES.RB_PROCESS_CHILD));

        if (object instanceof NSDictionary dictionary) {
            final WEB_BOOKMARK_TYPE webBookmarkType = getWebBookmarkType(dictionary);
            if (webBookmarkType == WEB_BOOKMARK_TYPE.LIST) {
                readAndAddList(dictionary, resultFolder);
            } else if (webBookmarkType == WEB_BOOKMARK_TYPE.LEAF) {
                readAndAddLeaf(dictionary, resultFolder);
            }
        } else {
            this.logger.atWarn().log(this.messages.getString(MESSAGES.RB_NOPROCESS_ELEMENT), "Child", object);
        }
    }

    /**
     * Ermitteln des WEB_BOOKMARK_TYPE des NSDictionary.
     *
     * @param dictionary NSDictionary.
     * @return WEB_BOOKMARK_TYPE.
     */
    private WEB_BOOKMARK_TYPE getWebBookmarkType(final NSDictionary dictionary) {
        final NSObject object = dictionary.get(KEY_WEB_BOOKMARK_TYPE);

        if (object instanceof NSString string) {
            final String webBookmarkType = string.getContent();

            if (webBookmarkType.equals(VALUE_WEB_BOOKMARK_TYPE_LIST)) {
                return WEB_BOOKMARK_TYPE.LIST;
            } else if (webBookmarkType.equals(VALUE_WEB_BOOKMARK_TYPE_LEAF)) {
                return WEB_BOOKMARK_TYPE.LEAF;
            } else if (webBookmarkType.equals(VALUE_WEB_BOOKMARK_TYPE_PROXY)) {
                this.logger.atDebug().log(this.messages.getString(MESSAGES.RB_TYPE_WRONG), KEY_WEB_BOOKMARK_TYPE,
                        VALUE_WEB_BOOKMARK_TYPE_PROXY);
                return WEB_BOOKMARK_TYPE.UNKNOWN;
            } else {
                this.logger.atWarn().log(this.messages.getString(MESSAGES.RB_TYPE_UNKNOWN), KEY_WEB_BOOKMARK_TYPE,
                        webBookmarkType);
                return WEB_BOOKMARK_TYPE.UNKNOWN;
            }
        } else {
            this.logger.atWarn().log(this.messages.getString(MESSAGES.RB_NOPROCESS_ELEMENT), KEY_WEB_BOOKMARK_TYPE,
                    object);
            return WEB_BOOKMARK_TYPE.UNKNOWN;
        }
    }

    /**
     * Lesen der List aus dem NSDictionary und addieren zum Folder.
     *
     * @param dictionary   NSDictionary.
     * @param resultFolder Folder zum addieren des Folder Objekt.
     */
    private void readAndAddList(final NSDictionary dictionary, final EFolder resultFolder) {
        this.logger.atDebug().log(this.messages.getString(MESSAGES.RB_PROCESS_LIST));

        final NSObject object = dictionary.get(KEY_CHILDREN);
        if (object instanceof NSArray array) {
            final String uuid = readString(dictionary, KEY_UUID);
            final String name = readString(dictionary, KEY_TITLE_FOLDER);
            if (uuid != null && name != null) {
                final EFolder folder = new EFolder(UUID.fromString(uuid), name);
                resultFolder.addChild(folder);
                for (final NSObject child : array.getArray()) {
                    readAndAddChild(child, folder);
                }
            }
        } else {
            this.logger.atWarn().log(this.messages.getString(MESSAGES.RB_NOPROCESS_ELEMENT), KEY_CHILDREN, object);
        }
    }

    /**
     * Lesen des Leaf aus dem NSDictionary und addieren zum Folder.
     *
     * @param dictionary   NSDictionary.
     * @param resultFolder Folder zum addieren des URL Objekt.
     */
    private void readAndAddLeaf(final NSDictionary dictionary, final EFolder resultFolder) {
        this.logger.atTrace().log(this.messages.getString(MESSAGES.RB_PROCESS_LEAF));

        final NSObject object = dictionary.get(KEY_METADATA_URL);
        if (object instanceof NSDictionary metadata) {
            final String uuid = readString(dictionary, KEY_UUID);
            final String name = readString(metadata, KEY_TITLE_URL);
            final String url = readString(dictionary, KEY_URL);
            if (uuid != null && name != null && url != null) {
                final EUrl bookmark = new EUrl(UUID.fromString(uuid), name, url);
                resultFolder.addChild(bookmark);
            }
        } else {
            this.logger.atWarn().log(this.messages.getString(MESSAGES.RB_NOPROCESS_ELEMENT), KEY_METADATA_URL, object);
        }
    }

    /**
     * Ermitteln des String Wert mit dem angegebenen Schlüssel aus dem NSDictionary.
     *
     * @param dictionary NSDictionary.
     * @param key        Schlüssel.
     * @return String Wert, falls dieser ermittelt werden konnte.
     */
    private String readString(final NSDictionary dictionary, final String key) {
        final NSObject object = dictionary.get(key);
        if (object instanceof NSString string) {
            return string.getContent();
        } else {
            this.logger.atWarn().log(this.messages.getString(MESSAGES.RB_NOPROCESS_ELEMENT), key, object);
            return null;
        }
    }

} // end of class ReaderBookmarksSafari
