package android.com.fastandroid.network;

import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.TimeUnit;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * 项目名称：FastAndroid
 * 包名：android.com.fastandroid.network
 * 当前类作用：用一句话描述当前类的功能
 * 作者：longlyboyhe on 2015/11/10 16:02
 * 邮箱：longlyboyhe@126.com
 */
public class RetrofitHelp {
    public static final String BASE_URL = "http://api.myservice.com";
    private static RESTApi api;
    public static RESTApi getApi() {
        if (api == null) {
            OkHttpClient client = new OkHttpClient();
            client.setReadTimeout(12, TimeUnit.SECONDS);
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            api = retrofit.create(RESTApi.class);
        }

        return api;
    }
}
