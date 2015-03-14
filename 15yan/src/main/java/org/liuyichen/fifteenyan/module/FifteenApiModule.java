package org.liuyichen.fifteenyan.module;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.liuyichen.fifteenyan.api.FifteenYanService;
import org.liuyichen.fifteenyan.fragment.DetailFragment;
import org.liuyichen.fifteenyan.fragment.SettingsFragment;
import org.liuyichen.fifteenyan.fragment.StoryFragment;
import org.liuyichen.fifteenyan.fragment.ViewPagerTabFragment;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * By liuyichen on 15-3-3 下午4:46.
 */
@Module(injects = {
            ViewPagerTabFragment.class,
            StoryFragment.class,
            DetailFragment.class,
            SettingsFragment.class
        },
        complete = false)
public class FifteenApiModule {

    @Provides
    @Singleton
    FifteenYanService provideFifteenYanService() {

        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(FifteenYanService.BASE_URL)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setConverter(new GsonConverter(gson))
                .build();

        return restAdapter.create(FifteenYanService.class);
    }
}
