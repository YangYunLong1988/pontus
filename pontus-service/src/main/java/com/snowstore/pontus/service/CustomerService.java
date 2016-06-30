package com.snowstore.pontus.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.SetJoin;

import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.snowstore.hera.connector.vo.tpp.D01000004_R;
import com.snowstore.pontus.domain.AdminUser;
import com.snowstore.pontus.domain.BankInfo;
import com.snowstore.pontus.domain.Customer;
import com.snowstore.pontus.domain.ValidateCode.Scene;
import com.snowstore.pontus.domain.ValidateCode.System;
import com.snowstore.pontus.enums.Enums;
import com.snowstore.pontus.enums.Enums.AssigneeWorkFlow;
import com.snowstore.pontus.enums.Enums.QuoteContractWorkFlow;
import com.snowstore.pontus.repository.CustomerRepository;
import com.snowstore.pontus.repository.QuoteContractRepository;
import com.snowstore.pontus.service.constants.TppConstant;
import com.snowstore.pontus.service.datagram.EsbHelper;
import com.snowstore.pontus.service.utils.TokenUtil;
import com.snowstore.pontus.service.vo.BankInfoForm;
import com.snowstore.pontus.service.vo.CustomerQueryForm;
import com.snowstore.pontus.service.vo.CustomerRegisterFrom;
import com.snowstore.pontus.service.vo.IdentityForm;
import com.snowstore.pontus.service.vo.ResetPasswordForm;
import com.snowstore.pontus.service.vo.RetrievePasswordForm;

