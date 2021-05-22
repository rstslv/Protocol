package protocol;

public class Constants {
  public static final byte[] PREFIX = new byte[] {0x59, 0x32};

  public static final byte TYPE_OK = 0;
  public static final byte TYPE_DATA = 1;
  public static final byte TYPE_ERROR = 2;
  public static final byte TYPE_CONNECT = 3;
  public static final byte TYPE_EXCHANGE = 4;
  public static final byte TYPE_DISCONNECT = 5;

  public static final byte MIN_LENGTH = 19;
  public static final int MAX_LENGTH = 251;

  public static final byte PREFIX_OFFSET = 0;
  public static final byte LENGTH_OFFSET = 2;
  public static final byte SEQUENCE_NUMBER_OFFSET = 6;
  public static final byte TYPE_OFFSET = 10;
  public static final byte CHECKSUM_OFFSET = 11;
  public static final byte FIELDS_OFFSET = 19;


  private Constants() {}
}
