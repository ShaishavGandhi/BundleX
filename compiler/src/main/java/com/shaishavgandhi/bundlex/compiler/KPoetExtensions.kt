package com.shaishavgandhi.bundlex.compiler

import com.shaishavgandhi.navigator.Extra
import javax.lang.model.element.Element

/**
 * Returns the key of the element annotated with @[Extra].
 * Checks if @[Extra.key] is specified and returns that
 * value, otherwise just returns the name of the element.
 *
 * @return [String]
 */
fun Element.getKey(): String {

    if (getAnnotation(Extra::class.java).key.isNotEmpty()) {
        return getAnnotation(Extra::class.java).key
    }
    return simpleName.toString()

}