package eu.schulze.app.SyncBrowser;

import java.io.File;

import ch.qos.logback.classic.Level;

/**
 * Kommandozeilen Argumente zum konvertieren von Browser Lesezeichen.<br>
 * <p>
 *
 * @param logLevel  Optional: Level der Logausgaben. Default: INFO
 * @param formatIn  Optional: Dateiformat der Lesezeichen Datei. Default: safari
 * @param fileIn    Eingabedatei mit Lesezeichen.
 * @param formatOut Optional: Dateiformat der Lesezeichen Datei. Default: chrome
 * @param fileOut   Ausgabedatei mit Lesezeichen.
 */
public final record Arguments(Level logLevel, String formatIn, File fileIn, String formatOut, File fileOut) {

}
