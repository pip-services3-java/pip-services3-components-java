package org.pipservices3.components.state;

import org.pipservices3.commons.config.ConfigParams;
import org.pipservices3.commons.config.IReconfigurable;
import org.pipservices3.commons.errors.ConfigException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * State store that keeps states in the process memory.
 *
 * Remember: This implementation is not suitable for synchronization of distributed processes.
 *
 * ### Configuration parameters ###
 * <ul>
 * - options:
 * <li> - timeout:               default caching timeout in milliseconds (default: disabled)
 * </ul>
 * @see org.pipservices3.components.cache.ICache
 *
 * ### Example ###
 * <pre>
 *  {@code
 *  MemoryStateStore store = new MemoryStateStore();
 *
 *  String value = store.load("123", "key1");
 *  ...
 *  store.save("123", "key1", "ABC");
 *  }
 *
 */
public class MemoryStateStore implements IStateStore, IReconfigurable {
    private final HashMap<String, StateEntry> _states = new HashMap<>();
    private long _timeout = 0;

    /**
     * Configures component by passing configuration parameters.
     *
     * @param configParams configuration parameters to be set.
     */
    @Override
    public void configure(ConfigParams configParams) throws ConfigException {
        this._timeout = configParams.getAsLongWithDefault("options.timeout", this._timeout);
    }

    /**
     * Clears component state.
     */
    private void cleanup() {
        if (this._timeout == 0) return;
        long cutOffTime = System.currentTimeMillis() - this._timeout;

        // Cleanup obsolete entries
        for (var key : this._states.keySet()) {
            StateEntry entry = this._states.get(key);
            // Remove obsolete entry
            if (entry.getLastUpdateTime() < cutOffTime) {
                this._states.remove(key);
            }
        }
    }

    /**
     * Loads state from the store using its key.
     * If value is missing in the store it returns null.
     *
     * @param correlationId (optional) transaction id to trace execution through call chain.
     * @param key           a unique state key.
     * @param <T>
     * @return the state value or <code>null</code> if value wasn't found.
     */
    @Override
    public <T> T load(String correlationId, String key) {
        if (key == null)
            throw new NullPointerException("Key cannot be null");


        // Cleanup the stored states
        this.cleanup();

        // Get entry from the store
        StateEntry entry = this._states.getOrDefault(key, null);

        // Store has nothing
        if (entry == null)
            return null;


        return (T) entry.getValue(); // TODO: maybe need make StateEntry as generic?
    }

    /**
     * Loads an array of states from the store using their keys.
     *
     * @param correlationId (optional) transaction id to trace execution through call chain.
     * @param keys          unique state keys.
     * @param <T>
     * @return an array with state values.
     */
    @Override
    public <T> List<StateValue<T>> loadBulk(String correlationId, List<String> keys) {
        // Cleanup the stored states
        this.cleanup();

        List<StateValue<T>> result = new ArrayList<>();

        for (var key : keys) {
            T value = this.load(correlationId, key);
            result.add(new StateValue<T>(key, value));
        }

        return result;
    }

    /**
     * Saves state into the store
     *
     * @param correlationId (optional) transaction id to trace execution through call chain.
     * @param key           a unique state key.
     * @param value         a state value.
     * @param <T>
     * @return The value that was stored in the cache.
     */
    @Override
    public <T> T save(String correlationId, String key, T value) {
        if (key == null)
            throw new NullPointerException("Key cannot be null");


        // Cleanup the stored states
        this.cleanup();

        // Get the entry
        StateEntry entry = this._states.getOrDefault(key, null);

        // Shortcut to remove entry from the cache
        if (value == null) {
            this._states.remove(key);
            return null;
        }

        // Update the entry
        if (entry != null) {
            entry.setValue(value);
        }
        // Or create a new entry
        else {
            entry = new StateEntry(key, value);
            this._states.put(key, entry);
        }

        return value;
    }

    /**
     * Deletes a state from the store by its key.
     *
     * @param correlationId (optional) transaction id to trace execution through call chain.
     * @param key           a unique value key.
     * @param <T>
     * @return deleted item
     */
    @Override
    public <T> T delete(String correlationId, String key) {
        if (key == null)
            throw new NullPointerException("Key cannot be null");

        // Cleanup the stored states
        this.cleanup();

        // Get the entry
        StateEntry entry = this._states.getOrDefault(key, null);

        // Remove entry from the cache
        if (entry != null) {
            this._states.remove(key);
            return (T) entry.getValue();
        }

        return null;
    }


}
