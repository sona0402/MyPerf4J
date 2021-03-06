package cn.myperf4j.base.http.client;

import cn.myperf4j.base.http.HttpHeaders;
import cn.myperf4j.base.http.HttpRequest;
import cn.myperf4j.base.http.HttpResponse;
import cn.myperf4j.base.http.server.Dispatcher;
import cn.myperf4j.base.http.server.SimpleHttpServer;
import cn.myperf4j.base.util.MapUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static cn.myperf4j.base.http.HttpRespStatus.OK;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Created by LinShunkang on 2020/05/16
 */
public class HttpClientTest {

    private static final String RESPONSE_BODY = "Hello!";

    private static final HttpClient httpClient = new HttpClient.Builder().build();

    private SimpleHttpServer server;

    @Before
    public void init() {
        server = new SimpleHttpServer(8086, new Dispatcher() {
            @Override
            public HttpResponse dispatch(HttpRequest request) {
                System.out.println("Dispatcher.dispatch(): request.body=" + new String(request.getBody()));
                return new HttpResponse(OK, new HttpHeaders(0), RESPONSE_BODY.getBytes(UTF_8));
            }
        });
        server.startAsync();
    }

    @After
    public void clean() {
        server.stop();
    }

    @Test
    public void testGet() {
        HttpRequest req = new HttpRequest.Builder()
                .remoteHost("www.baidu.com")
                .remotePort(80)
                .path("/")
//                .header("Connection", "close")
                .get()
                .build();
        try {
            HttpResponse resp = httpClient.execute(req);
            HttpHeaders headers = resp.getHeaders();
            System.out.println("Status=" + resp.getStatus());
            System.out.println("Connection=" + headers.get("Connection"));
            System.out.println(resp.getBodyString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testPost() {
        Map<String, List<String>> params = MapUtils.createHashMap(2);
        params.put("db", Collections.singletonList("http"));

        HttpRequest req = new HttpRequest.Builder()
                .remoteHost("localhost")
                .remotePort(8086)
                .path("/write")
                .params(params)
                .post("cpu_load_short,host=server01,region=us-west value=0.64 1434055562000000000\n" +
                        "cpu_load_short,host=server02,region=us-west value=0.96 1434055562000000000")
                .build();

        for (int i = 0; i < 10; i++) {
            try {
                HttpResponse resp = httpClient.execute(req);
                Assert.assertEquals(OK, resp.getStatus());
                Assert.assertEquals(RESPONSE_BODY, resp.getBodyString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
