package com.example.demo.common.security;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@ConditionalOnBean(FormLoginSecurityConfig.class)
@RequiredArgsConstructor
public class FormLoginUserDetailsServiceImpl implements UserDetailsService {
	private final PasswordEncoder passwordEncoder;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		switch (username) {
		case "user1":
			return new User(username, passwordEncoder.encode(username),
					AuthorityUtils.createAuthorityList("ROLE_USER"));
		case "user2":
			return new User(username, passwordEncoder.encode(username),
					AuthorityUtils.createAuthorityList("ROLE_ADMIN"));
		}

		throw new UsernameNotFoundException("It can not be acquired User");
	}

}
