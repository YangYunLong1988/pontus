package com.snowstore.pontus.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.snowstore.pontus.domain.Dictionary;

@Repository
public interface DictionaryRepository extends PagingAndSortingRepository<Dictionary, Long>, JpaSpecificationExecutor<Dictionary> {

}
