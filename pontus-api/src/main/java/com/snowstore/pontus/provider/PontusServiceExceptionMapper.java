package com.snowstore.pontus.provider;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.snowstore.pontus.service.PontusServiceException;
import com.snowstore.pontus.vo.ResponseVo;
import com.snowstore.pontus.vo.ResponseVo.RespCode;

/**
 * @author sm
 *
 */
@Provider
public class PontusServiceExceptionMapper implements ExceptionMapper<PontusServiceException> {

	@Override
	public Response toResponse(PontusServiceException exception) {
		ResponseVo responseVo = new ResponseVo();
		responseVo.setMemo(exception.getMessage());
		responseVo.setStatus(RespCode.ERROR);
		return Response.status(200).entity(responseVo).type(MediaType.APPLICATION_JSON).build();
	}
}
