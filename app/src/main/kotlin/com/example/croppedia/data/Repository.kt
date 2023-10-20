package com.example.croppedia.data

import javax.inject.Inject

class Repository @Inject constructor(
    remoteDataSource: RemoteDataSource,
    localDataSource: LocalDataSource
) {

    val local = localDataSource
    val remote = remoteDataSource
}