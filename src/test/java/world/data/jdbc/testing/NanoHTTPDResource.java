/*
 * dw-jdbc
 * Copyright 2017 data.world, Inc.

 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the
 * License.
 *
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * permissions and limitations under the License.
 *
 * This product includes software developed at data.world, Inc.(http://www.data.world/).
 */
package world.data.jdbc.testing;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.IHTTPSession;
import fi.iki.elonen.NanoHTTPD.Response;
import org.junit.rules.ExternalResource;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * JUnit @{code @Rule} for a simple HTTP server for use in unit tests.
 */
public abstract class NanoHTTPDResource extends ExternalResource {
    private final NanoHTTPD apiServer;

    public NanoHTTPDResource(int port) {
        this.apiServer = new NanoHTTPD(port) {
            @Override
            public Response serve(IHTTPSession session) {
                try {
                    return NanoHTTPDResource.this.serve(session);
                } catch (Exception e) {
                    e.printStackTrace();
                    return newResponse(Response.Status.INTERNAL_ERROR, "text/plain", getStackTraceAsString(e));
                }
            }
        };
    }

    @Override
    protected void before() throws Throwable {
        apiServer.start();
    }

    @Override
    protected void after() {
        apiServer.stop();
    }

    protected abstract Response serve(IHTTPSession session) throws Exception;

    protected static Response newResponse(Response.Status status, String mimeType, String body) {
        Response response = NanoHTTPD.newFixedLengthResponse(status, mimeType, body);
        response.addHeader("Connection", "close");  // avoid errors if the test impl ignores the POST body
        return response;
    }

    private String getStackTraceAsString(Throwable t) {
        StringWriter buf = new StringWriter();
        t.printStackTrace(new PrintWriter(buf));
        return buf.toString();
    }
}
