

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class AESCrptography {  
    public static byte[] AES_CBC_Encrypt(byte[] content, byte[] keyBytes, byte[] iv){  
        try{  
            KeyGenerator keyGenerator=KeyGenerator.getInstance("AES"); 
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            random.setSeed(keyBytes);
            keyGenerator.init(128, random);  
            SecretKey key=keyGenerator.generateKey();  
            Cipher cipher=Cipher.getInstance("AES/CBC/PKCS5Padding");  
            cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));  
            byte[] result=cipher.doFinal(content);  
            return result;  
        }catch (Exception e) {  
            System.out.println("exception:"+e.toString());  
        }   
        return null;  
    }  
    public static byte[] AES_CBC_Decrypt(byte[] content, byte[] keyBytes, byte[] iv){  
        try{  
        	KeyGenerator keyGenerator=KeyGenerator.getInstance("AES"); 
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            random.setSeed(keyBytes);
            keyGenerator.init(128, random); //key长可设为128，192，256位，这里只能设为128  
            SecretKey key=keyGenerator.generateKey();  
            Cipher cipher=Cipher.getInstance("AES/CBC/PKCS5Padding");  
            cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));  
            byte[] result=cipher.doFinal(content);  
            return result;  
        }catch (Exception e) {  
            // TODO Auto-generated catch block  
            System.out.println("exception:"+e.toString());  
        }   
        return null;  
    }  
    public static String byteToHexString(byte[] bytes) {  
        StringBuffer sb = new StringBuffer(bytes.length);  
        String sTemp;  
        for (int i = 0; i < bytes.length; i++) {  
            sTemp = Integer.toHexString(0xFF & bytes[i]);  
            if (sTemp.length() < 2)  
                sb.append(0);  
            sb.append(sTemp.toUpperCase());  
        }  
        return sb.toString();  
    }  
    public static String hexStr2Str(String hexStr) {
        String str = "0123456789ABCDEF";
        char[] hexs = hexStr.toCharArray();
        byte[] bytes = new byte[hexStr.length() / 2];
        int n;
        for (int i = 0; i < bytes.length; i++) {
            n = str.indexOf(hexs[2 * i]) * 16;
            n += str.indexOf(hexs[2 * i + 1]);
            bytes[i] = (byte) (n & 0xff);
        }
        return new String(bytes);
    }
    public static byte[] toBytes(String str) {
        if(str == null || str.trim().equals("")) {
            return new byte[0];
        }

        byte[] bytes = new byte[str.length() / 2];
        for(int i = 0; i < str.length() / 2; i++) {
            String subStr = str.substring(i * 2, i * 2 + 2);
            bytes[i] = (byte) Integer.parseInt(subStr, 16);
        }

        return bytes;
    }
}  
