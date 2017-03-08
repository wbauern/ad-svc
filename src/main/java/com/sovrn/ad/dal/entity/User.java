package com.sovrn.ad.dal.entity;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
@Entity
@Table(name = "\"user\"")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="user_id")
	private int id;

	@NotNull
	@Column(name="username", unique=true)
	private String username;
	
	@ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "user_size_assoc", 
    	joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "user_id"), 
    	inverseJoinColumns = @JoinColumn(name = "ad_size_id", referencedColumnName = "ad_size_id"))
	private Set<AdSize> adSizes;

	@ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "user_provider_assoc", 
    	joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "user_id"), 
    	inverseJoinColumns = @JoinColumn(name = "provider_id", referencedColumnName = "provider_id"))
	private Set<Provider> providers;

}
