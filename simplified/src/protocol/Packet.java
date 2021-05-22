package protocol;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Adler32;
import java.util.zip.CRC32;

public class Packet {
  static int sequenceNumber;
  public byte type;
  public byte previousType;
  public List<Field> fields = new ArrayList<>();
  public byte[] checksum;

  public Packet(byte type, byte subtype, List<Field> fields) {
    this.type = type;
    this.previousType = subtype;
    this.fields = fields;
  }

  public Packet(byte type, byte subtype) {
    this.type = type;
    this.previousType = subtype;
  }

  public static Packet parse(byte[] bytes) {
    if (bytes.length < Constants.MIN_LENGTH) {
      return null;
    }
    if (bytes[0] != Constants.PREFIX[0]
        || bytes[1] != Constants.PREFIX[1]
        || bytes[2] != Constants.PREFIX[2]) {
      return null;
    }

    int lastIndex = bytes.length - 1;
    //if (bytes[lastIndex - 1] != Constants.postfix[0] || bytes[lastIndex] != Constants.postfix[1]) {
    //  return null;
    //}

    var type = bytes[3];
    var subtype = bytes[4];

    return new Packet(type, subtype);
  }

  public static long calculateChecksum(byte[] bytes)
  {
      var crc32 = new CRC32();
      crc32.update(bytes);
      return crc32.getValue();
  }

  public byte[] toByteArray() {
    var packet = new ByteArrayOutputStream();
    packet.writeBytes(Constants.PREFIX);

    packet.write(this.type);
    packet.write(this.previousType);

    for (var field : fields) {
      packet.write(Field.id);
      packet.write(field.size);
      packet.writeBytes(field.content);
    }

    //packet.writeBytes(Constants.postfix);
    return packet.toByteArray();
  }
}
