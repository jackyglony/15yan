package org.liuyichen.fifteenyan;

import android.app.Application;

import ollie.Ollie;
/**
 * By liuyichen on 15-3-3 下午4:39.
 */
public class FifteenApp extends Application {

    private static FifteenApp self;

    @Override
    public void onCreate() {
        self = this;
        super.onCreate();

        Ollie
            .with(this)
            .setName(FifteenConstant.DB_NAME)
            .setVersion(FifteenConstant.DB_VERSION)
            .setLogLevel(Ollie.LogLevel.FULL)
            .setCacheSize(10 * 1024 * 1024)
            .init();
    }

    public static FifteenApp getSelf() {
        return self;
    }
}
