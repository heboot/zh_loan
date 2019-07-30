package com.example.http;


import com.waw.hr.mutils.base.BaseBean;
import com.waw.hr.mutils.base.BaseBeanEntity;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by jingbin on 16/11/21.
 * 网络请求类（一个接口一个方法）
 */
public interface HttpClient {

    class Builder {

        public static HttpClient getServer() {
            return HttpUtils.getInstance().getGuodongServer(HttpClient.class);
        }

        public static HttpClient getOtherServer() {
            return HttpUtils.getInstance().getGuodongServer(HttpClient.class);
        }
    }


    @FormUrlEncoded
    @POST("api/complete")
    Observable<BaseBean<BaseBeanEntity>> editInfo(@FieldMap Map<String, Object> params);


    @FormUrlEncoded
    @POST("api/applyLimit")
    Observable<BaseBean<BaseBeanEntity>> applyLimit(@FieldMap Map<String, Object> params);

}