/**
 * Copyright (c) 2004-2011 QOS.ch
 * All rights reserved.
 *
 * Permission is hereby granted, free  of charge, to any person obtaining
 * a  copy  of this  software  and  associated  documentation files  (the
 * "Software"), to  deal in  the Software without  restriction, including
 * without limitation  the rights to  use, copy, modify,  merge, publish,
 * distribute,  sublicense, and/or sell  copies of  the Software,  and to
 * permit persons to whom the Software  is furnished to do so, subject to
 * the following conditions:
 *
 * The  above  copyright  notice  and  this permission  notice  shall  be
 * included in all copies or substantial portions of the Software.
 *
 * THE  SOFTWARE IS  PROVIDED  "AS  IS", WITHOUT  WARRANTY  OF ANY  KIND,
 * EXPRESS OR  IMPLIED, INCLUDING  BUT NOT LIMITED  TO THE  WARRANTIES OF
 * MERCHANTABILITY,    FITNESS    FOR    A   PARTICULAR    PURPOSE    AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE,  ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */
package org.slf4j.helpers;

import org.slf4j.spi.*;
import org.slf4j.*;

/**
 * A NOP logger with:
 *  - all levels enabled
 *  - logback's MDC implementation
 *
 * The intent is that we want loggers to be fully invoked and track real MDC,
 * but we still don't want any output.
 */
public class EnabledNOPLogger implements Logger, SLF4JServiceProvider, ILoggerFactory {
    /**
     * The unique instance of NOPLogger.
     */
    public static final EnabledNOPLogger INSTANCE = new EnabledNOPLogger();

    /**
     * There is no point in creating multiple instances of NOPLogger. 
     * 
     * The present constructor should be "private" but we are leaving it as "protected" for compatibility.
     */
    public EnabledNOPLogger() {
    }

    /**
     * Always returns the string value "NOP".
     */
    @Override
    public String getName() {
        return "EnabledNOP";
    }

    /**
     * Always returns false.
     * @return always false
     */
    @Override
    final public boolean isTraceEnabled() {
        return true;
    }

    /** A NOP implementation. */
    @Override
    final public void trace(String msg) {
        // NOP
    }

    /** A NOP implementation.  */
    @Override
    final public void trace(String format, Object arg) {
        // NOP
    }

    /** A NOP implementation.  */
    @Override
    public final void trace(String format, Object arg1, Object arg2) {
        // NOP
    }

    /** A NOP implementation.  */
    @Override
    public final void trace(String format, Object... argArray) {
        // NOP
    }

    /** A NOP implementation. */
    @Override
    final public void trace(String msg, Throwable t) {
        // NOP
    }

    /**
     * Always returns false.
     * @return always false
     */
    final public boolean isDebugEnabled() {
        return true;
    }

    /** A NOP implementation. */
    final public void debug(String msg) {
        // NOP
    }

    /** A NOP implementation.  */
    final public void debug(String format, Object arg) {
        // NOP
    }

    /** A NOP implementation.  */
    final public void debug(String format, Object arg1, Object arg2) {
        // NOP
    }

    /** A NOP implementation.  */
    final public void debug(String format, Object... argArray) {
        // NOP
    }

    /** A NOP implementation. */
    final public void debug(String msg, Throwable t) {
        // NOP
    }

    /**
     * Always returns false.
     * @return always false
     */
    final public boolean isInfoEnabled() {
        // NOP
        return true;
    }

    /** A NOP implementation. */
    final public void info(String msg) {
        // NOP
    }

    /** A NOP implementation. */
    final public void info(String format, Object arg1) {
        // NOP
    }

    /** A NOP implementation. */
    final public void info(String format, Object arg1, Object arg2) {
        // NOP
    }

    /** A NOP implementation.  */
    final public void info(String format, Object... argArray) {
        // NOP
    }

    /** A NOP implementation. */
    final public void info(String msg, Throwable t) {
        // NOP
    }

    /**
     * Always returns false.
     * @return always false
     */
    final public boolean isWarnEnabled() {
        return true;
    }

    /** A NOP implementation. */
    final public void warn(String msg) {
        // NOP
    }

    /** A NOP implementation. */
    final public void warn(String format, Object arg1) {
        // NOP
    }

    /** A NOP implementation. */
    final public void warn(String format, Object arg1, Object arg2) {
        // NOP
    }

    /** A NOP implementation.  */
    final public void warn(String format, Object... argArray) {
        // NOP
    }

    /** A NOP implementation. */
    final public void warn(String msg, Throwable t) {
        // NOP
    }

    /** A NOP implementation. */
    final public boolean isErrorEnabled() {
        return true;
    }

    /** A NOP implementation. */
    final public void error(String msg) {
        // NOP
    }

    /** A NOP implementation. */
    final public void error(String format, Object arg1) {
        // NOP
    }

    /** A NOP implementation. */
    final public void error(String format, Object arg1, Object arg2) {
        // NOP
    }

    /** A NOP implementation.  */
    final public void error(String format, Object... argArray) {
        // NOP
    }

    /** A NOP implementation. */
    final public void error(String msg, Throwable t) {
        // NOP
    }

    // ============================================================
    // Added NOP methods since MarkerIgnoringBase is now deprecated
    // ============================================================
    /**
     * Always returns false.
     * @return always false
     */
    final public boolean isTraceEnabled(Marker marker) {
        // NOP
        return true;
    }

