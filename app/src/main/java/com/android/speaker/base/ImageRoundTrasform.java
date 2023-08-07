package com.android.speaker.base;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import com.android.speaker.util.ScreenUtil;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.TransformationUtils;

import java.security.MessageDigest;

/**
 * 图片圆角
 */
public class ImageRoundTrasform extends BitmapTransformation {
    private int topLeftRadius;
    private int topRightRadius;
    private int bottomLeftRadius;
    private int bottomRightRadius;

    public ImageRoundTrasform(int radiusDp) {
        int radiusPx = ScreenUtil.dip2px(radiusDp);
        this.topLeftRadius = radiusPx;
        this.topRightRadius = radiusPx;
        this.bottomLeftRadius = radiusPx;
        this.bottomRightRadius = radiusPx;
    }

    public ImageRoundTrasform(int topLeftRadius, int topRightRadius, int bottomLeftRadius, int bottomRightRadius) {
        this.topLeftRadius = topLeftRadius;
        this.topRightRadius = topRightRadius;
        this.bottomLeftRadius = bottomLeftRadius;
        this.bottomRightRadius = bottomRightRadius;
    }

    @Override
    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        return TransformationUtils.roundedCorners(pool, toTransform, topLeftRadius, topRightRadius, bottomRightRadius, bottomLeftRadius);
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {

    }
}
