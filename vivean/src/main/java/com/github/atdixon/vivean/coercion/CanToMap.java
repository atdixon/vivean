package com.github.atdixon.vivean.coercion;

import java.util.Map;

public interface CanToMap<K, V> {

    Map<K, V> toMap();

}
