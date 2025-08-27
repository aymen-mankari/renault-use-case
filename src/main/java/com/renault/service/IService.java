package com.renault.service;

public interface IService<U> {
    U save(U obj);
    U update(U obj);
    boolean delete(Long id);
}
