package DAO;

import java.util.List;
import java.util.Optional;

public interface BaseDao<T> {
    /**
     * Returns an object by its ID, if its found. Otherwise returns an empty optional (This is a common design pattern in Rust, I hope it's acceptable in Java)
     * 
     * @param id ID of the object to get from database
     * @return The object, if its found, otherwise an empty optional.
     */
    Optional<T> getItemById(int id);

    /**
     * Retrieves all items in the database.
     * @return A list of all items in the database.
     */
    List<T> getAllItems();

    /**
     * Insert a new item into the database.
     * @param item The item to insert.
     * @return True if item was inserted, and false if item failed to insert.
     */
    Boolean insert(T item);

    /**
     * Updates an existing item in the database.
     * @param item The item to update.
     * @return True if item was updated, and false if it failed to find the item.
     */
    Boolean update(T item);
    
    /**
     * Deletes an item from the database.
     * @param item The item to be deleted
     * @return True if item was deleted, and false if it failed to find the item.
     */
    Boolean delete(T item);
}