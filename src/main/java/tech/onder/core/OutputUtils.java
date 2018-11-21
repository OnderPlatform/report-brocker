package tech.onder.core;

import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;

public class OutputUtils {
    public static String kwhFormat(Double amount) {
        NumberFormat formatter = new DecimalFormat("#0.000");
        if (amount == null) {
            return "0.000";
        }
        return formatter.format(amount / 1000);
    }

    public static String tokenPrice(BigInteger amount) {
        return token(amount.multiply(BigInteger.valueOf(1000)));
    }

    public static String token(BigInteger amount) {
        String am = String.valueOf(amount);
        int diff = 18 - am.length();
        String addString = "";
        if (diff >= 0) {
            char[] add = new char[diff];
            Arrays.fill(add, '0');
            return "0." + new String(add) + am;
        } else {
            return am.substring(0, diff) + "." + am.substring(-diff);
        }
    }
}
