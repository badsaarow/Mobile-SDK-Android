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