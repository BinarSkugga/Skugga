package com.binarskugga.skugga.api.impl.endpoint;

import lombok.Getter;
import lombok.Setter;

public abstract class AbstractController {

	@Getter @Setter private HttpSession session;

}
