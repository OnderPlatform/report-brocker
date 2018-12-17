package tech.onder.reports.utils;

import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.NumberFormat;

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
        if (amount.equals(BigInteger.ZERO)) {
            return "0";
        }
        String am = String.valueOf(amount);
        
        return am;
    }
    
}
