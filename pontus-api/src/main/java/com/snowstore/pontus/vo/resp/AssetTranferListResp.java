package com.snowstore.pontus.vo.resp;

import java.util.List;

import com.snowstore.pontus.vo.ResponseVo;

public class AssetTranferListResp extends ResponseVo {

	private List<AssetTranferResp> assetTranferRespList;

	public List<AssetTranferResp> getAssetTranferRespList() {
		return assetTranferRespList;
	}

	public void setAssetTranferRespList(List<AssetTranferResp> assetTranferRespList) {
		this.assetTranferRespList = assetTranferRespList;
	}
}
