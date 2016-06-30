package com.snowstore.pontus.endpoint;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.Valid;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.snowstore.log.annotation.UserLog;
import com.snowstore.pontus.domain.BankInfo;
import com.snowstore.pontus.service.BankInfoService;
import com.snowstore.pontus.service.CustomerService;
import com.snowstore.pontus.service.PontusServiceException;
import com.snowstore.pontus.vo.req.BankAddReq;
import com.snowstore.pontus.vo.req.BankDelReq;
import com.snowstore.pontus.vo.req.BankListReq;
import com.snowstore.pontus.vo.resp.BankAddResp;
import com.snowstore.pontus.vo.resp.BankInfoResp;
import com.snowstore.pontus.vo.resp.BankListResp;


/**
 * 获取支行
 * @Project: pontus-api
 * @Author zy
 * @Company: 
 * @Create Time: 2016年5月5日 下午1:14:39
 */
@Path("/bankInfo")
@Produces(MediaType.APPLICATION_JSON)
public class BankInfoEndpoint {

	private static final Logger LOGGER = LoggerFactory.getLogger(BankInfoEndpoint.class);
	
	@Autowired
	private CustomerService customerService;
	@Autowired
	private BankInfoService bankInfoService;
	@Autowired
	Mapper dozerBeanMapper;
	
	/**
	 * 新增银行卡
	 * @Author zy
	 * @Company: 
	 * @Create Time: 
	 * @param req
	 * @return
	 */
	@Path("/bankAdd")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@UserLog(remark = "新增银行卡")
	public BankAddResp bankAdd(@BeanParam BankAddReq req) {
		LOGGER.debug("新增银行卡请求参数【" + req + "】");
		BankAddResp resp = new BankAddResp();
		try{
			// 查询用户
			String customerId = customerService.getCustomerId(req.getAccessToken());
			
			BankInfo bankInfo = new BankInfo();
			dozerBeanMapper.map(req, bankInfo);
			bankInfo.setAccount(replaceBlank(req.getAccount()));
			bankInfoService.addBankInfo(bankInfo ,Long.valueOf(customerId), req.getUserName());
		} catch(PontusServiceException e1){
			LOGGER.error(e1.getMessage());
			throw e1;
		}catch (Exception e) {
			LOGGER.error("新增银行卡异常", e);
			throw new PontusServiceException("新增银行卡异常", e);
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
	
	/**
	 * 删除银行卡
	 * @Author zy
	 * @Company: 
	 * @Create Time: 
	 * @param req
	 * @return
	 */
	@Path("/bankDel")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@UserLog(remark = "新增银行卡")
	public BankAddResp bankDel(@BeanParam @Valid BankDelReq req) {
		LOGGER.debug("删除银行卡请求参数【" + req + "】");
		BankAddResp resp = new BankAddResp();
		try{
			// 查询用户
			String customerId = customerService.getCustomerId(req.getAccessToken());
			bankInfoService.delBankInfo(Long.valueOf(req.getBankInfoId()),Long.valueOf(customerId));
			
		} catch(PontusServiceException e1){
			LOGGER.error(e1.getMessage());
			throw e1;
		}catch (Exception e) {
			LOGGER.error("删除银行卡异常", e);
			throw new PontusServiceException("删除银行卡失败！", e);
		}
		return resp;
	}
	
	/**
	 * 获取银行列表
	 * @Author zy
	 * @Company: 
	 * @Create Time: 
	 * @param req
	 * @return
	 */
	@Path("/bankList")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@UserLog(remark = "查询用户银行列表信息")
	public BankListResp bankList(@BeanParam @Valid BankListReq req) {
		LOGGER.debug("查询用户银行列表信息请求参数【" + req + "】");
		BankListResp resp = new BankListResp();
		try{
			// 查询用户
			String customerId = customerService.getCustomerId(req.getAccessToken());
			List<BankInfo> bankInfos = bankInfoService.findAllByCreateDesc(Long.valueOf(customerId));
			if(null != bankInfos) {
				List<BankInfoResp> bankInfoRespList = new ArrayList<BankInfoResp>();
				for (Iterator<BankInfo> iterator = bankInfos.iterator(); iterator.hasNext();) {
					BankInfo bankInfo = (BankInfo) iterator.next();
					BankInfoResp bankInfoResp = new BankInfoResp();
					BeanUtils.copyProperties(bankInfo, bankInfoResp);
					bankInfoResp.setId(bankInfo.getId());
					bankInfoResp.setDefaulted((null != bankInfo.getDefaulted()) && bankInfo.getDefaulted() ==true ? "1" : "0" );
					bankInfoResp.setTailAccount(null != bankInfo.getAccount() ? bankInfo.getAccount().substring(bankInfo.getAccount().length()-4) : "");
					bankInfoRespList.add(bankInfoResp);
				}
				resp.setBankInfoRespList(bankInfoRespList);
			}
		} catch (Exception e) {
			LOGGER.error("查询用户银行列表信息异常", e);
			throw new PontusServiceException("查询用户银行列表信息异常", e);
		}
		return resp;
	}
	
}
