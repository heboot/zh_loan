package com.example.http;


import com.waw.hr.mutils.base.BaseBean;
import com.waw.hr.mutils.base.BaseBeanEntity;
import com.waw.hr.mutils.bean.BillListBean;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;

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
    @POST("api/register")
    Observable<BaseBean<String>> register(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("api/login")
    Observable<BaseBean<String>> login(@FieldMap Map<String, Object> params);


    @FormUrlEncoded
    @POST("api/pwdfind")
    Observable<BaseBean<Object>> pwdfind(@FieldMap Map<String, Object> params);


    @FormUrlEncoded
    @POST("api/code")
    Observable<BaseBean<String>> getcode(@FieldMap Map<String, Object> params);


    @FormUrlEncoded
    @POST("api/applyLimit")
    Observable<BaseBean<BaseBeanEntity>> applyLimit(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @GET("api/refundRecord")
    Observable<BaseBean<BillListBean>> refundRecord(@FieldMap Map<String, Object> params);

    @Multipart
    @POST("api/upload")
    Observable<BaseBean<String>> upload(@PartMap Map<String, RequestBody> params);

}