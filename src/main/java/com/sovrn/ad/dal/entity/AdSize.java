package com.sovrn.ad.dal.entity;

import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
@Entity
public class AdSize {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="ad_size_id")
	private int id;
	
	@NotNull
	@Column(name="width")
	private int width;
	
	@NotNull
	@Column(name="height")
	private int height;
	
	@ManyToMany(mappedBy = "adSizes")
	private Set<User> users;
	
	@ManyToMany(mappedBy = "adSizes")
	private Set<Provider> providers;
	
	@Override
	public String toString() {
	    return "AdSize [id=" + id + ", width=" + width + ", height=" + height + "]";
	}
	
	@Override
	public int hashCode() {
	    return Objects.hash(id, width, height);
	}
}
