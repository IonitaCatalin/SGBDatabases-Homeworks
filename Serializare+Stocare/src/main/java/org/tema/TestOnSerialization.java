package org.tema;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class TestOnSerialization implements Serializable {
    int intMemberValue;
    float floatMemberValue;
    Integer integerMemberValue;
    String uniqueID;
    private static final long serialVersionUID = 42L;
    public TestOnSerialization(int intMemberValue, float floatMemberValue,Integer integerMemberValue)
    {
        this.intMemberValue=intMemberValue;
        this.floatMemberValue=floatMemberValue;
        this.integerMemberValue=integerMemberValue;
        uniqueID= UUID.randomUUID().toString();
    }

    public int getIntMemberValue() {
        return intMemberValue;
    }

    public void setIntMemberValue(int intMemberValue) {
        this.intMemberValue = intMemberValue;
    }

    public float getFloatMemberValue() {
        return floatMemberValue;
    }

    public void setFloatMemberValue(float floatMemberValue) {
        this.floatMemberValue = floatMemberValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestOnSerialization that = (TestOnSerialization) o;
        return intMemberValue == that.intMemberValue &&
                Float.compare(that.floatMemberValue, floatMemberValue) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(intMemberValue, floatMemberValue);
    }

    public String getUniqueID() {
        return uniqueID;
    }

    @Override
    public String toString() {
        return "TestOnSerialization{" +
                "intMemberValue=" + intMemberValue +
                ", floatMemberValue=" + floatMemberValue +
                ", integerMemberValue=" + integerMemberValue +
                ", uniqueID='" + uniqueID + '\'' +
                '}';
    }
}
