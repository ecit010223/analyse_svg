package com.year17.analyse_svg.SVGUtil;

/**
 * 作者：张玉辉
 * 时间：2017/9/12.
 */

public class SVGParseException extends RuntimeException{
    public SVGParseException(String s) {
        super(s);
    }

    public SVGParseException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public SVGParseException(Throwable throwable) {
        super(throwable);
    }
}
