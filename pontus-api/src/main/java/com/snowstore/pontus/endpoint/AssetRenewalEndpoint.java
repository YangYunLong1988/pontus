package com.snowstore.pontus.endpoint;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.snowstore.log.annotation.UserLog;
import com.snowstore.pontus.domain.OriginalContract;
import com.snowstore.pontus.domain.QuoteContract;
import com.snowstore.pontus.domain.Transfer;
import com.snowstore.pontus.enums.Enums;
import com.snowstore.pontus.enums.Enums.AssigneeWorkFlow;
import com.snowstore.pontus.enums.Enums.QuoteContractWorkFlow;
import com.snowstore.pontus.enums.Enums.TransferFlow;
import com.snowstore.pontus.service.AssigneeService;
import com.snowstore.pontus.service.CommonService;
import com.snowstore.pontus.service.CustomerService;
import com.snowstore.pontus.service.PontusServiceException;
import com.snowstore.pontus.service.QuoteContractService;
import com.snowstore.pontus.service.SpecialContractService;
import com.snowstore.pontus.service.TransferService;
import com.snowstore.pontus.service.common.Calendars;
import com.snowstore.pontus.service.vo.QuoteContractForm;
import com.snowstore.pontus.vo.req.AssetRenewalReq;
import com.snowstore.pontus.vo.req.AssetTranferReq;
import com.snowstore.pontus.vo.req.AssigneeRecordListReq;
import com.snowstore.pontus.vo.req.AssigneeSubmitReq;
import com.snowstore.pontus.vo.req.QueryTransferPriceReq;
import com.snowstore.pontus.vo.req.SpecialContractReq;
import com.snowstore.pontus.vo.req.TransferCancelReq;
import com.snowstore.pontus.vo.req.TransferQueryReq;
import com.snowstore.pontus.vo.req.TransferRecordListReq;
import com.snowstore.pontus.vo.req.TransferSubmitReq;
import com.snowstore.pontus.vo.resp.AssetRenewalListResp;
import com.snowstore.pontus.vo.resp.AssetRenewalResp;
import com.snowstore.pontus.vo.resp.AssetTranferListResp;
import com.snowstore.pontus.vo.resp.AssetTranferResp;
import com.snowstore.pontus.vo.resp.AssigneeRecordListResp;
import com.snowstore.pontus.vo.resp.AssigneeRecordResp;
import com.snowstore.pontus.vo.resp.AssigneeSubmitResp;
import com.snowstore.pontus.vo.resp.QueryTransferPriceResp;
import com.snowstore.pontus.vo.resp.SpecialContractListResp;
import com.snowstore.pontus.vo.resp.SpecialContractResp;
import com.snowstore.pontus.vo.resp.TranferRecordListResp;
import com.snowstore.pontus.vo.resp.TranferRecordResp;
import com.snowstore.pontus.vo.resp.TransferCancelResp;
import com.snowstore.pontus.vo.resp.TransferQueryListResp;
import com.snowstore.pontus.vo.resp.TransferQueryResp;
import com.snowstore.pontus.vo.resp.TransferSubmitResp;
import com.snowstore.pontus.domain.SpecialContract;

/**
 * @Project: pontus-api
 * @Author zy
 * @Company:
 * @Create Time: 2016年5月5日 下午1:14:39
 */
@Path("/asset")
@Produces(MediaType.APPLICATION_JSON)
@Component
public class AssetRenewalEndpoint {

	private static final Logger LOGGER = LoggerFactory.getLogger(AssetRenewalEndpoint.class);

	@Autowired
	private QuoteContractService quoteContractService;
	@Autowired
	private CustomerService customerService;
	@Autowired
	private TransferService transferService;
	@Autowired
	private AssigneeService assigneeService;
	@Autowired
	private CommonService commonService;
	@Autowired
	private SpecialContractService specialContractService;

