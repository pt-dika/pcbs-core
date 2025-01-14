package dk.apps.pcps.main.module.tapcash.function;

import dk.apps.pcps.main.utils.ISOConverters;
import dk.apps.pcps.main.utils.TripleDESUtils;
import dk.apps.pcps.main.module.tapcash.model.LogonData;

import java.util.Arrays;

public class MACCalculation {

    public static byte[] getMAC3desCBC(LogonData logon, byte[] msg){
        try {
            msg = Arrays.copyOfRange(msg, 0, msg.length - 8);
            byte[] tmk = ISOConverters.hexStringToBytes(logon.getTmk());
            byte[] rn = ISOConverters.hexStringToBytes(logon.getRn());
            byte[] sesKey = TripleDESUtils.encrypt3DES(rn, tmk);
            byte[] kek1 = ISOConverters.hexStringToBytes(logon.getKek1());
            kek1 = TripleDESUtils.decrypt3DES(kek1, sesKey);
            byte[] mack = ISOConverters.hexStringToBytes(logon.getMack());
            mack = TripleDESUtils.decrypt3DES(mack, kek1);
            byte[] pData = addPadding(msg);
            byte[] result = TripleDESUtils.encrypt3DES(pData, mack);
            byte[] block = Arrays.copyOfRange(result, result.length - 8, result.length);
            block = Arrays.copyOfRange(block, 0, 4);
            byte[] pad4 = new byte[]{0x00,0x00,0x00,0x00};
            byte[] mac = new byte[block.length + pad4.length];
            System.arraycopy(block, 0, mac, 0, block.length);
            System.arraycopy(pad4, 0, mac, block.length, pad4.length);
            return mac;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static byte[] addPadding(byte[] in) {
        int extra = in.length % 8;
        if (extra != 0)
            extra = 8 - extra;
        int newLength = in.length + extra;
        byte[] out = Arrays.copyOf(in, newLength);
        int offset = in.length;
        while (offset < newLength) {
            out[offset] = 0x00;
            offset++;
        }
        return out;
    }
}
