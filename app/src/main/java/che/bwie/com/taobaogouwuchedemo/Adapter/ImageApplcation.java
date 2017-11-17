package che.bwie.com.taobaogouwuchedemo.Adapter;

import android.app.Application;

import che.bwie.com.taobaogouwuchedemo.utils.ImageLoaderUtil;

/**
 * 此类的作用：
 *
 * @author: forever
 * @date: 2017/9/15 9:49
 */
public class ImageApplcation extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        ImageLoaderUtil.initConfig(this);
    }
}
