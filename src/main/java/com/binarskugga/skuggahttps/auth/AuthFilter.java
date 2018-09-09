package com.binarskugga.skuggahttps.auth;

import com.binarskugga.skuggahttps.annotation.*;
import com.binarskugga.skuggahttps.auth.role.*;
import com.binarskugga.skuggahttps.data.*;
import com.binarskugga.skuggahttps.http.*;
import com.binarskugga.skuggahttps.http.api.exception.*;
import com.binarskugga.skuggahttps.http.api.filter.*;

import java.util.*;

@Filter(-1)
public class AuthFilter extends PreFilter {

	private DataRepository identityRepository;

	public AuthFilter(DataRepository identityRepository) {
		this.identityRepository = identityRepository;
	}

	@Override
	public boolean apply(HttpSession session) {
		Cookie cookie = session.getCookies().get("token");
		List<Class<? extends AccessRole>> roles = session.getEndpoint().getAccess();

		if(roles.contains(AllAccess.class)) {
			return true;
		}

		if(cookie != null) {
			Token token = new Token(cookie.value);
			GenericUser user = (GenericUser) identityRepository.id(token.getId());

			if(user == null)
				throw new InvalidTokenException();

			boolean tokenValid = AuthService.get().isTokenValid(user, token);
			if(tokenValid) {
				// Admin goes through no matter wat
				if(AdminRole.class.isAssignableFrom(user.getAccessRole())) {
					session.setIdentity(user);
					return true;
				}

				if(roles.contains(SubjectiveAccess.class)) {
					Identifiable subject = (Identifiable) session.getArgs().get(session.getEndpoint().getSubject());
					if(subject.getId().equals(user.getId())) {
						return true;
					}
				} else {
					if(roles.contains(LoggedAccess.class)) {
						session.setIdentity(user);
						return true;
					}

					for(Class<? extends AccessRole> accessRole : roles) {
						if(DefaultRole.class.isAssignableFrom(accessRole)) {
							continue;
						}

						if(user.getAccessRole().equals(accessRole)) {
							session.setIdentity(user);
							return true;
						}
					}
				}
			} else {
				throw new InvalidTokenException();
			}
		}
		throw new InsufficientRoleException();
	}

}
