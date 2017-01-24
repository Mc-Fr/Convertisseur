package net.mcfr.util;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Logger {
  private static final Map<String, Logger> LOGGERS = new HashMap<>();
  private static final PrintStream OUT = System.out;
  private static final PrintStream ERR = System.err;

  /**
   * When called, prints to standard/error outputs (through println, print and format methods) will
   * be caught and information about calling classes and methods will be added.
   * 
   * @see Logger#stopCatchingOutputs()
   */
  public static void beginCatchingOutputs() {
    System.setOut(new OutStream(System.out));
    System.setErr(new OutStream(System.err));
  }

  /**
   * When called, prints to the standard/error outputs will no longer be caught.<br/>
   * <i>Note: if the {@link #beginCatchingOutputs()} has not been called beforehand, nothing will
   * happen, unless outputs have been modified by another class.</i>
   * 
   * @see Logger#beginCatchingOutputs()
   */
  public static void stopCatchingOutputs() {
    if (System.out != OUT)
      System.setOut(OUT);
    if (System.err != ERR)
      System.setOut(ERR);
  }

  /**
   * @return the logger for the calling class
   */
  public static Logger getLogger() {
    String name = Thread.currentThread().getStackTrace()[2].getClassName();

    if (LOGGERS.get(name) == null)
      LOGGERS.put(name, new Logger(name, OUT));

    return LOGGERS.get(name);
  }

  private final String className;
  private PrintStream stream;

  private Logger(String className, PrintStream stream) {
    this.className = className;
    setStream(stream);
  }

  public String getOwnerClassName() {
    return this.className;
  }

  public PrintStream getStream() {
    return this.stream;
  }

  public void setStream(PrintStream stream) {
    this.stream = stream;
  }

  public void log(String msg, Level level) {
    this.stream.format("[%s][%s] %s\n", level.getName(), this.className.substring(this.className.lastIndexOf('.') + 1), msg);
  }

  public void info(String msg) {
    log(msg, Level.INFO);
  }

  public void info(boolean b) {
    info("" + b);
  }

  public void info(char c) {
    info("" + c);
  }

  public void info(byte b) {
    info("" + b);
  }

  public void info(short s) {
    info("" + s);
  }

  public void info(int i) {
    info("" + i);
  }

  public void info(long l) {
    info("" + l);
  }

  public void info(float f) {
    info("" + f);
  }

  public void info(double d) {
    info("" + d);
  }

  public void info(Object o) {
    info("" + o);
  }

  public void warning(String msg) {
    log(msg, Level.WARNING);
  }

  public void warning(boolean b) {
    warning("" + b);
  }

  public void warning(char c) {
    warning("" + c);
  }

  public void warning(byte b) {
    warning("" + b);
  }

  public void warning(short s) {
    warning("" + s);
  }

  public void warning(int i) {
    warning("" + i);
  }

  public void warning(long l) {
    warning("" + l);
  }

  public void warning(float f) {
    warning("" + f);
  }

  public void warning(double d) {
    warning("" + d);
  }

  public void warning(Object o) {
    warning("" + o);
  }

  public void severe(String msg) {
    log(msg, Level.SEVERE);
  }

  public void severe(boolean b) {
    severe("" + b);
  }

  public void severe(char c) {
    severe("" + c);
  }

  public void severe(byte b) {
    severe("" + b);
  }

  public void severe(short s) {
    severe("" + s);
  }

  public void severe(int i) {
    severe("" + i);
  }

  public void severe(long l) {
    severe("" + l);
  }

  public void severe(float f) {
    severe("" + f);
  }

  public void severe(double d) {
    severe("" + d);
  }

  public void severe(Object o) {
    severe("" + o);
  }

  public static enum Level {
    INFO("INFO"),
    WARNING("WARN"),
    SEVERE("ERROR");

    private String name;

    private Level(String name) {
      this.name = name;
    }

    public String getName() {
      return this.name;
    }
  }

  private static class OutStream extends PrintStream {
    OutStream(OutputStream out) {
      super(out);
    }

    @Override
    public void println(boolean b) {
      addCallerName();
      super.println(b);
    }

    @Override
    public void println(char c) {
      addCallerName();
      super.println(c);
    }

    @Override
    public void println(char[] c) {
      addCallerName();
      super.println(c);
    }

    @Override
    public void println(double d) {
      addCallerName();
      super.println(d);
    }

    @Override
    public void println(float f) {
      addCallerName();
      super.println(f);
    }

    @Override
    public void println(int i) {
      addCallerName();
      super.println(i);
    }

    @Override
    public void println(long l) {
      addCallerName();
      super.println(l);
    }

    @Override
    public void println(String s) {
      addCallerName();
      super.println(s);
    }

    @Override
    public void println(Object o) {
      addCallerName();
      super.println(o);
    }

    @Override
    public void println() {
      addCallerName();
      super.println();
    }

    @Override
    public PrintStream format(Locale l, String format, Object... args) {
      addCallerName();
      return super.format(l, format, args);
    }

    @Override
    public PrintStream format(String format, Object... args) {
      addCallerName();
      return super.format(format, args);
    }

    private void addCallerName() {
      StackTraceElement e = Thread.currentThread().getStackTrace()[3];
      String className = e.getClassName();
      int line = e.getLineNumber();

      className = className.substring(className.lastIndexOf('.') + 1);

      super.append(String.format("[%s/%s(%s):%s] ", className, e.getMethodName(), line >= 0 ? "" + line : "?", this.out == OUT ? "STDOUT" : "STDERR"));
    }
  }
}
