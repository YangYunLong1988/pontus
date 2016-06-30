package com.snowstore.pontus.endpoint;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.Valid;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.snowstore.log.annotation.UserLog;
import com.snowstore.pontus.domain.BankInfo;
import com.snowstore.pontus.domain.Customer;
import com.snowstore.pontus.enums.Enums;
import com.snowstore.pontus.repository.CustomerRepository;
import com.snowstore.pontus.service.CustomerService;
import com.snowstore.pontus.service.IdAuthenticationService;
import com.snowstore.pontus.service.PontusServiceException;
import com.snowstore.pontus.vo.req.IdAuthCompleteReq;
import com.snowstore.pontus.vo.req.IdAuthInitReq;
import com.snowstore.pontus.vo.resp.IdAuthCompleteResp;
import com.snowstore.pontus.vo.resp.IdAuthInitResp;

/**
 * 身份认证流程
 * 
 * @Project: pontus-api
 * @Author zy
 * @Company:
 * @Create Time: 2016年5月5日 下午1:14:39
 */
@Path("/idAuthentication")
@Produces(MediaType.APPLICATION_JSON)
@Component
public class IdAuthenticationEndpoint {

	@Autowired
	private CustomerRepository customerRepository;
	@Autowired
	private CustomerService customerService;
	@Autowired
	private IdAuthenticationService idAuthenticationService;

	private static final Logger LOGGER = LoggerFactory.getLogger(IdAuthenticationEndpoint.class);

	@Path("/init")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@UserLog(remark = "身份认证初始提交")
	public IdAuthInitResp init(@Valid @BeanParam IdAuthInitReq req) {
		LOGGER.debug("身份认证初始请求参数【" + req + "】");
		IdAuthInitResp resp = new IdAuthInitResp();

		try {
			// 校验姓名及身份证是否已经被认证
			if (null != customerRepository.findByIdCardAccountAndWorkFlow(req.getIdCardAccount(), Enums.CustomerWorkFlow.PASSED.getValue())) {
				throw new PontusServiceException("此身份证号码已经被认证");
			}
			// 查询用户
			String customerId = customerService.getCustomerId(req.getAccessToken());
			Customer customer = customerRepository.findOne(Long.valueOf(customerId));
			customer.setIdCardName(req.getIdCardName());
			customer.setIdCardAccount(req.getIdCardAccount());
			customer.setStatus(Enums.CustomerState.VALID.getValue());
			customerRepository.save(customer);
		} catch (PontusServiceException e) {
			LOGGER.error("身份认证初始化异常", e);
			throw e;
		} catch (Exception e) {
			LOGGER.error("身份认证初始化异常", e);
			throw new PontusServiceException("身份认证初始化异常", e);
		}
		return resp;
	}

	@Path("/complete")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@UserLog(remark = "身份认证完成")
	public IdAuthCompleteResp complete(@Valid @BeanParam IdAuthCompleteReq req) {
		LOGGER.debug("身份认证完成请求参数【" + req + "】");
		IdAuthCompleteResp resp = new IdAuthCompleteResp();
		String customerId = null;
		try {
			// 查询用户
			customerId = customerService.getCustomerId(req.getAccessToken());
			BankInfo bankInfoForm = new BankInfo();
			bankInfoForm.setName(req.getBankName());
			bankInfoForm.setAccount(replaceBlank(req.getAccount()));
			bankInfoForm.setSubbranch(req.getSubbranch());
			bankInfoForm.setProvince(req.getProvince());
			bankInfoForm.setCity(req.getCity());

			idAuthenticationService.complete(bankInfoForm, customerId);
		} catch (PontusServiceException e1) {
			customerService.updateCustomerWorkFlow(Long.valueOf(customerId), Enums.CustomerWorkFlow.REJECT.getValue());
			LOGGER.error("身份认证完成时异常", e1);
			throw e1;
		} catch (Exception e) {
			customerService.updateCustomerWorkFlow(Long.valueOf(customerId), Enums.CustomerWorkFlow.REJECT.getValue());
			LOGGER.error("身份认证完成时异常", e);
			throw new PontusServiceException("身份认证完成时异常", e);
		}
		return resp;
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
