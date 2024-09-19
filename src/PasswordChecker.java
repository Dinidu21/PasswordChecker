import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class PasswordChecker {

    private static Set<String> loadCommonPasswords(List<String> fileNames) throws IOException {
        Set<String> commonPasswords = new HashSet<>();
        for (String fileName : fileNames) {
            try (InputStream inputStream = PasswordChecker.class.getClassLoader().getResourceAsStream(fileName)) {
                if (inputStream == null) {
                    throw new IOException("File not found: " + fileName);
                }
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        commonPasswords.add(line.trim().toLowerCase());
                    }
                }
                System.out.println("Successfully read file: " + fileName);
            }
        }
        return commonPasswords;
    }

    // Using a Set for special characters
    private static final Set<Character> SPECIAL_CHARS = Set.of(
            '!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '-', '_', '=', '+',
            '[', ']', '{', '}', ';', ':', '\'', '"', ',', '.', '/', '<', '>', '?',
            '\\', '|'
    );

    public static void passwordChecker(String s, Set<String> commonPasswords) {
        if (s.length() < 8) {
            System.out.println("Password should be at least 8 characters long.");
            return;
        }

        int countUpper = 0;
        int countLower = 0;
        int countDigit = 0;
        int countSpecial = 0;

        for (char ch : s.toCharArray()) {
            if (Character.isUpperCase(ch)) {
                countUpper++;
            } else if (Character.isLowerCase(ch)) {
                countLower++;
            } else if (Character.isDigit(ch)) {
                countDigit++;
            } else if (SPECIAL_CHARS.contains(ch)) {
                countSpecial++;
            }

            if (countUpper >= 2 && countLower >= 2 && countDigit >= 2 && countSpecial >= 2) {
                break;
            }
        }
        if(s.length() > 12 && countUpper >= 2 && countLower >= 2 && countDigit >= 2 && countSpecial >= 2){
            System.out.println("Password Strength: Very Strong");
        }else if (countUpper >= 2 && countLower >= 2 && countDigit >= 2 && countSpecial >= 2) {
            System.out.println("Password Strength: Strong");
        } else if (countUpper >= 1 && countLower >= 1 && countDigit >= 1 && countSpecial >= 1) {
            System.out.println("Password Strength: Medium");
        } else {
            System.out.println("Password Strength: Weak");
        }

        if (countUpper < 2 || countLower < 2 || countDigit < 2 || countSpecial < 2 || s.length() < 12) {
            System.out.println("Suggestions to improve your password:");
            if(s.length() < 12) System.out.println("- Add more characters.(12-16 characters could be more secure)");
            if (countUpper < 2) System.out.println("- Add more uppercase letters.");
            if (countLower < 2) System.out.println("- Add more lowercase letters.");
            if (countDigit < 2) System.out.println("- Add more digits.");
            if (countSpecial < 2) System.out.println("- Add more special characters.");
        }

        if (commonPasswords.contains(s.toLowerCase())) {
            System.out.println("Password is too common.");
        }
    }

    public static void main(String[] args) {
        List<String> fileNames = List.of("pwddb86500.txt");

        try {
            Set<String> commonPasswords = loadCommonPasswords(fileNames);

            Scanner sc = new Scanner(System.in);
            System.out.println("============================================");
            System.out.println("|       Welcome to My Password Checker     |");
            System.out.println("============================================");
            System.out.print("\nEnter your Password: ");
            String userPassword = sc.nextLine();

            passwordChecker(userPassword, commonPasswords);

        } catch (IOException e) {
            System.err.println("Error reading password files: " + e.getMessage());
        }
    }
}
