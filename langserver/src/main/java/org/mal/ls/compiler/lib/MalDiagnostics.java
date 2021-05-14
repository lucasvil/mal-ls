package org.mal.ls.compiler.lib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.lsp4j.Diagnostic;
import org.eclipse.lsp4j.DiagnosticSeverity;
import org.eclipse.lsp4j.Location;
import org.eclipse.lsp4j.Range;
import org.eclipse.lsp4j.Position;

public class MalDiagnostics {
  private Set<DiagnosticMessage> messages = new TreeSet<>();

  public void error(MalLocation location, String message) {
    addDiagnostic(location, message, DiagnosticSeverity.Error);
  }

  public void warn(MalLocation location, String message) {
    addDiagnostic(location, message, DiagnosticSeverity.Warning);
  }

  public void info(MalLocation location, String message) {
    addDiagnostic(location, message, DiagnosticSeverity.Information);
  }

  public void hint(MalLocation location, String message) {
    addDiagnostic(location, message, DiagnosticSeverity.Hint);
  }

  private void addDiagnostic(MalLocation location, String message, DiagnosticSeverity severity) {
    messages.add(new DiagnosticMessage(location, message, severity));
  }

  public List<Diagnostic> getDiagnostics() {
    List<Diagnostic> dList = new ArrayList<>();
    for (DiagnosticMessage msg : messages) {
      dList.add(new Diagnostic(msg.getRange(), msg.message));
    }
    return dList;
  }

  private class DiagnosticMessage extends MalLocation implements Comparable<DiagnosticMessage> {
    public final String message;
    public final DiagnosticSeverity severity;

    public DiagnosticMessage(MalLocation location, String message, DiagnosticSeverity severity) {
      super(location);
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
          && this.getRange().equals(other.getRange());
    }

    @Override
    public int compareTo(DiagnosticMessage other) {
      int cmp = Integer.compare(this.getRange().getStart().getLine(), other.getRange().getStart().getLine());
      if (cmp != 0) {
        return cmp;
      }
      cmp = Integer.compare(this.getRange().getStart().getCharacter(), other.getRange().getStart().getCharacter());
      if (cmp != 0) {
        return cmp;
      }
      cmp = this.severity.compareTo(other.severity);
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
