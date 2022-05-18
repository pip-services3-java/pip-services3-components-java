package org.pipservices3.components.state;

import java.util.List;

/**
 * Dummy state store implementation that doesn't do anything.
 *
 * It can be used in testing or in situations when state management is not required
 * but shall be disabled.
 */
public class NullStateStore implements IStateStore {
    /**
     * Loads state from the store using its key.
     * If value is missing in the stored it returns null.
     *
     * @param correlationId (optional) transaction id to trace execution through call chain.
     * @param key           a unique state key.
     * @param <T>
     * @return the state value or <code>null</code> if value wasn't found.
     */
    @Override
    public <T> T load(String correlationId, String key) {
        return null;
    }

    /**
     * Loads an array of states from the store using their keys.
     *
     * @param correlationId (optional) transaction id to trace execution through call chain.
     * @param keys          unique state keys.
     * @param <T>
     * @return an array with state values and their corresponding keys.
     */
    @Override
    public <T> List<StateValue<T>> loadBulk(String correlationId, List<String> keys) {
        return List.of();
    }

    /**
     * Saves state into the store.
     *
     * @param correlationId (optional) transaction id to trace execution through call chain.
     * @param key           a unique state key.
     * @param value         a state value.
     * @param <T>
     * @return The state that was stored in the store.
     */
    @Override
    public <T> T save(String correlationId, String key, T value) {
        return value;
    }

    /**
     * Deletes a state from the store by its key.
     *
     * @param correlationId (optional) transaction id to trace execution through call chain.
     * @param key           a unique value key.
     * @param <T>
     * @return deleted value
     */
    @Override
    public <T> T delete(String correlationId, String key) {
        return null;
    }
}
