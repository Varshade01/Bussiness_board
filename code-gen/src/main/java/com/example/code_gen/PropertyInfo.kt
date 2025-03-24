package com.example.code_gen

import com.squareup.kotlinpoet.ClassName

/**
 * Contains all the necessary property information to be used during code generation.
 */
data class PropertyInfo(
    val name: String,
    val className: ClassName,
    val isNullable: Boolean = false,
    val isAnnotated: Boolean = false,
    val annotationValue: Any? = null,
)