@Service
@Transactional
public class CustomerService {

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerService.class);

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private BankInfoService bankInfoService;

	@Autowired(required = false)
	private PasswordEncoder bcryptEncoder;

	@Autowired
	QuoteContractRepository contractRepository;

	@Autowired
	private StringRedisTemplate template;

	@Autowired
	private MemberAttachmentService memberAttachmentService;

	@Autowired
	private EsbHelper esbHelper;

	@Value("${token.timeout:30}")
	private long timeout;

	@Autowired
	Mapper dozerBeanMapper;

	@PersistenceContext
	private EntityManager em;

	@Autowired
	private AdminUserService adminUserService;

	public Customer get(Long id) {
		return customerRepository.findOne(id);
	}

	public Page<Customer> findAll(Specification<Customer> spc, Pageable pageable, CustomerQueryForm customerQueryForm) {
		return customerRepository.findAll(spc, pageable);
	}

	public Specification<Customer> buildSpecification(final CustomerQueryForm customerQueryForm) {
		return new Specification<Customer>() {
			@Override
			public Predicate toPredicate(Root<Customer> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<Predicate>();
				if (null != customerQueryForm.getWorkFlow()) {
					list.add(cb.equal(root.<String> get("workFlow"), customerQueryForm.getWorkFlow()));
				}

				if (null != customerQueryForm.getStatus()) {
					list.add(cb.equal(root.<String> get("status"), customerQueryForm.getStatus()));
				}

				if (null != customerQueryForm.getRegistTime()) {
					list.add(cb.between(root.<Date> get("createdDate"), customerQueryForm.getRegistFrom(), customerQueryForm.getRegistTo()));
				}

				if (null != customerQueryForm.getQuery()) {
					list.add(cb.or(cb.like(root.<String> get("phone"), '%' + customerQueryForm.getQuery() + '%'), cb.like(root.<String> get("idCardName"), '%' + customerQueryForm.getQuery() + '%'),
							cb.like(root.<String> get("idCardAccount"), '%' + customerQueryForm.getQuery() + '%')));
				}
				if (!AdminUser.Role.ADMIN.name().equals(adminUserService.getUserDetails().getRole())) {
					// 过滤平台用户
					SetJoin<Object, Object> join = root.joinSet("quoteContractSet", JoinType.INNER);
					List<String> platforms = adminUserService.getUserPlatforms();
					list.add(join.<String> get("platform").in(platforms));
					query.distinct(true);
				}
				return cb.and(list.toArray(new Predicate[list.size()]));
			}

		};

	}

	public Customer findByPhone(String phone) {
		if (phone == null) {
			return null;
		}
		return customerRepository.findByPhone(phone);
	}

	public boolean authenticationCustomer(String username, String password) {
		// 手机号不存在
		if (null == customerRepository.findByPhone(username)) {
			throw new PontusServiceException("手机号不存在!");
		}

		Customer customer = customerRepository.findByUsername(username);
		return bcryptEncoder.matches(password, customer.getPassword());
	}

	public boolean authenticationCustomer(Long id, String password) {
		Customer customer = get(id);
		return bcryptEncoder.matches(password, customer.getPassword());
	}

	public String loginCustomer(String username, String password) {
		if (!authenticationCustomer(username, password)) {
			throw new PontusServiceException("密码错误!");
		}
		Customer customer = customerRepository.findByUsername(username);
		if (!customer.getStatus().equals(Enums.CustomerState.VALID.getValue())) {
			throw new PontusServiceException("用户已被冻结!");
		}
		String token = TokenUtil.genToken(username, customer.getId());
		template.opsForValue().set(token, customer.getId() + "", timeout, TimeUnit.MINUTES);
		return token;

	}

	public String getCustomerId(String accessToken) {
		return template.opsForValue().get(accessToken);
	}

	/**
	 * 修改密码
	 * 
	 * @param id
	 *            Customer ID
	 * @param newPassword
	 *            新密码
	 */
	public void changePassword(Long id, String newPassword) {
		Customer customer = get(id);
		customer.setPassword(bcryptEncoder.encode(newPassword));
		customerRepository.save(customer);
	}

	/**
	 * 身份认证
	 * 
	 * @param id
	 *            Customer ID
	 * @param identityForm
	 *            认证信息
	 */
	public void identityAuthentication(Long id, IdentityForm identityForm) {
		Customer customer = get(id);
		// 保存银行卡信息
		BankInfoForm bankInfoForm = new BankInfoForm(identityForm.getBankName(), identityForm.getBankAccount(), identityForm.getBankSubbranch(), identityForm.getBankProvince(), identityForm.getBankCity());
		customer.getBankSet().add(bankInfoService.save(bankInfoForm));
		customer.setIdCardName(identityForm.getIdCardName());
		customer.setIdCardAccount(identityForm.getIdCardAccount());
		customer.setResidenceProvince(identityForm.getResidenceProvince());
		customer.setResidenceCity(identityForm.getResidenceCity());

		// 绑定证件（身份证，名片）
		memberAttachmentService.bind(identityForm.getIdCardImageA(), customer);
		memberAttachmentService.bind(identityForm.getIdCardImageB(), customer);
		memberAttachmentService.bind(identityForm.getVisitCardImage(), customer);

		customerRepository.save(customer);
	}

	public void regist(CustomerRegisterFrom form, boolean flag) {
		if (null != customerRepository.findByPhone(form.getPhone())) {
			throw new PontusServiceException("此手机号已注册!");
		}
		if (flag) {
			validPhoneCode(form.getPhone(), form.getScene(), form.getSystem(), form.getValidateCode());
		}
		Customer customer = new Customer();
		dozerBeanMapper.map(form, customer);
		customer.setUsername(form.getPhone());
		customer.setPassword(bcryptEncoder.encode(form.getPassword()));
		customer.setStatus(Enums.CustomerState.VALID.getValue());
		customer.setWorkFlow(Enums.CustomerWorkFlow.REGISTER.getValue());
		customerRepository.save(customer);
	}

	public String registAndLogin(CustomerRegisterFrom customerFrom) {
		regist(customerFrom, true);
		return loginCustomer(customerFrom.getUsername(), customerFrom.getPassword());
	}

	public void updateCustomerWorkFlow(Long id, String workFlow) {
		Customer customer = get(id);
		customer.setWorkFlow(workFlow);
		customerRepository.save(customer);
	}

	public void logout(String token) {
		try {
			template.delete(token);
		} catch (Exception e) {
			throw new PontusServiceException();
		}
	}

	public BigDecimal amount(Long customerId) {
		StringBuilder sb = new StringBuilder();
		sb.append("select sum(total) from ( ").append("select b.TOTAL_AMOUNT as total,a.work_flow from pontus_quote_contract a ").append("inner join pontus_original_contract b on a.contract_id = b.ID ")
				.append("where a.CUSTOMER_ID = " + customerId + " and a.work_flow not in('" + QuoteContractWorkFlow.NEW.getValue() + "','" + QuoteContractWorkFlow.PENDING.getValue() + "','" + QuoteContractWorkFlow.REJECT.getValue() + "','"
						+ QuoteContractWorkFlow.SUCCESS.getValue() + "') ")
				.append("union all ").append("select b.exp_Profit as total,a.work_flow  from pontus_assignee a ").append("inner join PONTUS_TRANSFER b on a.TRANSFER_ID = b.ID ").append("inner join pontus_quote_contract c on b.quote_id = c.id ")
				.append("inner join pontus_original_contract d on c.CONTRACT_ID = d.id ").append("where a.CUSTOMER_ID = " + customerId + " and a.work_flow in('" + AssigneeWorkFlow.SUCCESS.getValue() + "') ").append(")");

		// String sql = "select nvl(sum(nvl(principal,0))+sum(nvl(profit,0)),0)
		// from pontus_quote_contract t where t.work_Flow='" +
		// Enums.QuoteContractWorkFlow.PASSED.getValue() +
		// "' and t.CUSTOMER_ID = " + customerId;
		Object obj = em.createNativeQuery(sb.toString()).getSingleResult();
		if (null == obj) {
			return new BigDecimal(0);
		}
		return new BigDecimal(String.valueOf(obj));
	}

	/**
	 * 修改会员状态
	 * 
	 * @date 2016年5月6日
	 * @param id
	 * @param status
	 * @return boolean
	 */
	public boolean changeStatus(Long id, String status) {
		boolean action = false;
		try {
			Customer customer = get(id);
			customer.setStatus(status);
			customerRepository.save(customer);
			action = true;
		} catch (Exception e) {
			LOGGER.error("修改会员状态:" + e);
		}
		return action;
	}

	public void retrievePassword(RetrievePasswordForm form) {
		validPhoneCode(form.getPhone(), form.getScene(), form.getSystem(), form.getValidateCode());
		Customer customer = customerRepository.findByUsername(form.getPhone());
		if (null == customer)
			throw new PontusServiceException("重置密码找不到用户");
		changePassword(customer.getId(), form.getNewPassword());
	}

	public void validPhoneCode(String phone, Scene scene, System system, String validateCode) {
		String key = phone + ":" + scene.getValue() + ":" + system.getValue();
		if (!template.hasKey(key)) {
			throw new PontusServiceException("验证码不正确!");
		}
		if (!template.opsForValue().get(key).equals(validateCode)) {
			throw new PontusServiceException("验证码不正确!");
		}
	}

	public void resetPassword(ResetPasswordForm req) {
		if (null == req.getCustomerId())
			throw new PontusServiceException("重置密码找不到用户");
		Customer customer = get(req.getCustomerId());
		if (null == customer) {
			throw new PontusServiceException("重置密码找不到用户");
		}
		if (authenticationCustomer(customer.getUsername(), req.getOldPassword())) {
			changePassword(req.getCustomerId(), req.getNewPassword());
		} else {
			throw new PontusServiceException("输入密码不正确");
		}
	}

	public Customer changeEmail(String email, Customer customer) {
		customer.setEmail(email);
		return customerRepository.save(customer);
	}

	public Customer changeAdress(String provice, String city, Customer customer) {
		customer.setResidenceProvince(provice);
		customer.setResidenceCity(city);
		return customerRepository.save(customer);
	}

	public void save(Customer customer) {
		customerRepository.save(customer);
	}

	public void checkCustomerAndBankInfo(Customer customer, BankInfo bankInfo) {
		// 向TPP身份认证
		D01000004_R resp = esbHelper.sendTpp01000004(customer, bankInfo);
		// //TODO测试
		// D01000004_R resp = new D01000004_R();
		// resp.setOperateCode(TppConstant.ResponseCode.SUCCESS);
		LOGGER.debug("TPP验证账户返回码【" + resp.getOperateCode() + "】，返回信息【" + resp.getMemo() + "】");
		if (TppConstant.ResponseCode.SUCCESS.equals(resp.getOperateCode())) {
			if (!ifGrown_up(customer.getIdCardAccount())) {
				throw new PontusServiceException("请使用18周岁以上的居民身份证！");
			}

			bankInfo.setStatus(Enums.BankInfoState.PASSED.getValue());
			customer.setWorkFlow(Enums.CustomerWorkFlow.PASSED.getValue());
		} else {
			LOGGER.error("TPP身份认证失败[" + resp.getMemo() + "]");
			throw new PontusServiceException("认证失败！请填写正确身份认证信息重新认证。如有疑问，请联系相应平台客服。谢谢！");
		}
	}

	public boolean ifGrown_up(String idCardAccount) {
		if (idCardAccount.length() < 18) {
			idCardAccount = idCardAccount.substring(0, 6) + "19" + idCardAccount.substring(6);
		}
		int year = Integer.parseInt(idCardAccount.substring(6, 10));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date update = null;
		try {
			update = sdf.parse(String.valueOf(year + 18) + idCardAccount.substring(10, 14));
		} catch (ParseException e) {

		}
		Date today = new Date();
		return today.after(update);
	}

	public List<Customer> findByIdCard(String idcard) {
		return customerRepository.findByIdCard(idcard);
	}
}
