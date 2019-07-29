package com.example.http;


import com.waw.hr.mutils.base.BaseBean;
import com.waw.hr.mutils.base.BaseBeanEntity;
import com.waw.hr.mutils.bean.BalanceLogListBean;
import com.waw.hr.mutils.bean.BankInfoBean;
import com.waw.hr.mutils.bean.BarCodeInfoBean;
import com.waw.hr.mutils.bean.ChangeBrokerBean;
import com.waw.hr.mutils.bean.ConfigBean;
import com.waw.hr.mutils.bean.EmployeeListBean;
import com.waw.hr.mutils.bean.EnterpriseDetailBean;
import com.waw.hr.mutils.bean.EnterpriseListBean;
import com.waw.hr.mutils.bean.FollowBean;
import com.waw.hr.mutils.bean.LoginBean;
import com.waw.hr.mutils.bean.MyBrokerBean;
import com.waw.hr.mutils.bean.PreSearchBean;
import com.waw.hr.mutils.bean.RecommendListBean;
import com.waw.hr.mutils.bean.ShopListBean;
import com.waw.hr.mutils.bean.UploadTokenBean;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by jingbin on 16/11/21.
 * 网络请求类（一个接口一个方法）
 */
public interface HttpClient {

    class Builder {

        public static HttpClient getWawsServer() {
            return HttpUtils.getInstance().getGuodongServer(HttpClient.class);
        }

        public static HttpClient getOtherServer() {
            return HttpUtils.getInstance().getGuodongServer(HttpClient.class);
        }
    }


    @FormUrlEncoded
    @POST("enterprise/enterpriseList")
    Observable<BaseBean<EnterpriseListBean>> getEnterpriseList(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("enterprise/preSearch")
    Observable<BaseBean<PreSearchBean>> preSearch(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("enterprise/detail")
    Observable<BaseBean<EnterpriseDetailBean>> detail(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("employee/login")
    Observable<BaseBean<LoginBean>> login(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("employee/info")
    Observable<BaseBean<BaseBeanEntity>> info(@FieldMap Map<String, Object> params);


    @FormUrlEncoded
    @POST("employee/editInfo")
    Observable<BaseBean<BaseBeanEntity>> editInfo(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("employee/myBroker")
    Observable<BaseBean<MyBrokerBean>> myBroker(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("upload/getUploadToken")
    Observable<BaseBean<UploadTokenBean>> getUploadToken(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("enterprise/follow")
    Observable<BaseBean<FollowBean>> follow(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("enterprise/getMyEnterpriseList")
    Observable<BaseBean<EnterpriseListBean>> getMyEnterpriseList(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("enterprise/join")
    Observable<BaseBean<FollowBean>> join(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("employee/auth")
    Observable<BaseBean<BaseBeanEntity>> auth(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("common/feedback")
    Observable<BaseBean<BaseBeanEntity>> feedback(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("common/cooperate")
    Observable<BaseBean<BaseBeanEntity>> cooperate(@FieldMap Map<String, Object> params);


    @FormUrlEncoded
    @POST("employee/myRecommend")
    Observable<BaseBean<RecommendListBean>> myRecommend(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("employee/recommendUser")
    Observable<BaseBean<BaseBeanEntity>> recommendUser(@FieldMap Map<String, Object> params);


    @FormUrlEncoded
    @POST("config/appConfig")
    Observable<BaseBean<ConfigBean>> appConfig(@FieldMap Map<String, Object> params);


    @FormUrlEncoded
    @POST("employee/bankInfo")
    Observable<BaseBean<BaseBeanEntity>> bankInfo(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("employee/myBankInfo")
    Observable<BaseBean<BankInfoBean>> myBankInfo(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("employee/addEmployee")
    Observable<BaseBean<BaseBeanEntity>> addEmployee(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("employee/employeeList")
    Observable<BaseBean<EmployeeListBean>> employeeList(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("shop/shopList")
    Observable<BaseBean<ShopListBean>> shopList(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("employee/barCode")
    Observable<BaseBean<BarCodeInfoBean>> barCode(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("employee/balanceLog")
    Observable<BaseBean<BalanceLogListBean>> balanceLog(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("employee/changeBroker")
    Observable<BaseBean<ChangeBrokerBean>> changeBroker(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("employee/cash")
    Observable<BaseBean<BaseBeanEntity>> cash(@FieldMap Map<String, Object> params);

}