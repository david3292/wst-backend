package com.altioracorp.wst.repositorio;

import org.springframework.data.repository.CrudRepository;

import com.altioracorp.wst.dominio.EntidadBaseId;

public interface RepositorioBaseId<T extends EntidadBaseId> extends CrudRepository<T, Long>  {

}