	/**
	 * 查询特殊兑付
	 * 
	 * @Author zy
	 * @Company:
	 * @Create Time: 2016年5月13日 下午2:22:39
	 * @param req
	 * @return
	 */
	@Path("/specialContract")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@UserLog(remark = "特殊兑付查询")
	public SpecialContractListResp SpecialContract(@BeanParam @Valid SpecialContractReq req) {
		LOGGER.debug("特殊兑付查询【" + req + "】");
		SpecialContractListResp resp = new SpecialContractListResp();
		try {

			// 查询用户
			final String customerId = customerService.getCustomerId(req.getAccessToken());
//			Pageable pageable = new PageRequest(Integer.valueOf(req.getPage())-1, Integer.valueOf(req.getRows()), new Sort(new Order(Direction.DESC, "createdDate")));
//			Page<SpecialContract> page = specialContractService.findAll(specialContractService.buildSpecification(Long.valueOf(customerId)), pageable);
//			List<SpecialContract> specialContractList = page.getContent();
			
			List<Object[]> objList = specialContractService.findAll(Long.valueOf(customerId),Integer.valueOf(req.getPage()), Integer.valueOf(req.getRows()));
			
			
			List<SpecialContractResp> specialContractVoList = new ArrayList<SpecialContractResp>();
			if(!CollectionUtils.isEmpty(objList)) {
				for (Iterator<Object[]> iterator = objList.iterator(); iterator.hasNext();) {
					Object[] objs = (Object[]) iterator.next();
					SpecialContractResp specialContractVo = new SpecialContractResp();
					OriginalContract originalContract = (OriginalContract) objs[1];
					QuoteContract quoteContract = (QuoteContract) objs[2];
					specialContractVo.setContractCode(originalContract.getContractCode());
					if(null != originalContract.getContractEndDate()) {
						specialContractVo.setContractEndDate(Calendars.format(originalContract.getContractEndDate(), Calendars.YYYY_MM_DD));
					}
					specialContractVo.setPlatform(originalContract.getPlatform());
					specialContractVo.setPrincipal(originalContract.getPrincipal());
					specialContractVo.setQuoteContractId(quoteContract.getId());
					
					specialContractVoList.add(specialContractVo);
				}
				resp.setSpecialContractList(specialContractVoList);
			}
			return resp;
		} catch (PontusServiceException e) {
			LOGGER.error("特殊兑付查询失败", e);
			throw e;
		} catch (Exception e) {
			LOGGER.error("特殊兑付查询", e);
			throw new PontusServiceException("特殊兑付查询失败", e);
		}
	}
	
	/**
	 * 查询公开转让资产
	 * 
	 * @Author zy
	 * @Company:
	 * @Create Time: 2016年5月13日 下午2:22:39
	 * @param req
	 * @return
	 */
	@Path("/transferQuery")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@UserLog(remark = "查询公开转让资产")
	public TransferQueryListResp transferQuery(@BeanParam @Valid TransferQueryReq req) {
		LOGGER.debug("查询公开转让资产请求参数【" + req + "】");
		TransferQueryListResp resp = new TransferQueryListResp();
		try {

			// 查询用户
//			String customerId = customerService.getCustomerId(req.getAccessToken());
			List<Object[]> list = transferService.queryTransfer(req.getOrderBy(), Integer.valueOf(req.getPage()), Integer.valueOf(req.getRows()));
			List<TransferQueryResp> transferQueryRespList = new ArrayList<TransferQueryResp>();
			if(!CollectionUtils.isEmpty(list)) {
				for(int i = 0 ; i < list.size(); i++ ) {
					Object[] objs = list.get(i);
					TransferQueryResp transferQueryResp = new TransferQueryResp();
					transferQueryResp.setTransferId(String.valueOf(objs[0]));
					transferQueryResp.setQuoteContractId(String.valueOf(objs[9]));
					transferQueryResp.setContractCode(String.valueOf(objs[10]));
					transferQueryResp.setPrincipal(String.valueOf(objs[12]));
					transferQueryResp.setTransferedValue(String.valueOf(objs[7]));
					transferQueryResp.setExpProfit(String.valueOf(objs[8]));
					transferQueryResp.setActualPrice(String.valueOf(objs[4]));
					transferQueryResp.setDiscountRate(String.valueOf(objs[3]));
					transferQueryResp.setEndDate(String.valueOf(Calendars.format(new Date(((Timestamp)(objs[6])).getTime()), Calendars.YYYY_MM_DD)));
					transferQueryResp.setWorkFlow(String.valueOf(objs[1]));
					transferQueryResp.setLeftDays(commonService.leftTermForTransfer(new Date(((Timestamp)(objs[6])).getTime())));
					if(null == objs[13] || "".equals(objs[13])) {
						transferQueryResp.setBespeakCount(0);
					}else {
						transferQueryResp.setBespeakCount(Integer.valueOf(String.valueOf(objs[13])));
					}
					transferQueryRespList.add(transferQueryResp);
					
				}
				
			}
			resp.setTransferQueryRespList(transferQueryRespList);
			return resp;
		} catch (PontusServiceException e) {
			LOGGER.error("查询公开转让资产失败", e);
			throw e;
		} catch (Exception e) {
			LOGGER.error("查询公开转让资产", e);
			throw new PontusServiceException("查询公开转让资产失败", e);
		}
	}
	
