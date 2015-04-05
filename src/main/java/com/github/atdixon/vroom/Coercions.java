/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.atdixon.vroom;

import java.util.Map;

final class Coercions {

    private Coercions() {}

    static boolean toBoolean(Object o) throws CannotCoerceException {
        if (o == null) {
            throw new CannotCoerceException();
        } else if (o instanceof Boolean) {
            return (Boolean) o;
        } else if (o instanceof String) {
            if ("true".equals(o)) {
                return true;
            } else if ("false".equals(o)) {
                return false;
            }
        }
        throw new CannotCoerceException();
    }

    static boolean toBooleanOr(Object o, boolean def) {
        if (o == null) {
            return def;
        } else if (o instanceof Boolean) {
            return (Boolean) o;
        } else if (o instanceof String) {
            if ("true".equals(o)) {
                return true;
            } else if ("false".equals(o)) {
                return false;
            }
        }
        return def;
    }

    static int toIntegerOr(Object o, int def) {
        if (o == null) {
            return def;
        } else if (o instanceof Integer) {
            return (Integer) o;
        } else if (o instanceof Number) {
            return ((Number) o).intValue();
        }
        if (o instanceof String) {
            try {
                return Integer.parseInt((String)o);
            } catch (NumberFormatException e) {/*ok*/}
        }
        return def;
    }

    static int toInteger(Object o) throws CannotCoerceException {
        if (o == null) {
            throw new CannotCoerceException();
        } else if (o instanceof Integer) {
            return (Integer) o;
        } else if (o instanceof Number) {
            return ((Number) o).intValue();
        }
        if (o instanceof String) {
            try {
                return Integer.parseInt((String)o);
            } catch (NumberFormatException e) {/*ok*/}
        }
        throw new CannotCoerceException();
    }

    static float toFloat(Object o) throws CannotCoerceException {
        if (o == null) {
            throw new CannotCoerceException();
        } else if (o instanceof Float) {
            return (Float) o;
        } else if (o instanceof Number) {
            return ((Number) o).floatValue();
        }
        if (o instanceof String) {
            try {
                return Float.parseFloat((String) o);
            } catch (NumberFormatException e) {/*ok*/}
        }
        throw new CannotCoerceException();
    }

    static Integer toIntegerOr(Object o, Integer def) {
        if (o == null) {
            return def;
        } if (o instanceof Integer) {
            return (Integer) o;
        } else if (o instanceof Number) {
            return ((Number) o).intValue();
        }
        if (o instanceof String) {
            try {
                return Integer.valueOf((String) o);
            } catch (NumberFormatException e) {/*ok*/}
        }
        return def;
    }

    static Long toLongOr(Object o, Long def) {
        if (o == null) {
            return def;
        }
        if (o instanceof Long) {
            return (Long) o;
        } else if (o instanceof Number) {
            return ((Number) o).longValue();
        }
        if (o instanceof String) {
            try {
                return Long.valueOf((String) o);
            } catch (NumberFormatException e) {/*ok*/}
        }
        return def;
    }

    static float toFloatOr(Object o, float def) {
        if (o == null) {
            return def;
        } else if (o instanceof Float) {
            return (Float) o;
        } else if (o instanceof Number) {
            return ((Number) o).floatValue();
        }
        if (o instanceof String) {
            try {
                return Float.parseFloat((String) o);
            } catch (NumberFormatException e) {/*ok*/}
        }
        return def;
    }

    static double toDouble(Object o) throws CannotCoerceException {
        if (o == null) {
            throw new CannotCoerceException();
        } else if (o instanceof Double) {
            return (Double) o;
        } else if (o instanceof Number) {
            return ((Number) o).doubleValue();
        }
        if (o instanceof String) {
            try {
                return Double.parseDouble((String)o);
            } catch (NumberFormatException e) {/*ok*/}
        }
        throw new CannotCoerceException();
    }

    static double toDoubleOr(Object o, double def) {
        if (o == null) {
            return def;
        } else if (o instanceof Double) {
            return (Double) o;
        } else if (o instanceof Number) {
            return ((Number) o).doubleValue();
        }
        if (o instanceof String) {
            try {
                return Double.parseDouble((String) o);
            } catch (NumberFormatException e) {/*ok*/}
        }
        return def;
    }

    static Boolean toBooleanOr(Object o, Boolean def) {
        if (o == null) {
            return def;
        } if (o instanceof Boolean) {
            return (Boolean) o;
        } else if (o instanceof String) {
            if ("true".equals(o)) {
                return Boolean.TRUE;
            } else if ("false".equals(o)) {
                return Boolean.FALSE;
            }
        }
        return def;
    }

    static Double toDoubleOr(Object o, Double def) {
        if (o == null) {
            return def;
        } else if (o instanceof Double) {
            return (Double) o;
        } else if (o instanceof Number) {
            return ((Number) o).doubleValue();
        }
        if (o instanceof String) {
            try {
                return Double.valueOf((String)o);
            } catch (NumberFormatException e) {/*ok*/}
        }
        return def;
    }

    static Float toFloatOr(Object o, Float def) {
        if (o == null) {
            return def;
        } else if (o instanceof Float) {
            return (Float) o;
        } else if (o instanceof Number) {
            return ((Number) o).floatValue();
        }
        if (o instanceof String) {
            try {
                return Float.valueOf((String)o);
            } catch (NumberFormatException e) {/*ok*/}
        }
        return def;
    }

    static String toStringOr(Object o, String def) {
        if (o == null) {
            return def;
        }
        return o instanceof String ? (String) o : o.toString();
    }

    static String toString(Object o) {
        if (o == null) {
            throw new CannotCoerceException();
        }
        return o instanceof String ? (String) o : o.toString();
    }

    @SuppressWarnings("unchecked")
    static <T> T to(Object value, Class<T> type, T def) {
        Util.insist(!Util.isCollectionType(type));
        if (type == Boolean.class || type == boolean.class) {
            return (T) toBooleanOr(value, (Boolean) def);
        } else if (type == Integer.class || type == int.class) {
            return (T) toIntegerOr(value, (Integer) def);
        } else if (type == Long.class || type == long.class) {
            return (T) toLongOr(value, (Long) def);
        } else if (type == Float.class || type == float.class) {
            return (T) toFloatOr(value, (Float) def);
        } else if (type == Double.class || type == double.class) {
            return (T) toDoubleOr(value, (Double) def);
        } else if (type == String.class) {
            return (T) toStringOr(value, (String) def);
        } else if (type == Map.class || type == VMap.class || type.isInterface()) {
            final Map<String, Object> valueAsMap;
            if (value == null) {
                return def;
            } else if (value instanceof Map) {
                valueAsMap = (Map<String, Object>) value;
            } else if (value instanceof VMap) {
                valueAsMap = ((VMap) value).asMap();
            } else {
                throw new CannotCoerceException("not a map: " + value);
            }

            if (valueAsMap.isEmpty()) {
                return def;
            }
            if (type == Map.class) {
                return (T) valueAsMap;
            }
            if (type == VMap.class) {
                return (T) VMap.of(valueAsMap);
            }
            // assert: type.isInterface()
            return ProxyFactory.adapt(valueAsMap, type);
        } else if (value == null) {
            return def;
        } else {
            throw new IllegalArgumentException("unsupported: " + type);
        }
    }

}
