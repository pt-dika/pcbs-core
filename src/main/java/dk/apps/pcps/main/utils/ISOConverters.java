package dk.apps.pcps.main.utils;

import dk.apps.pcps.commonutils.Utility;
import org.joda.time.DateTime;
import org.joda.time.Seconds;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ISOConverters {

    public static String hexToAscii(String hexValue) {
        if (hexValue.length() % 2 != 0) {
            hexValue = "0" + hexValue;
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < hexValue.length(); i += 2) {
            String string = hexValue.substring(i, i + 2);
            // Change value from hexadecimal to decimal
            int hex = Integer.parseInt(string, 16);// Base 16 - Hexadecimal radix
            byte[] bytes = String.valueOf(Character.toChars(hex)).getBytes();
            builder.append(new String(bytes));
        }
        return builder.toString();
    }

    public static String asciiToHex(String asciiValue) {
        StringBuilder builder = new StringBuilder();
        char[] chars = asciiValue.toCharArray();
        String string;
        for (int i = 0; i < chars.length; i++) {
            string = Integer.toHexString((int) chars[i]).toUpperCase();
            if (string.length() == 1) {
                builder.append("0");
            }
            builder.append(string);
        }
        return builder.toString();
    }

    public static String hexToBin(String hexValue) {
        if (hexValue.length() % 2 != 0) {
            hexValue = "0" + hexValue;
        }
        StringBuilder output = new StringBuilder();
        // You need two hex digits to change to binary
        for (int i = 0; i < hexValue.length(); i += 2) {
            String string = hexValue.substring(i, i + 2);
            // Change value from hexadecimal to decimal
            int hex = Integer.parseInt(string, 16);
            String bin = Integer.toBinaryString(hex);
            int len = bin.length();
            if (len != 8) {
                for (int j = len; j < 8; j++) {
                    output.append("0");
                }
            }
            output.append(bin);
        }
        return output.toString();
    }

    public static String binToAscii(String binValue) {
        if (binValue.length() % 2 != 0) {
            binValue = "0" + binValue;
        }
        String string = new String();
        int length = binValue.length();
        for (int i = 0; i <= length - 1; i += 8) {
            int byteValue = Integer.parseInt(binValue.substring(i, i + 8), 2);
            string = string + (char) byteValue;
        }
        return string;
    }

    public static String binToHex(String binValue) {
        if (binValue.length() % 2 != 0) {
            binValue = "0" + binValue;
        }
        StringBuilder builder = new StringBuilder();
        int length = binValue.length();
        for (int i = 0; i <= length - 1; i += 8) {
            int intVal = Integer.parseInt(binValue.substring(i, i + 8), 2);
            String hexVal = Integer.toHexString(intVal).toUpperCase();
            if (hexVal.length() == 1) {
                builder.append("0");
            }
            builder.append(hexVal);
        }
        return builder.toString();
    }

    public static String asciiToBin(String asciiValue) {
        return hexToBin(asciiToHex(asciiValue));
    }

    public static int hexToInt(String hexValue) {
        if (hexValue.length() % 2 != 0) {
            hexValue = "0" + hexValue;
        }
        return Integer.parseInt(hexValue, 16);
    }

    public static byte[] hexToBytes(String hexValue) {
        if (hexValue.length() % 2 != 0) {
            hexValue = "0" + hexValue;
        }
        byte[] bytes = new byte[hexValue.length() / 2];
        for (int i = 0; i < hexValue.length(); i += 2) {
            String hexVal = hexValue.substring(i, i + 2);
                bytes[i / 2] = (byte) (Integer.parseInt(hexVal, 16) & 0xFF);
        }
        return bytes;
    }

    public static byte[] hexStringToBytes(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    public static String bytesToHex(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            builder.append(Character.forDigit((bytes[i] >> 4) & 0xF, 16));
            builder.append(Character.forDigit((bytes[i] & 0xF), 16));
        }
        return builder.toString().toUpperCase();
    }

    public static byte[] getRandomBytes() {
        Random rd = new Random();
        byte[] arr = new byte[8];
        rd.nextBytes(arr);
        return arr;
    }

    public static byte[] xor(byte[] array1, byte[] array2) {
        byte[] maskedArray = new byte[array1.length];
        for(int i = 0; i < array1.length; i++) {
            int a = array1[i] & 0xFF;
            int b = array2[i] & 0xFF;
            int result = a ^ b;
            maskedArray[i] = (byte)result;
        }

        return maskedArray;
    }

    public static String padLeftZeros(String inputString, int length) {
        if (inputString.length() >= length) {
            return inputString;
        }
        StringBuilder sb = new StringBuilder();
        while (sb.length() < length - inputString.length()) {
            sb.append('0');
        }
        sb.append(inputString);

        return sb.toString();
    }

    public static byte[] padLeftZeros(byte[] source, int size) {
        byte[] bs = new byte[size];

        if (size < source.length) {
            return null;
        }

        int startPos = size - source.length;
        System.arraycopy(source, 0, bs, startPos, source.length);

        return bs;
    }

    public static String negativeValue(Long val){
        Long value = (~(val - 1));
        String hexstr = Long.toString(value, 16);
        return hexstr;
    }

    public static String twoComplementValue(Long val){
        Long value = (~(val - 1));
        String hex = Long.toHexString(value);
        return hex.substring(10);
    }

    public static String getTxnDateTime(){
        DateTime now = DateTime.now();
        System.out.println(now);
        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd-MM-yyyy HH:mm:ss");
        DateTime dtStart = DateTime.parse("01-01-1995 00:00:00", formatter);
        int sec = Math.abs(Seconds.secondsBetween(dtStart, now).getSeconds());
        return Integer.toHexString(sec);
    }

    public static LocalDateTime hexToDate(String hex){
        int secDate = Integer.parseInt(hex, 16);
        LocalDateTime ld = OffsetDateTime.of( 1995 , 1 , 1 , 0 , 0 , 0 , 0 , ZoneOffset.UTC )
                .plusSeconds( secDate )  // Add the number of seconds given to you. Returns another `OffsetDateTime` object rather than altering the original (immutable objects pattern).
                .toLocalDateTime();
        return ld;
    }

    public static String getHexDateTime(){
        OffsetDateTime of1 = OffsetDateTime.of( 1995 , 1 , 1 , 0 , 0 , 0 , 0 , ZoneOffset.UTC );
        OffsetDateTime of2 = OffsetDateTime.now();
        long ld = of1.until(of2, ChronoUnit.SECONDS);
        return Long.toHexString(ld);
    }

    public static long convertBalanceEMoney(byte[] bytes)
    {
        return ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getInt();
    }

    public static byte[] reverseAmountBrizzi(byte[] arr) {
        return new byte[]{
                Arrays.copyOfRange(arr, arr.length - 1, arr.length)[0],
                Arrays.copyOfRange(arr, arr.length - 2, arr.length - 1)[0],
                Arrays.copyOfRange(arr, arr.length - 3, arr.length - 2)[0]
        };
    }

    public static long convertAmountBrizzi(byte[] bytes) {
        bytes = reverseAmountBrizzi(bytes);
        return Integer.parseInt(Utility.bytesToHex(bytes),16);
    }

    public static byte[] convertBalanceEMoney(long balance) {
        byte[] b = new byte[4];
        b[0] = (byte) (balance & 0xFF);
        b[1] = (byte) ((balance >> 8) & 0xFF);
        b[2] = (byte) ((balance >> 16) & 0xFF);
        b[3] = (byte) ((balance >> 24) & 0xFF);
        return b;
    }

    public static byte[] padLeftByteArray(byte[] source, int size) {
        byte[] bs = new byte[size];
        if (size < source.length) {
            return null;
        }
        int startPos = size - source.length;
        System.arraycopy(source, 0, bs, startPos, source.length);
        return bs;
    }

    public static String logISOMsg(ISOMsg req, ISOMsg resp, String process) {
        Map body = new HashMap();
        body.put("process", process);
        Map dataReq = new HashMap<String, String>();
        Map dataResp = new HashMap<String, String>();
        try {
            dataReq.put("MTI", req.getMTI());
            for (int i=1;i<=req.getMaxField();i++) {
                if (req.hasField(i)) {
                    dataReq.put("Field-"+i, req.getString(i));
                }
            }
            if (resp != null){
                dataResp.put("MTI", resp.getMTI());
                for (int i=1;i<=resp.getMaxField();i++) {
                    if (resp.hasField(i)) {
                        dataResp.put("Field-"+i, resp.getString(i));
                    }
                }
            }
        } catch (ISOException e) {
            e.printStackTrace();
        }
        body.put("request", dataReq);
        body.put("response", dataResp);
        return Utility.objectToString(body);
    }

    public static String logISOMsg(ISOMsg req, String process) {
        return logISOMsg(req, null, process);
    }
}