	/**
	 * 撤销转让
	 * 
	 * @Author zy
	 * @Company:
	 * @Create Time: 2016年5月13日 下午2:22:39
	 * @param req
	 * @return
	 */
	@Path("/transferCancel")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@UserLog(remark = "撤销转让")
	public TransferCancelResp transferCancel(@BeanParam @Valid TransferCancelReq req) {
		LOGGER.debug("撤销转让请求参数【" + req + "】");
		TransferCancelResp resp = new TransferCancelResp();
		try {

			// 查询用户
//			String customerId = customerService.getCustomerId(req.getAccessToken());
			transferService.transferCancel(Long.valueOf(req.getTransferId()));
			
			return resp;
		} catch (PontusServiceException e) {
			LOGGER.error("撤销转让失败", e);
			throw e;
		} catch (Exception e) {
			LOGGER.error("撤销转让", e);
			throw new PontusServiceException("撤销转让失败", e);
		}
	}
	
	/**
	 * 提交转让
	 * 
	 * @Author zy
	 * @Company:
	 * @Create Time: 2016年5月13日 下午2:22:39
	 * @param req
	 * @return
	 */
	@Path("/transferSubmit")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@UserLog(remark = "提交转让")
	public TransferSubmitResp transferSubmit(@BeanParam @Valid TransferSubmitReq req) {
		LOGGER.debug("提交转让请求参数【" + req + "】");
		TransferSubmitResp resp = new TransferSubmitResp();
		try {

			// 查询用户
//			String customerId = customerService.getCustomerId(req.getAccessToken());
			transferService.transferSubmit(Long.valueOf(req.getQuoteContractId()), new BigDecimal(req.getDiscountRate()));
			
			return resp;
		} catch (PontusServiceException e) {
			LOGGER.error("提交转让失败", e);
			throw e;
		} catch (Exception e) {
			LOGGER.error("提交转让", e);
			throw new PontusServiceException("提交转让失败", e);
		}
	}
	
