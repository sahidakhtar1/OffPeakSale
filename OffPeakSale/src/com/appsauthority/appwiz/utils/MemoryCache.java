package com.appsauthority.appwiz.utils;
/* Copyright (C)
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Kevin Irish Antonio <irish.antonio@yahoo.com>, February 2014
 */
import java.lang.ref.SoftReference;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
 
public class MemoryCache {
	
	private static MemoryCache sharedCache;
	
    public static MemoryCache sharedCache(Context context){
    	if(sharedCache == null){
    		sharedCache = new MemoryCache();
    	}
    	return sharedCache;    	
    }
    
    
    private Map<String, SoftReference<Bitmap>> cache=Collections.synchronizedMap(new HashMap<String, SoftReference<Bitmap>>());
 

    public Bitmap get(String id){
        if(!cache.containsKey(id))
            return null;
        SoftReference<Bitmap> ref=cache.get(id);
        return ref.get();
    }
 
    public void put(String id, Bitmap bitmap){
        cache.put(id, new SoftReference<Bitmap>(bitmap));
    }
 
    public void clear() {
        cache.clear();
    }
}
