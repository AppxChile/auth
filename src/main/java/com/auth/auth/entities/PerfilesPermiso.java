package com.auth.auth.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "perfiles_permisos")
public class PerfilesPermiso {

    @Id
    @Column(name = "perfil_id", nullable = false)
    private Long perfilId;

    @Column(name = "permiso_id", nullable = false)
    private Long permisoId;

    public Long getPerfilId() {
        return perfilId;
    }

    public void setPerfilId(Long perfilId) {
        this.perfilId = perfilId;
    }

    public Long getPermisoId() {
        return permisoId;
    }

    public void setPermisoId(Long permisoId) {
        this.permisoId = permisoId;
    }

}
