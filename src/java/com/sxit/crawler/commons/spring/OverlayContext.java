package com.sxit.crawler.commons.spring;

import java.util.ArrayList;
import java.util.Map;

/**
 * 需要覆盖KeyedProperties配置的数据调用接口
 * Interface for objects that can contribute 'overlays' to replace the
 * usual values in configured objects. 
 */
public interface OverlayContext {
    /** test if this context has actually been configured with overlays
     * (even if in fact no overlays were added) */
    public boolean haveOverlayNamesBeenSet();
    
    /** return a list of the names of overlay maps to consider */ 
    ArrayList<String> getOverlayNames();
    
    /** get the map corresponding to the overlay name */ 
    Map<String,Object> getOverlayMap(String name);
}
