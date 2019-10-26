package com.ivapps.aduc.db;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Api {

    @FormUrlEncoded
    @POST("register.php")
    Call<JsonObject> signUp(
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
    Call<JsonObject> signIn(
            @Field("user_email") String user_email,
            @Field("user_password") String user_password
    );

    @FormUrlEncoded
    @POST("postCount.php")
    Call<JsonObject> postCount(
            @Field("user_department") String user_department
    );

    @FormUrlEncoded
    @POST("getPosts.php")
    Call<JsonObject> getPosts(
            @Field("user_department") String user_department
    );

    @FormUrlEncoded
    @POST("getPostByID.php")
    Call<JsonObject> getPost(
            @Field("id") int id
    );

    @FormUrlEncoded
    @POST("getUser.php")
    Call<JsonObject> getUser(
            @Field("user_email") String user_email
    );

    @FormUrlEncoded
    @POST("setNotifs.php")
    Call<JsonObject> setNotification(
            @Field("user_email") String user_email,
            @Field("body") String body,
            @Field("type") String type,
            @Field("time") String time,
            @Field("link") String link,
            @Field("seen") boolean seen
    );

    @FormUrlEncoded
    @POST("updateNotif.php")
    Call<JsonObject> updateNotification(
            @Field("id") int id
    );

    @FormUrlEncoded
    @POST("getNotifs.php")
    Call<JsonObject> getNotification(
            @Field("user_email") String user_email
    );

    @FormUrlEncoded
    @POST("notifCount.php")
    Call<JsonObject> notificationCount(
            @Field("user_email") String user_email
    );

    @FormUrlEncoded
    @POST("notifCount2.php")
    Call<JsonObject> notificationCount2(
            @Field("user_email") String user_email
    );

}
