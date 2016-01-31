package io.github.xhinliang.birthday.util;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by xhinliang on 16-1-30.
 * xhinliang@gmail.com
 */
public class ImageUtils {
    private static final int MAX_IMAGE_SIZE = 0x1000;

    public static Bitmap getCompressBitmap(Uri uri, ContentResolver resolver) throws FileNotFoundException {
        InputStream inputStream = resolver.openInputStream(uri);
        //注意此处的InputStream不需要手动close！！！
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        //第二个Rect的参数用来测量图片的边距，省略
        BitmapFactory.decodeStream(inputStream, null, options);
        options.inJustDecodeBounds = false;
        int scale = 1;
        //缩放比,用高或者宽其中较大的一个数据进行计算
        if (options.outWidth > options.outHeight && options.outWidth > MAX_IMAGE_SIZE)
            scale = options.outWidth / MAX_IMAGE_SIZE;
        if (options.outWidth < options.outHeight && options.outWidth > MAX_IMAGE_SIZE)
            scale = options.outHeight / MAX_IMAGE_SIZE;
        scale++;
        options.inSampleSize = scale;//设置采样率
        //注意这里的inputStream需要重新生成
        return BitmapFactory.decodeStream(resolver.openInputStream(uri), null, options);
    }
}
