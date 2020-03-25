package com.courart.backend.api.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.courart.backend.api.model.Direcao;

public interface DirecaoRepository extends JpaRepository<Direcao, Long>{

	@Query("SELECT d FROM Direcao d WHERE d.placa LIKE %?1%")
	List<Direcao> findByPlaca(String placa);
	
	@Query("SELECT d FROM Direcao d WHERE d.cpf LIKE %?1%")
	List<Direcao> findByCPF(String cpf);
	
	@Query("SELECT d FROM Direcao d WHERE d.inicioDirecao BETWEEN ?1 AND ?2")
	List<Direcao> findGuidance(Date dataI, Date dataF);
	
}
