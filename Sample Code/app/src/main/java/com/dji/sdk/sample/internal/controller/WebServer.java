package com.dji.sdk.sample.internal.controller;

import android.util.Log;

import com.dji.sdk.sample.demo.flightcontroller.VirtualStickView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fi.iki.elonen.NanoHTTPD;

public class WebServer extends NanoHTTPD {

    public WebServer(int port) {
        super(port);
    }

    public WebServer(String hostname, int port) {
        super(hostname, port);
        DJISampleApplication.getEventBus().register(this);
        Log.i("WebServer", "Start WebServer " + hostname + ":" + port);
    }

    private void listItem(StringBuilder sb, Map.Entry<String, ? extends Object> entry) {
        sb.append("<li><code><b>").append(entry.getKey()).append("</b> = ").append(entry.getValue()).append("</code></li>");
    }

    @Override
    public Response serve(IHTTPSession session) {
        Map<String, List<String>> decodedQueryParameters = decodeParameters(session.getQueryParameterString());

        if (session.getUri().startsWith("/cmd")) {
            String cmd = session.getUri();
            // /cmd?lpx=5lpy=5&rpx=5&rpy=5
            Log.i("WebServer", toString(session.getParms()) + toString(decodedQueryParameters));
            this.passCommand(cmd);
            return newFixedLengthResponse(Response.Status.OK, MIME_PLAINTEXT, "OK");
        }


        StringBuilder sb = new StringBuilder();
        sb.append("<html>");
        sb.append("<head><title>Drone Remote Server</title></head>");
        sb.append("<body>");
        sb.append("<h1>Drone Remote Server</h1>");
        sb.append("<p><blockquote><b>URI</b> = ").append(String.valueOf(session.getUri())).append("<br />");
        sb.append("<b>Method</b> = ").append(String.valueOf(session.getMethod())).append("</blockquote></p>");
        sb.append("<h3>Headers</h3><p><blockquote>").append(toString(session.getHeaders())).append("</blockquote></p>");
        sb.append("<h3>Parms</h3><p><blockquote>").append(toString(session.getParms())).append("</blockquote></p>");
        sb.append("<h3>Parms (multi values?)</h3><p><blockquote>").append(toString(decodedQueryParameters)).append("</blockquote></p>");

        try {
            Map<String, String> files = new HashMap<String, String>();
            session.parseBody(files);
            sb.append("<h3>Files</h3><p><blockquote>").append(toString(files)).append("</blockquote></p>");
        } catch (Exception e) {
            e.printStackTrace();
        }

        sb.append("</body>");
        sb.append("</html>");

        return newFixedLengthResponse(sb.toString());
    }

    private void passCommand(String cmd) {
        Log.i("WebServer", "passCommand " + cmd);

        //should call every 100ms
        //m?lpx=5lpy=5&rpx=5&rpy=5
        DJISampleApplication.getEventBus().post(new VirtualStickView.WebControlEvent(cmd));

    }

    private String toString(Map<String, ? extends Object> map) {
        if (map.size() == 0) {
            return "";
        }
        return unsortedList(map);
    }

    private String unsortedList(Map<String, ? extends Object> map) {
        StringBuilder sb = new StringBuilder();
        sb.append("<ul>");
        for (Map.Entry<String, ? extends Object> entry : map.entrySet()) {
            listItem(sb, entry);
        }
        sb.append("</ul>");
        return sb.toString();
    }
}