package org.cb2384.corutils;

import java.lang.reflect.Type;

import org.checkerframework.checker.nullness.qual.*;
import org.checkerframework.dataflow.qual.*;

/**
 * Some utilities for dealing with Strings.
 * There are methods for determining if something starts with a vowel,
 * (for English-language a-an distinction), as well as one that prepends the appropriate indefinite article.
 * There is also a utility that is similar to {@link Class#getName}, {@link #getIdealName}
 * which is essentially {@link Class#getSimpleName} unless it is empty or just "{@code []}",
 * in which case returns {@link Class#getName()}.
 * There is a version for generic {@link Type}s as well.
 *
 * @author  Corinne Buxton
 */
public class StringUtils {
    
    /**
     * This should never be called
     *
     * @throws  IllegalAccessException    always
     */
    private StringUtils() throws IllegalAccessException {
        throw new IllegalAccessException("This should never be called‽");
    }
    
    /**
     * Default vowels. Y is not included because these are for the context of the beginning of a word,
     * and Y is essentially always a consonant when it begins a word.
     */
    public static final String DEFAULT_VOWELS = "AaEeIiOoUu";
    
    /**
     * Checks if the given {@link CharSequence} starts with a vowel,
     * according to {@link #DEFAULT_VOWELS}.
     *
     * @param   toComeAfter the {@link String} or other {@link CharSequence} to check
     *
     * @return  {@code true} if it starts with a vowel, otherwise {@code false}
     */
    @Pure
    public static boolean startsWithVowel(
            @NonNull CharSequence toComeAfter
    ) {
        return toComeAfter.toString().startsWith(DEFAULT_VOWELS);
    }
    
    /**
     * Prepends either "a" or "an" to a String of the given {@link CharSequence}.
     * If it does not start with a space, then one is added after the article.
     * If the second boolean is true, another space is added in front of the article as well.
     *
     * @param   toPrepend   a {@link CharSequence} representing the {@link String} that will have
     *                      the article prepended to it, which will then be returned.
     *
     * @param   addSpaceBefore  if {@code true}, a space is added in front of the article
     *
     * @return  a string consisting of possibly a space, then "a" or "an", then possibly a space,
     *          and finally the first argument, itself as a {@link String}.
     */
    @SideEffectFree
    public static @NonNull String aOrAnPrepend(
            @NonNull CharSequence toPrepend,
            boolean addSpaceBefore
    ) {
        String inString = toPrepend.toString();
        if (inString.isEmpty()) {
            return addSpaceBefore ? " a" : "a";
        }
        
        String trimString = inString.trim();
        if (trimString.codePointAt(0) == inString.codePointAt(0)) {
            inString = " " + inString;
        }
        final String result = startsWithVowel(trimString) ?
                "an" + inString :
                "a" + inString;
        
        return addSpaceBefore ? " " + result : result;
    }
    
    /**
     * Gets the name of this {@link Type}. If the given type is just a {@link Class}, calls
     * {@link #getIdealName(Class)} for that class.
     * Otherwise, just uses {@link Type#getTypeName()}
     * This is intended for verbosity of error messages.
     *
     * @param   type    the type variable to get a name for
     *
     * @return  a name for this {@link Type}
     */
    @SideEffectFree
    public static @NonNull String getIdealName(
            @NonNull Type type
    ) {
        if (type instanceof Class<?>) {
            return getIdealName((Class<?>) type);
        }
        return type.getTypeName();
    }
    
    /**
     * Gets a nice name for the given {@link Class} object. Prefers {@link Class#getSimpleName()} unless it returns
     * an empty string or just "{@code []}", in which case it returns {@link Class#getName()}
     * This is intended for verbosity of error messages.
     *
     * @param   clazz   the Class object to get a name for
     *
     * @return  a name for the given {@link Class}, either the simple or the normal name.
     */
    @SideEffectFree
    public static @NonNull String getIdealName(
            @NonNull Class<?> clazz
    ) {
        String name = clazz.getSimpleName();
        if (name.isEmpty()) {
            name = clazz.getName();
        } else if (name.equals("[]")) {
            final Class<?> componentClass = clazz.getComponentType();
            return getIdealName(componentClass) + "[]";
        }
        return name;
    }
}
