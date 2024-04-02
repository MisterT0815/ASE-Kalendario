package kalendario.domain.entities.benutzer;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswortHasher {
    MessageDigest digest;
    public PasswortHasher() throws NoSuchAlgorithmException {
        digest = MessageDigest.getInstance("SHA-256");
    }
    public String hashPasswort(String passwort){
        byte[] encodedhash = digest.digest(
                passwort.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(encodedhash);
    }

    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
