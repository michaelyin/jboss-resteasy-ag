package ag.login.client;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.ext.Provider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Provider
public class CookieRequestFilter implements ClientRequestFilter {
    private Cookie cookie;

    public CookieRequestFilter(Cookie cookie) {
        super();
        this.cookie = cookie;
    }

    @Override
    public void filter(ClientRequestContext clientRequestContext) throws IOException {
        List<Object> cookies = new ArrayList<Object>();
        cookies.add(this.cookie);
        clientRequestContext.getHeaders().put("Cookie", cookies);
    }
}