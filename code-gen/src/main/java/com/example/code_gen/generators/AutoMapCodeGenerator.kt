package com.example.code_gen.generators

import com.example.annotations.AutoMap
import com.example.code_gen.PropertyInfo
import com.example.code_gen.utils.getAnnotationParameterValue
import com.example.code_gen.utils.getProperty
import com.example.code_gen.utils.hasDuplicateNames
import com.example.code_gen.utils.isNullable
import com.example.code_gen.utils.isNumber
import com.example.code_gen.utils.isPublicDataClass
import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.DelicateKotlinPoetApi
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.ParameterizedTypeName
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.plusParameter
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.TypeVariableName
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.asTypeName
import kotlinx.metadata.KmClass
import kotlinx.metadata.jvm.KotlinClassMetadata
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement
import javax.lang.model.element.VariableElement
import javax.tools.Diagnostic

/*
 * Bugs:
 * - Do not save processingEnv.typeUtils as a class property - Processor will not generate code.
 * - When you annotate a class named GenEntity, the toMap will crash for some reason.
 *
 * Info:
 * - If you want KotlinPoet to automatically add your imports add functions as MemberName (%M) and
 * types as ClassName (%T) instead of simple literals.
 *
 * Formatting notes:
 * %T - type
 * %M - member
 * %S - string output
 * %L - literal
 */

@AutoService(Processor::class)
class AutoMapCodeGenerator : AbstractProcessor() {
    override fun getSupportedAnnotationTypes(): Set<String> {
        return setOf(AutoMap::class.java.canonicalName)
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latestSupported()
    }

    override fun process(
        annotations: Set<TypeElement>,
        roundEnv: RoundEnvironment
    ): Boolean {
        val annotatedElements: Set<Element> = roundEnv.getElementsAnnotatedWith(AutoMap::class.java)

        // This processor can be called again after it process AutoMap annotation. Do nothing.
        if (annotatedElements.isEmpty()) return true

        if (hasDuplicateNames(annotatedElements)) {
            val exception =
                Exception("Duplicate class name were found in classes annotated with AutoMap")
            processingEnv.messager.printMessage(Diagnostic.Kind.ERROR, exception.toString())
            throw exception
        }

        val annotatedTypes: List<TypeElement> =
            annotatedElements.filterIsInstance(TypeElement::class.java)
        val annotatedFields: List<Element> =
            annotatedElements.filter { it.kind == ElementKind.FIELD }

        createProcessorClass(annotatedTypes)
        createMappers(annotatedTypes, annotatedFields)

        return true
    }

    private fun createProcessorClass(elements: Collection<TypeElement>) {
        val codeBlockBuilder: CodeBlock.Builder = CodeBlock.builder()
            .addStatement("val className = map[%S]", "class")
            .addStatement("return when(className) {")
            .indent()

        // For each annotated class:
        // 1. Check its annotated className
        // 2. Convert it from map to its appropriate value
        // 3. Cast to T to return as generic, not Any
        elements.forEach { element ->
            // Getting className value for this annotated class. Don't add "when" branch if doesn't have value.
            val annotationValue =
                getAnnotationParameterValue(element, AutoMap::class.java, "className")
                    ?: return@forEach

            // Adding toEntity function as a MemberName for KotlinPoet to automatically include import if needed
            val toEntity = MemberName(AUTO_MAP_PACKAGE, "to${element.simpleName}")
            codeBlockBuilder.addStatement(
                "%S -> map.%M() as? T",
                annotationValue,
                toEntity
            )
        }

        codeBlockBuilder
            .addStatement("else -> null")
            .unindent()
            .addStatement("}")

        val resolveFunc = FunSpec.builder("resolveToEntity")
            .addTypeVariable(TypeVariableName("T"))
            .addParameter("map", mapType)
            .addCode(codeBlockBuilder.build())
            .returns(TypeVariableName("T").copy(nullable = true))
            .addKdoc(RESOLVE_TO_ENTITY_KDOC)
            .build()

        val autoProcessor = TypeSpec.classBuilder("AutoMapProcessor")
            .addKdoc(PROCESSOR_CLASS_KDOC)
            .addFunction(resolveFunc)
            .build()

        // Add this class to the package of the "first" element annotated with AutoMap.
        FileSpec.builder(AUTO_MAP_PACKAGE, "AutoMapProcessor")
            .addType(autoProcessor)
            .addFileComment(FILE_COMMENT)
            .build()
            .writeTo(processingEnv.filer)
    }

    private fun createMappers(elements: Collection<TypeElement>, annotatedFields: List<Element>) {
        elements.forEach { element ->
            val annotatedFieldsOfThisElement = annotatedFields.filter {
                it.enclosingElement == element
            }
            generateMappersForClass(element, annotatedFieldsOfThisElement)
        }
    }

