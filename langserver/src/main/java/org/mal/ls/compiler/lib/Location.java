package org.mal.ls.compiler.lib;

public class Location {
  public final String filename;
  public final Position start;
  public final Position end;

  public Location(String filename, Position start, Position end) {
    this.filename = filename;
    this.start = start;
    this.end = end;
  }

  public Location(Location location) {
    this.start = location.start;
    this.end = location.end;
    this.filename = location.filename;
  }

  public String locationString() {
    return String.format("<%s:%s,%s>", filename, start, end);
  }

  @Override
  public String toString() {
    return locationString();
  }
}
