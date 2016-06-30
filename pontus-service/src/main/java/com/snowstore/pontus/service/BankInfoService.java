package com.snowstore.pontus.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.snowstore.hera.connector.vo.tpp.D01000004_R;
import com.snowstore.pontus.domain.Assignee;
import com.snowstore.pontus.domain.BankInfo;
import com.snowstore.pontus.domain.Customer;
import com.snowstore.pontus.enums.Enums;
import com.snowstore.pontus.repository.AssigneeRepository;
import com.snowstore.pontus.repository.BankInfoRepository;
import com.snowstore.pontus.repository.CustomerRepository;
import com.snowstore.pontus.service.constants.TppConstant;
import com.snowstore.pontus.service.datagram.EsbHelper;
import com.snowstore.pontus.service.vo.BankInfoForm;

@Service
@Transactional
public class BankInfoService {

	private static final Logger LOGGER = LoggerFactory.getLogger(BankInfoService.class);

	@Autowired
	private BankInfoRepository bankInfoRepository;
	@Autowired
	private AssigneeRepository assigneeRepository;
	@Autowired
	private IdAuthenticationService idAuthenticationService;
	@Autowired
	private CustomerRepository customerRepository;
	@Autowired
	private EsbHelper esbHelper;
	@PersistenceContext
	private EntityManager em;

	public BankInfo save(BankInfoForm form) {
		BankInfo bankInfo = new BankInfo();
		try {
			BeanUtils.copyProperties(bankInfo, form);
			return bankInfoRepository.save(bankInfo);
		} catch (IllegalAccessException | InvocationTargetException e) {
			throw new PontusServiceException("保存 BankInfo 异常", e);
		}
	}

	public int countByCustomerId(Long customerId) {
		return bankInfoRepository.countByCustomerId(customerId);
	}

	@SuppressWarnings("unchecked")
	public List<BankInfo> queryByCustomerId(Long customerId, int page, int size) {
		Query q = em.createQuery("from BankInfo t order by t.id").setFirstResult((page - 1) * size).setMaxResults(size);
		return q.getResultList();
	}

	public Page<BankInfo> findAll(final Long customerId, int page, int size) {
		List<Order> orders = new ArrayList<Order>();
		orders.add(new Order(Direction.DESC, "id"));
		Sort sort = new Sort(orders);
		Pageable pageable = new PageRequest(page, size, sort);
		Specification<BankInfo> spec = new Specification<BankInfo>() {

			@Override
			public Predicate toPredicate(Root<BankInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<Predicate>();
				if (null != customerId) {
					list.add(cb.equal(root.<Customer> get("customer").<Long> get("id"), customerId));
				}
				return cb.and(list.toArray(new Predicate[list.size()]));
			}
		};
		return bankInfoRepository.findAll(spec, pageable);
	}

	public List<BankInfo> findAll(final Long customerId) {
		List<Order> orders = new ArrayList<Order>();
		orders.add(new Order(Direction.DESC, "defaulted"));
		orders.add(new Order(Direction.DESC, "id"));
		Sort sort = new Sort(orders);
		Specification<BankInfo> spec = new Specification<BankInfo>() {

			@Override
			public Predicate toPredicate(Root<BankInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<Predicate>();
				if (null != customerId) {
					list.add(cb.equal(root.<Customer> get("customer").<Long> get("id"), customerId));
				}
				return cb.and(list.toArray(new Predicate[list.size()]));
			}
		};
		return bankInfoRepository.findAll(spec, sort);
	}

	public List<BankInfo> findAllByCreateDesc(final Long customerId) {
		List<Order> orders = new ArrayList<Order>();
		orders.add(new Order(Direction.DESC, "id"));
		Sort sort = new Sort(orders);
		Specification<BankInfo> spec = new Specification<BankInfo>() {

			@Override
			public Predicate toPredicate(Root<BankInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<Predicate>();
				if (null != customerId) {
					list.add(cb.equal(root.<Customer> get("customer").<Long> get("id"), customerId));
				}
				return cb.and(list.toArray(new Predicate[list.size()]));
			}
		};
		return bankInfoRepository.findAll(spec, sort);
	}