    @OptIn(DelicateKotlinPoetApi::class)
    private fun generateMappersForClass(element: TypeElement, annotatedFields: List<Element>) {
        val metadataAnnotation = element.getAnnotation(Metadata::class.java)
        val metadata = KotlinClassMetadata.read(metadataAnnotation)

        if (!metadata.isPublicDataClass()) return
        val kmClass = (metadata as? KotlinClassMetadata.Class)?.kmClass ?: return

        val properties: List<PropertyInfo> = collectPropertiesInformation(
            element = element,
            annotatedFields = annotatedFields,
            kmClass = kmClass,
        )

        val toMapFunSpec = createToMapFunction(element, properties)
        val fromMapFunSpec = createFromMapFunction(element, properties)

        val className: ClassName = element.asClassName()

        FileSpec.builder(AUTO_MAP_PACKAGE, className.simpleName)
            .addFunction(toMapFunSpec)
            .addFunction(fromMapFunSpec)
            .addFileComment(FILE_COMMENT)
            .build()
            .writeTo(processingEnv.filer)
    }

    private fun collectPropertiesInformation(
        element: TypeElement,
        annotatedFields: List<Element>,
        kmClass: KmClass,
    ): List<PropertyInfo> {
        return element.enclosedElements
            .filterIsInstance<VariableElement>()
            .map { variable ->
                // Read kotlin metadata to get information about this property's type.
                val prop = getProperty(kmClass, variable.simpleName.toString())
                val isNullable = isNullable(kmClass, variable.simpleName.toString())

                // There is no way to directly check if this property is annotated or not.
                // You have to get a list of annotated fields separately.
                val isAnnotated =
                    annotatedFields.firstOrNull { it.simpleName == variable.simpleName }
                        ?.getAnnotation(AutoMap::class.java) != null

                PropertyInfo(
                    name = variable.simpleName.toString(),
                    className = prop!!,
                    isNullable = isNullable,
                    isAnnotated = isAnnotated,
                )
            }
    }

    /**
     * Generates `toMap` function to convert a data class to Map<String, Any?>.
     *
     * If a `value` was provided to [AutoMap] parameter `className`, the map will have an additional
     * pair: `"class" to "your_className_value"`
     *
     * Generated code will look like:
     * ```
     * public fun MyEntity.toMap(): Map<String, Any?> {
     *      `val map: Map<String, Any?> = mapOf(
     *          "field1" to field1,
     *          "field2" to field2,
     *      )
     *      return map
     * }
     * ```
     * If data class properties are annotated with [AutoMap], they themselves will be converted to map:
     * ```
     * "field1" to field1.toMap()
     * ```
     */
    @OptIn(DelicateKotlinPoetApi::class)
    private fun createToMapFunction(
        element: TypeElement,
        variables: List<PropertyInfo>,
    ): FunSpec {
        val annotationValue =
            getAnnotationParameterValue(element, AutoMap::class.java, AUTO_MAP_CLASS_NAME_PARAMETER)

        val codeBlockBuilder: CodeBlock.Builder = CodeBlock.builder()
            .addStatement("val map: %T = %M(", mapType, mapOfFunction)
            .indent()

        // add this element class name as one of the params
        if (annotationValue != null) {
            codeBlockBuilder.addStatement(
                "%S to %S%L",
                "class",
                annotationValue.toString(),
                ","
            )
        }

        variables.forEachIndexed { index: Int, propertyInfo: PropertyInfo ->
            val comma = if (index < variables.lastIndex) "," else ""

            if (propertyInfo.isAnnotated) {
                // Use ? operator with function calls for if this element is nullable.
                val nullOperator = if (propertyInfo.isNullable) "?" else ""

                // Call toMap function generated by this processor on this element.
                val toMap = MemberName(AUTO_MAP_PACKAGE, "toMap")
                codeBlockBuilder.addStatement(
                    "%S to %L%L.%M()%L",
                    propertyInfo.name,
                    propertyInfo.name,
                    nullOperator,
                    toMap,
                    comma
                ) // "entity" to entity.toMap()
            } else {
                // Pass the value of the element as is.
                codeBlockBuilder.addStatement(
                    "%S to %L%L",
                    propertyInfo.name,
                    propertyInfo.name,
                    comma
                ) // "id" to id
            }
        }

        codeBlockBuilder
            .unindent()
            .addStatement(")")
            .addStatement("return map")

        val className: ClassName = element.asClassName()

        return FunSpec.builder("toMap")
            .receiver(className)
            .returns(mapType)
            .addKdoc("Converts [%T] to [%T]", className, mapClass)
            .addCode(codeBlockBuilder.build())
            .build()
    }


