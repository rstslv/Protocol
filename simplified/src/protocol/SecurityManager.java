package protocol;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import java.security.*;
import java.util.Base64;

import static java.nio.charset.StandardCharsets.UTF_8;

public class SecurityManager {

  public String algorithm = "AES/CBC/PKCS5Padding";

  private PrivateKey privateKey;
  private PublicKey publicKey;

  private SecretKey AESKey;

  public void setAESKey(SecretKey key)
  {
    this.AESKey = key;
  }

  private IvParameterSpec Iv4;

  public SecurityManager() throws NoSuchAlgorithmException {
    KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
    keyGen.initialize(2048);
    KeyPair pair = keyGen.generateKeyPair();
    this.privateKey = pair.getPrivate();
    this.publicKey = pair.getPublic();

    KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
    keyGenerator.init(256);
    this.AESKey = keyGenerator.generateKey();
    this.Iv4 = generateIv();
  }

  public static IvParameterSpec generateIv() {
    byte[] iv = new byte[16];
    new SecureRandom().nextBytes(iv);
    return new IvParameterSpec(iv);
  }

  public static String sign(String plainText, PrivateKey privateKey) throws Exception {
    Signature privateSignature = Signature.getInstance("SHA256withRSA");
    privateSignature.initSign(privateKey);
    privateSignature.update(plainText.getBytes(UTF_8));

    byte[] signature = privateSignature.sign();

    return Base64.getEncoder().encodeToString(signature);
  }

  public static boolean verify(String plainText, String signature, PublicKey publicKey)
      throws Exception {
    Signature publicSignature = Signature.getInstance("SHA256withRSA");
    publicSignature.initVerify(publicKey);
    publicSignature.update(plainText.getBytes(UTF_8));

    byte[] signatureBytes = Base64.getDecoder().decode(signature);

    return publicSignature.verify(signatureBytes);
  }

  public static byte[] encrypt(byte[] plainText, PublicKey publicKey) throws Exception {
    Cipher encryptCipher = Cipher.getInstance("RSA");
    encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);

    return encryptCipher.doFinal(plainText);
  }

  public static byte[] decrypt(byte[] cipherText, PrivateKey privateKey) throws Exception {
    Cipher decryptCipher = Cipher.getInstance("RSA");
    decryptCipher.init(Cipher.DECRYPT_MODE, privateKey);

    return decryptCipher.doFinal(cipherText);
  }

  public PrivateKey getPrivateKey() {
    return privateKey;
  }

  public PublicKey getPublicKey() {
    return publicKey;
  }

  public SecretKey getAESKey() {
    return AESKey;
  }

  public byte[] encryptPacket(iPacket packet)
      throws NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException,
          IllegalBlockSizeException, InvalidAlgorithmParameterException, InvalidKeyException {
    byte[] bytes = packet.toByteArray();
    Cipher cipher = Cipher.getInstance(algorithm);
    cipher.init(Cipher.ENCRYPT_MODE, this.AESKey, this.Iv4);
    byte[] cipherText = cipher.doFinal(bytes);
    return cipherText;
  }

  public iPacket decryptPacket(byte[] bytes) throws Exception {
    Cipher cipher = Cipher.getInstance(algorithm);
    cipher.init(Cipher.DECRYPT_MODE, this.AESKey, this.Iv4);
    byte[] plainText = cipher.doFinal(bytes);
    return iPacket.toPacket(plainText);
  }
}
