package com.dji.sdk.sample.internal.controller;

import android.util.Log;

import com.dji.sdk.sample.demo.flightcontroller.VirtualStickView;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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
            // Parms
            // lpx = 5, lpy=5, rpx, rpy = 5
            Map<String, String> param =  session.getParms();
            this.passCommand(cmd, param);
            //return newFixedLengthResponse(Response.Status.OK, MIME_PLAINTEXT, "OK");
        } else  if (session.getUri().equals("/")) {
            StringBuilder sb = new StringBuilder();
            sb.append("<script>window.location.href = /status;</script>");
            return newFixedLengthResponse(sb.toString());
        } else if (session.getUri().equals("/control")) {
            StringBuilder sb = new StringBuilder();
            sb.append("<html>");
            sb.append("<head>");
            sb.append("<script>");
            sb.append("const joystick = {");
            sb.append("lpx: 0,");
            sb.append("lpy: 0,");
            sb.append("rpx: 0,");
            sb.append("rpy: 0,");
            sb.append("};");
            sb.append("");
            sb.append("");
            sb.append("var interval = null;");
            sb.append("function startMove() {");
            sb.append("if (interval == null) {");
            sb.append("interval = setInterval(function() {");
            sb.append("fetch('/cmd?lpx='+ joystick.lpx + '&lpy=' + joystick.lpx + '&rpx='+ joystick.lpx + '&rpy='+ joystick.lpx).then((response)=>console.log(response))");
            sb.append("}, 100);");
            sb.append("}");
            sb.append("};");
            sb.append("");
            sb.append("function stopMove() {");
            sb.append("joystick.lpx = 0;");
            sb.append("joystick.lpy = 0;");
            sb.append("joystick.rpx = 0;");
            sb.append("joystick.rpy = 0;");
            sb.append("clearInterval(interval);");
            sb.append("interval = null;");
            sb.append("};");
            sb.append("</script>");
            sb.append("</head>");
            sb.append("<body>");
            sb.append("<div>");
            sb.append("<div class=\"left\">");
            sb.append("<button id=\"lleft\">LEFT</button>");
            sb.append("<button id=\"lright\">RIGHT</button>");
            sb.append("<button id=\"lup\">UP</button>");
            sb.append("<button id=\"ldown\">DOWN</button>");
            sb.append("</div>");
            sb.append("<div class=\"right\">");
            sb.append("<button id=\"rleft\">LEFT</button>");
            sb.append("<button id=\"rright\">RIGHT</button>");
            sb.append("<button id=\"rup\">UP</button>");
            sb.append("<button id=\"rdown\">DOWN</button>");
            sb.append("</div>");
            sb.append("</div>");
            sb.append("<a href=/status>Status</a>");
            sb.append("<script>");
            sb.append("function onkeydown(e) {");
            sb.append("const key = document.getElementById(e.currentTarget.id);");
            sb.append("if (key.id == 'lleft') {");
            sb.append("joystick.lpx = -5;");
            sb.append("} else if (key.id == 'lright') {");
            sb.append("joystick.lpx = 5;");
            sb.append("} else if (key.id == 'lup') {");
            sb.append("joystick.lpy = 5;");
            sb.append("} else if (key.id == 'ldown') {");
            sb.append("joystick.lpy = -5;");
            sb.append("} else if (key.id == 'rleft') {");
            sb.append("joystick.rpx = -5;");
            sb.append("} else if (key.id == 'rright') {");
            sb.append("joystick.rpx = 5;");
            sb.append("} else if (key.id == 'rup') {");
            sb.append("joystick.rpy = 5;");
            sb.append("} else if (key.id == 'rdown') {");
            sb.append("joystick.rpy = -5;");
            sb.append("}");
            sb.append("startMove();");
            sb.append("};");
            sb.append("document.getElementById('lleft').onmousedown = onkeydown;");
            sb.append("document.getElementById('lright').onmousedown = onkeydown;");
            sb.append("document.getElementById('lup').onmousedown = onkeydown;");
            sb.append("document.getElementById('ldown').onmousedown = onkeydown;");
            sb.append("document.getElementById('rleft').onmousedown = onkeydown;");
            sb.append("document.getElementById('lright').onmousedown = onkeydown;");
            sb.append("document.getElementById('rup').onmousedown = onkeydown;");
            sb.append("document.getElementById('rdown').onmousedown = onkeydown;");
            sb.append("document.getElementById('lleft').onmouseup = stopMove;");
            sb.append("document.getElementById('lright').onmouseup = stopMove;");
            sb.append("document.getElementById('lup').onmouseup = stopMove;");
            sb.append("document.getElementById('ldown').onmouseup = stopMove;");
            sb.append("document.getElementById('rleft').onmouseup = stopMove;");
            sb.append("document.getElementById('rright').onmouseup = stopMove;");
            sb.append("document.getElementById('rup').onmouseup = stopMove;");
            sb.append("document.getElementById('rdown').onmouseup = stopMove;");
            sb.append("</script>");
            sb.append("</body>");
            sb.append("</html>");
            return newFixedLengthResponse(sb.toString());
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
        sb.append("<a href=/control>Controller</a>");
        sb.append("</body>");
        sb.append("</html>");

        return newFixedLengthResponse(sb.toString());
    }

    //Announce that the file server accepts partial content requests
    private Response createResponse(Response.Status status, String mimeType,
                                    InputStream message) {
        Response res = newChunkedResponse(status, mimeType, message);
        res.addHeader("Accept-Ranges", "bytes");
        return res;
    }

    /**
     * Serves file from homeDir and its' subdirectories (only). Uses only URI,
     * ignores all headers and HTTP parameters.
     */
    private Response serveFile(String uri, Map<String, String> header,
                               File file, String mime) {
        Response res;
        try {
            // Calculate etag
            String etag = Integer.toHexString((file.getAbsolutePath()
                    + file.lastModified() + "" + file.length()).hashCode());

            // Support (simple) skipping:
            long startFrom = 0;
            long endAt = -1;
            String range = header.get("range");
            if (range != null) {
                if (range.startsWith("bytes=")) {
                    range = range.substring("bytes=".length());
                    int minus = range.indexOf('-');
                    try {
                        if (minus > 0) {
                            startFrom = Long.parseLong(range
                                    .substring(0, minus));
                            endAt = Long.parseLong(range.substring(minus + 1));
                        }
                    } catch (NumberFormatException ignored) {
                    }
                }
            }

            // Change return code and add Content-Range header when skipping is
            // requested
            long fileLen = file.length();
            if (range != null && startFrom >= 0) {
                if (startFrom >= fileLen) {
                    res = newFixedLengthResponse(Response.Status.RANGE_NOT_SATISFIABLE,
                            NanoHTTPD.MIME_PLAINTEXT, "");
                    res.addHeader("Content-Range", "bytes 0-0/" + fileLen);
                    res.addHeader("ETag", etag);
                } else {
                    if (endAt < 0) {
                        endAt = fileLen - 1;
                    }
                    long newLen = endAt - startFrom + 1;
                    if (newLen < 0) {
                        newLen = 0;
                    }

                    final long dataLen = newLen;
                    FileInputStream fis = new FileInputStream(file) {
                        @Override
                        public int available() throws IOException {
                            return (int) dataLen;
                        }
                    };
                    fis.skip(startFrom);

                    res = createResponse(Response.Status.PARTIAL_CONTENT, mime,
                            fis);
                    res.addHeader("Content-Length", "" + dataLen);
                    res.addHeader("Content-Range", "bytes " + startFrom + "-"
                            + endAt + "/" + fileLen);
                    res.addHeader("ETag", etag);
                }
            } else {
                if (etag.equals(header.get("if-none-match")))
                    res = newFixedLengthResponse(Response.Status.NOT_MODIFIED, mime, "");
                else {
                    res = createResponse(Response.Status.OK, mime,
                            new FileInputStream(file));
                    res.addHeader("Content-Length", "" + fileLen);
                    res.addHeader("ETag", etag);
                }
            }
        } catch (IOException ioe) {
            res = newFixedLengthResponse(Response.Status.FORBIDDEN,
                    NanoHTTPD.MIME_PLAINTEXT, "FORBIDDEN: Reading file failed.");
        }

        return res;
    }

    private void passCommand(String cmd, Map<String, String> param) {
        Log.i("WebServer", "passCommand " + cmd + ", " + param.toString());

        //should call every 100ms
        //cmdm?lpx=5&lpy=5&rpx=5&rpy=5
        Map<String, String> axParam = new HashMap<String, String>();
        axParam.put("lpx", param.get("lpx").equals("0") ? "0": String.valueOf(Float.parseFloat(param.get("lpx")) / 10f));
        axParam.put("lpy", param.get("lpy").equals("0") ? "0": String.valueOf(Float.parseFloat(param.get("lpy")) / 10f));
        axParam.put("rpx", param.get("rpx").equals("0") ? "0": String.valueOf(Float.parseFloat(param.get("rpx")) / 10f));
        axParam.put("rpy", param.get("rpy").equals("0") ? "0": String.valueOf(Float.parseFloat(param.get("rpy")) / 10f));

        DJISampleApplication.getEventBus().post(new VirtualStickView.WebControlEvent(cmd, axParam));

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