package com.snowstore.pontus.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.snowstore.pontus.domain.ExtendFileImage;


@Repository
public interface ExtendFileImageRepository extends PagingAndSortingRepository<ExtendFileImage, Long>, JpaSpecificationExecutor<ExtendFileImage> {
	
	@Query("from ExtendFileImage a where a.pdfFile.id =?1 and a.pageNo = ?2")
	ExtendFileImage findByPdfFileAndPageNo(Long enewalAgreementId , Integer pageNo);
	
	@Modifying
	@Query("delete from ExtendFileImage a where a.pdfFile.id =?1")
	int deleleByEnewalAgreementId(Long enewalAgreementId);
}
