package com.example.fightagainstfake.Posts.Activities;

import com.example.fightagainstfake.notification.MyResponse;
import com.example.fightagainstfake.notification.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {

    @Headers(

            {
                 "Content-Type:application/json",
                    "Authorization:key=AAAA8Kezv9c:APA91bEGNwsO-eH8127BkINduiJUIarrRxlrTcvEyeF0vjXOd3hVibT9cSxtrh71M0J-TcUz3gmaDuqIbLlUS2onarxVpCBH4IRDMYAKMVwLwB1LZj3KqB05wf_bE9DN77Ux65H5LXfF"
            }

    )
    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);

}
