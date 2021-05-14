package org.mal.ls.compiler.lib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.lsp4j.Diagnostic;
import org.eclipse.lsp4j.DiagnosticSeverity;
import org.eclipse.lsp4j.Range;
import org.eclipse.lsp4j.Position;

public class MalDiagnostics {
  private Set<DiagnosticMessage> messages = new TreeSet<>();

  public void error(Location location, String message) {
    addDiagnostic(location, message, DiagnosticSeverity.Error);
  }

  public void warn(Location location, String message) {
    addDiagnostic(location, message, DiagnosticSeverity.Warning);
  }

  public void info(Location location, String message) {
    addDiagnostic(location, message, DiagnosticSeverity.Information);
  }

  public void hint(Location location, String message) {
    addDiagnostic(location, message, DiagnosticSeverity.Hint);
  }

  private void addDiagnostic(Location location, String message, DiagnosticSeverity severity) {
    messages.add(new DiagnosticMessage(location, message, severity));
  }

  public List<Diagnostic> getDiagnostics() {
    List<Diagnostic> dList = new ArrayList<>();
    for (DiagnosticMessage msg : messages) {
      dList.add(new Diagnostic(new Range(new Position(msg.location.start.line, msg.location.start.col),
          new Position(msg.location.end.line, msg.location.end.col)), msg.toString(), msg.severity, null));
    }
    return dList;
  }

  private class DiagnosticMessage implements Comparable<DiagnosticMessage> {
    public final Location location;
    public final String message;
    public final DiagnosticSeverity severity;

    public DiagnosticMessage(Location location, String message, DiagnosticSeverity severity) {
      this.location = location;
      this.message = message;
      this.severity = severity;
    }

    @Override
    public boolean equals(Object obj) {
      if (obj == null) {
        return false;
      }
      if (!(obj instanceof DiagnosticMessage)) {
        return false;
      }
      DiagnosticMessage other = (DiagnosticMessage) obj;
      return this.message.equals(other.message) && this.severity.equals(other.severity)
          && this.location.equals(other.location);
    }

    @Override
    public int compareTo(DiagnosticMessage other) {
      int cmp = this.severity.compareTo(other.severity);
      if (cmp != 0) {
        return cmp;
      }
      return this.message.compareTo(other.message);
    }

    @Override
    public String toString() {
      return message;
    }
  }
}
