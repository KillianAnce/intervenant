package org.miage.intervenantsservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Intervenant {

    @Id
    private String id;
    private String nom;
    private String prenom;
    private String commune;
    private String codepostal;

    public Intervenant(String nom, String prenom, String commune, String codepostal) {
        this.nom = nom;
        this.prenom = prenom;
        this.commune = commune;
        this.codepostal = codepostal;
    }
}
