package com.snowstore.pontus.endpoint;

import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.snowstore.log.annotation.UserLog;
import com.snowstore.pontus.domain.Customer;
import com.snowstore.pontus.repository.CustomerRepository;
import com.snowstore.pontus.service.BankInfoService;
import com.snowstore.pontus.service.CustomerService;
import com.snowstore.pontus.service.PontusServiceException;
import com.snowstore.pontus.vo.req.AccountAmontReq;
import com.snowstore.pontus.vo.req.AccountSearchReq;
import com.snowstore.pontus.vo.resp.AccountAmountResp;
import com.snowstore.pontus.vo.resp.AccountSearchResp;


/**
 * 获取支行
 * @Project: pontus-api
 * @Author zy
 * @Company: 
 * @Create Time: 2016年5月5日 下午1:14:39
 */
@Path("/account")
@Produces(MediaType.APPLICATION_JSON)
@Component
public class AccountEndpoint {

	private static final Logger LOGGER = LoggerFactory.getLogger(AccountEndpoint.class);
	
	@Autowired
	private CustomerRepository customerRepository;
	@Autowired
	private CustomerService customerService;
	@Autowired
	private BankInfoService bankInfoService;
	
	/**
	 * 我的账户	
	 * @Author zy
	 * @Company: 
	 * @Create Time: 2016年5月7日 上午9:08:57
	 * @param req
	 * @return
	 */
	@Path("/amount")
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@UserLog(remark = "我的账户")
	public AccountAmountResp amount(@BeanParam AccountAmontReq req) {
		LOGGER.debug("账户金额请求参数【" + req + "】");
		AccountAmountResp resp = new AccountAmountResp();
		try{
			resp.setInvestAmount(customerService.amount(Long.valueOf(customerService.getCustomerId(req.getAccessToken()))));
		} catch (Exception e) {
			LOGGER.error("获取账户信息异常", e);
			throw new PontusServiceException("获取账户信息异常", e);
		}
		return resp;
	}
	
	/**
	 * 查询用户基本信息
	 * @Author zy
	 * @Company: 
	 * @Create Time: 2016年5月7日 上午9:08:46
	 * @param req
	 * @return
	 */
	@Path("/search")
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@UserLog(remark = "查询用户基本信息")
	public AccountSearchResp search(@BeanParam AccountSearchReq req) {
		LOGGER.debug("查询用户基本信息请求参数【" + req + "】");
		AccountSearchResp resp = new AccountSearchResp();
		try{
			// 查询用户
			String customerId = customerService.getCustomerId(req.getAccessToken());
			Customer customer = customerRepository.findOne(Long.valueOf(customerId));
			resp.setIdCardAccount(customer.getIdCardAccount());
			resp.setIdCardName(customer.getIdCardName());
			resp.setBankCount(bankInfoService.countByCustomerId(Long.valueOf(customerId)));
			
		} catch (Exception e) {
			LOGGER.error("获取账户信息异常", e);
			throw new PontusServiceException("获取账户信息异常", e);
		}
		return resp;
	}
	
	
}
