package com.example.code_gen.utils;

import java.lang.System;

@kotlin.Metadata(mv = {1, 8, 0}, k = 2, d1 = {"\u0000D\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u001e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\u001a\u001e\u0010\u0000\u001a\u0004\u0018\u00010\u00012\u0006\u0010\u0002\u001a\u00020\u00032\n\u0010\u0004\u001a\u0006\u0012\u0002\b\u00030\u0005H\u0000\u001a&\u0010\u0006\u001a\u0004\u0018\u00010\u00072\u0006\u0010\b\u001a\u00020\u00032\n\u0010\u0004\u001a\u0006\u0012\u0002\b\u00030\u00052\u0006\u0010\t\u001a\u00020\nH\u0000\u001a\u001a\u0010\u000b\u001a\u0004\u0018\u00010\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\nH\u0000\u001a\u0016\u0010\u0010\u001a\u00020\u00112\f\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00030\u0013H\u0000\u001a\u0018\u0010\u0014\u001a\u00020\u00112\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\nH\u0000\u001a\u0010\u0010\u0015\u001a\u00020\u00112\u0006\u0010\u0016\u001a\u00020\fH\u0000\u001a\f\u0010\u0017\u001a\u00020\u0011*\u00020\u0018H\u0000\u00a8\u0006\u0019"}, d2 = {"findAnnotationMirror", "Ljavax/lang/model/element/AnnotationMirror;", "typeElement", "Ljavax/lang/model/element/Element;", "annotationClass", "Ljava/lang/Class;", "getAnnotationParameterValue", "", "element", "parameterName", "", "getProperty", "Lcom/squareup/kotlinpoet/ClassName;", "kmClass", "Lkotlinx/metadata/KmClass;", "name", "hasDuplicateNames", "", "elements", "", "isNullable", "isNumber", "className", "isPublicDataClass", "Lkotlinx/metadata/jvm/KotlinClassMetadata;", "code-gen"})
public final class ProcessorUtilsKt {
    
    /**
     * Checks if classes annotated with `AutoMap` annotation have duplicate values for their `className`
     * parameter. `className' values has to be unique.
     */
    public static final boolean hasDuplicateNames(@org.jetbrains.annotations.NotNull
    java.util.Collection<? extends javax.lang.model.element.Element> elements) {
        return false;
    }
    
    /**
     * Tries to find [AnnotationMirror] of the given [annotationClass] for this [Element].
     * This can provide information about this annotation e.g. the value of its parameters etc.
     */
    @org.jetbrains.annotations.Nullable
    public static final javax.lang.model.element.AnnotationMirror findAnnotationMirror(@org.jetbrains.annotations.NotNull
    javax.lang.model.element.Element typeElement, @org.jetbrains.annotations.NotNull
    java.lang.Class<?> annotationClass) {
        return null;
    }
    
    /**
     * If this [element] is annotated with [annotationClass] annotation, it will search for a value
     * given to the parameter [parameterName].
     *
     * @return the value of the parameter given to this annotation or `null`, if no value was given or
     * if this element is not annotated with [annotationClass].
     */
    @org.jetbrains.annotations.Nullable
    public static final java.lang.Object getAnnotationParameterValue(@org.jetbrains.annotations.NotNull
    javax.lang.model.element.Element element, @org.jetbrains.annotations.NotNull
    java.lang.Class<?> annotationClass, @org.jetbrains.annotations.NotNull
    java.lang.String parameterName) {
        return null;
    }
    
    /**
     * Finds a property of this [kmClass] by its [name] and returns its [ClassName].
     * This [ClassName] contains information about the `type` of this property.
     */
    @org.jetbrains.annotations.Nullable
    public static final com.squareup.kotlinpoet.ClassName getProperty(@org.jetbrains.annotations.NotNull
    kotlinx.metadata.KmClass kmClass, @org.jetbrains.annotations.NotNull
    java.lang.String name) {
        return null;
    }
    
    /**
     * Finds property by its [name] and checks if its type is nullable.
     */
    public static final boolean isNullable(@org.jetbrains.annotations.NotNull
    kotlinx.metadata.KmClass kmClass, @org.jetbrains.annotations.NotNull
    java.lang.String name) {
        return false;
    }
    
    /**
     * Checks if this class [className] is a subclass of Number.
     *
     * Temporary workaround.
     */
    public static final boolean isNumber(@org.jetbrains.annotations.NotNull
    com.squareup.kotlinpoet.ClassName className) {
        return false;
    }
    
    public static final boolean isPublicDataClass(@org.jetbrains.annotations.NotNull
    kotlinx.metadata.jvm.KotlinClassMetadata $this$isPublicDataClass) {
        return false;
    }
}