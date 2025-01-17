package com.dct.base.common;

import java.security.SecureRandom;

public class CredentialGenerator {

    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom random = new SecureRandom();

    public static String generateUsername(int length) {
        return generateRandomString(length);
    }

    public static String generatePassword(int length) {
        char lowerCase = getRandomCharacter("abcdefghijklmnopqrstuvwxyz");
        char upperCase = getRandomCharacter("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        char digit = getRandomCharacter("0123456789");
        char specialChar = getRandomCharacter("@#$%&");
        String remaining = generateRandomString(length - 4);
        return lowerCase + remaining + specialChar + digit + upperCase;
    }

    private static String generateRandomString(int length) {
        int size = CHARACTERS.length();
        StringBuilder sb = new StringBuilder(length);

        while(length-- > 0) {
            sb.append(CHARACTERS.charAt(random.nextInt(size)));
        }

        return sb.toString();
    }

    private static char getRandomCharacter(String characters) {
        return characters.charAt(random.nextInt(characters.length()));
    }
}
