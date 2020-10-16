package com.yosefmoq.firstcode.interfaceI

interface OnCompleteListener<T, E> {
    fun onSuccess(`object`: T)

    fun onFail(`object`: E, code: Int)
}