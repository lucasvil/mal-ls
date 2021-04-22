package org.mal.ls.contexts;

public interface LanguageServerContext {
  <V> V get(Key<V> key);

  <V> void put(Key<V> key, V value);

  class Key<K> {
  }
}
