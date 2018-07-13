package com.shaishavgandhi.bundlex.compiler

import com.google.auto.service.AutoService
import com.shaishavgandhi.navigator.Extra
import java.io.File
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements
import javax.lang.model.util.Types

@AutoService(Processor::class)
class BundleProcessor: AbstractProcessor() {

    private lateinit var elementUtils: Elements
    private lateinit var typeUtils: Types
    private lateinit var messager: Messager
    private lateinit var options: Map<String, String>
    private lateinit var outputDir: File

    override fun init(processingEnv: ProcessingEnvironment) {
        super.init(processingEnv)

        elementUtils = processingEnv.elementUtils
        typeUtils = processingEnv.typeUtils
        messager = processingEnv.messager
        options = processingEnv.options

        outputDir = processingEnv.options["kapt.kotlin.generated"]
            ?.replace("kaptKotlin", "kapt")
            ?.let(::File)
                ?: throw IllegalStateException(
            "No kapt.kotlin.generated option provided")

    }

    override fun process(set: MutableSet<out TypeElement>, roundedEnv: RoundEnvironment): Boolean {
        val elements = roundedEnv.getElementsAnnotatedWith(Extra::class.java)
        if (elements.isEmpty()) {
            return false
        }

        val writer = BundleExtensionWriter(messager, typeUtils, elementUtils)
        writer.writeExtensions(elements, outputDir)

        return true
    }

    override fun getSupportedAnnotationTypes(): Set<String> = setOf(Extra::class.java.canonicalName)

    override fun getSupportedSourceVersion(): SourceVersion = SourceVersion.latestSupported()

}