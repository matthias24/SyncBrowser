package eu.schulze.app.SyncBrowser;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;
import java.util.Set;

/**
 * Factory, die zum einfacheren ermitteln von Java Service-Providern genutzt
 * werden kann.<br>
 * <p>
 */
public final class FactorySPI {
    /**
     * Singleton Instanz.
     */
    private static final FactorySPI m_instance = new FactorySPI();

    /**
     * Map mit jeweils einem Set Implementierungen für jedes als Java Dienst
     * registrierte Interface.
     */
    private final Map<Class<?>, Set<?>> mapServicesRegistered = new HashMap<>();

    /**
     * Konstruktor.
     */
    private FactorySPI() {
    }

    /**
     * Methode zum ermitteln der Singleton Instanz.
     *
     * @return Instanz.
     */
    public static FactorySPI getInstance() {
        return m_instance;
    }

    /**
     * Ermitteln der ersten Implementierung, die als Java Service-Provider
     * registriert ist, für das angegebene Interface.
     *
     * @param type Typ des Interfaces, das die Implementierung implementieren muss.
     * @param <T>  Typ des implementierten Interface.
     * @return Implementierung.
     * @throws ServiceConfigurationError Falls beim instanziieren ein Fehler
     *                                   auftritt oder keine Implmentierung gefunden
     *                                   werden konnte.
     */
    public <T> T getServiceRegisteredFirst(final Class<T> type) throws ServiceConfigurationError {
        Set<T> setServicesRegistered = getSetServicesRegistered(type);

        if (setServicesRegistered.isEmpty()) {
            throw new ServiceConfigurationError("Implementierung konnte nicht ermittelt werden");
        } else {
            return setServicesRegistered.iterator().next();
        }
    }

    /**
     * Ermitteln einer Liste mit Implementierungen, die als Java Service-Provider
     * registriert sind, für das angegebene Interface.
     *
     * @param type Typ des Interfaces, das die Implementierungen implementieren
     *             müssen.
     * @param <T>  Typ des implementierten Interface.
     * @return Liste mit 0 bis n Implementierungen.
     * @throws ServiceConfigurationError Falls beim instanziieren ein Fehler
     *                                   auftritt.
     */
    @SuppressWarnings("unchecked")
    public <T> Set<T> getSetServicesRegistered(final Class<T> type) throws ServiceConfigurationError {
        if (type != null) {
            Set<?> setServicesRegistered = this.mapServicesRegistered.get(type);

            if (setServicesRegistered == null) {
                synchronized (this.mapServicesRegistered) {
                    setServicesRegistered = this.mapServicesRegistered.get(type);
                    if (setServicesRegistered == null) {
                        Set<T> set = new HashSet<>();
                        final Iterator<T> it = ServiceLoader.load(type).iterator();
                        while (it.hasNext()) {
                            set.add(it.next());
                        }

                        this.mapServicesRegistered.put(type, set);
                        setServicesRegistered = set;
                    }
                }
            }

            return (Set<T>) setServicesRegistered;
        }

        return new HashSet<>();
    }

} // end of class FactorySPI
