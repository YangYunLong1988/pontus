package com.snowstore.pontus.vo.resp;

import com.snowstore.pontus.service.vo.AssetRenewal;
import com.snowstore.pontus.vo.ResponseVo;

public class AssetRenewalResp extends ResponseVo {

	private AssetRenewal assetRenewal;

	public AssetRenewal getAssetRenewal() {
		return assetRenewal;
	}

	public void setAssetRenewal(AssetRenewal assetRenewal) {
		this.assetRenewal = assetRenewal;
	}

}
