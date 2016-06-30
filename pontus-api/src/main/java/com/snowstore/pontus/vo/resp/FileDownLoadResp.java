package com.snowstore.pontus.vo.resp;

import java.util.ArrayList;
import java.util.List;

import com.snowstore.pontus.vo.ResponseVo;

public class FileDownLoadResp extends ResponseVo {

	private List<String> base64FileStrList = new ArrayList<String>();

	public List<String> getBase64FileStrList() {
		return base64FileStrList;
	}

	public void setBase64FileStrList(List<String> base64FileStrList) {
		this.base64FileStrList = base64FileStrList;
	}

}
