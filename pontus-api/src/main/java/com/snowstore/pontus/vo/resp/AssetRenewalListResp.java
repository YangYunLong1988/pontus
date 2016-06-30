package com.snowstore.pontus.vo.resp;

import java.util.List;

import com.snowstore.pontus.service.vo.AssetRenewal;
import com.snowstore.pontus.vo.ResponseVo;

public class AssetRenewalListResp extends ResponseVo {

	private List<AssetRenewal> assetRenewalList;
	private int totalPages;

	public List<AssetRenewal> getAssetRenewalList() {
		return assetRenewalList;
	}

	public void setAssetRenewalList(List<AssetRenewal> assetRenewalList) {
		this.assetRenewalList = assetRenewalList;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}


}
