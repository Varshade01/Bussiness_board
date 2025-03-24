package com.example.code_gen.utils

import com.example.annotations.AutoMap
import com.example.code_gen.generators.AutoMapCodeGenerator
import com.squareup.kotlinpoet.ClassName
import kotlinx.metadata.KmClass
import kotlinx.metadata.KmClassifier
import kotlinx.metadata.KmProperty
import kotlinx.metadata.Visibility
import kotlinx.metadata.isData
import kotlinx.metadata.isInner
import kotlinx.metadata.isNullable
import kotlinx.metadata.jvm.KotlinClassMetadata
import kotlinx.metadata.visibility
import javax.lang.model.element.AnnotationMirror
import javax.lang.model.element.Element

/**
 * Checks if classes annotated with `AutoMap` annotation have duplicate values for their `className`
 * parameter. `className' values has to be unique.
 */
internal fun hasDuplicateNames(elements: Collection<Element>): Boolean {
    val names = elements.mapNotNull { element ->
        getAnnotationParameterValue(
            element,
            AutoMap::class.java,
            AutoMapCodeGenerator.AUTO_MAP_CLASS_NAME_PARAMETER
        )?.toString()
    }
    return hasDuplicates(names) { it }
}

/**
 * Tries to find [AnnotationMirror] of the given [annotationClass] for this [Element].
 * This can provide information about this annotation e.g. the value of its parameters etc.
 */
internal fun findAnnotationMirror(
    typeElement: Element,
    annotationClass: Class<*>
): AnnotationMirror? {
    return typeElement.annotationMirrors.find { it.annotationType.toString() == annotationClass.name }
}

/**
 * If this [element] is annotated with [annotationClass] annotation, it will search for a value
 * given to the parameter [parameterName].
 *
 * @return the value of the parameter given to this annotation or `null`, if no value was given or
 * if this element is not annotated with [annotationClass].
 */
internal fun getAnnotationParameterValue(
    element: Element,
    annotationClass: Class<*>,
    parameterName: String
): Any? {
    val annotationMirror = findAnnotationMirror(element, annotationClass)

    return if (annotationMirror != null) {
        val elementValues = annotationMirror.elementValues
        val parameterElement =
            elementValues.keys.find { it.simpleName.toString() == parameterName }
        elementValues[parameterElement]?.value
    } else null
}

/**
 * Finds a property of this [kmClass] by its [name] and returns its [ClassName].
 * This [ClassName] contains information about the `type` of this property.
 */
internal fun getProperty(kmClass: KmClass, name: String): ClassName? {
    val property: KmProperty? = kmClass.properties.find { it.name == name }
    val paramType = property?.returnType?.classifier as? KmClassifier.Class

    return paramType?.let {
        ClassName.bestGuess(paramType.name.replace('/', '.'))
    }
}

/**
 * Finds property by its [name] and checks if its type is nullable.
 */
internal fun isNullable(kmClass: KmClass, name: String): Boolean {
    val property: KmProperty? = kmClass.properties.find { it.name == name }
    return property?.returnType?.isNullable ?: false
}

/**
 * Checks if this class [className] is a subclass of Number.
 *
 * Temporary workaround.
 */
internal fun isNumber(className: ClassName): Boolean {
    return when (className.canonicalName) {
        "kotlin.Byte", "kotlin.Short", "kotlin.Int", "kotlin.Long", "kotlin.Float", "kotlin.Double" -> true
        else -> false
    }
}

internal fun KotlinClassMetadata.isPublicDataClass(): Boolean {
    return (this is KotlinClassMetadata.Class &&
            kmClass.isData &&
            kmClass.visibility == Visibility.PUBLIC &&
            !kmClass.isInner)
}