    /**
     * Generates `toEntity` function to convert a `Map<String, Any?>` back to original data class.
     *
     * If a `value` was provided to [AutoMap] parameter `className`, the map will have an additional
     * pair: `"class" to "your_className_value"`
     *
     * Generated code will look like:
     * ```
     * public fun Map<String, Any?>.toMyEntity(): MyEntity {
     *      val field1: String?** = this["field1"] as? String**
     *      val field2: String?** = this["field1"] as? String**
     *
     *      return MyEntity(
     *          field1 = field1 ?: throw Exception("Missing property")***,
     *          field2 = field2 ?: throw Exception("Missing property"),
     *      )
     * }
     * ```
     * ** - type of fields will match their original type.
     *
     * *** - nullability checks will be added only to non-nullable types.
     *
     * If data class properties are annotated with [AutoMap], they themselves will be converted
     * back to their entities:
     * ```
     * val field1: Entity? = (this["field1"] as? Map<String, Any?>)?.toMyEntity()
     * ```
     */
    @OptIn(DelicateKotlinPoetApi::class)
    private fun createFromMapFunction(
        element: TypeElement,
        variables: List<PropertyInfo>
    ): FunSpec {
        val className: ClassName = element.asClassName()
        val codeBlockBuilder: CodeBlock.Builder = CodeBlock.builder()

        // Read values for all fields from original map
        variables.forEach { propertyInfo: PropertyInfo ->
            val simpleName = propertyInfo.className.simpleName

            if (propertyInfo.isAnnotated) {
                // This value was converted to Map, convert it back.
                val toEntity = MemberName(AUTO_MAP_PACKAGE, "to$simpleName")

                codeBlockBuilder.addStatement(
                    "val %L: %T? = (this[%S] as? %T)?.%M()",
                    propertyInfo.name,
                    propertyInfo.className,
                    propertyInfo.name,
                    mapType,
                    toEntity
                )
            } else if (isNumber(propertyInfo.className)) {
                // This is a regular value. If it's Number, you can't use "as" casts.
                val toNumber = "to${propertyInfo.className.simpleName}"

                codeBlockBuilder.addStatement(
                    "val %L: %T? = (this[%S] as? %T)?.%L()",
                    propertyInfo.name,
                    propertyInfo.className,
                    propertyInfo.name,
                    Number::class,
                    toNumber
                )
            } else {
                // This is a regular value. Assign it as it is
                codeBlockBuilder.addStatement(
                    "val %L: %T? = this[%S] as? %T",
                    propertyInfo.name,
                    propertyInfo.className,
                    propertyInfo.name,
                    propertyInfo.className
                )
            }
        }

        // Return new Object of that Type
        codeBlockBuilder
            .addStatement("")
            .addStatement("return ${element.simpleName}(")
            .indent()

        // Initialize all its fields
        variables.forEachIndexed { index: Int, propertyInfo: PropertyInfo ->
            val comma = if (index < variables.lastIndex) "," else ""
            val name = propertyInfo.name
            val nullableMessage =
                if (!propertyInfo.isNullable) {
                    // Using interpunct character · prevents string line breaking, that can cause
                    // invalid strings in generated files. Alternatively use %S argument.
                    "·?:·throw·Exception(\"Missing·property·${name}\")"
                } else ""

            codeBlockBuilder.addStatement("%L = %L%L%L", name, name, nullableMessage, comma)
        }

        codeBlockBuilder
            .unindent()
            .addStatement(")")

        return FunSpec.builder("to${element.simpleName}")
            .receiver(mapType)
            .returns(className)
            .addKdoc("Converts [%T] to [%T]", mapClass, className)
            .addCode(codeBlockBuilder.build())
            .build()
    }

    companion object {
        const val PROCESSOR_CLASS_KDOC = "AutoMapProcessor contain functionality to help converting maps back to their original entities."
        const val RESOLVE_TO_ENTITY_KDOC = "Helps convert maps to their original entity. Important: only types that provide className value to their AutoMap annotation can be resolved."
        const val FILE_COMMENT = "This is a generated file. Do not edit"

        // Has to match the name of the parameter in AutoMap!
        const val AUTO_MAP_CLASS_NAME_PARAMETER = "className"
        const val AUTO_MAP_PACKAGE = "auto_map.internal"

        val mapOfFunction = MemberName("kotlin.collections", "mapOf")
        val mapClass = ClassName("kotlin.collections", "Map")

        // Map<String, Any?>
        val mapType: ParameterizedTypeName = mapClass
            .plusParameter(String::class.asTypeName())
            .plusParameter(Any::class.asTypeName().copy(nullable = true))
    }
}