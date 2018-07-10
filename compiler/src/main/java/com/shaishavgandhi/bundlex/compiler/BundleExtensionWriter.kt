package com.shaishavgandhi.bundlex.compiler

import com.google.auto.common.MoreElements
import com.squareup.kotlinpoet.*
import java.io.File
import java.util.HashMap
import javax.annotation.processing.Messager
import javax.lang.model.element.Element
import javax.tools.Diagnostic

class BundleExtensionWriter(val messager: Messager) {

    val bundleClass = ClassName.bestGuess("android.os.Bundle")

    /**
     * Java type -> Kotlin type mapper.
     * When we're using annotation processing, the type of the
     * elements still come as Java types. Since we're generating
     * Kotlin extensions, this isn't ideal. Fortunately, we're
     * dealing with a limited set of types with SharedPreferences,
     * so we can map them by hand.
     */
    private val kotlinMapper = hashMapOf(
        "java.lang.String" to ClassName.bestGuess("kotlin.String"),
        "java.lang.Long" to ClassName.bestGuess("kotlin.Long"),
        "long" to ClassName.bestGuess("kotlin.Long"),
        "int" to ClassName.bestGuess("kotlin.Int"),
        "java.lang.Integer" to ClassName.bestGuess("kotlin.Int"),
        "boolean" to ClassName.bestGuess("kotlin.Boolean"),
        "java.lang.Boolean" to ClassName.bestGuess("kotlin.Boolean"),
        "float" to ClassName.bestGuess("kotlin.Float"),
        "java.lang.Float" to ClassName.bestGuess("kotlin.Float"),
        "int[]" to ClassName.bestGuess("IntArray"),
        "java.lang.Integer[]" to ClassName.bestGuess("IntArray")
    )

    private val typeMapper = object : HashMap<String, String>() {
        init {
            put("java.lang.String", "String")
            put("java.lang.String[]", "StringArray")
            put("java.util.ArrayList<java.lang.String>", "StringArrayList")
            put("java.lang.Integer", "Int")
            put("int", "Int")
            put("int[]", "IntArray")
            put("java.lang.Integer[]", "IntArray")
            put("java.util.ArrayList<java.lang.Integer>", "IntegerArrayList")
            put("java.lang.Long", "Long")
            put("long", "Long")
            put("long[]", "LongArray")
            put("double", "Double")
            put("java.lang.Double", "Double")
            put("double[]", "DoubleArray")
            put("float", "Float")
            put("java.lang.Float", "Float")
            put("float[]", "FloatArray")
            put("byte", "Byte")
            put("byte[]", "ByteArray")
            put("short", "Short")
            put("short[]", "ShortArray")
            put("char", "Char")
            put("char[]", "CharArray")
            put("java.lang.CharSequence", "CharSequence")
            put("java.lang.CharSequence[]", "CharSequenceArray")
            put("java.util.ArrayList<java.lang.CharSequence>", "CharSequenceArrayList")
            put("android.util.Size", "Size")
            put("android.util.SizeF", "SizeF")
            put("boolean", "Boolean")
            put("boolean[]", "BooleanArray")
            put("java.lang.Boolean", "Boolean")
            put("java.lang.Boolean[]", "BooleanArray")
            put("android.os.Parcelable", "Parcelable")
            put("android.os.Parcelable[]", "ParcelableArray")
            put("java.util.ArrayList<android.os.Parcelable>", "ParcelableArrayList")
        }
    }

    fun writeExtensions(elements: Set<Element>, outputDir: File) {
        val packageName = MoreElements.getPackage(elements.first()).toString()
        val fileBuilder = FileSpec.builder(packageName, "BundleExtensions")
        for (element in elements) {
            val key = element.getKey()
            val returnType: TypeName? = kotlinMapper[element.asType().toString()]
            val bundleMapType = returnType(element)
            if (returnType != null) {
                val name = element.simpleName.toString()
                val getterName = "get${name.capitalize()}"
                fileBuilder.addFunction(FunSpec.builder(getterName)
                    .receiver(bundleClass)
                    .addParameter(ParameterSpec.builder("defaultValue", returnType.asNullable())
                        .build())
                    .returns(returnType)
                    .addStatement("return get%L(\"$key\")", bundleMapType)
                    .build())
            }
        }

        fileBuilder.build().writeTo(outputDir)
    }

    fun returnType(element: Element): String {
        val returnType = typeMapper[element.asType().toString()]
        if (returnType == null) {
            // TODO: Do parcelable, serializable checks here
            messager.printMessage(Diagnostic.Kind.WARNING, element.asType().toString())
        }
        return returnType!!
    }
}