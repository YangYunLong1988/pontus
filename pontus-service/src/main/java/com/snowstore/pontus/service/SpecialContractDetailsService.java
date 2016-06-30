package com.snowstore.pontus.service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.snowstore.pontus.domain.OriginalContract;
import com.snowstore.pontus.domain.SpecialContract;
import com.snowstore.pontus.domain.SpecialDetail;
import com.snowstore.pontus.repository.SpecialContractRepository;
import com.snowstore.pontus.repository.SpecialDetailRepository;
import com.snowstore.pontus.service.vo.Result;

@Service
public class SpecialContractDetailsService {
	public static final Logger LOGGER = LoggerFactory.getLogger(SpecialContractDetailsService.class);
	@Autowired
	private SpecialDetailRepository specialDetailRepository;
	@Autowired
	private SpecialContractService specialContractService;
	@Autowired
	private OriginalContractService originalContractService;

	@Autowired
	private SpecialContractRepository specialContractRepository;

	/**
	 * 根据特殊兑付id分页查询
	 * 
	 * @date 2016年6月17日
	 * @param spcialId
	 * @param pageable
	 * @return
	 */
	public Page<SpecialDetail> findBySpecialContract(Long spcialId, Pageable pageable) {
		Page<SpecialDetail> page = specialDetailRepository.findBySpecialContract(specialContractRepository.findOne(spcialId), pageable);
		LOGGER.info("特殊兑付明细,共{}条", page.getTotalElements());
		return page;
	}

	/**
	 * 添加兑付明细
	 * 
	 * @date 2016年6月17日
	 * @param specialDetail
	 * @return
	 */
	public void addSpecialContractDetails(SpecialDetail specialDetail) {
		specialDetailRepository.save(specialDetail);
	}

	/**
	 * 删除特殊兑付
	 * 
	 * @date 2016年6月17日
	 * @param id
	 */
	public void deleteSpecialContractDetails(Long id) {
		specialDetailRepository.delete(id);
	}

	public List<SpecialDetail> findByContractId(Long id) {
		OriginalContract originalContract = originalContractService.get(id);
		SpecialContract specialContract = specialContractService.findByOriginalContract(originalContract);
		if (null == specialContract) {
			return null;
		}
		return this.findBySpecialContractId(specialContract.getId());
	}

	public List<SpecialDetail> findBySpecialContractId(Long id) {
		return specialDetailRepository.findBySpecialContractId(id);
	}

	/**
	 * 添加特殊兑付详情
	 * 
	 * @date 2016年6月20日
	 * @param originalContractId
	 * @param listDetail
	 */
	public Result<String> addSpecialDetailsService(Long originalContractId, String listDetail, Map<String, String> emailMap) {
		Result<String> result = new Result<String>(Result.Type.FAILURE, "修改失败");
		try {
			OriginalContract originalContract = originalContractService.get(originalContractId);
			if (null != originalContract) {
				SpecialContract sc = specialContractService.findByOriginalContract(originalContract);
				if (null == sc) {// 添加特殊兑付
					if (!specialContractService.newSpecialContractProcess(originalContract, emailMap)) {
						result.addMessage("请检查合同是否有效！");// 修改状态失败
						return result;
					}
					sc = new SpecialContract();
					sc.setOriginalContract(originalContract);
					specialContractRepository.save(sc);
				}
				List<SpecialDetail> sdList = JSON.parseArray(listDetail, SpecialDetail.class);
				for (SpecialDetail sd : sdList) {
					sd.setSpecialContract(sc);
					specialDetailRepository.save(sd);
				}
				result.addMessage(0, sc.getId().toString());
				result.setType(Result.Type.SUCCESS);
			} else {
				result.addMessage("请检查合同是否存在！");// 修改状态失败
				return result;
			}
		} catch (Exception e) {
			LOGGER.error("添加特殊兑付明细错误:" + e);
		}
		return result;
	}

}
