package com.altioracorp.wst.servicio;

import java.util.List;

public interface ICRUD <T, V> {

	T registrar(T obj);

	T modificar(T obj);

	List<T> listarTodos();

	T listarPorId(V id);

	boolean eliminar(V id);
}
