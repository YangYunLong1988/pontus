package com.snowstore.pontus.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.domain.Specification;

import com.snowstore.pontus.domain.WorkDay;
import com.snowstore.pontus.repository.WorkDayRepository;
import com.snowstore.pontus.service.utils.DateUtils;

public class WorkDayService {
	private static final Logger LOGGER = LoggerFactory.getLogger(WorkDayService.class);

	@Autowired
	private WorkDayRepository workDayDao;

	@Cacheable(value = "poseidonCache", key = "'workDay'+#date.getTime()")
	public WorkDay findOneByDay(Date date) {
		return workDayDao.findOneByDay(date);
	}

	// 获取下一工作日
	public Date getNextWorkDay(Date date) {
		LOGGER.debug("开始查询下一个工作日");
		Date nextDate = DateUtils.addDate(date, 1);
		WorkDay workDay = findOneByDay(nextDate);
		if (workDay == null) {
			return nextDate;
		}
		if (workDay.getIsWork()) {
			return workDay.getDay();
		} else {
			return getNextWorkDay(nextDate);
		}

	}

	public Date getNextWorkDay(Date date, int nextDay) {
		Date nextDate = date;
		int count = 0;
		while (count < nextDay) {
			nextDate = DateUtils.addDate(nextDate, 1);
			WorkDay workDay = findOneByDay(nextDate);
			if (workDay.getIsWork()) {
				count++;
			}

		}
		return nextDate;
	}

	// 获取最近一工作日
	public Date getLatestWorkDay(Date date) {
		Date nextDate = DateUtils.addDate(date, -1);
		return getNextWorkDay(nextDate);
	}

	// 获取上一个工作日
	public Date getPreWorkDay(Date date) {
		Date nextDate = DateUtils.addDate(date, -1);
		WorkDay workDay = findOneByDay(nextDate);
		if (workDay == null) {
			return nextDate;
		}
		if (workDay.getIsWork()) {
			return workDay.getDay();
		} else {
			return getPreWorkDay(nextDate);
		}

	}

	public List<String> findOffDay(Date startDate) {
		return workDayDao.findOffDay(startDate);
	}

	public List<Date> findWorkDayOfBetween(Date start, Date end) {
		return workDayDao.findWorkDayOfBetween(start, end);
	}

	// 判断是否是工作日
	public boolean isWorkDay(Date date) {
		boolean flag = false;
		WorkDay workDay = findOneByDay(date);
		if (workDay == null) {
			return flag;
		} else {
			return workDay.getIsWork();
		}
	}

	/**
	 * 计算间隔天数后的起息日期
	 * 
	 * @param days
	 * @return
	 */
	public Date getNextProductValueDate(Date valueDate, int days) {
		if (days > 0) {
			for (; days > 0; days--) {
				valueDate = getNextWorkDay(valueDate);
			}
			return valueDate;
		} else {
			return DateUtils.addDate(valueDate, 0);
		}
	}

	@Cacheable(value = "poseidonCache", key = "'day'+#date.getTime()")
	public Date getUpperWorkDay(Date date) {
		Date nextDate = org.apache.commons.lang3.time.DateUtils.addDays(date, -1);
		WorkDay workDay = workDayDao.findOneByDay(nextDate);
		if (workDay.getIsWork()) {
			return workDay.getDay();
		} else {
			return getUpperWorkDay(nextDate);
		}
	}

	public List<WorkDay> findHoliday(final Date start, final Date end) {
		return workDayDao.findAll(new Specification<WorkDay>() {
			@Override
			public Predicate toPredicate(Root<WorkDay> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> p = new ArrayList<Predicate>();
				p.add(cb.isFalse(root.<Boolean> get("isWork")));
				if (start != null) {
					p.add(cb.greaterThanOrEqualTo(root.<Date> get("day"), start));
				}
				if (end != null) {
					p.add(cb.lessThanOrEqualTo(root.<Date> get("day"), end));
				}
				return cb.and(p.toArray(new Predicate[p.size()]));
			}
		});
	}
}
