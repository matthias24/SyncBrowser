package eu.schulze.app.SyncBrowser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * Ordner Entität für Browser Lesezeichen.<br>
 * <p>
 */
public final class EFolder implements IChild {
    /** UUID des Ordner. */
    private final UUID uuid;
    /** Name des Ordner. */
    private final String name;
    /** 0 bis n Kindelemente, die sich innerhalb des Ordner befinden. */
    private final List<IChild> children = new ArrayList<>();

    /**
     * Konstruktor.
     *
     * @param uuid UUID des Ordner.
     * @param name Name des Ordner.
     */
    public EFolder(final UUID uuid, final String name) {
        this.uuid = uuid;
        this.name = name;
    }

    /**
     * Addieren eines Kindelement.
     *
     * @param child Kindelement.
     */
    public void addChild(final IChild child) {
        if (child != null) {
            this.children.add(child);
        }
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
     * Ermitteln der Kindelemente.
     *
     * @return 0 bis n Kindelemente, die sich innerhalb des Ordner befinden.
     */
    public Collection<IChild> getChildren() {
        return this.children;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "EFolder(uuid=" + this.uuid + ", name=" + this.name + ", children=" + this.children + ")";
    }

} // end of class EFolder
