package org.dbtools.schema.schemafile;

import org.dbtools.schema.JavaType;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Time;
import java.util.Date;

/**
 * User: jcampbell
 * Date: 1/25/14
 */
public enum SchemaFieldType {
    UNKNOWN(null),
    BOOLEAN(new JavaType("boolean", true, true, boolean.class, Boolean.class, "Boolean")),
    BIT(new JavaType("boolean", true, true, boolean.class, Boolean.class, "Boolean")),
    TINYINT(new JavaType("boolean", true, true, boolean.class, Boolean.class, "Boolean")),
    SMALLINT(new JavaType("int", true, true, int.class, Integer.class, "Integer")),
    INTEGER(new JavaType("int", true, true, int.class, Integer.class, "Integer")),
    BIGINT(new JavaType("long", true, true, long.class, Long.class, "Long")),
    REAL(new JavaType("float", true, true, float.class, Float.class, "Float")),
    FLOAT(new JavaType("float", true, true, float.class, Float.class, "Float")),
    DOUBLE(new JavaType("double", true, true, double.class, Double.class, "Double")),
    CHAR(new JavaType("char", true, true, char.class, Character.class, "Character")),
    VARCHAR(new JavaType("String", false, true, String.class, String.class, "String")),
    LONGVARCHAR(new JavaType("String", false, true, String.class, String.class, "String")),
    DATE(new JavaType("java.util.Date", false, false, Date.class, Date.class, "java.util.Date")),
    TIME(new JavaType("Time", false, false, Time.class, Time.class, "Time")),
    TIMESTAMP(new JavaType("java.util.Date", false, false, Date.class, Date.class, "java.util.Date")),
    JAVA_OBJECT(new JavaType("Object", false, false, Object.class, Object.class, "Object")),
    DECIMAL(new JavaType("float", true, true, float.class, Float.class, "Float")),
    NUMERIC(new JavaType("float", true, true, float.class, Float.class, "Float")),
    BIGDECIMAL(new JavaType("java.math.BigDecimal", false, true, BigDecimal.class, BigDecimal.class, "java.math.BigDecimal")),
    BIGINTEGER(new JavaType("java.math.BigInteger", false, true, BigInteger.class, BigInteger.class, "java.math.BigInteger")),
    BLOB(new JavaType("byte[]", true, true, byte[].class, Byte[].class, "Byte[]")),
    CLOB(new JavaType("String", false, true, String.class, String.class, "String")),

    // not currently supported
    MONEY(new JavaType("com.jdc.datatypes.Money", false, true, Void.class, Void.class, "com.jdc.datatypes.Money")), // Money.class
    FRACTION(new JavaType("com.jdc.datatypes.Fraction", false, true, Void.class, Void.class, "com.jdc.datatypes.Fraction")); // Fraction.class


    private JavaType javaType;

    SchemaFieldType(JavaType javaType) {
        this.javaType = javaType;
    }

    public JavaType getJavaType() {
        return javaType;
    }

    public boolean isNumberDataType() {
        return this == INTEGER
                || this == DECIMAL
                || this == DOUBLE
                || this == NUMERIC
                || this == REAL
                || this == SMALLINT
                || this == BIGINT
                || this == TINYINT;
//                || this == MONEY
//                || this == FRACTION;
    }


    public Class<?> getJavaClassType(boolean isNullable) {
        if (!isNullable) {
            return javaType.getMainClass();
        } else {
            // field is nullable.... so we CANNOT use a primitive
            return javaType.getMatchingNonPrimitiveClass();
        }
    }

    // NOTE!!! BE SURE TO CHANGE getJavaClassType() to match changes to this method
    public String getJavaTypeText(boolean isNullable) {
        String fieldClass = null;

        // check to see if we need to change from a primitive to an Object
        if (!isNullable) {
            fieldClass = javaType.getJavaTypeText();
        } else {
            // field is nullable.... so we CANNOT use a primitive
            fieldClass = javaType.getMatchingNonPrimitiveClassText();
        }

        return fieldClass;
    }

    public boolean isJavaTypePrimative() {
        return isJavaTypePrimative(javaType.isImmutable());
    }

    public boolean isJavaTypePrimative(boolean isNullable) {
        if (!isNullable) {
            return javaType.isPrimitive();
        } else {
            return false;
        }
    }

    public boolean isJavaTypeImmutable() {
        return javaType.isImmutable();
    }

    public static SchemaFieldType getJavaTypeFromJDBCTypeID(int jdbcTypeID) {
        switch (jdbcTypeID) {
            case java.sql.Types.BIT:
                return BIT;
            case java.sql.Types.TINYINT:
                return TINYINT;
            case java.sql.Types.SMALLINT:
                return SMALLINT;
            case java.sql.Types.INTEGER:
                return INTEGER;
            case java.sql.Types.BIGINT:
                return BIGINT;
            case java.sql.Types.REAL:
                return REAL;
            case java.sql.Types.DOUBLE:
                return DOUBLE;
            case java.sql.Types.CHAR:
                return CHAR;
            case java.sql.Types.VARCHAR:
                return VARCHAR;
            case java.sql.Types.DATE:
                return DATE;
            case java.sql.Types.TIME:
                return TIME;
            case java.sql.Types.TIMESTAMP:
                return TIMESTAMP;
            case java.sql.Types.JAVA_OBJECT:
                return JAVA_OBJECT;
            case java.sql.Types.DECIMAL:
                return DECIMAL;
            case java.sql.Types.NUMERIC:
                return NUMERIC;
            case java.sql.Types.BLOB:
                return BLOB;
            case java.sql.Types.CLOB:
                return CLOB;
            default:
                System.out.println("WARNING... Unknown jdbc type specified: [" + jdbcTypeID + "]");
                return UNKNOWN;
        }
    }
}
