package eu.schulze.app.SyncBrowser;

import java.io.File;

/**
 * Lesezeichen Interface zum lesen von Browser Lesezeichen.<br>
 * <p>
 */
public interface IReaderBookmarks {

    /**
     * Ermitteln des Browserformat der Eingabedatei.
     * 
     * @return Browserformat.
     */
    String getFormat();

    /**
     * Lesen der angegebenen Datei mit Lesezeichen.
     *
     * @param file Datei.
     * @return Bookmarks mit allen Unterordnern und Lesezeichen.
     * @throws Exception Falls ein Fehler auftritt.
     */
    EBookmarks readFile(File file) throws Exception;

}
