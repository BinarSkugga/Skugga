package com.binarskugga.skuggahttps.api.impl.endpoint;

import com.binarskugga.skuggahttps.api.impl.HttpSession;
import lombok.Getter;
import lombok.Setter;

public abstract class AbstractController {

	 @Getter @Setter private HttpSession session;

}
