package org.miage.intervenantsservice.boundary;

import org.miage.intervenantsservice.entity.Intervenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel="intervenants")
public interface IntervenantResource extends JpaRepository<Intervenant, String> {
    //GET, POST, PUT, DELETE, PATCH
}