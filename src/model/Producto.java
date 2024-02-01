package model;

public class Producto {
	private String codigo;
	private String nombre;
	private float precio;
	private int disponible;
	
	public Producto(String codigo, String nombre, float precio, int disponible) {
		super();
		this.codigo = codigo;
		this.nombre = nombre;
		this.precio = precio;
		this.disponible = disponible;
	}
	
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public float getPrecio() {
		return precio;
	}
	public void setPrecio(float precio) {
		this.precio = precio;
	}
	public int getDisponible() {
		return disponible;
	}
	public void setDisponible(int disponible) {
		this.disponible = disponible;
	}
}
