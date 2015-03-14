package org.liuyichen.fifteenyan.provider;

import org.liuyichen.fifteenyan.FifteenConstant;

import ollie.OllieProvider;

/**
 * By liuyichen on 15-3-4 上午9:30.
 */
public class FifteenYanProvider extends OllieProvider {


    @Override
    protected String getDatabaseName() {
        return FifteenConstant.DB_NAME;
    }

    @Override
    protected int getDatabaseVersion() {
        return FifteenConstant.DB_VERSION;
    }
}
