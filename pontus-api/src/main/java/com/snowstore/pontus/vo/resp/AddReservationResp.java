package com.snowstore.pontus.vo.resp;

import com.snowstore.pontus.vo.ResponseVo;

public class AddReservationResp extends ResponseVo {

	private Long reservationId;//挂牌编号

	public Long getReservationId() {
		return reservationId;
	}

	public void setReservationId(Long reservationId) {
		this.reservationId = reservationId;
	}

}