	/**
	 * 提交受让预约
	 * 
	 * @Author zy
	 * @Company:
	 * @Create Time: 2016年5月13日 下午2:22:39
	 * @param req
	 * @return
	 */
	@Path("/assigneeSubmit")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@UserLog(remark = "提交受让预约")
	public AssigneeSubmitResp assigneeSubmit(@BeanParam @Valid AssigneeSubmitReq req) {
		LOGGER.debug("提交受让预约请求参数【" + req + "】");
		AssigneeSubmitResp resp = new AssigneeSubmitResp();
		try {

			// 查询用户
			String customerId = customerService.getCustomerId(req.getAccessToken());
			assigneeService.assigneeSubmit(Long.valueOf(req.getTransferId()), Long.valueOf(req.getBankInfoId()),Long.valueOf(customerId));
			
			return resp;
		} catch (PontusServiceException e) {
			LOGGER.error("提交受让预约失败", e);
			throw e;
		} catch (Exception e) {
			LOGGER.error("提交受让预约", e);
			throw new PontusServiceException("提交受让预约失败", e);
		}
	}
	
	
	/**
	 * 转让价格
	 * 
	 * @Author zy
	 * @Company:
	 * @Create Time: 2016年5月13日 下午2:22:39
	 * @param req
	 * @return
	 */
	@Path("/queryTransferPrice")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@UserLog(remark = "查询转让价格")
	public QueryTransferPriceResp queryTransferPrice(@BeanParam @Valid QueryTransferPriceReq req) {
		LOGGER.debug("转让价格请求参数【" + req + "】");
		QueryTransferPriceResp resp = new QueryTransferPriceResp();
		try {

			// 查询用户
//			String customerId = customerService.getCustomerId(req.getAccessToken());
			QuoteContract quoteContract = quoteContractService.findOne(Long.valueOf(req.getQuoteContractId()));
			BigDecimal transferPrice = new BigDecimal(0);
			if(null != quoteContract) {
				transferPrice = transferService.getTransferPrice(quoteContract,new BigDecimal(req.getDiscountRate()));
			}
			resp.setTransferPrice(transferPrice.toString());
			return resp;
		} catch (PontusServiceException e) {
			LOGGER.error("转让价格失败", e);
			throw e;
		} catch (Exception e) {
			LOGGER.error("转让价格", e);
			throw new PontusServiceException("转让价格失败", e);
		}
	}


	/**
	 * 查询受让资产记录
	 * 
	 * @Author zy
	 * @Company:
	 * @Create Time: 2016年5月13日 下午2:22:39
	 * @param req
	 * @return
	 */
	@Path("/listAssigneeRecord")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@UserLog(remark = "查询受让记录")
	public AssigneeRecordListResp listAssigneeRecord(@BeanParam @Valid AssigneeRecordListReq req) {
		LOGGER.debug("查询受让记录请求参数【" + req + "】");
		AssigneeRecordListResp resp = new AssigneeRecordListResp();
		try {

			// 查询用户
			String customerId = customerService.getCustomerId(req.getAccessToken());
			
			//设置需要查询AssigneeWorkFlow的列表
			List<String> workFlowList = new ArrayList<String>();
			workFlowList.add(AssigneeWorkFlow.AGREE.getValue());

			//设置排序规则
			List<String> orderByList = new ArrayList<String>();
			if(!StringUtils.isEmpty(req.getOrderBy())) {
				orderByList.add(req.getOrderBy());
			}
			List<Object[]> list = assigneeService.queryByAssigneeWorkFlowAndCustomerId(Long.valueOf(customerId), null, Integer.valueOf(req.getPage()), Integer.valueOf(req.getRows()));
			List<AssigneeRecordResp> assigneeRecordRespList = new ArrayList<AssigneeRecordResp>();
			if(!CollectionUtils.isEmpty(list)) {
				StringBuilder transferIds = new StringBuilder();
				//t.id,t.state,t.customer_id,t.transfer_id,t.attach_flow,t.code,t.work_flow,t.assignee_price,t1.DISCOUNT_RATE,t1.EXP_PROFIT,t1.END_DATE,t2.contract_Code,t2.principal,
				for (Iterator<Object[]> iterator = list.iterator(); iterator.hasNext();) {
					Object[] objs = (Object[]) iterator.next();
					AssigneeRecordResp assigneeRecordResp = new AssigneeRecordResp();
					assigneeRecordResp.setAssigneeId(Long.valueOf(String.valueOf(objs[0])));
					assigneeRecordResp.setQuoteContractId(Long.valueOf(String.valueOf(objs[15])));
					assigneeRecordResp.setContractCode(String.valueOf(objs[11]));
					assigneeRecordResp.setPrincipal(new BigDecimal(String.valueOf(objs[12])));
					//债权价值
					assigneeRecordResp.setCreditorRight(commonService.extValue(assigneeRecordResp.getPrincipal().add(new BigDecimal(String.valueOf(objs[14])))));
					assigneeRecordResp.setAssigneeWorkFlow(String.valueOf(objs[6]));
					assigneeRecordResp.setDiscountRate(new BigDecimal(String.valueOf(objs[8])));
					assigneeRecordResp.setExpectedReturn(new BigDecimal(String.valueOf(objs[9])));
					//受让价格
					assigneeRecordResp.setActualPrice(new BigDecimal(String.valueOf(objs[7])));
					assigneeRecordResp.setRemainderDate(String.valueOf(commonService.leftTermForTransfer(new Date(((Timestamp)(objs[10])).getTime()))));
					assigneeRecordResp.setTransferId(Long.valueOf(String.valueOf(objs[3])));
					
					transferIds.append(String.valueOf(objs[3])).append(",");
					assigneeRecordRespList.add(assigneeRecordResp);
				}
				transferIds.setLength(transferIds.length()-1);
				setCountForAssignee(assigneeRecordRespList,assigneeService.countByTransferIds(transferIds.toString()));
				
			}
			
			resp.setAssigneeRecordRespList(assigneeRecordRespList);
			return resp;
		} catch (PontusServiceException e) {
			LOGGER.error("查询受让记录失败", e);
			throw e;
		} catch (Exception e) {
			LOGGER.error("查询受让记录", e);
			throw new PontusServiceException("查询受让记录失败", e);
		}
	}
	
