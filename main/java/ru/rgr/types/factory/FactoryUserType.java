package ru.rgr.types.factory;

import java.util.ArrayList;
import java.util.Arrays;

import ru.rgr.types.userTypes.IntegerUserType;
import ru.rgr.types.userTypes.Point2DUserType;
import ru.rgr.types.userTypes.UserType;

public class FactoryUserType {
    private final static ArrayList<UserType> typeList = new ArrayList<>();

    static {
        ArrayList<UserType> buildersClasses = new ArrayList<>(Arrays.asList(new IntegerUserType(), new Point2DUserType()));
        typeList.addAll(buildersClasses);
    }
    public static ArrayList<String> getTypeNameList() {
        ArrayList<String> typeNameListString = new ArrayList<>();
        for (UserType userType : typeList) {
            typeNameListString.add(userType.typeName());
        }
        return typeNameListString;
    }
    public static UserType getBuilderByName(String name){
        if (name == null){
            throw new RuntimeException("Error! Name of type is empty!");
        }
        for (UserType userType : typeList) {
            if (name.equals(userType.typeName()))
                return userType;
        }
        return null;
    }
}
