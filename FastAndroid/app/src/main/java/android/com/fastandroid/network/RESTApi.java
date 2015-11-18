package android.com.fastandroid.network;

import android.com.fastandroid.entity.PersonBean;
import android.database.Observable;

import retrofit.http.Path;

/**
 * 项目名称：FastAndroid
 * 包名：android.com.fastandroid.network
 * 当前类作用：用一句话描述当前类的功能
 * 作者：longlyboyhe on 2015/11/10 16:03
 * 邮箱：longlyboyhe@126.com
 */
public interface RESTApi {
    Observable<PersonBean> listGankDay(@Path("year") int year, @Path("month") int month, @Path("day") int day);

}
