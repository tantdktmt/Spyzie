package com.tantd.spyzie.data.db;

import java.util.Collection;
import java.util.List;

public interface DbManager {

    <T> void put(T t);

    <T> void put(T ...ts);

    <T> void put(Collection<T> collection);

    <T> List<T> findAll(Class<T> tClass);

    <T> void removeAll(Class<T> tClass);
}
