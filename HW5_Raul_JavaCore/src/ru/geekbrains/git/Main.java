package ru.geekbrains.git;

import java.util.Arrays;

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
    static final int size = 10000000;
    static final int h = size / 2;
    static float[] arr = new float[size];

    public static void main(String[] args) {
        fill();
        System.out.println(single());               //вывод времени работы однопоточного метода
        fill();
        System.out.println(concurrent());               //вывод времени работы многопоточного метода
    }

    private static void fill() {
        Arrays.fill(arr, 1);    //автозаполнение массива единицами
    }

    private static long single() {      //однопоточный метод
        long a = System.currentTimeMillis();
        for(int i = 0; i<size; i++) {
            arr[i] = calculate(i, arr[i]);
        }
        long b = System.currentTimeMillis();
        return (b-a);
    }

    private static long concurrent() {      //двупоточный метод
        float[] a1 = new float[h];
        float[] a2 = new float[h];

        long a = System.currentTimeMillis();    //засекаем время

        System.arraycopy(arr,0,a1,0,h);
        System.arraycopy(arr, h, a2, 0, h);

        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < a1.length; i++) {
                a1[i] = calculate(i, a1[i]);
            }
            System.arraycopy(a1, 0, arr, 0, h); // сбор
        });
        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < a2.length; i++) {
                a2[i] = calculate(i, a2[i]);
            }
            System.arraycopy(a2, 0, arr, h, h); // сбор
        });

        thread1.start();        //запуск потоков в самом методе
        thread2.start();

        try {
            thread1.join();  // ?
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();        // ?
        }
        long b = System.currentTimeMillis();    //время окончания
        return (b-a);           // расчет, сколько прошло времени
    }

    private static float calculate(int i, float val) {
        return (float) (val * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
    }
}

