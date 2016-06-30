package com.snowstore.pontus.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "pontus_platform")
public class Platform extends AbstractEntity {

	private static final long serialVersionUID = -1600824709064266346L;
	// Fields
	private String platFormName;// 平台名称
	private String describe;// 描述
	// Cascade
	@ManyToMany(mappedBy = "platFormSet")
	private Set<AdminUser> adminUserSet = new HashSet<AdminUser>();
	// Constructors

	/** default constructor ***/
	public Platform() {

	}

	// property accessors
	public String getPlatFormName() {
		return platFormName;
	}

	public void setPlatFormName(String platFormName) {
		this.platFormName = platFormName;
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	public Set<AdminUser> getAdminUserSet() {
		return adminUserSet;
	}

	public void setAdminUserSet(Set<AdminUser> adminUserSet) {
		this.adminUserSet = adminUserSet;
	}

}
