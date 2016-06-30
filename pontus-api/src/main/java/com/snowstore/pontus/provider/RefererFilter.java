package com.snowstore.pontus.provider;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.springframework.http.HttpHeaders;

/**
 * @author sm
 * 
 */
@Provider
public class RefererFilter implements ContainerRequestFilter {

	@Context
	Configuration config;

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		String referer = requestContext.getHeaderString(HttpHeaders.REFERER);
		String domain = (String) config.getProperty("domain");
		if (domain.equals("*"))
			return;
		if (referer == null || !referer.matches(".*" + domain + ".*")) {
			requestContext.abortWith(Response.serverError().build());
		}
	}
}
