package com.snowstore.pontus.provider;

import java.io.IOException;
import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import com.snowstore.pontus.vo.ResponseVo;
import com.snowstore.pontus.vo.ResponseVo.RespCode;

/**
 * @author sm
 * 
 */
@Provider
public class FileSizeFilter implements ContainerRequestFilter {

	@Context
	private HttpServletRequest request;

	private final static String URL = "file/uploadFile";

	@Context
	Configuration config;

	private BigDecimal maxsize;

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		if (URL.equals(requestContext.getUriInfo().getPath())) {
			if (null == maxsize)
				maxsize = (BigDecimal) config.getProperty("fileMaxsize");
			BigDecimal length = new BigDecimal(requestContext.getHeaderString("content-length"));
			BigDecimal mb = length.divide(new BigDecimal(1024)).divide(new BigDecimal(1024));
			if (mb.compareTo(maxsize) > 0) {
				ResponseVo responseVo = new ResponseVo();
				responseVo.setStatus(RespCode.ERROR);
				responseVo.setMemo("上传的附件不能超过" + maxsize + "M");
				requestContext.abortWith(Response.ok(responseVo, MediaType.APPLICATION_JSON).build());
//				requestContext.abortWith(Response.serverError().build());
			}
		}
	}

}
