package com.year17.analyse_svg.SVGUtil;

import android.graphics.Picture;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;

/**
 * 作者：张玉辉
 * 时间：2017/9/12.
 * Describes a vector Picture object, and optionally its bounds.
 */

public class SVG {
    /**
     * The parsed Picture object.
     */
    private final Picture picture;

    /**
     * These are the bounds for the SVG specified as a hidden "bounds" layer in the SVG.
     */
    private final RectF bounds;

    /**
     * These are the estimated bounds of the SVG computed from the SVG elements while parsing. Note that this could be
     * null if there was a failure to compute limits (ie. an empty SVG).
     */
    private RectF limits = null;

    private PictureDrawable drawable = null;

    /**
     * Construct a new SVG.
     *
     * @param picture the parsed picture object.
     * @param bounds the bounds computed from the "bounds" layer in the SVG.
     */
    SVG(Picture picture, RectF bounds) {
        this.picture = picture;
        this.bounds = bounds;
    }

    /**
     * Set the limits of the SVG, which are the estimated bounds computed by the parser.
     *
     * @param limits the bounds computed while parsing the SVG, may not be entirely accurate.
     */
    void setLimits(RectF limits) {
        this.limits = limits;
    }

    /**
     * Get a {@link Drawable} of the SVG.
     *
     * @return the PictureDrawable.
     */
    public PictureDrawable getDrawable() {
        if (drawable == null) {
            drawable = new PictureDrawable(picture);
        }
        return drawable;
        // return new PictureDrawable(picture) {
        // @Override
        // public int getIntrinsicWidth() {
        // if (bounds != null) {
        // return (int) bounds.width();
        // } else if (limits != null) {
        // return (int) limits.width();
        // } else {
        // return -1;
        // }
        // }
        //
        // @Override
        // public int getIntrinsicHeight() {
        // if (bounds != null) {
        // return (int) bounds.height();
        // } else if (limits != null) {
        // return (int) limits.height();
        // } else {
        // return -1;
        // }
        // }
        // };
    }

    /**
     * Get the parsed SVG picture data.
     *
     * @return the picture.
     */
    public Picture getPicture() {
        return picture;
    }

    /**
     * Gets the bounding rectangle for the SVG, if one was specified.
     *
     * @return rectangle representing the bounds.
     */
    public RectF getBounds() {
        return bounds;
    }

    /**
     * Gets the bounding rectangle for the SVG that was computed upon parsing. It may not be entirely accurate for
     * certain curves or transformations, but is often better than nothing.
     *
     * @return rectangle representing the computed bounds.
     */
    public RectF getLimits() {
        return limits;
    }
}
