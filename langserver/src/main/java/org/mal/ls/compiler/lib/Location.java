package org.mal.ls.compiler.lib;

public class Location implements Comparable<Location> {
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

  public Location getLocation() {
    return new Location(this.filename, this.start, this.end);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof Location)) {
      return false;
    }
    var other = (Location) obj;
    return this.start.line == other.start.line && this.start.col == other.start.col && this.end.line == other.end.line
        && this.end.col == other.end.col;
  }

  @Override
  public int compareTo(Location o) {
    int cmp = Integer.compare(this.start.line, o.start.line);
    if (cmp != 0) {
      return cmp;
    }
    return Integer.compare(this.start.col, o.start.col);
  }

  @Override
  public String toString() {
    return locationString();
  }
}
