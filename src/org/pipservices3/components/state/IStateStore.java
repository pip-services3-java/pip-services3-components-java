package org.pipservices3.components.state;

import java.util.List;

/**
 * Interface for state storages that are used to store and retrieve transaction states.
 */
public interface IStateStore {

    /**
     * Loads state from the store using its key.
     * If value is missing in the store it returns null.
     *
     * @param correlationId (optional) transaction id to trace execution through call chain.
     * @param key           a unique state key.
     * @param <T>
     * @return the state value or <code>null</code> if value wasn't found.
     */
    <T> T load(String correlationId, String key);

    /**
     * Loads an array of states from the store using their keys.
     *
     * @param correlationId (optional) transaction id to trace execution through call chain.
     * @param keys          unique state keys.
     * @param <T>
     * @return an array with state values and their corresponding keys.
     */
    <T> List<StateValue<T>> loadBulk(String correlationId, List<String> keys);

    /**
     * Saves state into the store.
     *
     * @param correlationId (optional) transaction id to trace execution through call chain.
     * @param key           a unique state key.
     * @param value         a state value.
     * @param <T>
     * @return The state that was stored in the store.
     */
    <T> T save(String correlationId, String key, T value);

    /**
     * Deletes a state from the store by its key.
     *
     * @param correlationId (optional) transaction id to trace execution through call chain.
     * @param key           a unique value key.
     * @param <T>
     * @return deleted item.
     */
    <T> T delete(String correlationId, String key);
}
