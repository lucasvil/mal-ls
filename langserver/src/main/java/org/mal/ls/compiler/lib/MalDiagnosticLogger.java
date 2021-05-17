package org.mal.ls.compiler.lib;

import java.util.Set;
import java.util.TreeSet;

import org.eclipse.lsp4j.Diagnostic;
import org.eclipse.lsp4j.DiagnosticSeverity;

public class MalDiagnosticLogger {
  public Set<MalDiagnostic> messages;
  private static MalDiagnosticLogger instance = null;

  public MalDiagnosticLogger() {
    this.messages = new TreeSet<>();
  }

  public static MalDiagnosticLogger getInstance() {
    if (instance == null)
      instance = new MalDiagnosticLogger();
    return instance;
  }

  public static void reset() {
    instance = null;
  }

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
    this.messages.add(new MalDiagnostic(location, message, severity));
  }

  private class MalDiagnostic extends Diagnostic implements Comparable<MalDiagnostic> {
    public final String uri;

    public MalDiagnostic(MalLocation location, String message, DiagnosticSeverity severity) {
      super(location.getRange(), message, severity, null);
      this.uri = location.getUri();
    }

    @Override
    public boolean equals(Object obj) {
      if (obj == null) {
        return false;
      }
      if (!(obj instanceof MalDiagnostic)) {
        return false;
      }
      MalDiagnostic other = (MalDiagnostic) obj;
      return this.uri.equals(other.uri) && this.getMessage().equals(other.getMessage())
          && this.getRange().equals(other.getRange());
    }

    @Override
    public int compareTo(MalDiagnostic other) {
      int cmp = Integer.compare(this.getRange().getStart().getLine(), other.getRange().getStart().getLine());
      if (cmp != 0) {
        return cmp;
      }
      cmp = Integer.compare(this.getRange().getStart().getCharacter(), other.getRange().getStart().getCharacter());
      if (cmp != 0) {
        return cmp;
      }
      cmp = this.getSeverity().compareTo(other.getSeverity());
      if (cmp != 0) {
        return cmp;
      }
      return this.getMessage().compareTo(other.getMessage());
    }
  }

}
