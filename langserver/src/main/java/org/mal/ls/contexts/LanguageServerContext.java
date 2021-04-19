package org.mal.ls.contexts;

public interface LanguageServerContext {
  <V> void put(Key<V> key, V value);

  <V> V get(Key<V> key);

  class Key<K> {
  }
}
