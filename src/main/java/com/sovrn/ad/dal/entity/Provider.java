package com.sovrn.ad.dal.entity;

import java.util.Objects;
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
@Table(name = "provider")
public class Provider {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="provider_id")
	private int id;

	@NotNull
	@Column(name="provider_name", unique=true)
	private String name;
	
	@NotNull
	@Column(name="url", unique=true)
	private String url;
	
	@ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "provider_size_assoc", 
    	joinColumns = @JoinColumn(name = "provider_id", referencedColumnName = "provider_id"), 
    	inverseJoinColumns = @JoinColumn(name = "ad_size_id", referencedColumnName = "ad_size_id"))
	private Set<AdSize> adSizes;
	
	@ManyToMany(mappedBy = "providers")
	private Set<User> users;

	@Override
	public String toString() {
	    return "Provider [id=" + id + ", name=" + name + ", url=" + url + "]";
	}
	
	@Override
	public int hashCode() {
	    return Objects.hash(id, name, url);
	}
}
