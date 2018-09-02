package com.binarskugga.skuggahttps.http.api.filter;

import com.binarskugga.skuggahttps.http.*;

import java.util.*;
import java.util.function.*;

public class FilterChain {

	private List<AbstractFilter> filters;

	public FilterChain() {
		this.filters = new ArrayList<>();
	}

	public FilterChain addFilter(AbstractFilter filter) {
		this.filters.add(filter);
		return this;
	}

	public void applyPre(HttpSession session) {
		this.apply(session, f -> f instanceof PreFilter);
	}

	public void applyPost(HttpSession session) {
		this.apply(session, f -> f instanceof PostFilter);
	}

	private void apply(HttpSession session, Predicate<AbstractFilter> condition) {
		boolean next;
		for(AbstractFilter filter : this.filters) {
			if(condition.test(filter)) {
				next = filter.apply(session);
				if(!next) {
					break;
				}
			}
		}
	}
}
