package com.github.atdixon.vroom.coercion;

import java.util.Map;

public interface CanToMap<K, V> {

    Map<K, V> toMap();

}
