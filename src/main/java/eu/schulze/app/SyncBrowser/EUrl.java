package eu.schulze.app.SyncBrowser;

import java.util.UUID;

/**
 * URL Entität für Browser Lesezeichen.<br>
 * <p>
 */
public final class EUrl implements IChild {
    /** UUID des Lesezeichen. */
    private final UUID uuid;
    /** Name des Lesezeichen. */
    private final String name;
    /** URL des Lesezeichen. */
    private final String url;

    /**
     * Konstruktor.
     *
     * @param uuid UUID des Lesezeichen.
     * @param name Name des Lesezeichen.
     * @param url  URL des Lesezeichen.
     */
    public EUrl(final UUID uuid, final String name, final String url) {
        this.uuid = uuid;
        this.name = name;
        this.url = url;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UUID getUUID() {
        return this.uuid;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * Ermitteln der URL.
     *
     * @return URL des Lesezeichen.
     */
    public String getUrl() {
        return this.url;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "EUrl(uuid=" + this.uuid + ", name=" + this.name + ", url=" + this.url + ")";
    }

} // end of class EUrl
