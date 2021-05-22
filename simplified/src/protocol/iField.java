package protocol;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class iField {
  public byte[] content;

  public iField(Object[] objs) throws IOException {
    var values = new ArrayList<>();
    Collections.addAll(values, objs);
    this.content = Serializer.serialize(values);
  }

  public iField(ArrayList<Object> objs) throws IOException {
    this.content = Serializer.serialize(objs);
  }

  public iField(Object obj) throws IOException {
    var values = new ArrayList<>();
    values.add(obj);
    this.content = Serializer.serialize(values);
  }

  public iField(byte[] bytes) throws IOException, ClassNotFoundException {
    var obj = Serializer.deserialize(bytes);
    this.content = Serializer.serialize(obj);
  }

  public void addData(Object obj) throws IOException, ClassNotFoundException {
    var fieldObjects = getValue();
    fieldObjects.add(obj);
    this.content = Serializer.serialize(fieldObjects);
  }

  public void addData(Object[] objs) throws IOException, ClassNotFoundException {
    var fieldObjects = getValue();
    fieldObjects.add(objs);
    this.content = Serializer.serialize(fieldObjects);
  }

  public void addData(ArrayList<Object> objs) throws IOException, ClassNotFoundException {
    var fieldObjects = getValue();
    fieldObjects.add(objs);
    this.content = Serializer.serialize(fieldObjects);
  }

  public ArrayList<Object> getValue() throws IOException, ClassNotFoundException {
    return (ArrayList<Object>) Serializer.deserialize(this.content);
  }

  public int getLength() {
    return this.content.length;
  }
}