	/**
	 * 查询转让资产记录
	 * 
	 * @Author zy
	 * @Company:
	 * @Create Time: 2016年5月13日 下午2:22:39
	 * @param req
	 * @return
	 */
	@Path("/listTransferRecord")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@UserLog(remark = "查询转让记录")
	public TranferRecordListResp listTransferRecord(@BeanParam @Valid TransferRecordListReq req) {
		LOGGER.debug("查询转让记录请求参数【" + req + "】");
		TranferRecordListResp resp = new TranferRecordListResp();
		try {

			// 查询用户
			String customerId = customerService.getCustomerId(req.getAccessToken());
			
			//设置需要查询QuoteContractWorkFlow的列表
			List<QuoteContractWorkFlow> workFlowList = new ArrayList<QuoteContractWorkFlow>();
			workFlowList.add(QuoteContractWorkFlow.RENEWED);
			
			//设置需要查询TransferFlow的列表,除了已取消其它都要加进来
			List<TransferFlow> transferFlowList = new ArrayList<TransferFlow>();
			transferFlowList.add(TransferFlow.PENDING);
			transferFlowList.add(TransferFlow.TRADING);
			transferFlowList.add(TransferFlow.ENSURE);
			transferFlowList.add(TransferFlow.REJECT);
			transferFlowList.add(TransferFlow.SUCCESS);

			//设置排序规则
			List<String> orderByList = new ArrayList<String>();
			if(!StringUtils.isEmpty(req.getOrderBy())) {
				orderByList.add(req.getOrderBy());
			}
			
			List<Transfer> transfers = transferService.queryTransfer(Long.valueOf(customerId),transferFlowList, null, orderByList, Enums.TransferState.VALID.getValue(), Integer.valueOf(req.getPage()), Integer.valueOf(req.getRows()));
			List<TranferRecordResp> tranferRecordRespList = new ArrayList<TranferRecordResp>();
			if(!CollectionUtils.isEmpty(transfers)) {
				StringBuilder transferIds = new StringBuilder();
				for (Iterator<Transfer> iterator = transfers.iterator(); iterator.hasNext();) {
					Transfer transfer = (Transfer) iterator.next();
					TranferRecordResp tranferRecordResp = new TranferRecordResp(); 
					tranferRecordResp.setTransferId(transfer.getId());
					tranferRecordResp.setQuoteContractId(transfer.getQuoteContract().getId());
					tranferRecordResp.setContractCode(transfer.getQuoteContract().getContractCode());
					tranferRecordResp.setPrincipal(transfer.getQuoteContract().getOriginalContract().getPrincipal());
					//债权价值
					tranferRecordResp.setCreditorRight(commonService.extValue(tranferRecordResp.getPrincipal().add(transfer.getQuoteContract().getOriginalContract().getUnPayedInterest())));
					tranferRecordResp.setTransferWorkFlow(transfer.getWorkFlow());
					tranferRecordResp.setDiscountRate(transfer.getDiscountRate());
					tranferRecordResp.setExpectedReturn(transfer.getExpProfit());
					tranferRecordResp.setActualPrice(transfer.getActualPrice());
					tranferRecordResp.setRemainderDate(String.valueOf(commonService.leftTermForTransfer(transfer.getEndDate())));
					tranferRecordRespList.add(tranferRecordResp);
					
					transferIds.append(transfer.getId()).append(",");
				}
				transferIds.setLength(transferIds.length()-1);
				
				setCount(tranferRecordRespList,assigneeService.countByTransferIds(transferIds.toString()));
			}
			
			resp.setTranferRecordRespList(tranferRecordRespList);
			return resp;
		} catch (PontusServiceException e) {
			LOGGER.error("查询转让记录失败", e);
			throw e;
		} catch (Exception e) {
			LOGGER.error("查询转让记录", e);
			throw new PontusServiceException("查询转让记录失败", e);
		}
	}
	
	
	/**
	 * 查询可转让资产
	 * 
	 * @Author zy
	 * @Company:
	 * @Create Time: 2016年5月13日 下午2:22:39
	 * @param req
	 * @return
	 */
	@Path("/listAsset")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@UserLog(remark = "查询可转让资产")
	public AssetTranferListResp listQuoteContract(@BeanParam @Valid AssetTranferReq req) {
		LOGGER.debug("查询可转让资产请求参数【" + req + "】");
		AssetTranferListResp resp = new AssetTranferListResp();
		try {

			// 查询用户
			String customerId = customerService.getCustomerId(req.getAccessToken());

			//设置需要查询QuoteContractWorkFlow的列表
			List<QuoteContractWorkFlow> workFlowList = new ArrayList<QuoteContractWorkFlow>();
			workFlowList.add(QuoteContractWorkFlow.RENEWED);

			//设置排序规则
			List<String> orderByList = new ArrayList<String>();
			if(!StringUtils.isEmpty(req.getOrderBy())) {
				orderByList.add(req.getOrderBy());
			}
			List<QuoteContract> quoteContractList = quoteContractService.queryQuoteContract(Long.valueOf(customerId), workFlowList, orderByList, Integer.valueOf(req.getPage()), Integer.valueOf(req.getRows()));
			List<AssetTranferResp> assetTranferRespList = new ArrayList<AssetTranferResp>();
			if (!CollectionUtils.isEmpty(quoteContractList)) {
				for (Iterator<QuoteContract> iterator = quoteContractList.iterator(); iterator.hasNext();) {
					QuoteContract quoteContract = (QuoteContract) iterator.next();
					AssetTranferResp assetTranferResp = new AssetTranferResp();
					assetTranferResp.setQuoteContractId(quoteContract.getId());
					assetTranferResp.setContractCode(quoteContract.getOriginalContract().getContractCode());
					assetTranferResp.setPlatform(quoteContract.getOriginalContract().getPlatform());
					assetTranferResp.setPrincipal(quoteContract.getOriginalContract().getPrincipal());
					assetTranferResp.setPaybackType(quoteContract.getOriginalContract().getPaybackType());
					assetTranferResp.setIsTransfer(Enums.QuoteContractWorkFlow.RENEWED.getValue().equals(quoteContract.getWorkFlow()) ? "1" : "");
					// 债权价值
					assetTranferResp.setCreditorRight(commonService.extValue(assetTranferResp.getPrincipal().add(quoteContract.getOriginalContract().getUnPayedInterest())));
					// 预期收益
					assetTranferResp.setExpectedReturn(commonService.expectProfit(assetTranferResp.getPrincipal().add(quoteContract.getOriginalContract().getUnPayedInterest())));
					
					assetTranferRespList.add(assetTranferResp);
				}
			}
			resp.setAssetTranferRespList(assetTranferRespList);
			return resp;
		} catch (PontusServiceException e) {
			LOGGER.error("查询可转让资产失败", e);
			throw e;
		} catch (Exception e) {
			LOGGER.error("查询可转让资产", e);
			throw new PontusServiceException("查询可转让资产失败", e);
		}
	}

