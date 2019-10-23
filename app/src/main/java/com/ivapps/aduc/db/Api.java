package com.ivapps.aduc.db;

import com.ivapps.aduc.utils.User;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Api {

    @FormUrlEncoded
    @POST("register.php")
    Call<ResponseBody> createUser(
            @Field("user_name") String user_name,
            @Field("user_pass") String user_pass
    );

    @FormUrlEncoded
    @POST("register.php")
    Call<ResponseBody> signUp(
            @Field("user_name") String user_name,
            @Field("user_email") String user_email,
            @Field("user_phone") int user_phone,
            @Field("user_college") String user_college,
            @Field("user_job") String user_job,
            @Field("user_password") String user_password,
            @Field("user_profile") String user_profile,
            @Field("user_gender") String user_gender,
            @Field("user_department") String user_department,
            @Field("user_born") String user_born,
            @Field("user_forgot") String user_forgot,
            @Field("user_bio") String user_bio,
            @Field("user_since") String user_since,
            @Field("user_access") String user_access
    );

    @FormUrlEncoded
    @POST("login.php")
    Call<ResponseBody> signIn(
            @Field("user_email") String user_email,
            @Field("user_password") String user_password
    );

    @FormUrlEncoded
    @POST("getUserByEmail.php")
    Call<ResponseBody> getUser(
            @Field("user_email") String user_email
    );

    @FormUrlEncoded
    @POST("postCount.php")
    Call<ResponseBody> getPostCount(
            @Field("user_department") String user_department
    );

}
