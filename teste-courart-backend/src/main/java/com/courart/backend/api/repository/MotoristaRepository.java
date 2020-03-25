package com.courart.backend.api.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.courart.backend.api.model.Motorista;

public interface MotoristaRepository extends JpaRepository<Motorista, Long>{

	@Query("SELECT m FROM Motorista m WHERE m.cpf LIKE %?1%")
	List<Motorista> findByCPF(String cpf);
	
	@Query("SELECT m FROM Motorista m WHERE m.cpf=?1")
	Motorista getByCPF(String cpf);
	
	@Query("SELECT m FROM Motorista m WHERE m.nome LIKE %?1%")
	List<Motorista> findByNome(String nome);
	
	@Query("SELECT m FROM Motorista m WHERE m.dataNascimento BETWEEN ?1 AND ?2")
	List<Motorista> findBirthdays(Date dataI, Date dataF);
}