	/**
	 * 资产展期
	 * 
	 * @Author zy
	 * @Company:
	 * @Create Time: 2016年5月7日 上午9:08:57
	 * @param req
	 * @return
	 */
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@UserLog(remark = "资产展期查询列表")
	public AssetRenewalListResp queryList(@BeanParam @Valid AssetRenewalReq req) {
		LOGGER.debug("资产展期请求参数【" + req + "】");
		AssetRenewalListResp resp = new AssetRenewalListResp();
		try {
			QuoteContractForm contractForm = new QuoteContractForm();
			contractForm.setCustomerId(Long.valueOf(customerService.getCustomerId(req.getAccessToken())));
			contractForm.setPage(req.getCurrentPage());
			contractForm.setRows(req.getPageSize());
			List<QuoteContractWorkFlow> workFlowList = new ArrayList<QuoteContractWorkFlow>();
			workFlowList.add(Enums.QuoteContractWorkFlow.PASSED);
			contractForm.setWorkFlowList(workFlowList);
			resp.setAssetRenewalList(quoteContractService.queryOriginalContractList(contractForm,Enums.QuoteContracState.INVALID.getValue()));

		} catch (Exception e) {
			LOGGER.error("资产展期异常", e);
			throw new PontusServiceException("资产展期异常", e);
		}
		return resp;
	}

