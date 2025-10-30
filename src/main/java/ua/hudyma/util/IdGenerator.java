package ua.hudyma.util;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalTime;

public class IdGenerator {
    private final static SecureRandom secureRandom = new SecureRandom();

    public static LocalDate generateIssuedOn() {
        var today = LocalDate.now();
        int daysBack = new SecureRandom().nextInt(365 * 10);
        return today.minusDays(daysBack);
    }

    public static String generateProductCode(String catName) {
        return catName
                .substring(0,2)
                .toUpperCase() +
                generateRandomDigits(10);
    }

    public static LocalTime generateRandomTime() {
        var random = new SecureRandom();
        int secondsInDay = 24 * 60 * 60;
        int randomSecondOfDay = random.nextInt(secondsInDay);
        return LocalTime.ofSecondOfDay(randomSecondOfDay);
    }

    public static String generateId(int letterLength, int numberLength) {
        String letters = generateRandomUppercaseLetters(letterLength);
        String numbers = generateRandomDigits(numberLength);
        return letters + numbers;
    }

    public static String generateRandomUppercaseLetters(int length) {
        return secureRandom
                .ints('A',
                        'Z' + 1)
                .limit(length)
                .collect(StringBuilder::new,
                        StringBuilder::appendCodePoint,
                        StringBuilder::append)
                .toString();
    }

    public static String generateRandomDigits(int length) {
        return secureRandom
                .ints('0',
                        '9' + 1)
                .limit(length)
                .collect(
                        StringBuilder::new,
                        StringBuilder::appendCodePoint,
                        StringBuilder::append)
                .toString();
    }

    public static String generateVendorCode() {
        return "VE" + generateRandomDigits(10);
    }

    public static String generateBuyerCode() {
        return "BU" + generateRandomDigits(10);
    }

    public static <T extends Enum<T>> T getRandomEnum(Class<T> enumClass) {
        T[] values = enumClass.getEnumConstants();
        int index = secureRandom.nextInt(values.length);
        return values[index];
    }
}