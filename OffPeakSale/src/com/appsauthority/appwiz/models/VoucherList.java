package com.appsauthority.appwiz.models;

import java.util.ArrayList;
import java.util.List;

public class VoucherList {

	List<Voucher> vouchers;

	public List<Voucher> getVouchers() {
		if (vouchers == null) {
			vouchers = new ArrayList<Voucher>();
		}
		return vouchers;
	}

	public void setVouchers(List<Voucher> vouchers) {
		this.vouchers = vouchers;
	}
}
