package dk.apps.pcps.main.utils;

import dk.apps.pcps.commonutils.StringUtil;
import dk.apps.pcps.commonutils.TripleDESFunction;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOPackager;
import org.jpos.iso.packager.GenericPackager;

import java.security.MessageDigest;
import java.util.Arrays;


public class PlainToTLEConfig {

    private String packager;

    public PlainToTLEConfig(String packager){
        this.packager = packager;
    }

    public ISOMsg plainToTLEMsg(ISOMsg imPlainMsg, String hsTID, String LTWK_ID, String hsMAK, String hsDEK) throws ISOException {
        System.out.println("convert plain message into bni tle message...");

        ISOPackager isoPack = new GenericPackager(packager);

        ISOMsg imBuffMsg = new ISOMsg();

        try {
            imPlainMsg.setPackager(isoPack);

            imBuffMsg = imPlainMsg;

            byte[] baMAK = StringUtil.hexStringToBytes(hsMAK);

            byte[] baBuffMsg = imBuffMsg.pack();

            if (baBuffMsg[0] == 0) {
                byte[] tmp = new byte[baBuffMsg.length - 1];
                System.arraycopy(baBuffMsg, 1, tmp, 0, tmp.length);
                baBuffMsg = tmp;
            }
            baBuffMsg = Arrays.copyOfRange(baBuffMsg, 0, baBuffMsg.length - 8);

            //System.out.println("iso8583 plain: " + bytesToHex(baBuffMsg));

            System.out.println("calculate mac...");
            byte[] baDE64 = calculateMAC(baBuffMsg, baMAK);
            imBuffMsg.set(64, bytesToHex(baDE64));

            byte[] baDEK = StringUtil.hexStringToBytes(hsDEK);
            //System.out.println("dek: " + bytesToHex(baDEK));

            String hsHTLE = asciiToHex("HTLE");
            String hsTLEVersion = asciiToHex("03");
            String hsACQ_ID = asciiToHex("001");
            String hsLTID = asciiToHex(hsTID);
            String hsEncryptionMethode = asciiToHex("201");
            String hsLTWK_ID = asciiToHex(LTWK_ID);
            int iLenDE57Content = 0;
            String hsDE57PlainContent = "";

            // de2 - pan
            if (imBuffMsg.getString(2) != null) {
                //System.out.println("imBuffMsg.getString(2): " + imBuffMsg.getString(2));
                int lenPAN = imBuffMsg.getString(2).length();
                if (lenPAN%2 != 0) {
                    //System.out.println("pan is odd.");
                    hsDE57PlainContent = hsDE57PlainContent
                            + "02"
                            + String.format("%02x", (lenPAN/2)+2)
                            + String.format("%02d", lenPAN)
                            + imBuffMsg.getString(2)
                            + "0";
                } else {
                    //System.out.println("pan is even.");
                    hsDE57PlainContent = hsDE57PlainContent
                            + "02"
                            + String.format("%02x", (lenPAN/2)+1)
                            + String.format("%02d", lenPAN)
                            + imBuffMsg.getString(2);
                }
            }

            // de4 - amount
            if (imBuffMsg.getString(4) != null) {
                String hsAmt = String.format("%012d", Long.parseLong(imBuffMsg.getString(4)));
                //System.out.println("imBuffMsg.getString(4): " + hsAmt);
                int lenAmt = hsAmt.length()/2;
                hsDE57PlainContent = hsDE57PlainContent
                        + "04"
                        + String.format("%02x", lenAmt)
                        + hsAmt;
            }

            // de14 - card expired date
            if (imBuffMsg.getString(14) != null) {
                String cardExpiredDate = imBuffMsg.getString(14);
                //System.out.println("imBuffMsg.getString(14): " + cardExpiredDate);
                int lenCardExpiredDate = cardExpiredDate.length()/2;
                hsDE57PlainContent = hsDE57PlainContent
                        + "16"
                        + String.format("%02x", lenCardExpiredDate)
                        + cardExpiredDate;
            }

            // de35 - track 2 data
            if (imBuffMsg.getString(35) != null) {
                String track2Data = imBuffMsg.getString(35);
                //System.out.println("imBuffMsg.getString(35): " + track2Data);
                int lenTrack2Data = imBuffMsg.getString(35).length();

                String strBuffPart1;
                String strBuffPart2;
                String strBuffPart3;

                if (track2Data.contains("A") || track2Data.contains("B")
                        || track2Data.contains("C") || track2Data.contains("D")
                        || track2Data.contains("E") || track2Data.contains("F")) {
                    strBuffPart1 = track2Data.substring(0, track2Data.length() - 2);
                    //System.out.println("strBuffPart1 = " + strBuffPart1);
                    strBuffPart2 = track2Data.substring(track2Data.length() - 2, track2Data.length() - 1);
                    //System.out.println("strBuffPart2 = " + strBuffPart2);
                    strBuffPart3 = track2Data.substring(track2Data.length() - 1);
                    //System.out.println("strBuffPart3 = " + strBuffPart3);

                    if (lenTrack2Data % 2 == 0) {
                        if(strBuffPart2.contains("0")) {
                            strBuffPart2 = "1";
                        } else if(strBuffPart2.contains("2")) {
                            strBuffPart2 = "3";
                        } else if(strBuffPart2.contains("4")) {
                            strBuffPart2 = "5";
                        } else if(strBuffPart2.contains("6")) {
                            strBuffPart2 = "7";
                        } else if(strBuffPart2.contains("8")) {
                            strBuffPart2 = "9";
                        }
                    }

                    track2Data = strBuffPart1 + strBuffPart2 + strBuffPart3;

                    track2Data = track2Data.replace("A", "1");
                    track2Data = track2Data.replace("B", "2");
                    track2Data = track2Data.replace("C", "3");
                    track2Data = track2Data.replace("D", "4");
                    track2Data = track2Data.replace("E", "5");
                    track2Data = track2Data.replace("F", "6");
                }
                track2Data = track2Data.replace("=", "D");
                //System.out.println("track2Data = " + track2Data);

                if (lenTrack2Data % 2 != 0) {
                    System.out.println("track 2 data is odd.");
                    hsDE57PlainContent = hsDE57PlainContent
                            + "43"
                            + String.format("%02x", (lenTrack2Data/2)+2)
                            + String.format("%02d", lenTrack2Data)
                            + track2Data
                            + "0";
                } else {
                    System.out.println("track 2 data is even.");
                    hsDE57PlainContent = hsDE57PlainContent
                            + "43"
                            + String.format("%02x", (lenTrack2Data/2)+1)
                            + String.format("%02d", lenTrack2Data)
                            + track2Data;
                }
            }

            // de55 - emv data
            if (imBuffMsg.getString(55) != null) {
                String hsEMVData = imBuffMsg.getString(55);
                //System.out.println("imBuffMsg.getString(55) = " + hsEMVData);
                int lenEMVData = hsEMVData.length()/2;
                //System.out.println("lenEMVData = " + lenEMVData);
                if (lenEMVData <= 127) {
                    System.out.println("emv data length less than 127 (0-127)."); // length = 0-127 -> 0xxxxxx
                    hsDE57PlainContent = hsDE57PlainContent
                            + "67"
                            + String.format("%02x", (lenEMVData)+2)
                            + String.format("%04d", lenEMVData)
                            + hsEMVData;
                } else if(lenEMVData <= 255) {
                    System.out.println("emv data length less than 255 (128-255)."); // length = 128-255 -> 10000001 xxxxxxxx
                    hsDE57PlainContent = hsDE57PlainContent
                            + "67"
                            + "81"
                            + String.format("%02x", (lenEMVData)+2)
                            + String.format("%04d", lenEMVData)
                            + hsEMVData;
                } else {
                    System.out.println("emv data length more than 255 (256-65535)."); // length = 256-65535 -> 10000010 xxxxxxxx xxxxxxxx
                    hsDE57PlainContent = hsDE57PlainContent
                            + "67"
                            + "82"
                            + String.format("%04x", (lenEMVData)+2)
                            + String.format("%04d", lenEMVData)
                            + hsEMVData;
                }
            }
            //System.out.println("hsDE57PlainContent = " + hsDE57PlainContent);

            byte[] baDE57EcryptedContent = null;
            byte[] baDE57PlainContentPadded = null;
            byte[] baDE57PlainContent = null;
            if (hsDE57PlainContent != "") {
                baDE57PlainContent = StringUtil.hexStringToBytes(hsDE57PlainContent);
                //System.out.println("baDE57PlainContent: " + bytesToHex(baDE57PlainContent));
                iLenDE57Content = baDE57PlainContent.length;

                byte[] zero8 = new byte[] { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };
                int iLen;
                if (iLenDE57Content % 8 != 0) {
                    iLen = ((iLenDE57Content / 8) + 1) * 8;
                    //System.out.println("inLen = " + inLen);
                    //System.out.println("lenDE57Content = " + lenDE57Content);
                    //System.out.println(8 - (lenDE57Content % 8));
                    baDE57PlainContentPadded = new byte[iLen];
                    System.arraycopy(baDE57PlainContent, 0, baDE57PlainContentPadded, 0, baDE57PlainContent.length);
                    System.arraycopy(zero8, 0, baDE57PlainContentPadded, iLenDE57Content, 8 - (iLenDE57Content % 8));
                } else {
                    iLen = iLenDE57Content;
                    baDE57PlainContentPadded = new byte[iLen];
                    System.arraycopy(baDE57PlainContent, 0, baDE57PlainContentPadded, 0, baDE57PlainContent.length);
                }

                //System.out.println("baDE57PlainContentPadded: " + bytesToHex(baDE57PlainContentPadded));
                baDE57EcryptedContent = TripleDESFunction.encrypt3DES(baDE57PlainContentPadded, baDEK);
                //System.out.println("baDE57EcryptedContent: " + bytesToHex(baDE57EcryptedContent));
            }

            String hsDE57Header;
            String hsDE57;
            if (!(baDE57EcryptedContent == null)) {
                hsDE57Header = hsHTLE + hsTLEVersion + hsACQ_ID + hsLTID + hsEncryptionMethode + hsLTWK_ID
                        + asciiToHex("0008") + asciiToHex(String.format("%03d", baDE57PlainContent.length)) + "0000000000";
                hsDE57 = hsDE57Header + bytesToHex(baDE57EcryptedContent);
            } else {
                hsDE57Header = hsHTLE + hsTLEVersion + hsACQ_ID + hsLTID + hsEncryptionMethode + hsLTWK_ID
                        + asciiToHex("0008") + asciiToHex("000") + "0000000000";
                hsDE57 = hsDE57Header;
            }

            //byte[] baDE57Header = StringUtil.hexStringToBytes(hsDE57Header);
            //System.out.println("baDE57Header = " + bytesToHex(baDE57Header));
            //System.out.println("hsDE57 = " + hsDE57);

            byte[] baDE57 = StringUtil.hexStringToBytes(hsDE57);
            //System.out.println("baDE57 = " + bytesToHex(baDE57));

            imBuffMsg.set(57, baDE57);

            // unset sensitive data
            imBuffMsg.unset(2);
            imBuffMsg.unset(4);
            imBuffMsg.unset(14);
            imBuffMsg.unset(35);
            imBuffMsg.unset(55);

        } catch (ISOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return imBuffMsg;
    }

    public byte[] ISOMsgToByteArray(ISOMsg imPlainMsg) throws ISOException {
        ISOMsg imBuffMsg = new ISOMsg();

        try {
            ISOPackager isoPack = new GenericPackager(packager);
            imBuffMsg.setPackager(isoPack);

            imBuffMsg = imPlainMsg;

            byte[] baBuffMsg = imBuffMsg.pack();
            //String hsBuffMsg = bytesToHex(baBuffMsg);
            //System.out.println("hsBuffMsg = " + hsBuffMsg);

            if (baBuffMsg[0] == 0) {
                byte[] tmp = new byte[baBuffMsg.length - 1];
                System.arraycopy(baBuffMsg, 1, tmp, 0, tmp.length);
                baBuffMsg = tmp;
            }
            baBuffMsg = Arrays.copyOfRange(baBuffMsg, 0, baBuffMsg.length - 8);

            //System.out.println("ISO8583 Message: " + bytesToHex(baBuffMsg));
        } catch (ISOException e) {
            e.printStackTrace();
        }

        return imBuffMsg.pack();
    }

    private static byte[] calculateMAC(byte[] msg, byte[] key) {
        byte[] baMAC = null;

        try {
            MessageDigest cript = MessageDigest.getInstance("SHA-1");
            cript.reset();
            cript.update(msg);
            byte[] xx = cript.digest();
            byte[] pad = new byte[] { (byte) 0x80, 0x00, 0x00, 0x00 };
            byte[] xxx = concatenateByteArrays(xx, pad);
            //System.out.println("xx: " + bytesToHex(xxx));
            byte[] yy = TripleDESFunction.encrypt3DES(xxx, key);
            //System.out.println("yy: " + bytesToHex(yy));

            byte[] MAC4 = new byte[4];
            byte[] zero = new byte[] { 0x00, 0x00, 0x00, 0x00 };
            System.arraycopy(yy, 16, MAC4, 0, 4);
            baMAC = concatenateByteArrays(MAC4, zero);
            //System.out.println("MAC: " + bytesToHex(baMAC));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return baMAC;
    }

    private static String bytesToHex(byte[] bytes) {
        char[] hexArray = "0123456789ABCDEF".toCharArray();
        char[] hexChars = new char[bytes.length * 2];

        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }

        return new String(hexChars);
    }

    private static byte[] concatenateByteArrays(byte[] a, byte[] b) {
        byte[] result = new byte[a.length + b.length];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);

        return result;
    }

    private static String asciiToHex(String asciiValue) {
        char[] chars = asciiValue.toCharArray();
        StringBuffer hex = new StringBuffer();

        for (int i = 0; i < chars.length; i++) {
            hex.append(Integer.toHexString((int) chars[i]));
        }

        return hex.toString();
    }
}
