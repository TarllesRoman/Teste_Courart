package com.courart.backend.api.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.courart.backend.api.model.Veiculo;

public interface VeiculoRepository extends JpaRepository<Veiculo, Long>{

	@Query("SELECT v FROM Veiculo v WHERE v.placa LIKE %?1%")
	List<Veiculo> findByPlaca(String placa);
	
	@Query("SELECT v FROM Veiculo v WHERE v.placa=?1")
	Veiculo getByPlaca(String placa);
	
	@Query("SELECT v FROM Veiculo v WHERE v.modelo LIKE %?1%")
	List<Veiculo> findByModelo(String modelo);
	
	@Query("SELECT v FROM Veiculo v WHERE v.dataCadastro BETWEEN ?1 AND ?2")
	List<Veiculo> findAtivados(Date dataI, Date dataF);
}
