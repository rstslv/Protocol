package protocol;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import static protocol.SecurityManager.generateIv;

public class Main {

  public static void main(String[] args) throws Exception
  {
    var booleanField = new Field(true);
    var charField = new Field('e');
    var stringField = new Field("String data");
    var intField = new Field(10);
    var floatField = new Field(10.4);
    // System.out.println(new Float(new String(floatField.content)));
    List<Field> fields = new ArrayList<>();
    // fields.add(field2);
    // fields.add(field3);

    /*var packet = new Packet(Constants.TYPE_DATA, Constants.TYPE_OK, fields);
    var bytes = packet.toByteArray();
    var p1 = Packet.parse(bytes);
    System.out.println(bytes.length);

    var ch = Packet.calculateChecksum(bytes);
    var ch2 = Packet.calculateChecksum(bytes);

    ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
    buffer.putLong(ch + 1);
    byte t = 0x01;*/

    var t1 = (Object) 1123123123;
    var t2 = (Object) 2.2;
    var t3 = (Object) "strasdasdasdasdstrasdasdasdasdstrasdasdasdasdstra";
    var arr = new Object[] {t1, t2, t3};
    ArrayList<Object> a = new ArrayList<>();
    //a.add(t1);
    //a.add(t2);
    a.add(t3);
    var field = new iField(a);
    var objects = (ArrayList<Object>) Serializer.deserialize(field.content);
    //objects.trimToSize();
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ObjectOutputStream out = new ObjectOutputStream(baos);
    out.writeObject(objects);
    out.close();
    System.out.println(baos.toByteArray().length);
    for (var e : objects) {
      System.out.println(e);
    }

    System.out.println((byte) 220);

    var complex = new Object[] {"/command", 1};
    var o = "/command";
    //var p = new iPacket(Constants.TYPE_DATA, complex);
    var o1 = "/command";
    var p1 = new iPacket(Constants.TYPE_DATA, "/Command1");
    System.out.println(p1.data.getValue());
    p1.sequenceNumber = 77;
    var b = p1.toByteArray();
    var v = iPacket.toPacket(b);
    System.out.println(v.data.getValue());
    //p.addObject(123);
    //var f = new iField("test");
    //System.out.println(p.data.getValue());
    //System.out.println(p.calculateChecksum());
    //System.out.println(p1.calculateChecksum());

    var server = new SecurityManager();
    var client = new SecurityManager();

    //var aes = server.getAESKey();

    var publicKeyServer = server.getPublicKey();
    var publicKeyClient = client.getPublicKey();
    var privateKeyServer = server.getPrivateKey();
    var privateKeyClient = client.getPrivateKey();

    //String message1 = "test message";
    //String message2 = "tst msg";
    IvParameterSpec ivParameterSpec = generateIv();
    String algorithm = "AES/CBC/PKCS5Padding";
    //String cipherText = SecurityManager.encrypt(algorithm, new byte[] {0x00, 0x01}, aes, ivParameterSpec);
    //String plainText = SecurityManager.decrypt(algorithm, cipherText, aes, ivParameterSpec);

    //System.out.println(cipherText);
    //System.out.println(plainText.getBytes()[1]);

    //var key = Base64.getEncoder().encodeToString(aes.getEncoded());
    //System.out.println("key: " + key); //String encodedKey



    //byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
    //SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");

    //String cipherText1 = encrypt(key, publicKey1);
    //String result1 = decrypt(cipherText1, secretKey2);
    //System.out.println("result: " + result1);
    //System.out.println("encoded: " + cipherText1);
    //System.out.println(result1.equals(key));

    //String signature = sign("foobar", user.getPrivateKey());

    //boolean isCorrect = verify("foobar", signature, user.getPublicKey());
    //System.out.println("Signature correct: " + isCorrect);

    System.out.println("___________________________________________");



    //byte[] cipherText = sec.encrypt(ta, client.getPublicKey());
    //byte[] result = sec.decrypt(cipherText, client.getPrivateKey());
    //System.out.println(result);
    SecurityManager sec = new SecurityManager();
    var devicePublicKey = client.getPublicKey();
    //сервер получает публичный ключ устройства
    //затем сервер шифрует ключ AES публичным ключом устройства
    var key = server.getAESKey();
    var encrkey = sec.encrypt(key.getEncoded(), client.getPublicKey());
    //сервер отправляет зашифрованный ключ клиенту
    //расшифровали закрытым ключом
    var result = sec.decrypt(encrkey, client.getPrivateKey());
    //клиент восстанавливает ключ
    SecretKey originalKey = new SecretKeySpec(result, sec.algorithm);

    System.out.println(key.getEncoded()[0]);
    System.out.println(originalKey.getEncoded()[0]);


    //key - ключ сервера result - ключ клиента
    //iPacket pac = new iPacket(Constants.TYPE_CONNECT, "data");
    //byte[] bts = sec.encryptPacket(pac);
    //sec.setAESKey(result);
    //iPacket pacc = sec.decryptPacket(bts);
    //System.out.println(pacc.data.getValue());



    //iPacket pac = new iPacket(Constants.TYPE_CONNECT, "data");
    //
    //byte[] ta = sec.encryptPacket(pac);
    //var pacc = sec.decryptPacket(ta);
    //System.out.println(pacc.data.getValue());
    //System.out.println(ta.length);
    //System.out.println("result: " + result1);
    //System.out.println("encoded: " + cipherText1);
    //System.out.println(result1.equals(key));



  }
}
