package com.snowstore.pontus.service.userDetails;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class WebUserDetailsImpl implements UserDetails {

	private static final long serialVersionUID = 3711688568017944654L;

	private Long id;
	private String username;
	private String password;

	public WebUserDetailsImpl(Long id, String username, String password) {
		this.id = id;
		this.username = username;
		this.password = password;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}

	public Long getId() {
		return id;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	public String getVagueUsername() {
		if (username.length() < 7) {
			return username;
		}
		return this.username.substring(0, 3) + "****" + this.username.substring(username.length() - 4);
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}