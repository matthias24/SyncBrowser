package eu.schulze.app.SyncBrowser;

import java.io.File;

/**
 * Lesezeichen Interface zum schreiben von Browser Lesezeichen.<br>
 * <p>
 */
public interface IWriterBookmarks {

    /**
     * Ermitteln des Browserformat der Ausgabedatei.
     * 
     * @return Browserformat.
     */
    String getFormat();

    /**
     * Schreiben der angegebenen Datei mit Lesezeichen.
     *
     * @param file   Datei.
     * @param folder Ordner mit allen Unterordnern und Lesezeichen.
     * @throws Exception Falls ein Fehler auftritt.
     */
    void writeFile(File file, EBookmarks folder) throws Exception;

}
