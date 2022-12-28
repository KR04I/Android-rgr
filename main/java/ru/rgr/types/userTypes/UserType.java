package ru.rgr.types.userTypes;

import java.io.IOException;
import java.io.InputStreamReader;

import ru.rgr.types.comparators.Comparator;

public interface UserType {
    /**
     * Получить название ТД
     * @return
     */
    public String typeName();

    /**
     * Создать объект ТД
     * @return
     */
    public Object create();


    /**
     * Создать копию объекта
     * @param object
     * @return
     */
    public Object clone(Object object);

    /**
     * Чтение экземпляра с потока
     * @param in
     * @return
     * @throws IOException
     */
    public Object readValue(InputStreamReader in) throws IOException;

    /**
     * Парсить значение со строки
     * @param ss
     * @return
     */
    public Object parseValue(String ss);

    /**
     * Сравнение
     * @return
     */
    public Comparator getTypeComparator();

    public String toString(Object object);
}
