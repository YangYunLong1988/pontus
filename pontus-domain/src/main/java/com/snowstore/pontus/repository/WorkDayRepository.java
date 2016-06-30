package com.snowstore.pontus.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.snowstore.pontus.domain.WorkDay;

@Repository
public interface WorkDayRepository extends PagingAndSortingRepository<WorkDay, Long>, JpaSpecificationExecutor<WorkDay> {

	WorkDay findOneByDay(Date day);

	@Query("select w.day from WorkDay w where w.isWork = false and w.day > ?1 order by w.day")
	List<String> findOffDay(Date startDate);

	@Query("select w.day from WorkDay w where w.day between ?1 and ?2 and w.isWork = true order by w.day")
	List<Date> findWorkDayOfBetween(Date start, Date end);

	List<WorkDay> findByIsWork(Boolean isWork);
}
