# Drone

## Step
- browser에서 직접 적속하면 http라서 연결이 대부분 막히므로 curl로 확인한다.
- 
```shell
sungyong@m1pro ~ % nc -nvv -w 1 -z 192.168.123.225 1-9000
sungyong@m1pro ~ % curl 192.168.123.225:8888
<html><body><h1>Hello server</h1>
<form action='?' method='get'>
<p>Your name: <input type='text' name='username'></p>
</form>
</body></html>

```

## Web to Drone

```shell
2022-05-16 21:35:42.525 24786-25650/com.dji.sdk.sample D/InputEventReceiver: channel '852b1f7 com.dji.sdk.sample/com.dji.sdk.sample.internal.controller.MainActivity (client)' ~ Disposing input event receiver.
2022-05-16 21:35:42.526 24786-25650/com.dji.sdk.sample D/InputEventReceiver: channel '852b1f7 com.dji.sdk.sample/com.dji.sdk.sample.internal.controller.MainActivity (client)' ~NativeInputEventReceiver.
2022-05-16 21:35:42.537 24786-24786/com.dji.sdk.sample D/ViewRootImpl@b495fbb[MainActivity]: MSG_WINDOW_FOCUS_CHANGED 1
2022-05-16 21:35:42.538 24786-25650/com.dji.sdk.sample E/ViewRootImpl: sendUserActionEvent() returned.
2022-05-16 21:35:44.614 24786-24793/com.dji.sdk.sample I/zygote64: Do partial code cache collection, code=928KB, data=548KB
2022-05-16 21:35:44.617 24786-24793/com.dji.sdk.sample I/zygote64: After code cache collection, code=928KB, data=548KB
2022-05-16 21:35:44.617 24786-24793/com.dji.sdk.sample I/zygote64: Increasing code cache capacity to 3MB
2022-05-16 21:36:01.092 24786-27013/com.dji.sdk.sample I/WebServer: passCommand /xp
2022-05-16 21:36:01.092 24786-27013/com.dji.sdk.sample I/VirtualStick: onWebControlEvent /xp
2022-05-16 21:36:09.447 24786-27181/com.dji.sdk.sample I/WebServer: passCommand /xp
2022-05-16 21:36:09.447 24786-27181/com.dji.sdk.sample I/VirtualStick: onWebControlEvent /xp
2022-05-16 21:36:14.269 24786-24799/com.dji.sdk.sample I/zygote64: Background concurrent copying GC freed 818877(27MB) AllocSpace objects, 6(120KB) LOS objects, 32% free, 50MB/74MB, paused 825us total 187.449ms
2022-05-16 21:36:15.408 24786-27288/com.dji.sdk.sample I/WebServer: passCommand /xp
2022-05-16 21:36:15.409 24786-27288/com.dji.sdk.sample I/VirtualStick: onWebControlEvent /xp
2022-05-16 21:36:25.159 24786-27446/com.dji.sdk.sample I/WebServer: passCommand /xp
2022-05-16 21:36:25.159 24786-27446/com.dji.sdk.sample I/VirtualStick: onWebControlEvent /xp
2022-05-16 21:36:26.955 24786-27480/com.dji.sdk.sample I/WebServer: passCommand /xp
2022-05-16 21:36:26.956 24786-27480/com.dji.sdk.sample I/VirtualStick: onWebControlEvent /xp
2022-05-16 21:36:30.757 24786-27537/com.dji.sdk.sample I/WebServer: passCommand /xp
2022-05-16 21:36:30.758 24786-27537/com.dji.sdk.sample I/VirtualStick: onWebControlEvent /xp
2022-05-16 21:36:31.895 24786-24786/com.dji.sdk.sample E/koy$fdd: send to server success: Response{protocol=h2, code=200, message=, url=https://statistical-report.djiservice.org/api/report/web}
2022-05-16 21:36:36.686 24786-27630/com.dji.sdk.sample I/WebServer: passCommand /xp
2022-05-16 21:36:36.687 24786-27630/com.dji.sdk.sample I/VirtualStick: onWebControlEvent /xp
```

