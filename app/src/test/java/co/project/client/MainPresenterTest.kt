package co.project.client

import com.nhaarman.mockito_kotlin.whenever
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyList
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

import rx.Observable
import co.project.client.data.DataManager
import co.project.client.test.common.TestDataFactory
import co.project.client.ui.main.MainPresenter
import co.project.client.util.RxSchedulersOverrideRule

import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import co.project.client.ui.main.MainMvp

@RunWith(MockitoJUnitRunner::class)
class MainPresenterTest {

    @Rule @JvmField
    val overrideSchedulersRule = RxSchedulersOverrideRule()

    @Mock
    lateinit var mockMainMvpView: MainMvp.View

    @Mock
    lateinit var mockDataManager: DataManager

    lateinit var mainPresenter: MainPresenter

    @Before
    fun setUp() {
        mainPresenter = MainPresenter(mockDataManager)
        mainPresenter.attachView(mockMainMvpView)
    }

    @After
    fun tearDown() {
        mainPresenter.detachView()
    }

    @Test
    fun loadRibotsReturnsRibots() {
        val ribots = TestDataFactory.makeListRibots(10)
        whenever(mockDataManager.getRibots()).thenReturn(Observable.just(ribots))

        mainPresenter.loadRibots()
        verify(mockMainMvpView).showRibots(ribots)
        verify(mockMainMvpView, never()).showRibotsEmpty()
        verify(mockMainMvpView, never()).showError()
    }

    @Test
    fun loadRibotsReturnsEmptyList() {
        whenever(mockDataManager.getRibots()).thenReturn(Observable.just(emptyList<Ribot>()))

        mainPresenter.loadRibots()
        verify(mockMainMvpView).showRibotsEmpty()
        verify(mockMainMvpView, never()).showRibots(anyList<Ribot>())
        verify(mockMainMvpView, never()).showError()
    }

    @Test
    fun loadRibotsFails() {
        whenever(mockDataManager.getRibots()).
                thenReturn(Observable.error<List<Ribot>>(RuntimeException()))

        mainPresenter.loadRibots()
        verify(mockMainMvpView).showError()
        verify(mockMainMvpView, never()).showRibotsEmpty()
        verify(mockMainMvpView, never()).showRibots(anyList<Ribot>())
    }
}
