package ru.rgr.list;

import java.util.Arrays;

import ru.rgr.types.comparators.Comparator;

public class MyListOfArrays {
    private int sizeOfArrays; // размерность каждого массива
    private int size; // Размер списка
    private int totalElements; // Количество элементов во всех массивах

    private Node head;
    private Node tail;

    public MyListOfArrays(int sizeOfArrays) {
        this.sizeOfArrays = (int) Math.sqrt(sizeOfArrays);
        //this.cidx = 1; // не пустая = 1 линейный массив
        this.size = 1;
        this.head = this.tail = new Node(this.sizeOfArrays);
        this.totalElements = 0;
    }


    /**
     * Добавить элемент в конец всей структуры
     * @param value
     */
    public void add(Object value) {
        Node current = this.tail;
        current.addElementOnArray(value);

        if (current.getCountOfElementsInArray() == this.sizeOfArrays ) {
            int kk1 = this.sizeOfArrays  * 3/4; // 75% в старом
            int kk2 = this.sizeOfArrays  - kk1; // 25% в новом

            // Новое количество элементов в старом массиве
            current.setCountOfElementsInArray(kk1);

            // Создаём новый узел и ставим ему количество элементов в массиве
            Node newNode = new Node(this.sizeOfArrays );
            current.next = newNode;
            newNode.prev = current;
            this.tail = newNode;
            newNode.setCountOfElementsInArray(kk2);

            // Копируем 25% данных в новый узел из предыдущего узла
            for (int i = 0; i < kk2; i++) {
                newNode.array[i] = current.array[kk1 + i];
                current.array[kk1 + i] = null;
            }

            // Увеличиваем счётчик узлов списка
            this.size++;
        }

        this.totalElements++;
    }

    public Object get(int logicalIndexOfElement) {
        try {
            int[] physicalIndex = getIndex(logicalIndexOfElement);
            if (physicalIndex[0] == -1 || physicalIndex[1] == -1) {
                throw new IndexOutOfBoundsException("Error. Index out of bounds");
            }

            Node current = getNode(physicalIndex[0]);

            return current.array[physicalIndex[1]];
        } catch (IndexOutOfBoundsException error) {
            System.out.println(error.getMessage());
        }
        return null;
    }

    /**
     * Преобразование логического номера в физический
     * @param logicalIndexOfElement
     * @return Возвращает массив из двух элементов:
     * 1 - индекс узла
     * 2 - физический индекс элемента
     */
    private int[] getIndex(int logicalIndexOfElement) {
        // Обработка неверного значения
        if (logicalIndexOfElement < 0 || logicalIndexOfElement >= this.totalElements) {
            return new int[] {-1, -1};
        }

        // Преобразование в физический индекс
        Node tmp = head;
        int indexNode;
        int physicalIndexOfElement = logicalIndexOfElement;
        for (indexNode = 0; tmp != null; indexNode++) {
            if (physicalIndexOfElement < tmp.getCountOfElementsInArray()) {
                return new int[] {indexNode, physicalIndexOfElement};
            }

            physicalIndexOfElement -= tmp.getCountOfElementsInArray();
            tmp = tmp.next;
        }


        return new int[] {-1, -1};
    }

    /**
     * Вспомогательный метод поиска узла
     * @param index
     * @return
     */
    private Node getNode(int index) {
        try {
            if (index < 0 || index >= this.size) {
                throw new IndexOutOfBoundsException();
            }
            if (index == 0) {
                return this.head;
            }
            Node tmp = this.head;
            for (int i = 0; i < index; i++) {
                tmp = tmp.next;
            }

            return tmp;
        } catch (IndexOutOfBoundsException ex) {
            System.out.println("Error. Out of bounds list");
        }

        return null;
    }

