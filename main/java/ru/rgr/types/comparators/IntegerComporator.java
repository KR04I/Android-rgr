package ru.rgr.types.comparators;

import java.io.Serializable;

import ru.rgr.types.IntegerType;

public class IntegerComporator implements Comparator, Serializable {
    @Override
    public float compare(Object object1, Object object2) {
        return ((IntegerType)object1).getIntValue() - ((IntegerType)object2).getIntValue();
    }
}
