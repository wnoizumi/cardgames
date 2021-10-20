package com.logmein.cardgames.domain.entities;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(indexes = @Index(columnList = "uuid"))
public class Game {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(unique = true, nullable = false)
	private UUID uuid;
	
	private String name;
	
	@OneToMany()
	private Set<Player> players;
	
	public Game() {
		this.uuid = UUID.randomUUID();
	}
	
	public Game(String name) {
		this();
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	private void setId(Long id) {
		this.id = id;
	}

	public UUID getUuid() {
		return uuid;
	}

	private void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int hashCode() {
		return Objects.hash(uuid);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Game other = (Game) obj;
		return Objects.equals(uuid, other.uuid);
	}

	@Override
	public String toString() {
		return "Game [uuid=" + uuid + ", name=" + name + "]";
	}
}
