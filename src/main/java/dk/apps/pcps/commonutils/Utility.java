package dk.apps.pcps.commonutils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.Primitives;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class Utility {

    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    public static String getUser() {
        return "";
//        Authentication authentication = SecurityContextHolder.getContext()
//                .getAuthentication();
//        if (authentication == null)
//            return "";
//        return authentication.getName();
    }

    public static String getUserEdc(JSONObject jo) {
        if(jo == null)
            return "";
        String strUsername = jo.getString("sub");
        return strUsername;
    }

    public static Long getUserExpiredEdc(JSONObject jo) {
        if(jo == null)
            return null;
        Long exp = jo.getLong("exp");
        return exp;
    }

    public static JSONObject jwtExtractor(String token){
        String strUsername = "";
        String strToken = token.replaceFirst("Murdoc ", "");
        try {
            String[] saToken = strToken.split("\\.");
            String strTokenBody = getJson(saToken[1]);
            JSONObject joTokenBody = new JSONObject(strTokenBody);
            if(!joTokenBody.has("sub"))
                return null;
            return joTokenBody;
        } catch (Exception e){
            return null;
        }
    }

    private static String getJson(String strEncoded) throws UnsupportedEncodingException {
        byte[] decoded = Base64.getDecoder().decode(strEncoded);

        return new String(decoded, "UTF-8");
    }

    public static Gson initGson(FieldNamingPolicy namingPolicy){
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(namingPolicy)
                .create();
        return gson;
    }

    public static String objectToString(Object o) {
        Gson g = initGson(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        String json = g.toJson(o);
        return json;
    }

    public static <T> T jsonArrayToDto(String jsonArray, TypeReference<T> toValueTypeRef) {
        Object o = jsonToObject(jsonArray, Object.class);
        com.fasterxml.jackson.databind.ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(o, toValueTypeRef);
    }

    public static <T> T jsonToObject(String json, Class<T> classOfT) {
        if (json == null)
            return null;
        Gson g = initGson(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        Object o = g.fromJson(json, classOfT);
        return Primitives.wrap(classOfT).cast(o);
    }

    public static <T> T mapperToDto(Object o, Class<T> classOfT) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper.convertValue(o, classOfT);
    }

    public static <T> T mapperToDto(Object o, TypeReference<T> toValueTypeRef) {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(o, toValueTypeRef);
    }

    public static String responseFormat(Object obj){
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .setDateFormat("yyyy-MM-dd hh:mm:ss")
                .create();
        String json = gson.toJson(obj);
        return json;
    }

//    public static Map toMap(Throwable map) {
//        Map<String, String> newMap = new HashMap<>();
//        if (map != null) {
//            String[] aArr = (map.toString()).split(",");
//            for (String strA : aArr) {
//                String[] bArr = strA.split("=");
//                for (String strB : bArr) {
//                    newMap.put(bArr[0], strB.trim());
//                }
//            }
//        } else {
//            newMap.put("error", "");
//            newMap.put("error_description", "");
//        }
//        return newMap;
//    }


    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    public static byte[] getRandomByteArray(int numchars) {
        Random r = new Random();
        String temp;
        StringBuffer sb = new StringBuffer();
        while (sb.length() < numchars) {
            sb.append(Integer.toHexString(r.nextInt()));
        }

        temp = sb.toString().substring(0, numchars);
        return StringToByteArray(temp);
    }

    public static byte[] StringToByteArray(String szData) {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < szData.length(); i += 2) {
            String str = szData.substring(i, i + 2);
            output.append((char) Integer.parseInt(str, 16));
        }

        try {
            return output.toString().getBytes();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] getRandomBytes() {
        Random rd = new Random();
        byte[] arr = new byte[8];
        rd.nextBytes(arr);
        return arr;
    }

    public static String hexString(String... str){
        return String.join("", str);
    }

    public static Map toMap(Throwable map) {
        Map<String, String> newMap = new HashMap<>();
        if (map != null) {
            String[] aArr = (map.toString()).split(",");
            for (String strA : aArr) {
                String[] bArr = strA.split("=");
                for (String strB : bArr) {
                    newMap.put(bArr[0], strB.trim());
                }
            }
        } else {
            newMap.put("error", "");
            newMap.put("error_description", "");
        }
        return newMap;
    }

    public static String maskedCan(String strCan){
        String strMaskedString = new String(new char[strCan.length()]).replace('\0','*');
        String strMaskedCAN = strCan.substring(0,6) + strMaskedString.substring(6,strCan.length()-4) + strCan.substring(strCan.length()-4,strCan.length());
        System.out.println("masked can: " + strMaskedCAN);
        return strMaskedCAN;
    }

    public static Timestamp getTimeStamp(){
        return new Timestamp(System.currentTimeMillis());
    }

    public static String getDateTime(){
        return getDateTime("MMddhhmmss");
    }

    public static String getDateTime(String format){
        Date currentDate = new Date(System.currentTimeMillis());

        //printing value of Date
        System.out.println("current Date: " + currentDate);

        DateFormat df = new SimpleDateFormat(format);
        return df.format(currentDate);
    }

    public static String getDateTime(String format, boolean isLastDay, int days){
        DateTimeFormatter dtf = DateTimeFormatter
                .ofPattern(format);
        LocalDate now = LocalDate.now();
        LocalDate dates = null;
        if (isLastDay)
            dates = now.minusDays(days);
        else
            dates = now.plusDays(days);
        return dtf.format(dates);
    }

    public static long countDays(String from, String to){
        if (from.equals("") || to.equals("")){
            return 0;
        }
        return ChronoUnit.DAYS.between(strToLocalDate(from).atStartOfDay(), strToLocalDate(to).atStartOfDay());
    }


    public static Long timeToSeconds(String time) {
        Date date = strToDateTime("HH:mm:ss", time);
        return date.getTime();
    }

    public static Time strToTime(String val){
        Date dt = strToDateTime("HH:mm:ss", val);
        return new Time(dt.getTime());
    }

    public static String timeToStr(Time val){
        return strDateTimeFormat("HH:mm:ss", val);
    }

    public static String[] getTimes(String val){
        String tf = "HH:mm:ss";
        Date dt = strToDateTime(tf, val);
        return strDateTimeFormat(tf, dt).split(":");
    }

    public static String dateTimeFormat(String format, LocalDateTime val){
        DateTimeFormatter df = DateTimeFormatter.ofPattern(format);
        String dt = df.format(val);
        return dt;
    }

    public static String strDateTimeFormat(String format, Date val){
        DateFormat df = new SimpleDateFormat(format);
        String dt = df.format(val);
        return dt;
    }

    public static String strDateTimeFormat(String format, Time val){
        DateFormat df = new SimpleDateFormat(format);
        String dt = df.format(val);
        return dt;
    }

    public static Date strToDateTime(String format, String val){
        DateFormat df = new SimpleDateFormat(format);
        Date dt = null;
        try {
            dt = df.parse(val);
        } catch (ParseException e){

        }
        return dt;
    }

    public static LocalDate strToLocalDate(String val){
        LocalDate dt = LocalDate.parse(val);
        return dt;
    }

    public static String encodeToString(BufferedImage image, String type) {
        String imageString = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try {
            ImageIO.write(image, type, bos);
            byte[] imageBytes = bos.toByteArray();

            imageString = Base64.getEncoder().encodeToString(imageBytes);

            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return imageString;
    }
}
