package com.snowstore.pontus.provider;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.apache.commons.lang3.StringUtils;

import com.snowstore.pontus.service.utils.PropertiesReader;
import com.snowstore.pontus.vo.ResponseVo;
import com.snowstore.pontus.vo.ResponseVo.RespCode;

/**
 * @author sm
 * 
 */
@Provider
public class ValidationExceptionMapper implements ExceptionMapper<ValidationException> {

	@Override
	public Response toResponse(ValidationException exception) {
		ResponseVo responseVo = new ResponseVo();
		Set<ConstraintViolation<?>> constraintViolations = ((ConstraintViolationException) exception).getConstraintViolations();
		StringBuilder message = new StringBuilder();
		if (constraintViolations.size() > 0) {
			for (ConstraintViolation<?> constrainViolation : constraintViolations) {
				message.append(PropertiesReader.readAsString(StringUtils.substringAfterLast(constrainViolation.getPropertyPath().toString(), ".")) + constrainViolation.getMessage() + "|");
			}
		}
		responseVo.setMemo(message.toString());
		responseVo.setStatus(RespCode.ERROR);
		return Response.status(200).entity(responseVo).type(MediaType.APPLICATION_JSON).build();
	}

}
