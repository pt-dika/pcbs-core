package dk.apps.pcps.main.utils;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Security;

public class TripleDESUtils {

	public static byte[] decrypt3DES(byte[] message, byte[] bkey) {
		try {
			Security.addProvider(new BouncyCastleProvider());
			SecretKey keySpec = new SecretKeySpec(bkey, "DESede");
			IvParameterSpec iv = new IvParameterSpec(new byte[8]);
			Cipher d_cipher = Cipher.getInstance("DESede/CBC/NoPadding", "BC");
			d_cipher.init(Cipher.DECRYPT_MODE, keySpec, iv);
			byte[] cipherText = d_cipher.doFinal(message);

			return cipherText;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		return null;
	}
	
	public static byte[] encrypt3DES(byte[] message, byte[] bkey) {
		try {
			Security.addProvider(new BouncyCastleProvider());
			SecretKey keySpec = new SecretKeySpec(bkey, "DESede");
			IvParameterSpec iv = new IvParameterSpec(new byte[8]);
			Cipher e_cipher = Cipher.getInstance("DESede/CBC/NoPadding", "BC");
			e_cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv);
			byte[] cipherText = e_cipher.doFinal(message);

			return cipherText;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		return null;
	}
}
