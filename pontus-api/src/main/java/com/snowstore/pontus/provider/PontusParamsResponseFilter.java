package com.snowstore.pontus.provider;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.snowstore.pontus.vo.ResponseVo;
import com.snowstore.pontus.vo.ResponseVo.RespCode;

@Provider
public class PontusParamsResponseFilter implements ContainerResponseFilter {

	
	private static final Logger LOGGER = LoggerFactory.getLogger(PontusParamsResponseFilter.class);
	
	@Override
	public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {

		try{
			Object obj = responseContext.getEntity();
			if(obj instanceof ResponseVo) {
				ResponseVo responseVo =  (ResponseVo)obj;
				if(responseVo.getStatus().equals(RespCode.ERROR)) {
					if(!StringUtils.isEmpty(responseVo.getMemo()) && responseVo.getMemo().length() == (responseVo.getMemo().lastIndexOf("|") +1)) {
						responseVo.setMemo(responseVo.getMemo().substring(0, responseVo.getMemo().lastIndexOf("|")));
					}
				}
			}
		}catch(Exception e) {
			LOGGER.error("处理返回参数异常【" + e + "】");
		}
	}
}
