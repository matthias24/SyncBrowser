package eu.schulze.app.SyncBrowser;

import java.util.HashMap;
import java.util.Map;

/**
 * Bookmarks Entität für Browser Lesezeichen.<br>
 * <p>
 */
public final class EBookmarks {
    /**
     * Schlüssel zum ermitteln des Wurzelordner mit den Lesezeichen der
     * Lesezeichenleiste.
     */
    public static final String KEY_BOOKMARKS_BAR = "BookmarksBar";
    /**
     * Schlüssel zum ermitteln des Wurzelordner mit den Lesezeichen im Hauptmenu.
     */
    public static final String KEY_BOOKMARKS_MENU = "BookmarksMenu";
    /**
     * Schlüssel zum ermitteln des Wurzelordner mit den Webseiten der Leseliste.
     */
    public static final String KEY_READINGLIST = "ReadingList";

    /** Map mit allen Wurzelordnern. */
    private final Map<String, EFolder> mapRootfolders = new HashMap<>();

    /** Konstruktor. */
    public EBookmarks() {
    }

    /**
     * Setzen des Wurzelordner für den angegebenen Schlüssel.
     *
     * @param key    Schlüssel.
     * @param folder Wurzelordner.
     */
    public void setRootfolder(final String key, final EFolder folder) {
        this.mapRootfolders.put(key, folder);
    }

    /**
     * Ermitteln des Wurzelordner für den angegebenen Schlüssel.
     *
     * @param key Schlüssel.
     * @return Wurzelordner.
     */
    public EFolder getRootfolder(final String key) {
        return this.mapRootfolders.get(key);
    }

} // end of class EBookmarks
