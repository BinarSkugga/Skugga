package com.binarskugga.skuggahttps.http.api;

import lombok.*;
import com.binarskugga.skuggahttps.http.*;

public abstract class AbstractController implements Cloneable {

	@Getter @Setter private HttpSession session;

	public AbstractController copy() {
		try {
			return (AbstractController) this.clone();
		} catch(CloneNotSupportedException e) {
			return null;
		}
	}

}
