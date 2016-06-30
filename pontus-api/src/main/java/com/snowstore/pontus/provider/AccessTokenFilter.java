package com.snowstore.pontus.provider;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;

import com.snowstore.pontus.audit.ApiSession;
import com.snowstore.pontus.config.WebConfig;
import com.snowstore.pontus.vo.ResponseVo;
import com.snowstore.pontus.vo.ResponseVo.RespCode;

/**
 * @author sm
 * 
 */
@Provider
public class AccessTokenFilter implements ContainerRequestFilter {

	@Autowired
	private StringRedisTemplate template;

	@Context
	private HttpServletRequest request;

	@Context
	Configuration config;

	private String[] excludeUrlArray = { "login", "getCode", "register", "retrieve","asset/transferQuery" };

	private Set<String> excludeUrls = new HashSet<String>(Arrays.asList(excludeUrlArray));

	private Long timeout;

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {

		String token = request.getParameter(WebConfig.ACCESS_TOKEN);
		if (!excludeUrls.contains(requestContext.getUriInfo().getPath())) {
			if (StringUtils.isEmpty(token) || !this.template.hasKey(token)) {
				ResponseVo responseVo = new ResponseVo();
				responseVo.setStatus(RespCode.DENIED);
				responseVo.setMemo("请重新登录!");
				requestContext.abortWith(Response.ok(responseVo, MediaType.APPLICATION_JSON).build());
			} else {
				if (null == timeout) {
					timeout = (Long) config.getProperty("timeout");
				}
				ApiSession.setCustomerId(this.template.opsForValue().get(token));
				template.expire(token, timeout, TimeUnit.MINUTES);
			}

		}

	}
}
