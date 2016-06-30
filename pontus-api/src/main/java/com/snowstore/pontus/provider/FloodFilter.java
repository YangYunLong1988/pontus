package com.snowstore.pontus.provider;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.google.common.cache.LoadingCache;

/**
 * @author sm
 * 
 */
@Provider
public class FloodFilter implements ContainerRequestFilter {

	@Context
	private HttpServletRequest request;

	private final static String URL = "getCode";

	@Autowired
	LoadingCache<String, CountPertime> loadingCache;

	@Context
	Configuration config;

	private Long limit;

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {

		if (URL.equals(requestContext.getUriInfo().getPath())) {
			String ip = requestContext.getHeaderString("X-Real-IP");
			ip = (StringUtils.isEmpty(ip)) ? request.getRemoteAddr() : ip;

			CountPertime countPertime = loadingCache.getUnchecked(ip);
			int count = countPertime.getCount();
			if (null == limit) {
				limit = (Long) config.getProperty("smsLimit");
			}
			// 大于阈值
			if (count > limit) {
				if (countPertime.getRecodeTime().before(DateTime.now().plusHours(-1).toDate())) {
					loadingCache.invalidate(ip);
				} else {
					// 拒绝
					requestContext.abortWith(Response.serverError().build());
				}
			} else {
				loadingCache.put(ip, countPertime.addCount());
			}

		}
	}

	public static class CountPertime {
		private int count = 1;
		private Date recodeTime = new Date();

		public int getCount() {
			return count;
		}

		public void setCount(int count) {
			this.count = count;
		}

		public Date getRecodeTime() {
			return recodeTime;
		}

		public void setRecodeTime(Date recodeTime) {
			this.recodeTime = recodeTime;
		}

		public CountPertime addCount() {
			this.count++;
			return this;
		}

	}
}
