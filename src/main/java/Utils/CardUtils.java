package Utils;

import com.example.demo.Dtos.TransactionDTO;
import com.example.demo.Models.Account;
import com.example.demo.Models.Transaction;

public final class CardUtils {

    private CardUtils() {

    }


    public static String generate4RandomDigits() {
        String newNumber = "";
        for (int i = 0; i < 4; i++) {
            int num = (int) (Math.random() * 10);
            newNumber += String.valueOf(num);
        }
        return newNumber;
    }

    public static String generate3RandomDigits() {
        String newNumber = "VIN";
        for (int i = 0; i < 3; i++) {
            int num = (int) (Math.random() * 10);
            newNumber += String.valueOf(num);
        }
        return newNumber;
    }

    public static String generateNumbers(int randomDigits) {
        String newNumber = "";
        for (int i = 0; i < randomDigits; i++) {
            int num = (int) (Math.random() * 10);
            newNumber += String.valueOf(num);
        }
        return newNumber;


    }
}

/*
    public static Double balanceOperations(Account account) {
        Double total = account.getBalance();
        for (TransactionDTO t : transactions) {
            switch (t.getType()) {
                case DEBIT:
                    total -= t.getAmount();
                    break;
                case CREDIT:
                    total += t.getAmount();
                    break;
            }
        }
        return total;
    }
}
*/





