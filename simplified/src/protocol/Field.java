package protocol;

import java.nio.charset.StandardCharsets;

public class Field {
  public static byte id = 0;
  public byte size;
  public byte[] content;

  public Field(Object data) {
    id++;
    this.content = data.toString().getBytes(StandardCharsets.UTF_8);
    this.size = getSize();
  }

  private byte getSize() {
    byte size = 0;
    // id
    size += Byte.BYTES;
    // size
    size += Byte.BYTES;
    // content
    size += this.content.length;
    return size;
  }
}
