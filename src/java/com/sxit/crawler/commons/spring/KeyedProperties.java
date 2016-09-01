package com.sxit.crawler.commons.spring;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 可覆盖的key-value存储结构
 * Map for storing overridable properties.
 * 
 * An object wanting to allow its properties to be overridden contextually will
 * store those properties in this map. Its accessors (like getProp() and
 * setProp()) will only pass-through to the 'prop' entry in this map.)
 * 
 */
public class KeyedProperties extends ConcurrentHashMap<String, Object> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7323258204540924549L;

	/**
	 * the alternate global property-paths leading to this map TODO: consider if
	 * deterministic ordered list is important
	 */
	private HashSet<String> externalPaths = new HashSet<String>();
	
	/**
	 * ThreadLocal (contextual) collection of pushed override maps
	 */
	private static ThreadLocal<ArrayList<OverlayContext>> threadOverrides = new ThreadLocal<ArrayList<OverlayContext>>() {
		protected ArrayList<OverlayContext> initialValue() {
			return new ArrayList<OverlayContext>();
		}
	};

	/**
	 * Add a path by which the outside world can reach this map
	 * 
	 * @param path
	 *            String path
	 */
	public void addExternalPath(String path) {
		externalPaths.add(path);
	}

	/**
	 * Get the given value, checking override maps if appropriate.
	 * 
	 * @param key
	 * @return discovered override, or local value
	 */
	public Object get(String key) {
		ArrayList<OverlayContext> overlays = threadOverrides.get();
		for (int i = overlays.size() - 1; i >= 0; i--) {
			OverlayContext ocontext = overlays.get(i);
			for (int j = ocontext.getOverlayNames().size() - 1; j >= 0; j--) {
				String name = ocontext.getOverlayNames().get(j);
				Map<String, Object> m = ocontext.getOverlayMap(name);
				for (String ok : getOverrideKeys(key)) {
					Object val = m.get(ok);
					if (val != null) {
						return val;
					}
				}
			}
		}

		return super.get(key);
	}

	/**
	 * Compose the complete keys (externalPath + local key name) to use for
	 * checking for contextual overrides.
	 * 
	 * @param key
	 *            local key to compose
	 * @return List of full keys to check
	 */
	protected List<String> getOverrideKeys(String key) {
		ArrayList<String> keys = new ArrayList<String>(externalPaths.size());
		for (String path : externalPaths) {
			keys.add(path + "." + key);
		}
		return keys;
	}

	//
	// CLASS SERVICES
	//



	/**
	 * Add an override map to the stack
	 * 
	 * @param m
	 *            Map to add
	 */
	public static void pushOverrideContext(OverlayContext ocontext) {
		threadOverrides.get().add(ocontext);
	}

	/**
	 * Remove last-added override map from the stack
	 * 
	 * @return Map removed
	 */
	public static OverlayContext popOverridesContext() {
		return threadOverrides.get().remove(threadOverrides.get().size() - 1);
	}

	public static void clearAllOverrideContexts() {
		threadOverrides.get().clear();
	}

	public static void loadOverridesFrom(OverlayContext ocontext) {
		assert ocontext.haveOverlayNamesBeenSet();
		pushOverrideContext(ocontext);
	}

	public static boolean clearOverridesFrom(OverlayContext ocontext) {
		return threadOverrides.get().remove(ocontext);
	}

	public static void withOverridesDo(OverlayContext ocontext, Runnable todo) {
		try {
			loadOverridesFrom(ocontext);
			todo.run();
		} finally {
			clearOverridesFrom(ocontext);
		}
	}

	public static boolean overridesActiveFrom(OverlayContext ocontext) {
		return threadOverrides.get().contains(ocontext);
	}
}
