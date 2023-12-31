package com.android.speaker.util;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.speaker.MainApplication;
import com.android.speaker.base.CornerTransform;
import com.android.speaker.base.ImageRoundTrasform;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

public class GlideUtil {

    public static void loadCornerImage(ImageView imageView, String filePath, RequestListener listener, int radius) {
        CornerTransform transform = null;
        if (radius > 0) {
            transform = new CornerTransform(MainApplication.getInstance(), ScreenUtil.dip2px(radius));
        }

        RequestOptions options = new RequestOptions()
                .centerCrop();
        if (transform != null) {
            options = options.transform(transform);
        }
        Glide.with(MainApplication.getInstance())
                .load(filePath)
                .apply(options)
                .listener(listener)
                .into(imageView);
    }

    public static void loadCornerImage(ImageView imageView, String filePath, RequestListener listener, int radiusDp, int targetWidth, int targetHeight) {
        CornerTransform transform = null;
        if (radiusDp > 0) {
            transform = new CornerTransform(MainApplication.getInstance(), ScreenUtil.dip2px(radiusDp));
        }

        RequestOptions options = new RequestOptions()
                .centerCrop();
        if (transform != null) {
            options = options.transform(transform);
        }
        options.override(targetWidth, targetHeight);
        Glide.with(MainApplication.getInstance())
                .load(filePath)
                .apply(options)
                .listener(listener)
                .into(imageView);
    }

    public static void loadCornerImage(ImageView imageView, String filePath, RequestListener listener, int radius, boolean topLeftExcept, boolean topRightExcept, boolean bottomLeftExcept, boolean bottomRightExcept) {
        CornerTransform tran = new CornerTransform(MainApplication.getInstance(), ScreenUtil.dip2px(radius));
        tran.setExceptCorner(topLeftExcept, topRightExcept, bottomLeftExcept, bottomRightExcept);
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .transform(tran);
        Glide.with(MainApplication.getInstance())
                .asBitmap()
                .skipMemoryCache(true)
                .load(filePath)
                .apply(options)
                .listener(listener)
                .into(imageView);
    }

    public static void loadImage(ImageView imageView, String filePath, RequestListener listener) {
        Glide.with(MainApplication.getInstance())
                .load(filePath)
                .listener(listener)
                .into(imageView);
    }

    public static void loadCircleImage(ImageView imageView, String filePath, RequestListener listener) {
        RequestOptions options = new RequestOptions()
                .transform(new CircleCrop());
        Glide.with(MainApplication.getInstance())
                .load(filePath)
                .apply(options)
                .listener(listener)
                .into(imageView);
    }

    public static void loadImageWithPlaceHolder(ImageView imageView, String filePath, int defaultRes) {
        Glide.with(MainApplication.getInstance())
                .load(filePath)
                .apply(new RequestOptions().error(defaultRes))
                .into(imageView);
    }

    public static void clear(ImageView imageView) {
        Glide.with(MainApplication.getInstance()).clear(imageView);
    }

    public static void loadUserIcon(ImageView imageView, Object uri, int defaultResId, int radius) {
        Glide.with(MainApplication.getInstance())
                .load(uri)
                .placeholder(defaultResId)
                .apply(new RequestOptions().centerCrop().error(defaultResId))
                .into(imageView);
    }

    public static void loadTargetView(View view, String url) {
        CustomViewTarget target = new CustomViewTarget<View, Drawable>(view) {
            @Override
            protected void onResourceCleared(@Nullable Drawable placeholder) {

            }

            @Override
            public void onLoadFailed(@Nullable Drawable errorDrawable) {

            }

            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                view.setBackground(resource);
            }
        };
        Glide.with(MainApplication.getInstance())
                .load(url)
                .into(target);
    }
}
