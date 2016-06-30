package com.snowstore.pontus.provider;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Provider
public class PontusParamsRequestFilter implements ContainerRequestFilter {

	private static final Logger LOGGER = LoggerFactory.getLogger(PontusParamsRequestFilter.class);
	
	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {

		try{
			String url = requestContext.getUriInfo().getRequestUri().toString();
			if(-1 != url.indexOf("/bankInfo/bankAdd") || -1 != url.indexOf("/idAuthentication/complete")) {
				requestContext.setProperty("account", replaceBlank(String.valueOf(requestContext.getProperty("account"))));
			}
		} catch (Exception e) {
			LOGGER.error("处理返回参数异常【" + e + "】");
		}
	}
	
	public String replaceBlank(String str) {
        String dest = "";
        if (str!=null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }
}
