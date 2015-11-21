package com.appauthority.appwiz.interfaces;

import com.appsauthority.appwiz.models.PayPalModelObject;

public interface PayPalCaller {

	public void payPalDataDownloaded(PayPalModelObject payPalObj);
	public void payPalDataFailed(String errorMsg);
}
