package example.jackliu.nonewfriends;

/**
 * Created by irenepatt on 3/7/18.
 */

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

import org.springframework.http.HttpEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

//server key: AAAAfdpPd-A:APA91bH8oJcnXZQ0aWqxc1ZM8gjmeRJWvhCdFn6DGik_Dp7EDz6KgN9aPEUaElBBw65t4uK7rzUAirRPtO51aDBxrH-blJiQPk4VitBXlx5m5y87sIHxbB6XsoDGY4kHdfw-N7l_n3SM

//@Service
public class AndroidPushNotificationsService {

    private static final String FIREBASE_SERVER_KEY = "AAAAfdpPd-A:APA91bH8oJcnXZQ0aWqxc1ZM8gjmeRJWvhCdFn6DGik_Dp7EDz6KgN9aPEUaElBBw65t4uK7rzUAirRPtO51aDBxrH-blJiQPk4VitBXlx5m5y87sIHxbB6XsoDGY4kHdfw-N7l_n3SM";
    private static final String FIREBASE_API_URL = "https://fcm.googleapis.com/fcm/send";

    //@Async
    public CompletableFuture<String> send(HttpEntity<String> entity) {

        RestTemplate restTemplate = new RestTemplate();

        /**
         https://fcm.googleapis.com/fcm/send
         Content-Type:application/json
         Authorization:key=FIREBASE_SERVER_KEY*/

        ArrayList<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        interceptors.add(new HeaderRequestInterceptor("Authorization", "key=" + FIREBASE_SERVER_KEY));
        interceptors.add(new HeaderRequestInterceptor("Content-Type", "application/json"));
        restTemplate.setInterceptors(interceptors);

        String firebaseResponse = restTemplate.postForObject(FIREBASE_API_URL, entity, String.class);

        return CompletableFuture.completedFuture(firebaseResponse);
    }
}