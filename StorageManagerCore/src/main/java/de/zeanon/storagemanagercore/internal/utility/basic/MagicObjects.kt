package de.zeanon.storagemanagercore.internal.utility.basic

/**
 * This class exists since Java is incapable to handle Type-Parameters properly...
 */
class MagicObjects {

    inline fun <reified T> toDefInternal(obj: Any): T {
        when (T::class) {
            Integer::class -> {
                return Objects.toInt(obj) as T
            }
            Long::class -> {
                return Objects.toLong(obj) as T
            }
            Double::class -> {
                return Objects.toDouble(obj) as T
            }
            Float::class -> {
                return Objects.toFloat(obj) as T
            }
            Short::class -> {
                return Objects.toShort(obj) as T
            }
            Boolean::class -> {
                return Objects.toBoolean(obj) as T
            }
            String::class -> {
                return Objects.toString(obj) as T
            }
            else -> {
                return obj as T
            }
        }
    }
}