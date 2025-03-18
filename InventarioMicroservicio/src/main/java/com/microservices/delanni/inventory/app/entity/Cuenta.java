package com.microservices.delanni.inventory.app.entity;

import java.util.List;

import com.microservices.delanni.inventory.app.entity.pagos.Moneda;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Cuenta {

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@NotBlank
	private String descripcion;
	@NotBlank
	private String numero_cuenta;
	
	//Ultimos 4 números
	private Integer ult_num;
	
	private Double ingreso;
	
	private Double egreso;
	
	private Double saldo;
	
	private Double saldo_ant;
	
	@OneToOne
	private Moneda moneda;
	
	//Tipo de cuenta
	@NotBlank
	private String tpp;
	
	/*@OneToMany
	private List<Transaccion> cuentas;*/

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Integer getUlt_num() {
		return ult_num;
	}

	public void setUlt_num(Integer ult_num) {
		this.ult_num = ult_num;
	}

	public Double getIngreso() {
		return ingreso;
	}

	public void setIngreso(Double ingreso) {
		this.ingreso = ingreso;
	}

	public Double getEgreso() {
		return egreso;
	}

	public void setEgreso(Double egreso) {
		this.egreso = egreso;
	}

	public Double getSaldo_ant() {
		return saldo_ant;
	}

	public void setSaldo_ant(Double saldo_ant) {
		this.saldo_ant = saldo_ant;
	}

	public String getTpp() {
		return tpp;
	}

	public void setTpp(String tpp) {
		this.tpp = tpp;
	}

	public Double getSaldo() {
		return saldo;
	}

	public void setSaldo(Double saldo) {
		this.saldo = saldo;
	}

	public String getNumero_cuenta() {
		return numero_cuenta;
	}

	public void setNumero_cuenta(String numero_cuenta) {
		this.numero_cuenta = numero_cuenta;
	}

	public Moneda getMoneda() {
		return moneda;
	}

	public void setMoneda(Moneda moneda) {
		this.moneda = moneda;
	}
	
	
	
	
	
}
