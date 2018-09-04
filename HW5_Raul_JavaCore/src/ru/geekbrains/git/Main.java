package ru.geekbrains.git;

/**
 * Урок 5. Многопоточность
 * 1. Необходимо написать два метода, которые делают следующее:
 * 1) Создают одномерный длинный массив, например:
 * static final int size = 10000000;
 * static final int h = size / 2;
 * float[] arr = new float[size];
 * 2) Заполняют этот массив единицами;
 * 3) Засекают время выполнения: long a = System.currentTimeMillis();
 * 4) Проходят по всему массиву и для каждой ячейки считают новое значение по формуле:
 * arr[i] = (float)(arr[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
 * 5) Проверяется время окончания метода System.currentTimeMillis();
 * 6) В консоль выводится время работы: System.out.println(System.currentTimeMillis() - a);
 * Отличие первого метода от второго:
 * Первый просто бежит по массиву и вычисляет значения.
 * Второй разбивает массив на два массива, в двух потоках высчитывает новые значения и потом склеивает эти массивы
 * обратно в один.
 *
 * Пример деления одного массива на два:
 * System.arraycopy(arr, 0, a1, 0, h);
 * System.arraycopy(arr, h, a2, 0, h);
 *
 * Пример обратной склейки:
 * System.arraycopy(a1, 0, arr, 0, h);
 * System.arraycopy(a2, 0, arr, h, h);
 *
 * Примечание:
 * System.arraycopy() копирует данные из одного массива в другой:
 * System.arraycopy(массив-источник, откуда начинаем брать данные из массива-источника, массив-назначение, откуда
 * начинаем записывать данные в массив-назначение, сколько ячеек копируем)
 * По замерам времени:
 * Для первого метода надо считать время только на цикл расчета:
 * for (int i = 0; i < size; i++) {
 * arr[i] = (float)(arr[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
 * }
 * Для второго метода замеряете время разбивки массива на 2, просчета каждого из двух массивов и склейки.
 */

public class Main {

    public static void main(String[] args) {
        long a = System.currentTimeMillis();
        System.out.println(a + " время начала работы программы");                  //1 вывод времени начала работы программы
        first_metod();                                                             // вызов 1 метода

        new Thread(() -> second_metod()).start();
        new Thread(() -> second_metod()).start();
    }

    static final int size = 10000000;
    static final int h = size / 2;

    public static void first_metod () {
        float[] arr = new float[size];
        long b = System.currentTimeMillis();
        System.out.println(b + "время запуска 1 цикла 1 метода");                  //2 вывод времени начала работы 1 цикла 1 метода
        for (int i = 0; i<arr.length; i++) {
            arr[i]=1;
        }
        long c = System.currentTimeMillis();
        System.out.println(c + " время завершения 1 цикла 1 метода");                  //3 вывод времени конца работы 1 цикла 1 метода
        for (int i = 0; i<arr.length; i++) {
            arr[i] = (float)(arr[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
        }
        long d = System.currentTimeMillis();
        System.out.println(d + " время завершения 2 цикла 1 метода");                  //4 вывод времени конца работы 2 цикла 1 метода
        long e = d - c;
        System.out.println(e + " милисекунд ушло на расчет");
    }

    public synchronized static void second_metod () {
        float[] arr = new float[size];
        float[] a1 = new float[h];
        float[] a2 = new float[h];
        long b = System.currentTimeMillis();
        System.out.println(b + "время до разбивки во втором методе");                  //5 вывод времени до разбивки
        System.arraycopy(arr,0,a1,0,h);
        System.arraycopy(arr, h, a2, 0, h);
        for (int i = 0; i<a1.length; i++) {
            a1[i]=1;
        }
        for (int i = 0; i<arr.length; i++) {
            arr[i] = (float)(arr[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
        }

        System.arraycopy(a1, 0, arr, 0, h);
        System.arraycopy(a2, 0, arr, h, h);

        long f = System.currentTimeMillis();
        System.out.println(f + "время после разбивки во втором методе");                  //5 вывод времени после разбивки

        long e = f - b;
        System.out.println(e + " милисекунд ушло на расчет во 2 методе");
    }
}