    /**
     * Вставка по логическому индексу в структуру
     * @param value
     * @param logicalIndexOfElement
     * @return
     */
    public int insert(Object value, int logicalIndexOfElement) {
        try {
            int[] physicalIndex = getIndex(logicalIndexOfElement);
            if (physicalIndex[0] == -1 || physicalIndex[1] == -1) {
                throw new IndexOutOfBoundsException("Error. Index out of bounds");
            }

            Node current = getNode(physicalIndex[0]);

            // Раздвижка в массиве
            for (int i = current.getCountOfElementsInArray() - 1; i >= physicalIndex[1]; i--) {
                current.array[i + 1] = current.array[i];
            }

            // Вставка нового элемента
            current.array[physicalIndex[1]] = value;
            current.setCountOfElementsInArray(current.getCountOfElementsInArray() + 1);
            this.totalElements++;

            // В случае переполнения -> раздвижка списка
            if (current.getCountOfElementsInArray() == this.sizeOfArrays ) {
                Node newNode = new Node(this.sizeOfArrays );

                // Перекидываем указатели
                newNode.prev = current;
                newNode.next = current.next;
                if (current.next == null) {
                    // Если current это последний узел списка
                    this.tail = newNode;
                } else {
                    // Если за current ещё есть узел
                    current.next.prev = newNode;
                }
                current.next = newNode;

                // Перенос половины current в новый узел
                current.setCountOfElementsInArray(this.sizeOfArrays / 2);
                newNode.setCountOfElementsInArray(this.sizeOfArrays - current.getCountOfElementsInArray());
                int countElementsOfNewNode = newNode.getCountOfElementsInArray();
                for (int i = 0; i < countElementsOfNewNode; i++) {
                    newNode.array[i] = current.array[this.sizeOfArrays / 2 + i];
                    current.array[this.sizeOfArrays / 2 + i] = null;
                }

                this.size++;
            }

            return 1;
        } catch (IndexOutOfBoundsException error) {
            System.out.println(error.getMessage());
        }
        return 0;
    }

    public int remove(int logicalIndexOfElement) {
        try {
            int[] physicalIndex = getIndex(logicalIndexOfElement);
            if (physicalIndex[0] == -1 || physicalIndex[1] == -1) {
                throw new IndexOutOfBoundsException("Error. Index out of bounds");
            }

            Node current = getNode(physicalIndex[0]);

            // Сдвиг в массиве
            current.array[physicalIndex[1]] = null;
            for (int i = physicalIndex[1]; i < current.getCountOfElementsInArray(); i++) {
                current.array[i] = current.array[i + 1];
            }

            this.totalElements--;
            current.setCountOfElementsInArray(current.getCountOfElementsInArray() - 1);

            // Если массив оказался пустым, то удалим узел перекинув указатели
            if (current.getCountOfElementsInArray() == 0) {

                if (current.prev != null) {
                    if (current.next != null) {
                        current.prev.next = current.next;
                    } else {
                        current.prev.next = null;
                        this.tail = current.prev;
                    }
                }
                if (current.next != null) {
                    if (current.prev != null) {
                        current.next.prev = current.prev;
                    } else {
                        current.next.prev = null;
                        this.head = current.next;
                    }
                }

                if (this.size != 1) {
                    this.size--;
                }

            }

            return 1;
        } catch (IndexOutOfBoundsException error) {
            System.out.println(error.getMessage());
        }

        return 0;
    }


    public void forEach(Action<Object> a) {
        Node tmp = this.head;

        while (tmp != null) {
            a.toDo(tmp.array);
            tmp = tmp.next;
        }
    }

    /**
     * Разбиение массива на части
     * @param arr
     * @param comporator
     */
    private void mergeSortArray(Object[] arr, Comparator comporator, int countElem) {
        int arrSize = countElem;
        if (arrSize == 1) {
            return;
        }

        int middle = arrSize / 2;

        Object[] left = new Object[middle];
        Object[] right = new Object[arrSize - middle];

        for (int i = 0; i < middle; i++) {
            left[i] = arr[i];
        }
        for (int i = 0; i < arrSize - middle; i++) {
            right[i] = arr[middle + i];
        }

        mergeSortArray(left, comporator, left.length);
        mergeSortArray(right, comporator, right.length);
        mergeArrays(arr, left, right, comporator);
    }