	/**
	 * 资产展期
	 * 
	 * @Author zy
	 * @Company:
	 * @Create Time: 2016年5月7日 上午9:08:57
	 * @param req
	 * @return
	 */
	@GET
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@UserLog(remark = "单个资产展期查询")
	public AssetRenewalResp getOne(@PathParam("id") Long id, @QueryParam("accessToken") String accessToken) {
		LOGGER.debug("资产展期详情请求参数【id：" + id + "，accessToken：" + accessToken + "】");
		AssetRenewalResp resp = new AssetRenewalResp();
		try {
			resp.setAssetRenewal(quoteContractService.getOne(id, Long.valueOf(customerService.getCustomerId(accessToken))));
		} catch (Exception e) {
			LOGGER.error("资产展期异常", e);
			throw new PontusServiceException("资产展期异常", e);
		}
		return resp;
	}

	
	private void setCountForAssignee(List<AssigneeRecordResp> tranferRecordRespList,Map<Long,String> countMap) {
		for (Iterator<AssigneeRecordResp> iterator = tranferRecordRespList.iterator(); iterator.hasNext();) {
			AssigneeRecordResp assigneeRecordResp = (AssigneeRecordResp) iterator.next();
			if(StringUtils.isEmpty(countMap.get(assigneeRecordResp.getTransferId()))) {
				assigneeRecordResp.setBespeakCount(0);
			}else {
				assigneeRecordResp.setBespeakCount(Integer.valueOf(countMap.get(assigneeRecordResp.getTransferId())));
			}
		}
	}
	
	private void setCount(List<TranferRecordResp> tranferRecordRespList,Map<Long,String> countMap) {
		for (Iterator<TranferRecordResp> iterator = tranferRecordRespList.iterator(); iterator.hasNext();) {
			TranferRecordResp tranferRecordResp = (TranferRecordResp) iterator.next();
			if(StringUtils.isEmpty(countMap.get(tranferRecordResp.getTransferId()))) {
				tranferRecordResp.setBespeakCount(0);
			}else {
				tranferRecordResp.setBespeakCount(Integer.valueOf(countMap.get(tranferRecordResp.getTransferId())));
			}
		}
	}
	
}