    /** A NOP implementation. */
    @Override
    final public void trace(Marker marker, String msg) {
        // NOP
    }

    /** A NOP implementation. */
    @Override
    final public void trace(Marker marker, String format, Object arg) {
        // NOP
    }

    /** A NOP implementation. */
    @Override
    final public void trace(Marker marker, String format, Object arg1, Object arg2) {
        // NOP
    }

    /** A NOP implementation. */
    @Override
    final public void trace(Marker marker, String format, Object... argArray) {
        // NOP
    }

    /** A NOP implementation. */
    @Override
    final public void trace(Marker marker, String msg, Throwable t) {
        // NOP
    }

    /**
     * Always returns false.
     * @return always false
     */
    final public boolean isDebugEnabled(Marker marker) {
        return true;
    }

    /** A NOP implementation. */
    @Override
    final public void debug(Marker marker, String msg) {
        // NOP
    }

    /** A NOP implementation. */
    @Override
    final public void debug(Marker marker, String format, Object arg) {
        // NOP
    }

    /** A NOP implementation. */
    @Override
    final public void debug(Marker marker, String format, Object arg1, Object arg2) {
        // NOP
    }

    @Override
    final public void debug(Marker marker, String format, Object... arguments) {
        // NOP
    }

    @Override
    final public void debug(Marker marker, String msg, Throwable t) {
        // NOP
    }

    /**
     * Always returns false.
     * @return always false
     */
    @Override
    public boolean isInfoEnabled(Marker marker) {
        return true;
    }

    /** A NOP implementation. */
    @Override
    final public void info(Marker marker, String msg) {
        // NOP
    }

    /** A NOP implementation. */
    @Override
    final public void info(Marker marker, String format, Object arg) {
        // NOP
    }

    /** A NOP implementation. */
    @Override
    final public void info(Marker marker, String format, Object arg1, Object arg2) {
        // NOP
    }

    /** A NOP implementation. */
    @Override
    final public void info(Marker marker, String format, Object... arguments) {
        // NOP
    }

    /** A NOP implementation. */
    @Override
    final public void info(Marker marker, String msg, Throwable t) {
        // NOP
    }

    /**
     * Always returns false.
     * @return always false
     */
    @Override
    final public boolean isWarnEnabled(Marker marker) {
        return true;
    }

    /** A NOP implementation. */
    @Override
    final public void warn(Marker marker, String msg) {
        // NOP
    }

    /** A NOP implementation. */
    @Override
    final public void warn(Marker marker, String format, Object arg) {
        // NOP
    }

    /** A NOP implementation. */
    @Override
    final public void warn(Marker marker, String format, Object arg1, Object arg2) {
        // NOP
    }

    /** A NOP implementation. */
    @Override
    final public void warn(Marker marker, String format, Object... arguments) {
        // NOP
    }

    /** A NOP implementation. */
    @Override
    final public void warn(Marker marker, String msg, Throwable t) {
        // NOP
    }

    /**
     * Always returns false.
     * @return always false
     */
    @Override
    final public boolean isErrorEnabled(Marker marker) {
        return true;
    }

    /** A NOP implementation. */
    @Override
    final public void error(Marker marker, String msg) {
        // NOP
    }

    /** A NOP implementation. */
    @Override
    final public void error(Marker marker, String format, Object arg) {
        // NOP
    }

    /** A NOP implementation. */
    @Override
    final public void error(Marker marker, String format, Object arg1, Object arg2) {
        // NOP
    }

    /** A NOP implementation. */
    @Override
    final public void error(Marker marker, String format, Object... arguments) {
        // NOP
    }

    /** A NOP implementation. */
    @Override
    final public void error(Marker marker, String msg, Throwable t) {
        // NOP
    }
    // ===================================================================
    // End of added NOP methods since MarkerIgnoringBase is now deprecated
    // ===================================================================





    // --- service provider stuff
 
    /**
     * Return the instance of {@link ILoggerFactory} that 
     * {@link org.slf4j.LoggerFactory} class should bind to.
     * 
     * @return instance of {@link ILoggerFactory} 
     */
    public ILoggerFactory getLoggerFactory() {
        return INSTANCE;
    }

    /**
     * Return the instance of {@link IMarkerFactory} that 
     * {@link org.slf4j.MarkerFactory} class should bind to.
     * 
     * @return instance of {@link IMarkerFactory} 
     */
    public IMarkerFactory getMarkerFactory() {
        return null;
    }

    /**
     * Return the instance of {@link MDCAdapter} that
     * {@link MDC} should bind to.
     * 
     * @return instance of {@link MDCAdapter} 
     */
    public MDCAdapter getMDCAdapter() {
        return new ch.qos.logback.classic.util.LogbackMDCAdapter();
    }

    /**
     * Return the maximum API version for SLF4J that the logging
     * implementation supports.
     *
     * <p>For example: {@code "2.0.1"}.
     *
     * @return the string API version.
     */
    public String getRequestedApiVersion() {
        return "2.0.1";
    }

    /**
     * Initialize the logging back-end.
     * 
     * <p><b>WARNING:</b> This method is intended to be called once by 
     * {@link LoggerFactory} class and from nowhere else. 
     * 
     */
    public void initialize() {}

    public Logger getLogger(String name) {
        return INSTANCE;
    }
}
