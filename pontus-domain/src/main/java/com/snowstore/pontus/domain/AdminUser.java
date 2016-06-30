package com.snowstore.pontus.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "pontus_admin_user")
@EntityListeners(AuditingEntityListener.class)
public class AdminUser extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6398840872607982669L;
	private String username;
	private String password;
	private String role;
	// Cascade
	@ManyToMany
	@JoinTable(name = "pontus_admin_platform", joinColumns = { @JoinColumn(name = "admin_user_id") }, inverseJoinColumns = { @JoinColumn(name = "platform_id") })
	public Set<Platform> platFormSet = new HashSet<Platform>();

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Set<Platform> getPlatFormSet() {
		return platFormSet;
	}

	public void setPlatFormSet(Set<Platform> platFormSet) {
		this.platFormSet = platFormSet;
	}


	public enum Role{
		ADMIN,USER
	}
}
