# InMemoryCache

A high-performance, thread-safe in-memory caching library with configurable eviction policies and automatic entry expiration.

## ✅ Expectations from Interviewer

You're expected to:

- Design a generic, reusable, in-memory cache library
- Support pluggable eviction policies like LRU, TTL
- Ensure thread safety
- Allow configuration for max size, eviction strategy, TTL per entry
- Possibly expose APIs like put(K key, V value), get(K key), remove(K key), size()
- Write clean code with proper abstractions and testable structure

## Overview

This InMemoryCache implementation provides a generic, thread-safe caching solution with the following features:

- Generic key-value pair storage
- Thread-safety using ReentrantLock
- Configurable maximum capacity
- Pluggable eviction policies (LRU implementation provided)
- Time-based entry expiration
- O(1) complexity for common operations (get, put, remove)

## Architecture

The system follows a modular design with clean separation of concerns:

### Core Components

1. **Cache Interface (`Cache<K,V>`)**: Defines the contract for all cache implementations
   - `get(K key)`: Retrieve a value by key
   - `put(K key, V value)`: Store a value with the given key
   - `remove(K key)`: Remove an entry by key
   - `size()`: Get the current cache size

2. **InMemoryCache Implementation**: Thread-safe cache implementation using HashMap for storage
   - Implements automatic eviction when capacity is reached
   - Supports entry expiration
   - Thread-safe operation using ReentrantLock

3. **Eviction Policy Interface (`EvictionPolicy<K>`)**: Defines contract for cache eviction strategies
   - `evictKey()`: Determine which key to evict next
   - `keyAccessed(K key)`: Update access metadata for a key

4. **LRU Eviction Policy**: Least Recently Used implementation
   - Uses a doubly linked list for O(1) operations
   - Maintains access order for efficient eviction decisions

5. **Supporting Classes**:
   - `CacheEntry<V>`: Wrapper for values with expiration metadata
   - `DoublyLinkedList<K>`: Custom doubly linked list for LRU implementation
   - `Node<K>`: Node in the doubly linked list

## Class Diagram

```
┌───────────────┐     uses     ┌──────────────────┐
│  Cache<K,V>   │◄────────────┤    Client Code    │
└───────┬───────┘              └──────────────────┘
        │
        │ implements
        ▼
┌───────────────────┐    uses    ┌────────────────┐
│ InMemoryCache<K,V>│◄──────────┤EvictionPolicy<K>│
└───────────────────┘            └────────┬───────┘
        │                                 │
        │ contains                        │ implements
        ▼                                 ▼
┌───────────────┐               ┌─────────────────┐
│CacheEntry<V>   │              │LRUEvictionPolicy│
└───────────────┘               └────────┬────────┘
                                         │
                                         │ uses
                                         ▼
                                ┌─────────────────┐
                                │DoublyLinkedList │
                                └────────┬────────┘
                                         │
                                         │ contains
                                         ▼
                                ┌─────────────────┐
                                │     Node<K>     │
                                └─────────────────┘
```

## Usage Examples

### Basic Usage

```java
// Create cache with LRU eviction and capacity of 100
Cache<String, User> userCache = new InMemoryCache<>(new LRUEvictionPolicy<>(), 100);

// Store a value
userCache.put("user:1", new User("Alice", "alice@example.com"));

// Retrieve a value
User user = userCache.get("user:1");

// Remove a value
userCache.remove("user:1");
```

### Custom Eviction Policy

You can implement your own eviction policy by implementing the `EvictionPolicy<K>` interface:

```java
public class FIFOEvictionPolicy<K> implements EvictionPolicy<K> {
    private final Queue<K> queue = new LinkedList<>();
    private final Set<K> keySet = new HashSet<>();
    
    @Override
    public K evictKey() {
        if (queue.isEmpty()) return null;
        K key = queue.poll();
        keySet.remove(key);
        return key;
    }
    
    @Override
    public void keyAccessed(K key) {
        if (!keySet.contains(key)) {
            queue.add(key);
            keySet.add(key);
        }
    }
}
```

## Thread Safety

The InMemoryCache implementation is thread-safe using Java's ReentrantLock. All cache operations (get, put, remove, size) are synchronized to ensure consistent behavior in a concurrent environment.

## Testing

The caching system includes a comprehensive test suite covering:

1. Basic operations (put, get, remove, size)
2. Eviction policy behavior
3. Entry expiration
4. Thread safety with concurrent operations
5. Edge cases (null values, policy interaction)

To run the tests:

```bash
mvn test
```

## Performance Considerations

- Get/Put operations have O(1) complexity
- Thread synchronization may impact performance under high concurrency
- The LRU implementation maintains O(1) complexity for all operations
- Memory usage is proportional to the configured capacity
- Expired entries are lazily removed on access

## Future Enhancements

- Add support for write-through/write-behind strategies
- Implement additional eviction policies (LFU, FIFO, etc.)
- Add statistics collection for cache hits/misses
- Support for distributed caching
- Add callback mechanisms for eviction events
