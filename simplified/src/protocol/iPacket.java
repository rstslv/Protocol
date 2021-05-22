package protocol;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.zip.CRC32;

public class iPacket {
  public byte[] prefix = Constants.PREFIX; // 2
  public int length; // 4
  public int sequenceNumber; // 4
  public byte type; // 1
  public byte[] checksum; // 8
  iField data; // 0-235 всего 16-251

  public iPacket(byte type, Object obj) throws IOException {
    this.data = new iField(obj);
    this.length = Constants.MIN_LENGTH + this.data.getLength();
    this.type = type;
  }

  public iPacket(byte type, Object[] objs) throws IOException {

    this.data = new iField(objs);
    this.length = Constants.MIN_LENGTH + this.data.getLength();
    this.type = type;
  }

  public iPacket(byte type, ArrayList<Object> objs) throws IOException {
    this.data = new iField(objs);
    this.length = Constants.MIN_LENGTH + this.data.getLength();
    this.type = type;
  }

  public iPacket(int length, int sequenceNumber, byte type, byte[] checksum, iField field)
      throws Exception {
    this.length = length;
    this.sequenceNumber = sequenceNumber;
    this.type = type;
    this.data = field;
    this.length = Constants.MIN_LENGTH + this.data.getLength();
    if (checksumVerified(checksum)) {
      this.checksum = checksum;
    } else {
      throw new Exception("Checksum verification error");
    }
  }

  public static long byteArrayToLong(byte[] bytes) {
    var buffer = ByteBuffer.allocate(Long.BYTES);
    buffer.put(bytes);
    buffer.flip();
    return buffer.getLong();
  }

  public static final byte[] intToByteArray(int value) {
    return new byte[] {
      (byte) (value >>> 24), (byte) (value >>> 16), (byte) (value >>> 8), (byte) value
    };
  }

  public static byte[] readArrayPart(byte[] arr, int from, int to) {
    return Arrays.copyOfRange(arr, from, to);
  }

  public static iPacket toPacket(byte[] bytes) throws Exception {
    if (bytes.length > Constants.MAX_LENGTH || bytes.length < Constants.MIN_LENGTH) {
      return null;
    }
    if (bytes[0] != Constants.PREFIX[0] || bytes[1] != Constants.PREFIX[1]) {
      return null;
    }
    var lengthBuffer =
        ByteBuffer.wrap(
            readArrayPart(bytes, Constants.LENGTH_OFFSET, Constants.SEQUENCE_NUMBER_OFFSET));
    var length = lengthBuffer.getInt();
    var sequenceNumberBuffer =
        ByteBuffer.wrap(
            readArrayPart(bytes, Constants.SEQUENCE_NUMBER_OFFSET, Constants.TYPE_OFFSET));
    var sequenceNumber = sequenceNumberBuffer.getInt();
    var type = bytes[Constants.TYPE_OFFSET];
    var checksum = readArrayPart(bytes, Constants.CHECKSUM_OFFSET, Constants.FIELDS_OFFSET);
    var data = new iField(readArrayPart(bytes, Constants.FIELDS_OFFSET, bytes.length));
    return new iPacket(length, sequenceNumber, type, checksum, data);
  }

  public boolean checksumVerified(byte[] toCheck) {
    return byteArrayToLong(calculateChecksum()) == byteArrayToLong(toCheck);
  }

  public void addData(Object obj) throws IOException, ClassNotFoundException {
    this.data.addData(obj);
    this.length = Constants.MIN_LENGTH + this.data.getLength();
  }

  public void addData(Object[] objs) throws IOException, ClassNotFoundException {
    this.data.addData(objs);
    this.length = Constants.MIN_LENGTH + this.data.getLength();
  }

  public void addData(ArrayList<Object> objs) throws IOException, ClassNotFoundException {
    this.data.addData(objs);
    this.length = Constants.MIN_LENGTH + this.data.getLength();
  }

  public byte[] calculateChecksum() {
    var stream = new ByteArrayOutputStream();
    stream.write(this.sequenceNumber);
    stream.write(this.type);
    stream.writeBytes(this.data.content);
    var bytes = stream.toByteArray();
    var crc32 = new CRC32();
    crc32.update(bytes);
    ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
    buffer.putLong(crc32.getValue());
    return buffer.array();
  }

  public byte[] toByteArray() {
    var stream = new ByteArrayOutputStream();
    stream.writeBytes(Constants.PREFIX);
    stream.writeBytes(intToByteArray(this.length));
    stream.writeBytes(intToByteArray(this.sequenceNumber));
    stream.write(this.type);
    stream.writeBytes(calculateChecksum());
    stream.writeBytes(this.data.content);
    return stream.toByteArray();
  }
}
