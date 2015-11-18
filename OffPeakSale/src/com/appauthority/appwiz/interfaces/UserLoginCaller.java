package com.appauthority.appwiz.interfaces;

import com.appsauthority.appwiz.models.Profile;

public interface UserLoginCaller {

	public void loggedIn(Boolean isSucess, String msg,Profile userProfile);
}
