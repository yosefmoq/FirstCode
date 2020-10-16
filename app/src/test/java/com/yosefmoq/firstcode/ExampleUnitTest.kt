package com.yosefmoq.firstcode

import org.junit.Test

import org.junit.Assert.*
import org.mockito.Mock
import org.mockito.ArgumentMatchers

import org.mockito.Mockito

import org.mockito.InOrder

import org.mockito.Mockito.`when`







/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    private lateinit var postViewModel:PostViewModel

    @Mock
    private var repository: Repository

    @Mock
    private var networkStatus:NetworkUtils


    @Before
    fun setup(){
        MockitoAnnotations.initMocks(this);
        ViewModelProvider(this).get(PostViewModel::class.java)
    }
    @Test
    fun getPostList() {
        Mockito.`when`(networkStatus.isOnline()).thenReturn(true)
        presenter.getImageList(networkStatus)
        verify(repository).getList(
            networkStatusArgumentCaptor.capture(),
            callBackListenerArgumentCaptor.capture()
        )
        callBackListenerArgumentCaptor.getValue().onSuccess(ArgumentMatchers.anyList<Example>())
        val inOrder: InOrder = Mockito.inOrder(view)
        inOrder.verify<Any>(view).showProgressBar(true)
        inOrder.verify<Any>(view).showProgressBar(false)
        inOrder.verify<Any>(view).showImageList(ArgumentMatchers.anyList<Example>())
    }
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun fetchValidDataShouldLoadIntoView() {

    }


}