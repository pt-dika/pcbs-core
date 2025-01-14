package dk.apps.pcps;

import dk.apps.pcps.commonutils.ISOConverters;
import dk.apps.pcps.commonutils.TripleDESFunction;
import dk.apps.pcps.commonutils.Utility;
import dk.apps.pcps.model.iso8583.bni.model.AdditionalData;
import dk.apps.pcps.model.iso8583.bni.model.CardData;
import dk.apps.pcps.model.payload.ReqPayment;
import org.joda.time.*;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class Tests {
    public static void main(String[] args) {
        String leftPad = String.format("%06d", 10);
        System.out.println(leftPad);

        byte[] baData = Utility.hexStringToByteArray("76C70000");
        long val = convertBalance(baData);
        System.out.println(val);


    }

    public static long convertBalance (byte[] bytes)
    {
        return ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getInt();
    }


    public static void test() throws ParseException, InterruptedException, IOException{
        int i = 0;
        int batas = 10;

        do {
            System.out.println(i);
            System.out.println("Test");
            System.out.println(ISOConverters.padLeftZeros(""+i* 10, 4));
            i++;
        } while (i != batas);


        AdditionalData additionalData = new AdditionalData();
        additionalData.respAdditionalDataCardUpdate("0600271031B996691219461900000000E42A224EA4DA73AA9BA84C3CD89BBE02");

        System.out.println(additionalData.getCreditCryptogram());
        System.out.println(additionalData.getTransLogRecord());






        //String ds= getDateTimes("d MMM yyyy hh:mm:ss");
        //System.out.println(ds);
        //Timestamp ts = new Timestamp(Long.parseLong("6051B182", 16));
        System.out.println(hexToDate("6054D1F4"));
        OffsetDateTime of1 = OffsetDateTime.of( 1995 , 1 , 1 , 0 , 0 , 0 , 0 , ZoneOffset.UTC );
        OffsetDateTime of2 = OffsetDateTime.now();
        long ld = of1.until(of2, ChronoUnit.SECONDS);
        System.out.println(Long.toHexString(ld));
        System.out.println(getTxnDateTime());

        String blac = "001100017546000000000063000000000175460000010560980000000001754613000000003500000000017546130000008400000000000175461300000091920000000001754613000001236000000000017546130000014069000000000175461300000558980000000001754613000005591400000000017546130000055922000000";
        byte[] bData = ISOConverters.hexStringToBytes(blac);
        byte[] bNext = Arrays.copyOfRange(bData, 0, 2);
        byte[] bCount = Arrays.copyOfRange(bData, 2, 4);
        //byte[] bCan = Arrays.(bData, 4, 10);

        System.out.println(Integer.parseInt("0000"));

        Map body = new HashMap();
        Map req = new HashMap<String, String>();
        Map resp = new HashMap<String, String>();
        body.put("process", "init");
        req.put("name","q");
        req.put("address","qa");
        resp.put("name","q");
        resp.put("address","qa");
        body.put("request", req);
        body.put("response", resp);
        System.out.println(Utility.objectToString(body));

        Double numb = 13.0/10;
        System.out.println(numb);
        System.out.println(Math.round(numb));
        Runnable run = () -> System.out.println("Thread Running.");
        new Thread(run).start();
        Thread.sleep(5000);

        Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
        System.out.println(threadSet);

        byte[] samId = Utility.hexStringToByteArray("0050000000000017");
        byte[] sessionKey = Utility.hexStringToByteArray("19D7B6A19A84AB1DD310B1E511B35208");
        byte[] encData = TripleDESFunction.encrypt3DES(samId, sessionKey);
        System.out.println(Utility.bytesToHex(encData));
        long sec = timeToSeconds("00:10:00");
        DateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        Date dt = sdf.parse("00:10:00");
        String ti = sdf.format(dt);
        System.out.println("Time: "+new Time(sec));
        int hours = (int) sec / 3600;
        int remainder = (int) sec - hours * 3600;
        int mins = remainder / 60;
        remainder = remainder - mins * 60;
        int secs = remainder;

        System.out.println("Minutes: "+ mins);

        ReqPayment reqPayment = new ReqPayment();
        CardData card = new CardData();
        card.setTrn("0000000000000000");
        reqPayment.setCardData(card);
        CardData cardData = (CardData) reqPayment.getCardData();

        System.out.println(cardData.getTrn());

        System.out.println(ISOConverters.hexToDate("3165f8ad"));

        System.out.println(Utility.getTimeStamp());

        System.out.println(Utility.strToDateTime("dd-MM-yyyy", "12-09-2020"));

        String json = "{\"first_name\": \"diyas\"}";

        Object obj = Utility.jsonToObject(json, Object.class);

        System.out.println(obj);

        JsonTest jsonTest = Utility.mapperToDto(obj, JsonTest.class);

        System.out.println(jsonTest.getFirstName());
        System.out.println(Utility.getDateTime("yyyy-MM-dd", true,7));
        System.out.println(Utility.countDays("2021-04-20", "2021-04-27"));
        Long[] amount = new Long[]{1L,1L,1L,1L};
        long total = Arrays.stream(amount).mapToLong(Long::longValue).sum();
        System.out.println(total);

        System.out.println(new File(System.getProperty("user.home")).getPath());
    }

    public static void mainThread() throws InterruptedException {
        int jumlah = 10;
        Thread thread = new Thread(() -> {
            try{
                for(int w=1; w<=jumlah; w++){
                    System.out.println("Nomor: "+w);
                    Thread.sleep(1000); //Waktu Pending
                    if (w == 3)
                        mainThread();
                }
            }catch(InterruptedException ex){
                ex.printStackTrace();
            }
        });
        thread.start();
    }

    public static Long timeToSeconds(String time) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        Date date = sdf.parse(time);
        return date.getTime();
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

    public static Date addSeconds(Date date, Integer seconds) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.SECOND, seconds);
        return cal.getTime();
    }

    public static String getTxnDateTime(){
        DateTime now = DateTime.now();
        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd-MM-yyyy HH:mm:ss");
        DateTime dtStart = DateTime.parse("01-01-1995 00:00:00", formatter);
        int sec = Math.abs(Seconds.secondsBetween(dtStart, now).getSeconds());
        System.out.println("Seconds : "+sec);
        return Integer.toHexString(sec);
    }

    public static String getDateTime(Long ts){
        long unixTime = ts / 1000;
        byte[] dates = new byte[]{
                (byte) (unixTime >> 24),
                (byte) (unixTime >> 16),
                (byte) (unixTime >> 8),
                (byte) unixTime

        };
        return ISOConverters.bytesToHex(dates);
    }

    public static String getDateTimes(String format){
        Date currentDate = new Date(System.currentTimeMillis());
        System.out.println("current Date: " + currentDate);
        System.out.println(getDateTime(currentDate.getTime()));
        DateFormat df = new SimpleDateFormat(format);
        return df.format(currentDate);
    }


    public static class DateFormatConverter {

        private String inputDateFormat;
        private String outputDateFormat;

        private DateFormatConverter(String inputDateFormat, String outputDateFormat) {
            this.inputDateFormat = inputDateFormat;
            this.outputDateFormat = outputDateFormat;
        }

        private String convert(String inputDate) throws ParseException {
            SimpleDateFormat idf = new SimpleDateFormat(inputDateFormat);
            SimpleDateFormat odf = new SimpleDateFormat(outputDateFormat);
            java.util.Date date = idf.parse(inputDate);
            String outputDate = odf.format(date);
            return outputDate;
        }

        public static String toJulian(String inputFormat, String inputDate) throws ParseException {
            String suffixFormat = "yyDDD";
            String prefixFormat = "yyyy";
            String suffix = new DateFormatConverter(inputFormat, suffixFormat).convert(inputDate);
            int centuryPrefix = Integer.parseInt(new DateFormatConverter(inputFormat, prefixFormat).convert(inputDate).substring(0, 2)) - 19;
            return centuryPrefix + suffix;
        }
    }
}
