package com.snowstore.pontus.provider;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import org.glassfish.hk2.api.MultiException;
import org.glassfish.jersey.server.ParamException;

import com.snowstore.pontus.vo.ResponseVo;
import com.snowstore.pontus.vo.ResponseVo.RespCode;

//@Provider
public class ParamExceptionMapper implements ExceptionMapper<MultiException> {

	@Override
	public Response toResponse(MultiException exception) {
		ResponseVo responseVo = new ResponseVo();
		String error = "";
		for (Throwable e : exception.getErrors()) {
			if (e instanceof ParamException) {
				error = ((ParamException) e).getCause().getMessage();
			}
		}
		responseVo.setMemo(error);
		responseVo.setStatus(RespCode.ERROR);
		return Response.status(200).entity(responseVo).type(MediaType.APPLICATION_JSON).build();
	}
}