    /**
     * Слияние подмассивов
     * @param arr результирующий массив
     * @param leftArr первый подмассив
     * @param rightArr второй подмассив
     * @param comporator объект для сравнения значений пользовательских типов данных
     */
    private void mergeArrays(Object[] arr, Object[] leftArr, Object[] rightArr, Comparator comporator) {
        int leftSize = leftArr.length;
        int rightSize = rightArr.length;

        int i = 0;
        int j = 0;
        int idx = 0;

        while (i < leftSize && j < rightSize) {
            if (comporator.compare(leftArr[i], rightArr[j]) < 0) {
                arr[idx] = leftArr[i];
                i++;
            } else {
                arr[idx] = rightArr[j];
                j++;
            }

            idx++;
        }

        // Если размеры массивов были разными,
        // то добавляем в результирующий массив остатки
        for (int ll = i; ll < leftSize; ll++) {
            arr[idx++] = leftArr[ll];
        }
        for (int rr = j; rr < rightSize; rr++) {
            arr[idx++] = rightArr[rr];
        }

    }

    public MyListOfArrays sort(Comparator comporator) {
        Object[] arrayOfAllElements = arraysInOneArray();

        mergeSortArray(arrayOfAllElements, comporator, arrayOfAllElements.length);

        // Создаём новый список
        MyListOfArrays newList = new MyListOfArrays((int)Math.pow(sizeOfArrays, 2));

        for (int i = 0; i < arrayOfAllElements.length; i++) {
            newList.add(arrayOfAllElements[i]);
        }

        return newList;
    }

    private Object[] arraysInOneArray() {
        Object[] arrayOfAllElements = new Object[totalElements];
        for (int i = 0; i < this.totalElements; i++) {
            arrayOfAllElements[i] = this.get(i);
        }

        return arrayOfAllElements;
    }

    public void clear() {
        this.size = 0;
        this.totalElements = 0;
        this.head = this.tail = null;
    }

    public void show() {
        Node tmp = this.head;
        int numOfCurrentNode = 0;
        while (tmp != null) {
            System.out.println(numOfCurrentNode + ": " + Arrays.toString(tmp.array));

            numOfCurrentNode++;
            tmp = tmp.next;
        }
    }

    public int getSize() {
        return size;
    }
    public int getTotalElements() {
        return totalElements;
    }
    public int getSizeOfArrays() {
        return this.sizeOfArrays;
    }

    @Override
    public String toString() {
        String str = "";
        String elements = "";
        int indexNode = 0;

        Node tmp = head;
        while (tmp != null) {
            for (int j = 0; j < tmp.getCountOfElementsInArray(); j++) {
                if (tmp.array[j] != null) {
                    elements += tmp.array[j].toString() + " ";
                }
            }

            str += indexNode + ": " + "[" + elements + "]\n";
            tmp = tmp.next;
            indexNode++;
            elements = "";
        }


        return str;
    }

    private class Node {
        Object[] array;

        int countOfElementsInArray;
        Node next;
        Node prev;

        public Node(int size) {
            this.array = new Object[size];
            this.countOfElementsInArray = 0;
            this.next = this.prev = null;
        }

        public void addElementOnArray(Object value) {
            this.array[countOfElementsInArray] = value;
            this.countOfElementsInArray++;
        }

        public int getCountOfElementsInArray() {
            return countOfElementsInArray;
        }
        public void setCountOfElementsInArray(int countOfElementsInArray) {
            this.countOfElementsInArray = countOfElementsInArray;
        }

        @Override
        public String toString() {
            return Arrays.toString(array);
        }

    }
}
