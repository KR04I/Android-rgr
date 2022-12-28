package ru.rgr.types.userTypes;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ru.rgr.types.Point2DType;
import ru.rgr.types.comparators.Comparator;
import ru.rgr.types.comparators.Point2DComparator;

public class Point2DUserType implements UserType {
    private static final float MAX = 10.0F;
    private static final float MIN = -10.0F;

    private static final String REG_EXPR = "\\(([-]?[0-9]+(?:[.,][0-9]+){0,1});([-]?[0-9]+(?:[.,][0-9]+){0,1})\\)";

    @Override
    public String typeName() {
        return "Point2D";
    }

    @Override
    public Object create() {
        Random random = new Random();
        double x = random.nextDouble() * (MAX - MIN) + MIN;
        double y = random.nextDouble() * (MAX - MIN) + MIN;
        return new Point2DType((float) x,
                (float) y);
    }

    @Override
    public Object clone(Object object) {
        return new Point2DType(((Point2DType)object).getX(),
                ((Point2DType)object).getY());
    }

    @Override
    public Object readValue(InputStreamReader in) throws IOException {
        return in.read();
    }

    @Override
    public Object parseValue(String ss) {
        Pattern ptrnString = Pattern.compile(REG_EXPR);
        Matcher matcher = ptrnString.matcher(ss);

        if(matcher.find()) {
            return new Point2DType(Float.valueOf(matcher.group(1)),
                    Float.valueOf(matcher.group(2)));
        }

        return null;
    }

    @Override
    public Comparator getTypeComparator() {
        return new Point2DComparator();
    }

    @Override
    public String toString(Object object) {
        return object.toString();
    }
}
