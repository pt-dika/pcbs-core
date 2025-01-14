package dk.apps.pcps.db.function;

import org.springframework.data.domain.Sort;
import dk.apps.pcps.commonutils.ISOConverters;

public class Global {
    public static Sort toSort(String sortBy){
        Sort sort;
        String[] str = sortBy.split(":");
        String[] fields = new String[0];
        if (str.length > 1)
            fields = str[1].split(",");
        if (fields.length > 0 && !fields[0].isEmpty())
            sort = Sort.by(Sort.Direction.fromString(str[0]), fields);
        else
            sort = Sort.by(Sort.Direction.ASC, "id");
        return sort;
    }

    public static boolean findBinNumber(String can, String start, String end){
        long lCAN = Long.valueOf(can);
        int canLength = can.length();
        boolean bBinRangeMatchFlag = false;

        Long lBinRangeStartBuff = Long.parseUnsignedLong(rightPaddingZeros(start,canLength));
        Long lBinRangeEndBuff = Long.parseUnsignedLong(rightPaddingNines(end,canLength));

        if (lBinRangeStartBuff<lCAN && lBinRangeEndBuff>lCAN) {
            bBinRangeMatchFlag = true;
        }
        return bBinRangeMatchFlag;
    }

    public static String rightPaddingZeros(String str, int num) {
        return String.format("%1$-" + num + "s", str).replace(' ', '0');
    }

    public static String rightPaddingNines(String str, int num) {
        return String.format("%1$-" + num + "s", str).replace(' ', '9');
    }

    public static String baseAmountPadding(long baseAmount){
        String asc = Integer.toHexString((int) baseAmount);
        String str = String.format("%06d", baseAmount);
        return str;
    }

    public static String getDateTime(){
        int unixTime = (int)(System.currentTimeMillis() / 1000);
        byte[] dates = new byte[]{
                (byte) (unixTime >> 24),
                (byte) (unixTime >> 16),
                (byte) (unixTime >> 8),
                (byte) unixTime

        };
        return ISOConverters.bytesToHex(dates);
    }
}
