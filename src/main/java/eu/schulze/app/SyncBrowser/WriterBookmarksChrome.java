package eu.schulze.app.SyncBrowser;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Writer für Browser Lesezeichen in Chrome und Brave.<br>
 * <p>
 */
public final class WriterBookmarksChrome implements IWriterBookmarks {
    /** Logger. */
    private final Logger logger = LoggerFactory.getLogger(getClass());
    /** ResourceBundle mit Meldungen in der passenden Sprache. */
    private final ResourceBundle messages = ResourceBundle.getBundle(MESSAGES.PATH_MESSAGES);
    /**
     * Zeitpunkt, als das Element zu den Lesezeichen addiert wurde, in Mikrosekunden
     * seit 1601-01-01.
     */
    private static final String dateAdded1601 = Long.toString( //
            System.currentTimeMillis() - new GregorianCalendar(1601, 0, 1).getTimeInMillis()) + "000";

    /** Konstruktor. */
    public WriterBookmarksChrome() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFormat() {
        return "chrome";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeFile(final File file, final EBookmarks bookmarks) throws Exception {
        this.logger.atDebug().log(this.messages.getString(MESSAGES.WB_WRITE_FILE), file);

        final AtomicInteger id = new AtomicInteger(1);
        final MessageDigest md = MessageDigest.getInstance("MD5");

        // Wurzelordner mit den Lesezeichen der Lesezeichenleiste ermitteln.
        final EFolder folderBookmarksBar = bookmarks.getRootfolder(EBookmarks.KEY_BOOKMARKS_BAR);
        Folder bookmark_bar = null;
        if (folderBookmarksBar != null) {
            bookmark_bar = toFolder(folderBookmarksBar, id, md);
        }

        // Wurzelordner mit den Lesezeichen im Hauptmenu ermitteln.
        final EFolder folderBookmarksMenu = bookmarks.getRootfolder(EBookmarks.KEY_BOOKMARKS_MENU);
        Folder other = null;
        if (folderBookmarksMenu == null) {
            other = new Folder(UUID.randomUUID().toString(), Integer.toString(id.getAndIncrement()),
                    EBookmarks.KEY_BOOKMARKS_MENU, new ArrayList<>());
            other.updateChecksum(md);
        } else {
            other = toFolder(folderBookmarksMenu, id, md);
        }

        final Folder synced = new Folder(UUID.randomUUID().toString(), Integer.toString(id.getAndIncrement()),
                "Mobile Bookmarks", new ArrayList<>());
        synced.updateChecksum(md);

        final String checksum = toHexdigest(md);
        final Bookmarks bookmarksJson = new Bookmarks(checksum, new Roots(bookmark_bar, other, synced));

        final ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(file, bookmarksJson);
    }

    /**
     * Umwandeln des MessageDigest in einen Hexdigest String.
     *
     * @param md MessageDigest zum berechnen der Prüfsumme.
     * @return Hexdigest String.
     */
    private String toHexdigest(final MessageDigest md) {
        final byte[] bytes = md.digest();
        final StringBuilder hexString = new StringBuilder();

        for (byte element : bytes) {
            final String hex = Integer.toHexString(0xFF & element);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }

        return hexString.toString();
    }

    /**
     * Umwandeln der Ordner Entität in ein Folder Objekt.
     *
     * @param folder   Ordner Entität.
     * @param id       Eindeutige ID des Element von 1 bis n, noch nicht verbraucht.
     * @param resultMd MessageDigest zum berechnen der Prüfsumme.
     * @return Folder Objekt.
     */
    private Folder toFolder(final EFolder folder, final AtomicInteger id, final MessageDigest resultMd) {
        this.logger.atDebug().log(this.messages.getString(MESSAGES.WB_PROCESS_FOLDER), folder);

        final int idFolder = id.getAndIncrement();
        final List<Object> children = new ArrayList<>();
        for (final IChild child : folder.getChildren()) {
            if (child instanceof EFolder folderSub) {
                children.add(toFolder(folderSub, id, resultMd));
            } else if (child instanceof EUrl urlSub) {
                this.logger.atTrace().log(this.messages.getString(MESSAGES.WB_PROCESS_URL), urlSub);

                final Url url = new Url(urlSub.getUUID().toString(), Integer.toString(id.getAndIncrement()),
                        urlSub.getName(), urlSub.getUrl());
                url.updateChecksum(resultMd);
                children.add(url);
            }
        }

        final Folder folderConverted = new Folder(folder.getUUID().toString(), Integer.toString(idFolder),
                folder.getName(), children);
        folderConverted.updateChecksum(resultMd);

        return folderConverted;
    }

    /**
     * Bookmarks Entität für Browser Lesezeichen.<br>
     * <p>
     *
     * @param checksum Checksumme.
     * @param roots    Roots Entität.
     */
    private final record Bookmarks(String checksum, Roots roots) {

        /**
         * Ermitteln der Version der Datei.
         *
         * @return Version.
         */
        @SuppressWarnings("unused")
        public int getVersion() {
            return 1;
        }

    }

    /**
     * Roots Entität für Browser Lesezeichen.<br>
     * <p>
     *
     * @param bookmark_bar Lesezeichenleiste.
     * @param other        Andere Lesezeichen.
     * @param synced       Mobile Lesezeichen.
     */
    private final record Roots(Folder bookmark_bar, Folder other, Folder synced) {
    }

    /**
     * Ordner Entität für Browser Lesezeichen.<br>
     * <p>
     *
     * @param guid     UUID des Ordner.
     * @param id       Eindeutige ID des Element von 1 bis n.
     * @param name     Name des Ordner.
     * @param children 0 bis n Kindelemente, die sich innerhalb des Ordner befinden.
     */
    private final record Folder(String guid, String id, String name, Collection<?> children) {

        /**
         * Zeitpunkt, als der Folder zu den Lesezeichen addiert wurde.
         *
         * @return Zeitpunkt in Mikrosekunden seit 1601-01-01.
         */
        @SuppressWarnings("unused")
        public String getDate_added() {
            return WriterBookmarksChrome.dateAdded1601;
        }

        /**
         * Zeitpunkt, als der Folder zum letzten Mal benutzt wurde.
         *
         * @return Zeitpunkt in Mikrosekunden seit 1601-01-01.
         */
        @SuppressWarnings("unused")
        public String getDate_last_used() {
            return "0";
        }

        /**
         * Zeitpunkt, als der Folder zum letzten Mal geändert wurde.
         *
         * @return Zeitpunkt in Mikrosekunden seit 1601-01-01.
         */
        @SuppressWarnings("unused")
        public String getDate_modified() {
            return "0";
        }

        /**
         * Ermitteln der Typ des Element.
         *
         * @return Typ.
         */
        public String getType() {
            return "folder";
        }

        /**
         * Prüfsumme der Lesezeichen Datei aktualisieren mit den Daten dieses Elements.
         *
         * @param resultMd MessageDigest zum berechnen der Prüfsumme.
         */
        public void updateChecksum(final MessageDigest resultMd) {
            resultMd.update(this.id.getBytes(StandardCharsets.US_ASCII));
            resultMd.update(this.name.getBytes(StandardCharsets.UTF_16LE));
            resultMd.update(this.getType().getBytes());
        }

    } // end of class Folder

    /**
     * URL Entität für Browser Lesezeichen.<br>
     * <p>
     *
     * @param guid UUID des Lesezeichen.
     * @param id   Eindeutige ID des Element von 1 bis n.
     * @param name Name des Lesezeichen.
     * @param url  URL des Lesezeichen.
     */
    private final record Url(String guid, String id, String name, String url) {

        /**
         * Zeitpunkt, als die Url zu den Lesezeichen addiert wurde.
         *
         * @return Zeitpunkt in Mikrosekunden seit 1601-01-01.
         */
        @SuppressWarnings("unused")
        public String getDate_added() {
            return WriterBookmarksChrome.dateAdded1601;
        }

        /**
         * Zeitpunkt, als die Url zum letzten Mal benutzt wurde.
         *
         * @return Zeitpunkt in Mikrosekunden seit 1601-01-01.
         */
        @SuppressWarnings("unused")
        public String getDate_last_used() {
            return "0";
        }

        /**
         * Ermitteln der Typ des Element.
         *
         * @return Typ.
         */
        public String getType() {
            return "url";
        }

        /**
         * Prüfsumme der Lesezeichen Datei aktualisieren mit den Daten dieses Elements.
         *
         * @param resultMd MessageDigest zum berechnen der Prüfsumme.
         */
        public void updateChecksum(final MessageDigest resultMd) {
            resultMd.update(this.id.getBytes(StandardCharsets.US_ASCII));
            resultMd.update(this.name.getBytes(StandardCharsets.UTF_16LE));
            resultMd.update(this.getType().getBytes());
            resultMd.update(this.url.getBytes(StandardCharsets.US_ASCII));
        }

    } // end of class Url

} // end of class WriterBookmarksChrome
