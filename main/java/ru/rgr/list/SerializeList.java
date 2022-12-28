package ru.rgr.list;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Objects;

import ru.rgr.types.userTypes.UserType;

public class SerializeList {

    private static void parseString(MyListOfArrays list, String s, UserType userType) {
        s = s.replace("[", "");
        s = s.replace("]", "");
        String[] arrayFromString = s.split(", ");

        for (int i = 0; i < arrayFromString.length; i++) {
            if (!Objects.equals(arrayFromString[i], "null")) {
                list.add(userType.parseValue(arrayFromString[i]));
            }
        }
    }

    public static void saveToFile(MyListOfArrays list, String filename, UserType userType) {
        try (PrintWriter writer = new PrintWriter(filename)) {
            writer.println(userType.typeName());
            writer.println(list.getSizeOfArrays());
            list.forEach(el -> writer.println(Arrays.toString((Object[])el)));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static MyListOfArrays loadFromFile(String filename, UserType userType) throws Exception {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filename))) {
            String line;
            int sizeOfArrays = 0;

            line = bufferedReader.readLine();
            if (!userType.typeName().equals(line)) {
                throw new Exception("Wrong file structure");
            }
            line = bufferedReader.readLine();
            sizeOfArrays = Integer.parseInt(line);

            MyListOfArrays list = new MyListOfArrays((int)Math.pow(sizeOfArrays, 2));

            while ((line = bufferedReader.readLine()) != null) {
                parseString(list, line, userType);

            }
            return list;
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException();
        }
    }

}
