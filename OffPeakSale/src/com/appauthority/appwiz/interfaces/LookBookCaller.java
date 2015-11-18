package com.appauthority.appwiz.interfaces;

import com.appsauthority.appwiz.models.LookBookInfoResponse;

public interface LookBookCaller {

	public void lookBookDataDownloaded(LookBookInfoResponse lookbookresponseObj);
	public void lookBookitemLiked(int itemIndex,String likeCount,String errorMsg);
	public void lookBookitemLikedFailed(String errorMsg);
}
