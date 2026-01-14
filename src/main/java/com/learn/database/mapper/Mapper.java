package com.learn.database.mapper;

public interface Mapper<A,B> {

    B mapFromAToB(A a);

    A mapFromBToA(B a);
}
