package com.sphinxs.autombstone.processor.constant;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 类型
 */
public class TypeString {

    public final static String STRING = String.class.getCanonicalName();

    public final static String INTEGER = Integer.class.getCanonicalName();

    public final static String SHORT = Short.class.getCanonicalName();

    public final static String DOUBLE = Double.class.getCanonicalName();

    public final static String FLOAT = Float.class.getCanonicalName();

    public final static String BOOLEAN = Boolean.class.getCanonicalName();

    public final static String LONG = Long.class.getCanonicalName();

    public final static String BUNDLE = "android.os.Bundle";

    public final static String ARRAYLIST = ArrayList.class.getCanonicalName();

    public final static String ARRAY = "Array";

    public final static String CHARSEQUENCE = CharSequence.class.getCanonicalName();

    public final static String PARCELABLE = "android.os.Parcelable";

    public final static String SERIALIZABLE = Serializable.class.getCanonicalName();

    public final static String SPARSEARRAY = "android.util.SparseArray";

    public final static String SIZE = "android.util.Size";

    public final static String SIZEF = "android.util.SizeF";
}