	public void addBankInfo(BankInfo bankInfo, Long customerId, String userName) {
		// 校验此卡的名字是否跟登录用户名的名字一致
		Customer customer = customerRepository.findOne(customerId);
		if (!customer.getIdCardName().equals(userName)) {
			LOGGER.error("首次实名身份认证的姓名【" + customer.getIdCardName() + "】，此银行卡的姓名【" + userName + "】");
			throw new PontusServiceException("此银行卡与首次认证的姓名不一致!");
		}

		// 校验此银行卡之前是否被新增成功过
		if (null != bankInfoRepository.findByNameAndAccount(bankInfo.getName(), bankInfo.getAccount())) {
			LOGGER.error("根据此银行名字【" + bankInfo.getName() + "】与卡号【" + bankInfo.getAccount() + "】查询已经有此记录！");
			throw new PontusServiceException("此银行卡已被新增过!");
		}

		// 实名身份认证
		D01000004_R resp = esbHelper.sendTpp01000004(customer, bankInfo);
		// //TODO 测试
		// resp.setOperateCode(TppConstant.ResponseCode.SUCCESS);

		LOGGER.debug("TPP验证账户返回码【" + resp.getOperateCode() + "】，返回信息【" + resp.getMemo() + "】");
		if (TppConstant.ResponseCode.SUCCESS.equals(resp.getOperateCode())) {
			if (!idAuthenticationService.ifGrown_up(customer.getIdCardAccount())) {
				throw new PontusServiceException("请使用18周岁以上的居民身份证！");
			}

			bankInfo.setStatus(Enums.BankInfoState.PASSED.getValue());
		} else {
			LOGGER.error("TPP身份认证失败[" + resp.getMemo() + "]");
			throw new PontusServiceException("认证失败！请填写正确身份认证信息重新认证。如有疑问，请联系相应平台客服。谢谢！");
		}

		// 保存
		bankInfo.setCustomer(customer);
		bankInfoRepository.save(bankInfo);
	}

	public void delBankInfo(Long bankInfoId, Long customerId) {
		// 校验是否初次认证的卡，
		Customer customer = customerRepository.findOne(customerId);
		BankInfo firstBankInfo = customer.getBankInfo();
		if (null == firstBankInfo) {
			LOGGER.error("首次认证的银行卡为空");
		}
		// 校验此用户对应这张银行卡在应受让单里面有没有【回访取消】记录
		if (!CollectionUtils.isEmpty(assigneeRepository.findByCustomerIdAndBankIdAndNotInWorkFlow(customerId, Enums.AssigneeWorkFlow.REJECT.getValue(), bankInfoId))) {
			LOGGER.error("此用户【" + customerId + "】对应这张银行卡【" + bankInfoId + "】在应受让单里面有存在有非【回访取消】有记录");
			throw new PontusServiceException("不能删除预约成功使用过的银行卡!");
		}

		BankInfo bankInfo = bankInfoRepository.findOne(bankInfoId);
		if (firstBankInfo.getName().equals(bankInfo.getName()) && firstBankInfo.getAccount().equals(bankInfo.getAccount())) {
			LOGGER.error("首次认证的银行名称跟卡号与现在删除的一样，不能删除！");
			throw new PontusServiceException("不能删除首次认证的银行卡!");
		}

		// 删除银行卡
		// bankInfoRepository.delete(bankInfo);
		em.createNativeQuery("delete from pontus_bank_info t where t.id = '" + bankInfoId + "'").executeUpdate();

		// 更新【回访取消 的受让单对应的银行卡id 为空】
		List<Assignee> updateList = assigneeRepository.findByCustommerAndWorkFlow(customerId, Enums.AssigneeWorkFlow.REJECT.getValue());
		if (!CollectionUtils.isEmpty(updateList)) {
			for (Iterator<Assignee> iterator = updateList.iterator(); iterator.hasNext();) {
				Assignee assignee = iterator.next();
				assignee.setBankInfo(null);
				assigneeRepository.save(assignee);
			}
		}
	}

	public void save(BankInfo bank) {
		bankInfoRepository.save(bank);
	}

}
