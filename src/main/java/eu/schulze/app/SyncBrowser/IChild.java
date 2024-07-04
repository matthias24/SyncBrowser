package eu.schulze.app.SyncBrowser;

import java.util.UUID;

/**
 * Kindelement Interface für Browser Lesezeichen.<br>
 * <p>
 */
public interface IChild {

    /**
     * Ermitteln der UUID.
     *
     * @return UUID des Kindelement.
     */
    UUID getUUID();

    /**
     * Ermitteln des Namen.
     *
     * @return Name des Kindelement.
     */
    String getName();

}
