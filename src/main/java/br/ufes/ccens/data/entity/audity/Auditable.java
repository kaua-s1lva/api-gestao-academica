package br.ufes.ccens.data.entity.audity;

public interface Auditable {
    //ver se vai pegar o id do usuário
    void setCreatedBy(String user);
    void setUpdatedBy(String user);
}
