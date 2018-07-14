package com.shaishavgandhi.bundlex.compiler

import com.google.auto.common.MoreElements
import com.squareup.kotlinpoet.*
import java.io.File
import java.util.HashMap
import javax.annotation.processing.Messager
import javax.lang.model.element.Element
import javax.lang.model.type.DeclaredType
import javax.lang.model.type.TypeMirror
import javax.lang.model.util.Elements
import javax.lang.model.util.Types
import javax.tools.Diagnostic

class BundleExtensionWriter(
    val messager: Messager,
    val typeUtils: Types,
    val elementUtils: Elements) {

    val bundleClass = ClassName.bestGuess("android.os.Bundle")
    val kotlinArrayList = ClassName("kotlin.collections", "ArrayList")

    /**
     * Java type -> Kotlin type mapper.
     * When we're using annotation processing, the type of the
     * elements still come as Java types. Since we're generating
     * Kotlin extensions, this isn't ideal. Fortunately, we're
     * dealing with a limited set of types with SharedPreferences,
     * so we can map them by hand.
     */
    private val kotlinMapper = hashMapOf(
        "java.lang.String" to ClassName("kotlin", "String"),

        "java.lang.Long" to ClassName.bestGuess("kotlin.Long"),
        "long" to ClassName.bestGuess("kotlin.Long"),
        "long[]" to ClassName.bestGuess("LongArray"),
        "java.lang.Long[]" to ClassName.bestGuess("LongArray"),

        "int" to ClassName.bestGuess("kotlin.Int"),
        "java.lang.Integer" to ClassName.bestGuess("kotlin.Int"),
        "int[]" to ClassName.bestGuess("IntArray"),
        "java.lang.Integer[]" to ClassName.bestGuess("IntArray"),

        "boolean" to ClassName.bestGuess("kotlin.Boolean"),
        "java.lang.Boolean" to ClassName.bestGuess("kotlin.Boolean"),
        "boolean[]" to ClassName("kotlin", "BooleanArray"),
        "java.lang.Boolean[]" to ClassName("kotlin", "BooleanArray"),

        "float" to ClassName.bestGuess("kotlin.Float"),
        "java.lang.Float" to ClassName.bestGuess("kotlin.Float"),
        "float[]" to ClassName("kotlin", "FloatArray"),
        "java.lang.Float[]" to ClassName("kotlin", "FloatArray"),

        "java.lang.Double" to ClassName("kotlin", "Double"),
        "double" to ClassName("kotlin", "Double"),
        "double[]" to ClassName("kotlin", "DoubleArray"),
        "java.lang.Double[]" to ClassName("kotlin", "DoubleArray"),

        "byte" to ClassName("kotlin", "Byte"),
        "java.lang.Byte" to ClassName("kotlin", "Byte"),
        "byte[]" to ClassName("kotlin", "ByteArray"),
        "java.lang.Byte[]" to ClassName("kotlin", "ByteArray"),

        "short" to ClassName("kotlin", "Short"),
        "java.lang.Short" to ClassName("kotlin","Short"),
        "short[]" to ClassName("kotlin", "ShortArray"),
        "java.lang.Short[]" to ClassName("kotlin","ShortArray"),

        "char" to ClassName("kotlin", "Char"),
        "java.lang.Character" to ClassName("kotlin", "Char"),
        "char[]" to ClassName("kotlin", "CharArray"),
        "java.lang.Character[]" to ClassName("kotlin", "CharArray"),

        "java.lang.CharSequence" to ClassName("kotlin", "CharSequence"),
        "java.lang.CharSequence[]" to ParameterizedTypeName.get(
            ClassName("kotlin", "Array"),
            ClassName("kotlin", "CharSequence")),
        "java.util.ArrayList<java.lang.CharSequence>" to ParameterizedTypeName.get(
            kotlinArrayList, ClassName("kotlin", "CharSequence"))
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
            put("java.lang.Long[]", "LongArray")
            put("double", "Double")
            put("java.lang.Double", "Double")
            put("double[]", "DoubleArray")
            put("java.lang.Double[]", "DoubleArray")
            put("float", "Float")
            put("java.lang.Float", "Float")
            put("float[]", "FloatArray")
            put("java.lang.Float[]", "FloatArray")
            put("byte", "Byte")
            put("java.lang.Byte", "Byte")
            put("byte[]", "ByteArray")
            put("java.lang.Byte[]", "ByteArray")
            put("short", "Short")
            put("java.lang.Short", "Short")
            put("short[]", "ShortArray")
            put("java.lang.Short[]", "ShortArray")
            put("char", "Char")
            put("java.lang.Character", "Char")
            put("char[]", "CharArray")
            put("java.lang.Character[]", "CharArray")
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

            preprocessElement(element)

            val returnType: TypeName = if (kotlinMapper[element.asType().toString()] != null)
                kotlinMapper[element.asType().toString()]!! else ClassName.bestGuess(element.asType().toString())

            val bundleMapType = returnType(element)
            val name = element.simpleName.toString()
            val getterName = "get${name.capitalize()}"
            val putterName = "put${name.capitalize()}"

            val isCastNecessary = castingNecessary(bundleMapType)

            // Nullable getter
            val nullableGetterBuilder = FunSpec.builder(getterName)
                .receiver(bundleClass)
                .returns(returnType.asNullable())

            fileBuilder.addFunction(
                addReturnStatement(bundleMapType, key, returnType, nullableGetterBuilder, isCastNecessary)
                    .build()
            )

            // Non-null getter
            val nonNullGetterBuilder = FunSpec.builder(getterName)
                .receiver(bundleClass)
                .addParameter(ParameterSpec.builder("defaultValue", returnType)
                    .build())
                .returns(returnType)
                .beginControlFlow("if (containsKey(\"%L\"))", key)


            fileBuilder.addFunction(
                addReturnStatement(bundleMapType, key, returnType, nonNullGetterBuilder, isCastNecessary)
                .endControlFlow()
                .addStatement("return defaultValue")
                .build())

            // Putter
            fileBuilder.addFunction(FunSpec.builder(putterName)
                .receiver(bundleClass)
                .addParameter(ParameterSpec.builder("value", returnType).build())
                .addStatement("put%L(\"%L\", %L)", bundleMapType, key, "value")
                .build())

        }

        fileBuilder.build().writeTo(outputDir)
    }


    /**
     * Pre-processes the elements in case they are Parcelable,
     * SparseArray<Parcelable> or Serializable. Since the given
     * elements are actually subtypes of the aforementioned types,
     * we add them to the [kotlinMapper] as well as [typeMapper]
     * to get their types while code generating.
     *
     * @param element to pre-process
     */
    private fun preprocessElement(element: Element) {
        if (isParcelable(typeUtils, elementUtils, element.asType())) {
            // Add it to the Bundle mapper.
            typeMapper[element.asType().toString()] = "Parcelable"
        } else if (isParcelableList(typeUtils, elementUtils, element.asType())) {
            // Add it to the Bundle mapper.
            typeMapper[element.asType().toString()] = "ParcelableArrayList"

            // Convert the java.util.ArrayList to kotlin.collections.ArrayList
            // Even if the element is declared in Kotlin class, the underlying JVM
            // impl is still java.util.ArrayList and that's what we get.
            val genericType = (element.asType() as DeclaredType).typeArguments[0].asTypeName()
            kotlinMapper[element.asType().toString()] = ParameterizedTypeName.get(kotlinArrayList, genericType)
        } else if (isSparseParcelableArrayList(typeUtils, elementUtils, element.asType())) {
            // Add it to the Bundle mapper.
            typeMapper[element.asType().toString()] = "SparseParcelableArray"

            // Add to the kotlin mapper.
            kotlinMapper[element.asType().toString()] = element.asType().asTypeName()
        } else if (isParcelableArray(typeUtils, elementUtils, element.asType())) {
            // Add it to the Bundle mapper.
            typeMapper[element.asType().toString()] = "ParcelableArray"
            // Add to the kotlin mapper.
            kotlinMapper[element.asType().toString()] = element.asType().asTypeName()
        } else if (isSerializable(typeUtils, elementUtils, element.asType()) && typeMapper[element.asType().toString()] == null) {
            typeMapper[element.asType().toString()] = "Serializable"
        }
    }

    /**
     * Adds return statement to the getter. Takes
     * care of whether we have to add type casting
     * like with Serializable or Parcelable arrays.
     *
     * @param bundleMapType Corresponding mapper to Bundle
     * @param key of the Bundle
     * @param returnType type to be returned
     * @param function to add return statement to.
     * @param castNecessary whether casting is necessary
     *
     * @return function builder with the return statement.
     */
    internal fun addReturnStatement(bundleMapType: String,
                           key: String,
                           returnType: TypeName,
                           function: FunSpec.Builder,
                           castNecessary: Boolean = false): FunSpec.Builder {
        if (castNecessary) {
            return function.addStatement("return get%L(\"$key\") as %T", bundleMapType, returnType)
        } else {
            return function.addStatement("return get%L(\"$key\")", bundleMapType)
        }
    }

    /**
     * Check if casting is necessary for the given bundleMapType
     *
     * @param bundleMapType
     * @return whether casting is necessary
     */
    private fun castingNecessary(bundleMapType: String): Boolean {
        return bundleMapType == "Serializable" || bundleMapType == "ParcelableArray"
    }

    /**
     * Gives the return type of the given element. Checks
     * the typeMapper
     *
     * @param element
     * @return returnType of element
     */
    internal fun returnType(element: Element): String {
        val returnType = typeMapper[element.asType().toString()]

        if (returnType == null) {
            messager.printMessage(Diagnostic.Kind.ERROR, "Element ${element.simpleName} cannot be used with Bundle")
        }
        return returnType!!
    }

    private fun isParcelable(
        typeUtils: Types,
        elementUtils: Elements,
        typeMirror: TypeMirror
    ): Boolean {
        return typeUtils.isAssignable(typeMirror, elementUtils.getTypeElement("android.os.Parcelable").asType())
    }

    private fun isParcelableArray(
        typeUtils: Types,
        elementUtils: Elements,
        typeMirror: TypeMirror
    ): Boolean {
        return typeUtils.isAssignable(typeMirror, typeUtils.getArrayType(elementUtils.getTypeElement("android.os.Parcelable").asType()))
    }

    private fun isParcelableList(
        typeUtils: Types,
        elementUtils: Elements,
        typeMirror: TypeMirror
    ): Boolean {
        val type = typeUtils.getDeclaredType(
            elementUtils.getTypeElement("java.util" + ".ArrayList"),
            elementUtils.getTypeElement("android.os.Parcelable").asType()
        )
        if (typeUtils.isAssignable(typeUtils.erasure(typeMirror), type)) {
            val typeArguments = (typeMirror as DeclaredType).typeArguments
            return typeArguments != null &&
                    typeArguments.size >= 1 &&
                    typeUtils.isAssignable(typeArguments[0], elementUtils.getTypeElement("android.os.Parcelable").asType())
        }
        return false
    }

    private fun isSparseParcelableArrayList(
        typeUtils: Types,
        elementUtils: Elements,
        typeMirror: TypeMirror
    ): Boolean {
        val type = typeUtils.getDeclaredType(
            elementUtils.getTypeElement("android.util.SparseArray"),
            elementUtils.getTypeElement("android.os.Parcelable").asType()
        )
        if (typeUtils.isAssignable(typeUtils.erasure(typeMirror), type)) {
            val typeArguments = (typeMirror as DeclaredType).typeArguments
            return typeArguments != null && typeArguments.size >= 1 &&
                    typeUtils.isAssignable(typeArguments[0], elementUtils.getTypeElement("android.os.Parcelable").asType())
        }
        return false
    }

    private fun isSerializable(
        typeUtils: Types,
        elementUtils: Elements,
        typeMirror: TypeMirror
    ): Boolean {
        return typeUtils.isAssignable(typeMirror, elementUtils.getTypeElement("java.io.Serializable").asType())
    }
}